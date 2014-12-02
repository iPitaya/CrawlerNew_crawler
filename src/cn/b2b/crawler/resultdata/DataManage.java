package cn.b2b.crawler.resultdata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.b2b.crawler.config.ConfigResultData;
import cn.b2b.crawler.db.DataToMongodb;
import cn.b2b.crawler.extract.GetRuleFromDb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DataManage {

	private JsonData jsondata = new JsonData();

	private BasicDBObject mginfo = new BasicDBObject();

	private ConfigResultData configresult = new ConfigResultData();
	private DataMySQL datasql = new DataMySQL();
	private DataToMongodb mgdb = new DataToMongodb();
	private String tablename;
	private DataCompanyId datacompanyid = new DataCompanyId();
	private DataSiteMsg sitemsg = new DataSiteMsg();
	
	private StoreFile storefile = new StoreFile();
	
	private DataCompanyId mysqlid = new DataCompanyId();

	public DataManage() {
		datasql.initMysql(configresult);
		tablename = configresult.getMysqltable();

		mgdb.initMongodb(configresult);
		mysqlid.setFilename("conf/resultdata/mysqlidnum.txt");
	}

	public BasicDBObject getBasicDBObject(String str) throws JSONException {
		return jsondata.getMapData(str);
	}

	// ��mongodb�е����� ���չ�˾id�������ݱȽϿ�
	public void updateMongoData(int companyid, BasicDBObject info) {
		info.put("companyid", companyid);
		info.put("docnum", companyid);
		info.removeField("tradeid");
		info.removeField("industryid");
		DBObject updateCondition = new BasicDBObject();
		updateCondition.put("companyid", companyid);
		DBObject updateSetValue = new BasicDBObject("$set", info);
		mgdb.getColl().update(updateCondition, updateSetValue, true, false);
	}

	// redis ��� ���� ���������
	public String isUrlInRedis(String url) {
		long time0 = 0;
		long time1 = 1;
		time0 = new Date().getTime();
		String val = UJedis.getNew("crawler:" + url);
		time1 = new Date().getTime();
		System.out.println("redis time " + (time1 - time0));
		return val;
	}

	// redis��������
	public void UrlInsertRedis(String key, String value) {
		UJedis.set("crawler:" + key, value);
	}

	public int useMysql() throws SQLException, JSONException {
		int sqlid = mysqlid.readdata();
		int endid = 0;
		String sql = "select * from " + tablename + " where id > "+sqlid+"  and  flag = 0 limit 50";
		String updatesql = "update  " + tablename + "  set flag = 1 where id =";
		ResultSet rs = datasql.readData(sql);
		int num = 0;
		long time0 = 0;
		long time1 = 0;

		while (rs.next()) {
			mginfo.clear();
			endid = Integer.valueOf(rs.getString("id"));
			try{
			mginfo = getBasicDBObject(rs.getString("resultstr").replaceAll("\n", ""));
			}catch(Exception e){
				e.printStackTrace();
			}
			datasql.updateData(updatesql + rs.getString("id"));
			if (mginfo.isEmpty()){
				System.out.println("mysql messages have problem, id " + rs.getString("id"));
				continue;
			}
			if (isBasicObjectOk(mginfo)) {
				String redisvalue = isUrlInRedis(mginfo.getString("url"));
				mginfo = sitemsg.getSiteMsg(mginfo, mginfo.getString("url"));
				if (redisvalue == null) {
					System.out.println(mginfo.getString("url") + "not exist");
					mginfo = datacompanyid.addCompanyId(mginfo);
					int companyid = datacompanyid.getCompanyid();
					UrlInsertRedis(mginfo.getString("url"),
							String.valueOf(companyid));
					System.out.println(mginfo.getString("url") +"  "+ companyid);
					storefile.writedata(mginfo.getString("url") +" "+ companyid);
					time0 = new Date().getTime();
					mgdb.insertData(mginfo);
					time1 = new Date().getTime();
					System.out.println("mongo insert time " + (time1 - time0));
				} else {
					System.out.println(mginfo.getString("url") + " "
							+ redisvalue + "exist");
					time0 = new Date().getTime();
					updateMongoData(Integer.valueOf(redisvalue), mginfo);
					time1 = new Date().getTime();
					System.out.println("mongo update time " + (time1 - time0));
				}

			}
			num++;
		}
		rs.close();
		
		System.out.println("endid " + endid);
		if ( num > 0){
			mysqlid.writedata(endid);
		}
		// System.out.println(num + "num");

		return num;
	}

	// �ж�basicoject ��֤�����б������ϵ��ʽ �Ƿ������mongodb
	public boolean isBasicObjectOk(BasicDBObject info) {
		if (info.get("title").equals("")) {
			return false;
		}

		if (info.get("telphone").equals("") && info.get("mobile").equals("")) {
			return false;
		}else{
			String telphone = info.get("telphone").toString();
			String mobile = info.get("mobile").toString();
			info.put("telphone", mobile);
			info.put("mobile", telphone);
		}

		if (info.get("address").equals("")) {
			return false;
		}

		return true;
	}

	public static void main(String args[]) throws SQLException, JSONException {
		DataManage manage = new DataManage();
		int num = 0;
		// int num = manage.useMysql();

		while (true) {
			try{
			num = manage.useMysql();
			/*
			 * if (manage.useMysql() == 0) { num++; } else { num = 0; } if (num
			 * > 5) { System.out.println("û�������ݡ����� ����ֹͣ  "); break; }
			 */
			System.out.println("num ................. ....... " +  num);
			if (num == 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("waiting ...");
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			// System.out.println(num);

			/*
			 * System.out.println(new Date().getTime()); long time=1277106667;
			 * Date date=new Date(time); SimpleDateFormat format=new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
			 * str=format.format(date); System.out.println(str);
			 */

		}

	}
}
