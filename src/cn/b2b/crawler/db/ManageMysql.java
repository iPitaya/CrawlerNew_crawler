package cn.b2b.crawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.b2b.crawler.config.ConfigXML;

public class ManageMysql {
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

	public ManageMysql(ConfigXML config) {
		super();
		url = config.getUrldb();
		user = config.getUsername();
		password = config.getPasswd();
	}

	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // ����������


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
	
	public void checkConn(){
		if ( conn == null ){
			init();
		}
	}
}
