package com.ffmsg.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Utils1 {
	public static C3P0Utils1 c3p0Utils1=null;
	public static ComboPooledDataSource com=null;
	
	private C3P0Utils1(){
		if(com==null){
			com=new ComboPooledDataSource();
		}
		com.setUser("root");
		com.setPassword("root");
		com.setJdbcUrl("jdbc:mysql://localhost:8080/ffduanx1?characterEncoding=UTF-8%setUnicode=true");
	}
	
}
