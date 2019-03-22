package com.ffmsg.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.ffmsg.db.DB;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;
import com.google.gson.annotations.JsonAdapter;

public class SearchUser extends HttpServlet {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		HttpSession session = req.getSession();
		System.out.println(session.getAttribute("role"));
	
		String username = req.getParameter("username");
		String start_time = req.getParameter("start_time");
		String end_time = req.getParameter("end_time");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject back_SeaUer = new JSONObject();
		JSONArray ja = new JSONArray();
	
		int status = 0;
		String sqlString = "select id, username ,balance, add_time,password from app_account where 1=1";
		if (username != null && !username.equals("")) {

			sqlString = sqlString + " and username like binary '%" + username + "%'";
		}
		if (start_time != null && !start_time.trim().equals("")) {
			start_time += " 00:00:00";
			sqlString = sqlString + " and send_time >= '" + start_time + "'";

		}
		if (end_time != null && !end_time.trim().equals("")) {
			end_time += " 23:59:59";
			sqlString = sqlString + " and send_time <= '" + end_time + "'";
		}
		if (username != null && username.equals("")) {
			sqlString = sqlString + " and role =-1";
		}
		if (session.getAttribute("role").equals("-1")) {

			try {
				JSONObject pwd = new JSONObject();
				con = DB.getInstance().getConnection();
				ps = con.prepareStatement(sqlString);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject back_js_SeaUer = new JSONObject();
					back_js_SeaUer.put("id", rs.getInt(1));
					back_js_SeaUer.put("username", rs.getString(2));
					back_js_SeaUer.put("balance", rs.getString(3));
					back_js_SeaUer.put("update_time", rs.getString(4));

					JSONObject get_others_balance = new JSONObject(
							Tools.GetBalnce(username, Tools.MD5EncodeSmall(rs.getString(5))));// 存储成功后参数
					back_js_SeaUer.put("other_balance", get_others_balance.getInt("balance"));
					
				
				
					ja.add(back_js_SeaUer);

				}
				back_SeaUer.put("data", ja);
				resp.getWriter().write(back_SeaUer.toString());
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}
		} else if (session.getAttribute("role").equals("1")) {
			try {
				con = DB.getInstance().getConnection();
				ps = con.prepareStatement(sqlString);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject back_js_SeaUer = new JSONObject();
					back_js_SeaUer.put("id", rs.getInt(1));
					back_js_SeaUer.put("username", rs.getString(2));
					back_js_SeaUer.put("balance", rs.getString(3));
					back_js_SeaUer.put("update_time", rs.getString(4));
					System.out.println(rs.getString(2) + "-----" + rs.getString(5));
					JSONObject get_others_balance = new JSONObject(
							Tools.GetBalnce(rs.getString(2), Tools.MD5EncodeSmall(rs.getString(5))));// 存储成功后参数
					back_js_SeaUer.put("other_balance", get_others_balance.getInt("balance"));
				
					
					ja.add(back_js_SeaUer);

				}
				back_SeaUer.put("data", ja);
				resp.getWriter().write(back_SeaUer.toString());
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}

		}

		try {
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Tools.SaveErrorLog(e);

		}
	}
}
