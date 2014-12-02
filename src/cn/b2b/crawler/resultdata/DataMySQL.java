package cn.b2b.crawler.resultdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.b2b.crawler.config.ConfigResultData;

public class DataMySQL {
	
	public Connection conn = null;
	public ResultSet rs;
	
	private String url = "jdbc:mysql://localhost/mycrawler"; // URLָ��Ҫ���ʵ����ݿ���scutcs

	private String user = "root"; // MySQL����ʱ���û���

	private String password = ""; // MySQL����ʱ������

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public DataMySQL() {
		super();
		
		//init();

	}
	
	public void initMysql(ConfigResultData config){
		url = config.getJdbcmysql();
		user = config.getMysqluser();
		password = config.getMysqlpass();
		init();
	}

	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // ����������

		//String url = "jdbc:mysql://localhost/mycrawler"; // URLָ��Ҫ���ʵ����ݿ���scutcs

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

	// ���ص��ǽ����
	public ResultSet readData(String sqlstr) {
		Statement statement = null;
		try {
			
			statement = conn.createStatement(); // statement����ִ��SQL���

			String sql = sqlstr; // Ҫִ�е�SQL���

			rs = statement.executeQuery(sql); // �����
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			init();
		}
		return rs;
	}

	public boolean writeData(String sqlstr) {
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement����ִ��SQL���

			// System.out.println(sql);

			statement.execute(sqlstr); // �����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateData(String sqlstr) {
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement����ִ��SQL���

			// System.out.println(sql);

			statement.execute(sqlstr); // �����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void checkConn(){
		if ( conn == null ){
			init();
		}
	}
	
	public static void main(String args[]){
		
	}
}
