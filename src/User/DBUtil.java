package User;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * 
 * @Title: DBUtil.java
 * @Package User 
 * @Description: TODO(数据库操作工具类)    
 * @date 2018年3月11日 上午9:31:21 
 * @version V1.0   
 * @author 李云飞
 */
public class DBUtil {
	private static String driver;
	private static String url;	
	private static String username;
	private static String password;

	static{
		Properties prop=new Properties();
		//Reader in;
		try {
			prop.load(DBUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver = prop.getProperty("driver");
		url = prop.getProperty("url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");
	}

	public static Connection open(){
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, username,password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
