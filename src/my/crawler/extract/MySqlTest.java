package my.crawler.extract;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MySqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String driver = "com.mysql.jdbc.Driver";         // ����������

        
        String url = "jdbc:mysql://localhost/mycrawler";     // URLָ��Ҫ���ʵ����ݿ���scutcs          
        
        String user = "root";       // MySQL����ʱ���û���

        String password = "";      // MySQL����ʱ������

        try { 
         
         Class.forName(driver);    // ������������

        
         Connection conn = DriverManager.getConnection(url, user, password);      // �������ݿ�

         if(!conn.isClosed()) 
          System.out.println("Succeeded connecting to the Database!");     //��֤�Ƿ����ӳɹ�

         
         Statement statement = conn.createStatement();               // statement����ִ��SQL���

        
         String sql = "select * from productRules";                  // Ҫִ�е�SQL���

        
         ResultSet rs = statement.executeQuery(sql);       // �����

         System.out.println("-----------------------------------------");
         System.out.println("ִ�н��������ʾ:");
         System.out.println("-----------------------------------------");
         System.out.println(" ѧ��" + "\t" + " ����" + "\t\t" + "�Ա�");
         System.out.println("-----------------------------------------");

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
         
         Map<String, String> map = new HashMap<String, String>();
         
         while(rs.next()) {
 
         
          name = rs.getString("name");                           // ѡ��sname��������
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
          
          Iterator it = map.entrySet().iterator();
          while (it.hasNext()){
        	  Map.Entry entry = (Map.Entry)it.next();
        	  Object key = entry.getKey();
        	  Object value = entry.getValue();
        	  System.out.println(key + "  :  " + value);
          }
         
          System.out.println(rs.getString("domain") + "\t" + name + "\t" + rs.getString("place"));        // ������
         }

         rs.close();
         conn.close();
         //����URL
         URL aURL = new URL("http://java.sun.com:80/docs/books/tutorial" + "/index.html?name=networking#DOWNLOADING");
         System.out.println(aURL.getHost());

        } catch(ClassNotFoundException e) {


         System.out.println("Sorry,can`t find the Driver!"); 
         e.printStackTrace();


        } catch(SQLException e) {


         e.printStackTrace();


        } catch(Exception e) {


         e.printStackTrace();


        }
	}

}
