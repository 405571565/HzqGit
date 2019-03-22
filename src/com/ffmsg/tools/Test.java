package com.ffmsg.tools;

import org.apache.log4j.Logger;

public class Test {
	public static Logger logger =org.apache.log4j.Logger.getLogger(Test.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.debug("This is debug message."); 
		 // 记录info级别的信息 
		 logger.info("This is info message."); 
		 // 记录error级别的信息 
		 logger.error("This is error message."); 

	}

}
