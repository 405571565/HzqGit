package com.ffmsg.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Tools {

	public static Logger logger = org.apache.log4j.Logger.getLogger(Tools.class);


	// 获取用户短信余量url
	private static String get_balance = "http://api.app2e.com/getBalance.api.php";
	// 发送短信url
	private static String sen_msg = "http://api.app2e.com/smsBigSend.api.php";
	// 管理员信息
	public static Map<String, String> Adminsmap = new HashMap<String, String>();
	// 用户信息
	public static Map<String, String> Usersmap = new HashMap<String, String>();
	// 系统balance
	public static Map<String, Integer> Balancemap = new HashMap<String, Integer>();
	// 第三方balance
	public static Map<String,Integer> OtherBalancemap = new HashMap<String, Integer>();
   //需要过滤的url 
	public static Map<String ,String> Urlmap=new HashMap<String, String>();
	
	
	// 发送请求
	public static String SendPost(Map<String, String> map) {
		StringBuffer backS = new StringBuffer();
		String string = "";
		if (map != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (Map.Entry<String, String> cans : map.entrySet()) {
				stringBuffer.append(cans.getKey());
				stringBuffer.append("=");
				stringBuffer.append(cans.getValue());
				stringBuffer.append("&");
			}
			string = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
		}

		System.out.println("string---:" + map.toString());

		HttpURLConnection httpurlconn = null;
		try {
			URL url = new URL(sen_msg);
			httpurlconn = (HttpURLConnection) url.openConnection();
			httpurlconn.setDoInput(true);
			httpurlconn.setDoOutput(true);
			httpurlconn.setRequestMethod("POST");
			httpurlconn.setUseCaches(false);
			httpurlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpurlconn.getOutputStream(), "utf-8");
			outputStreamWriter.write(string);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(httpurlconn.getInputStream(), "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String len = "";
			while ((len = bufferedReader.readLine()) != null) {
				backS.append(len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backS.toString();
	}

	// 测试
	// 发送请求
	public static String SendPostTest(Map<String, String> map) {
		String string = "";
		StringBuffer backS = new StringBuffer();
		if (map != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (Map.Entry<String, String> cans : map.entrySet()) {
				stringBuffer.append(cans.getKey());
				stringBuffer.append("=");
				stringBuffer.append(cans.getValue());
				stringBuffer.append("&");
			}
			string = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
		}

		HttpURLConnection httpurlconn = null;
		try {
			URL url = new URL("http://192.168.31.37:8080/FFMSG_JAVA/AliPayCreateOrder");
			httpurlconn = (HttpURLConnection) url.openConnection();
			httpurlconn.setDoInput(true);
			httpurlconn.setDoOutput(true);
			httpurlconn.setRequestMethod("POST");
			httpurlconn.setUseCaches(false);
			httpurlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			;
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpurlconn.getOutputStream(), "UTF-8");
			outputStreamWriter.write(string);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(httpurlconn.getInputStream(), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String len = "";
			while ((len = bufferedReader.readLine()) != null) {
				backS.append(len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backS.toString();
	}


	// 获得app用户短信余量
	public static String GetBalnce(String username, String password) {
		String balance_info = null;
		 String get_balance2 = get_balance + "?username=" + username + "&pwd=" + password;

		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpGet httpget = new HttpGet(get_balance2);
			
			CloseableHttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				balance_info = EntityUtils.toString(entity);
			}
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance_info;

	}

	// MD5加密
	public static String MD5EncodeSmall(String origin) {
		String result = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update((origin).getBytes("UTF-8"));
			byte b[] = md5.digest();

			int i;
			StringBuffer buf = new StringBuffer("");

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString(); // toUpperCase大写
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 系统错误日志保存
	 * 
	 * @param e
	 *            错误信息
	 * @param logger
	 * @return
	 */
	public static void saveLogging(HttpServletRequest req, String error) {
		StringBuffer sb = new StringBuffer();
		if (req != null) {
			sb.append(req.getServletPath() + "------>:" + error + "\n\n\n");
		} else {
			sb.append("\t:" + error + "\n\n\n");
		}
		logger.error(sb);
	}

	/**
	 * 记录错误日志
	 * 
	 * @param ex
	 */
	public static void SaveErrorLog(Exception ex) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream pout = new PrintStream(out);
		ex.printStackTrace(pout);
		String ret = new String(out.toByteArray());
		pout.close();
		try {
			out.close();
		} catch (Exception e) {
		}
		logger.error("异常------------>" + ret);
	}

	// 日期转字符串作比较
	public static long DateToS(String date) {
		long longdate = Long.valueOf(date.replaceAll("[-\\s:]", ""));

		return longdate;

	}



	/**
	 * JSON转MAP
	 * 
	 * @param paramObject
	 * @return
	 * @throws JSONException
	 * @throws org.json.JSONException 
	 */
	public static Map<String, String> JSONToMap(JSONObject paramObject) throws JSONException{
		Map<String, String> paramMap= new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Iterator<String> itr = paramObject.keys();
		while (itr.hasNext()) {
			String key = itr.next();
			paramMap.put(key, paramObject.getString(key));

		}
		return paramMap;
	}


	/**
	 * 得到随机生成的数字
	 * 
	 * @param length：长度
	 * @return
	 */
	public static int GetRandomForInt6(){
		return (int)((Math.random()*9+1)*100000);
	}




	/**
	 * 读取请求参数
	 * 
	 * @param req
	 * @return
	 * @throws JSONException
	 * @throws IOException 
	 */
	public static JSONObject GetRequestParam(HttpServletRequest req) throws JSONException, IOException{
		//读取参数
		JSONObject param_js = new JSONObject();
		@SuppressWarnings("rawtypes")
		Enumeration params = req.getParameterNames();
		while(params.hasMoreElements()){
			String name = (String) params.nextElement();
			String values = req.getParameter(name);
			param_js.put(name, values);
		}

		
		if(param_js == null || param_js.length() <= 0){
			StringBuffer sb=new StringBuffer();
			BufferedReader br=null;
			br=req.getReader();
			char[] buf=new char[1024];
			int len=0;
			while((len=br.read(buf))!=-1){
				sb.append(buf,0,len);
			}
			
			if(sb != null && sb.length() > 0){
				param_js = new JSONObject(sb.toString());
			}
		}

		return param_js;
	}
	
	
	/**
	 * 校验加密串
	 * 
	 * @param userName
	 * @param Amount
	 * @param sign:md5(用户名+金额*10+YYYY-MM-DD)
	 * @return
	 */
	public static boolean checkSign(String userName,int Amount,String sign){
		int amount = Amount*10;
		String local_sign = MD5EncodeSmall(userName+amount+GetNowDataForDay());
		Tools.saveLogging(null, "local_sign:"+local_sign+",sign:"+sign);
		
		if(local_sign.trim().equals(sign)){
			return true;
		}
		
		return false;
	}
	

	
	
	/**
	 * 获取当前时间 (yyyy-MM-dd) 日期
	 * @return 
	 */
	public static String GetNowDataForDay(){
		SimpleDateFormat sdf_day=new SimpleDateFormat("yyyy-MM-dd");
		return sdf_day.format(new Date());
	}
	
	
	public static String GetMapData(Map<String,String> map){
	
		StringBuffer stringBuffer = new StringBuffer();
	
		for(Map.Entry<String,String> map1: map.entrySet()){
			stringBuffer.append(map1.getKey());
			stringBuffer.append("=");
			stringBuffer.append(map1.getValue());
			stringBuffer.append("&");
		}
		String string=stringBuffer.substring(0,stringBuffer.length()-1);
		return  string;
	}

	
}
