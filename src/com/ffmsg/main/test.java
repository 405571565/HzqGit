package com.ffmsg.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



import org.json.JSONObject;

import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{

			Map data = new HashMap<String, String>();

//			data.put("password", "8YPUIm4R");
//			data.put("username","xiaojinbei");
//			//data.put("username", "xiaojinbei");
//			data.put("phone", "18759321601");
//			data.put("content", "【小金贝】尊敬的小金贝用户：您好， 509326为您的登录验证码，请于2分钟内填写。如非本人操作，请忽略本短信。");

			
			int Amount = 1;
			String userName = "xiaojinbei";
			
			
			int amount = Amount*10;
			String local_sign = Tools.MD5EncodeSmall(userName+amount+Tools.GetNowDataForDay());
			data.put("Amount", 1+"");
			data.put("userName", userName);
			data.put("sign", local_sign);
			
			System.out.println(""+Tools.SendPostTest(data));;
		}catch (Exception e) {
			e.printStackTrace();
		}



	}

}
