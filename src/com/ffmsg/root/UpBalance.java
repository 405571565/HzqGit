package com.ffmsg.root;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.ffmsg.db.DB;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;
import com.google.gson.annotations.JsonAdapter;

public class UpBalance extends HttpServlet {

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
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		System.out.println("session.getAttribute:" + session.getAttribute("role"));
		String username = req.getParameter("username");// app用户
		String operator = req.getParameter("operator");// 操作员
		String first_balance = req.getParameter("first_balance");// 修改前的余额
		String last_balance = req.getParameter("last_balance");// 修改后的余额
		String remarks = req.getParameter("remarks");// 备注
		System.out.println(username);
		System.out.println(operator);
		System.out.println(first_balance);
		System.out.println(last_balance);
		System.out.println(remarks);
		int status = -1;
		String msg = "";
		JSONObject back_upbalance = new JSONObject();
		JSONArray ja = new JSONArray();
			try {
				if(first_balance!=null&&last_balance!=null){
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", username);
				map.put("operator", operator);
				map.put("first_balance", first_balance);
				map.put("last_balance", last_balance);
				map.put("remarks", remarks);
				int sum=Integer.parseInt(first_balance)+Integer.parseInt(last_balance);
				boolean up_stauts = DbTool.upbalance_ob(String.valueOf(sum), username);
				if (up_stauts) {
					String id = DbTool.findrootId(username);
					boolean b = DbTool.insetob(map, id ,sum);
					System.out.println("b-" + b);
					status = 1;
					msg = "true";
					back_upbalance.put("status", status);
					back_upbalance.put("msg", msg);
					resp.getWriter().write(back_upbalance.toString());			
				} else {
					status = -1;
					msg = "false";
					back_upbalance.put("status", status);
					back_upbalance.put("msg", msg);
					resp.getWriter().write(back_upbalance.toString());
				}
				}else{
					status = -1;
					msg = "修改短信量不能為空";
					back_upbalance.put("status", status);
					back_upbalance.put("msg", msg);
					resp.getWriter().write(back_upbalance.toString());

				}
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}
	}
}
