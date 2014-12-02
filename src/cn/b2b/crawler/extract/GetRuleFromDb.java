package cn.b2b.crawler.extract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class GetRuleFromDb {
	public Connection conn = null;
	public String url;
	public String ruletable;
	public String datatypes;
	private String user;
	private String password;
	private List<String>  vc = null; //typelist
	//private Vector<Map<String, String>> vcr = new Vector<Map<String, String>>(); //getRules

	public GetRuleFromDb(String urlname) {
		super();
		url = urlname;
		user = "root";
		password = "";
		 
		// init();
	}
	
	

	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;	
	}



	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // ����������

		//String url = "jdbc:mysql://127.0.0.1:3306/mycrawler"; // URLָ��Ҫ���ʵ����ݿ���scutcs

		//String user = "root"; // MySQL����ʱ���û���

		//String password = ""; // MySQL����ʱ������

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

	public String getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
		initTypeList();
	}
	public  String  getTypesStr(){
		return datatypes.replace('|', ',');
	}
	
	public  void  initTypeList(){
		vc = new ArrayList<String>();
		String[]  str = datatypes.split("\\|");
		for (int i=0; i<str.length;i++){
			vc.add(str[i]);
			//System.out.println(str[i]);
		}
	}
	
	public  List<String> getTypeList(){
		return vc;
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
		public boolean writeUrl(String url, String type,int depth) {
			//if (isExitinDb(url,type)){
			//	return false;
			//}
			try {
				Statement statement = null;
				
				statement = conn.createStatement();
				// statement����ִ��SQL���
				String sql = "insert into  urltable(url,type,depth)  values ( '" + url + "','" + type + "',"+depth+")"; // Ҫִ�е�SQL���
				//System.out.println(sql);

				statement.execute(sql); // �����
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
	
	public Vector getRules(String site){
		Statement statement = null;

		Vector<Map<String, String>> vcr = new Vector<Map<String, String>>();
		//vcr.clear();
		/*
		for (int i=0;i<vcr.size();i++){
			vcr.remove(i);
		}
		*/
		
		try {
			
			statement = conn.createStatement(); // statement����ִ��SQL���
			
			String  typestr = getTypesStr();
			String[] parts = site.split("\\.");
			if (parts.length > 2) {
				site = parts[parts.length - 2] + "." + parts[parts.length - 1];
			}

			String sql = "select " +typestr + " from companyrule  where domain = '" + site+ "'"; // Ҫִ�е�SQL���

			ResultSet rs;
			//System.out.println(sql);
			rs = statement.executeQuery(sql); // �����
			List vct = getTypeList();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				//System.out.println(vct.size());
				for(int i=0;i<vct.size();i++){
					  String type =(String) vct.get(i);
					  //System.out.println(type);
					  map.put(type, rs.getString(type));
					}
				vcr.add(map);
			}
			
			
		}catch(SQLException e1){
			e1.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			init();
		}
		return vcr;
	}
	
	//��json���ݴ���mysql
	public boolean writeDataStr(String str,String url) {
		//if (isExitinDb(url,type)){
		//	return false;
		//}
		String sql = null;
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement����ִ��SQL���
			sql = "insert into  dataresult(resultstr,url)  values ( '" + str + "','"+ url +"')"; // Ҫִ�е�SQL���
			//System.out.println(sql);

			statement.execute(sql); // �����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	public boolean updateDataStrById(String str,String id) {
		//if (isExitinDb(url,type)){
		//	return false;
		//}
		String sql = null;
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement����ִ��SQL���
			sql = "update dataresult set flag = 0 , resultstr ='"+ str +"'  where id = "+ id ;
			System.out.println(sql);

			statement.execute(sql); // �����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	public int getIdByUrl(String url){
		Statement statement = null;
		int id = -1;
		try {
			statement = conn.createStatement();
			String sql = "select id  from dataresult  where  url = '" + url +"'";
			ResultSet rs;
			rs = statement.executeQuery(sql); // �����
			if (rs.next()){
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return id;
	}
	
	public int returnIdAfterInsert(String str,String url) {
		
		String sql = "insert into  dataresult(resultstr,url)  values ( '" + str + "','"+ url +"')"; // Ҫִ�е�SQL���
		PreparedStatement pstmt;
		int id = -1;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.execute(); 
	        // ��������ִ�д� Statement ����������������Զ����ɵļ� 
	        ResultSet rs = pstmt.getGeneratedKeys(); 
	        
	        if (rs.next()) {
	            id = rs.getInt(1); 
	            System.out.println("����������" + id); 
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ���ʹ�þ�̬��SQL������Ҫ��̬�������
        
        return id;
	}
	
	
}
