package com.ffmsg.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DB {

	private static DB dbcputils = null;
	private ComboPooledDataSource cpds = null; 
	
	private DB() {
		if (cpds == null) {
			cpds = new ComboPooledDataSource();
		}
		
		ResourceBundle param = ResourceBundle.getBundle("jdbc");
		
		cpds.setUser(param.getString("username"));
		cpds.setPassword(param.getString("password"));
		cpds.setJdbcUrl(param.getString("url"));
		try {
			cpds.setDriverClass(param.getString("driver"));
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setInitialPoolSize(10);
		cpds.setMaxIdleTime(50);//最大空闲时间,20 秒内未使用则连接被丢弃
		cpds.setMaxPoolSize(500);
		cpds.setMinPoolSize(5);
		
		cpds.setBreakAfterAcquireFailure(false);//为true会导致连接池占满后不提供服务。所以必须为false
		cpds.setAcquireRetryAttempts(10);//获取连接失败时重试10次，默认重试30次，减少重试次数。
		cpds.setUnreturnedConnectionTimeout(60);//连接回收超时时间，设置比maxIdleTime大
		
	}

	public synchronized static DB getInstance() {
		if (dbcputils == null)
			dbcputils = new DB();
		return dbcputils;
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	
	public static Connection GetConnect(){
		return getInstance().getConnection();
	}
	

}
