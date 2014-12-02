package my.crawler.extract;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GetRuleFromSql {

	public Connection conn = null;

	public GetRuleFromSql() {
		super();

	}

	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // ����������

		String url = "jdbc:mysql://127.0.0.1:3306/mycrawler"; // URLָ��Ҫ���ʵ����ݿ���scutcs

		String user = "root"; // MySQL����ʱ���û���

		String password = ""; // MySQL����ʱ������

		try {
			Class.forName(driver);// ������������

			conn = DriverManager.getConnection(url, user, password); // �������ݿ�

			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!"); // ��֤�Ƿ����ӳɹ�
			} else {
				System.out.println("error connecting to the Database!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�ж�url�Ƿ����
	public boolean  isExitinDb(String url,String type){
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// statement����ִ��SQL���
			String sql = "select  *  from  urltable   where  url  =  '" + url + "'" ;
			ResultSet rs;
			rs = statement.executeQuery(sql); // �����
			if (rs.next()){
				System.out.println(url + "�Ѵ���");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  false;
	}

	//�ѳ�ȡ��url���
	public boolean writeUrl(String url, String type) {
		//if (isExitinDb(url,type)){
		//	return false;
		//}
		try {
			Statement statement = null;
			statement = conn.createStatement();
			// statement����ִ��SQL���
			String sql = "insert into  urltable(url,type)  values ( '" + url + "','" + type + "')"; // Ҫִ�е�SQL���
			//System.out.println(sql);

			statement.execute(sql); // �����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Vector GetRules(String site) {
		Statement statement = null;

		Vector<Map<String, String>> vcr = new Vector<Map<String, String>>();
		try {
			statement = conn.createStatement(); // statement����ִ��SQL���

			String sql = "select * from productRules  where domain = '" + site
					+ "'"; // Ҫִ�е�SQL���

			ResultSet rs;
			rs = statement.executeQuery(sql); // �����

			String name = null;
			String domain = null;
			String picture = null;
			String place = null;
			String contact = null;
			String mobile = null;
			String telephone = null;
			String email = null;
			String address = null;
			String summry = null;
			String website = null;
			String price = null;
			String brands = null;
			String company = null;

			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				name = rs.getString("name"); // ѡ��sname��������
				domain = rs.getString("domain");
				picture = rs.getString("picture");
				place = rs.getString("place");
				contact = rs.getString("contact");
				mobile = rs.getString("mobile");
				telephone = rs.getString("telephone");
				email = rs.getString("email");
				address = rs.getString("address");
				summry = rs.getString("summry");
				website = rs.getString("website");
				price = rs.getString("price");
				brands = rs.getString("brands");
				company = rs.getString("company");

				map.put("name", name);
				map.put("domain", domain);
				map.put("picture", picture);
				map.put("place", place);
				map.put("contact", contact);
				map.put("mobile", mobile);
				map.put("telephone", telephone);
				map.put("email", email);
				map.put("address", address);
				map.put("summry", summry);
				map.put("website", website);
				map.put("price", price);
				map.put("brands", brands);
				map.put("company", company);
				vcr.add(map);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return vcr;
	}
}
