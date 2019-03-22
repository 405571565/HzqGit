package com.ffmsg.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.Tool;

import org.json.JSONObject;

import com.alipay.api.domain.PassInfoOpenApiModel;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;

public class GetCaptcha extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int status = 0;
		String msg = "";
		try {
			JSONObject request = Tools.GetRequestParam(req);
					String username = request.getString("username");
					String pwd = request.getString("password");
					String content = request.getString("message");
					String phone = request.getString("phone");
				Tools.saveLogging(req,
						"-----------username" + username + "--pwd" + pwd + "---content" + content + "-----phone"+phone);
				
				int send_count = 0;// 发送成功的条数
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", username);
				map.put("pwd", Tools.MD5EncodeSmall(pwd));
				map.put("isUrlEncode", "no");
				map.put("charSetStr", "utf-8");
				map.put("msg", content);
				map.put("p", phone);

				JSONObject get_back = new JSONObject();// 存储请求成功后的参数，判断status具体信息
				Tools.saveLogging(req, "请求第三方参数Map:"+map.toString());
				if (!Tools.Balancemap.containsKey(username)) {// map用户balance为空时不校验

					get_back = new JSONObject(Tools.SendPost(map));// 得到具体的返回信息100:成功104:请求超时102:手机号错误

					System.out.println("get_back------------" + get_back);
				} else {
					int balance = Tools.Balancemap.get(username);// 获取map中用户balance
					if (balance > 0 ) {
						get_back = new JSONObject(Tools.SendPost(map));
					} else {
						status = 105;
						msg = "短信余量不足";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);
					}
				}

				if (get_back != null && get_back.length() > 0) {
					if (get_back.get("status").toString().equals("100")) { // 发送成功
						// 准备执行插入语句
						if(!Tools.Usersmap.containsKey(username)){
							Tools.Usersmap.put(username, pwd);
						}else{// 更新密码
							if(!Tools.Usersmap.get(username).equals(pwd)){
								boolean up_pwd = DbTool.isuppassword_app(username, pwd);
								Tools.Usersmap.put(username, pwd);
							}
						}
						
						status = get_back.getInt("status");

						JSONObject userInfoJsonObject = DbTool.findapp(username, pwd);
						Tools.saveLogging(req, "userInfoJsonObject="+userInfoJsonObject.toString());
						Tools.saveLogging(req, username+"  "+pwd);
						System.out.println("userInfoJsonObject" + userInfoJsonObject);
						if (userInfoJsonObject.length() <= 0) {// 发送成功后数据库无app信息进行插入
							JSONObject get_others_balance = new JSONObject(
									Tools.GetBalnce(username, Tools.MD5EncodeSmall(pwd)));// 存储成功后参数
							int system_balance =50000>get_others_balance.getInt("balance")?get_others_balance.getInt("balance"):5000;							
							boolean inert_acc = DbTool.insertsql(map, pwd, system_balance);
							System.out.println("inert_acc--------" + inert_acc);
							Tools.Balancemap.put(username, system_balance);
						} else {
							send_count = get_back.getInt("count");// 发送成功的条数
							if (!Tools.Balancemap.containsKey(username)) {
								// 防止第一次发短信后服务器立马重启balancemap报空指针
								int system_balance = userInfoJsonObject.getInt("balance") - send_count;
								DbTool.upbalance(username, pwd, system_balance);
								Tools.Balancemap.put(username, system_balance);
							} else {
								int system_balance = userInfoJsonObject.getInt("balance") - send_count;
								DbTool.upbalance(username, pwd, system_balance);
								System.out.println("balance" + DbTool.findapp(username, pwd).get("balance"));
								Tools.Balancemap.put(username, system_balance);
							}
						}
					}
					else if (get_back.get("status").toString().toString().equals("102")) {
						status = 102;
						msg = "手机号码填写错误";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("104")) {
						status = 104;
						msg = "请求超时，请稍后再试";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("106")) {
						status = 106;
						msg = "第三方平台账号或密码错误";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("103")) {
						status = 103;
						msg = "提供的参数不足";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("107")) {
						status = 107;
						msg = "提交号码超限";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("120")) {
						status = 120;
						msg = "内容长度超长，请不要超过500个字";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("121")) {
						status = 121;
						msg = "内容中有屏蔽词";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);

					} else if (get_back.get("status").toString().toString().equals("131")) {
						status = 131;
						msg = "IP不合法 ";
						Tools.saveLogging(req, "GetCaptcha----------------status" + status + " error" + msg);
					}
				}

				String id = DbTool.findId(username, pwd);

				boolean inset_sendf = DbTool.insersendsql(map, status, msg, id);
//			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			msg = e.toString();
			Tools.SaveErrorLog(e);
		}
		try {
			JSONObject send_info = new JSONObject();
			send_info.put("status", status);
			send_info.put("msg", msg);
			Tools.saveLogging(req, "返回参数："+send_info.toString());
			resp.getWriter().write(send_info.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
