package com.utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtils {
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	static {
		try {
			Properties prop = new Properties();
			prop.load(JdbcUtils.class.getResourceAsStream("config.properties"));
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			Class.forName(driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 得到数据库连接
	 * @return 返回得到的数据库连接,如果没有得到则返回null
	 * @throws SQLException
	 */
	public static Connection open() throws SQLException{
		Connection conn = null;
		conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
		return conn;
	}
	/**
	 * 释放数据库资源
	 * @param res 得到的Resultset对象
	 * @param stmt 已创建的执行句柄
	 * @param conn 得到的数据库链接
	 */
	public static void close(ResultSet res,Statement stmt,Connection conn){
		if(res!=null){
			try {
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
