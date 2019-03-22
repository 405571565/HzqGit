package com.ffmsg.search;

import java.io.IOException;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.enterprise.inject.New;
import javax.jms.Session;
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

public class SearchSend extends HttpServlet {

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

		HttpSession session = req.getSession();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int totol = 0;
		int size = 10;
		int success = 0;
		int bad = 0;

		String start_time = req.getParameter("start_time");
		String end_time = req.getParameter("end_time");
		String page = req.getParameter("page") != null ? req.getParameter("page") : "0";
		String content = req.getParameter("content");
		String username = req.getParameter("username");
		String status = req.getParameter("status");
		System.out.println("start_time======" + start_time);
		System.out.println("end_time======" + end_time);

		JSONObject back_js_SeaSen = new JSONObject();
		JSONArray ja_SeaSen = new JSONArray();
		String sql_succes = "select SQL_CALC_FOUND_ROWS  s.id  from send_count s , app_account a where s.app_id=a.id and status =100";
		String sqlString = "select SQL_CALC_FOUND_ROWS  s.id,s.username,s.content,s.phone,s.send_time,s.status,s.back_info from send_count s,app_account a where s.app_id=a.id";

		if (username != null && !username.trim().equals("")) {
			sqlString = sqlString + " and s.username like  '%" + username + "%'";
			sql_succes += " and s.username like   '%" + username + "%'";
		}

		if (start_time != null && !start_time.trim().equals("")) {
			start_time += " 00:00:00";
			sqlString = sqlString + " and  '" + start_time + "'<= s.send_time ";
			sql_succes = sql_succes + " and  '" + start_time + "'<= s.send_time ";
		}
		if (end_time != null && !end_time.trim().equals("")) {
			end_time += " 23:59:59";
			sqlString = sqlString + " and s.send_time  <= '" + end_time + "'";
			sql_succes += " and s.send_time <= '" + end_time + "'";
		}

		if (content != null) {
			sqlString = sqlString + " and s.content like  '%'" + content + "'%'";
			sql_succes += " and s.content like  '%'" + content + "'%'";
		}
		if (status != null) {
			if (status.trim().equals("1")) {
				sqlString = sqlString + " and s.status=100 ";
			} else if (status.trim().equals("-1")) {
				sqlString = sqlString + " and status!=100 ";
				sql_succes = "select SQL_CALC_FOUND_ROWS  s.id  from send_count s , app_account a where s.app_id=a.id and status !=100";
			} else {
				sqlString = sqlString + " and 1=1 ";
			}
		}

		sqlString = sqlString + "  order by id desc limit " + Integer.parseInt(page) * size + "," + size;
		if (session.getAttribute("role").toString().trim().equals("-1")) {
			try {

				con = DB.getInstance().getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlString);
				while (rs.next()) {
					JSONObject date = new JSONObject();
					date.put("id", rs.getInt(1));
					date.put("username", rs.getString(2));
					date.put("content", rs.getString(3));
					date.put("phone", rs.getString(4));
					date.put("send_time", rs.getString(5));
					date.put("status", rs.getInt(6));
					date.put("back_info", rs.getString(7));
					ja_SeaSen.add(date);
				}
				back_js_SeaSen.put("data", ja_SeaSen);

				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					totol = rs.getInt(1);
				}
				back_js_SeaSen.put("total", totol);

				st = con.createStatement();
				rs = st.executeQuery(sql_succes);
				System.out.println("sql_succes++++" + sql_succes);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					success = rs.getInt(1);
				}
				if (status != null && status.equals("-1")) {
					bad = success;
					success = 0;
					back_js_SeaSen.put("bad", bad);
					back_js_SeaSen.put("success", success);
				} else {
					bad = totol - success;
					back_js_SeaSen.put("bad", bad);
					back_js_SeaSen.put("success", success);
				}
				resp.getWriter().write(back_js_SeaSen.toString());
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}

		} else if (session.getAttribute("role").toString().trim().equals("1")) {
			try {

				con = DB.getInstance().getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlString);

				while (rs.next()) {
					JSONObject date = new JSONObject();
					date.put("id", rs.getInt(1));
					date.put("username", rs.getString(2));
					date.put("content", rs.getString(3));
					date.put("phone", rs.getString(4));
					date.put("send_time", rs.getString(5));
					date.put("status", rs.getInt(6));
					date.put("back_info", rs.getString(7));
					ja_SeaSen.add(date);

				}
				back_js_SeaSen.put("data", ja_SeaSen);

				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					totol = rs.getInt(1);
				}

				back_js_SeaSen.put("total", totol);

				st = con.createStatement();
				rs = st.executeQuery(sql_succes);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					success = rs.getInt(1);
				}

				if (status != null && status.equals("-1")) {
					bad = success;
					success = 0;
					System.out.println("+++++++++++++++");
					back_js_SeaSen.put("bad", bad);
					back_js_SeaSen.put("success", success);
				} else {

					bad = totol - success;

					back_js_SeaSen.put("bad", bad);
					back_js_SeaSen.put("success", success);
				}

				resp.getWriter().write(back_js_SeaSen.toString());
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
			Tools.SaveErrorLog(e);
		}

	}

}
