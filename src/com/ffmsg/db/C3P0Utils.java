package com.ffmsg.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Utils {
	private static C3P0Utils dbcputils = null;
	private ComboPooledDataSource cpds = null; 
	
	private C3P0Utils() {
		if (cpds == null) {
			cpds = new ComboPooledDataSource();
		}
		cpds.setUser("root");
		cpds.setPassword("ddbg2015");
		cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/fftest_duanx1?useUnicode=true&characterEncoding=UTF-8&useSSL=true&rewriteBatchedStatements=true");
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
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

	public synchronized static C3P0Utils getInstance() {
		if (dbcputils == null)
			dbcputils = new C3P0Utils();
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
}
