package com.utiles;
import java.util.Properties;
import java.util.LinkedList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtilsPool {
	private static Properties prop;
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	private static LinkedList<Connection> pool = new LinkedList<>();
	/**
	 * 从配置文件中拿到驱动所需参数并加载驱动
	 */
	static{
		prop = new Properties(); 		//创建一个配置文件对象
		try {
			prop.load(JdbcUtilsPool.class.getResourceAsStream("config.properties"));
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			Class.forName(driver);			//通过Class.forName()加载驱动
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 初始化连接池为其池内创建20个连接
	 * @throws SQLException		创建连接时发生了一场
	 */
	public static void initPool() throws SQLException{
		for (int i = 0; i < 20; i++) {
			pool.add(DriverManager.getConnection(url,user,password));
		}
	}
	/**
	 * 从连接池内得到连接
	 * @return	返回一个数据库连接
	 */
	public static Connection getConnection(){
		Connection conn = null;
		if(pool.size()>0){
			conn = pool.pop();
		}else{
			System.out.println("获得连接失败！等待获取连接...");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getConnection();
		}
		return conn;
	}
	/**
	 * 释放数据库资源
	 * @param res	得到的ResultSet结果集
	 * @param stmt	创建的Statement执行句柄
	 * @param conn	从连接池拿到的数据库连接
	 */
	public void close(ResultSet res, Statement stmt, Connection conn){
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
			pool.add(conn);		//将使用完的连接归还到连接池内
		}
	}
}

