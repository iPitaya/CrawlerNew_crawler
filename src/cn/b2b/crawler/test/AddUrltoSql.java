package cn.b2b.crawler.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import cn.b2b.crawler.config.ConfigResultData;
import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.db.ManageMysql;
import cn.b2b.crawler.resultdata.DataMySQL;

public class AddUrltoSql {
	//private ConfigXML config = new ConfigXML();
	//private ManageMysql mgsql = new ManageMysql(config);
	private ConfigResultData configresult = new ConfigResultData();
	private DataMySQL mgsql = new DataMySQL();
	private int idnum = 0;

	// private Jedis jedis = new Jedis("192.168.3.205", 6379);

	public AddUrltoSql() {
		//mgsql.init();
		mgsql.initMysql(configresult);
	}

	public void sqldata() {
		String sql = "select id,resultstr from dataresult where url is NULL and id > " + idnum
				+ " limit 100";

		ResultSet rs = mgsql.readData(sql);
		boolean endflag = true;
		try {
			while (rs.next()) {
				endflag = false;

				String resultstr = rs.getString("resultstr");
				String id = rs.getString("id");
				System.out.println(id);
				try {
					JSONObject json = new JSONObject(resultstr);
					Object ob = json.get("url");

					System.out.println(ob.toString());
					boolean flag = redisControl(ob.toString(), id);
					System.out.println(flag);
					if (flag) {
						sql = "update dataresult set url = '" + ob.toString()
								+ "' where id = " + id;
						mgsql.writeData(sql);
					} else {
						sql = "delete from dataresult  where id =" + id;
						mgsql.writeData(sql);
						System.out.println("delete: " + id + " "
								+ ob.toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				idnum = rs.getInt("id");
			}
			if (endflag) {
				System.exit(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean redisControl(String url, String id) {
		String redisvalue = UJedis.get("mysqlnew:" + url);
		System.out.println(redisvalue);
		if (redisvalue == null) {
			UJedis.set("mysqlnew:" + url, id);
			return true;
		} else {
			System.out.println("cunzai: " + url);
			if (redisvalue.equals(id)) {
				return true;
			} else {
				return false;
			}

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AddUrltoSql addurl = new AddUrltoSql();
		while (true) {
			addurl.sqldata();
		}
	}

}
