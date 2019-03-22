package com.ffmsg.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

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
import com.gargoylesoftware.htmlunit.Cache;

public class Search_Operater extends HttpServlet {

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

		// TODO Auto-generated method stub
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String start_time = req.getParameter("start_time");
		String end_time = req.getParameter("end_time");

		HttpSession session = req.getSession();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		JSONArray jArray = new JSONArray();
		JSONObject back_to = new JSONObject();
		String page = req.getParameter("page") != null ? req.getParameter("page") : "0";
		System.out.println("page=======" + page);
		int size = 10;
		int totol = 0;
		String sqlString = "select SQL_CALC_FOUND_ROWS id,username,operator_time,last_balance,remarks  from operater_balance where 1=1";
		System.out.println("-------------username=" + username);
		if (username != null && !username.trim().toString().equals("")) {
			sqlString = sqlString + " and username like binary '%" + username + "%'";
		}
		if (start_time != null && !start_time.trim().toString().equals("")) {
			sqlString = sqlString + " and operator_time >= '" + start_time + "'";
		}
		if (end_time != null && !end_time.trim().toString().equals("")) {
			sqlString = sqlString + " and operator_time <= '" + end_time + "'";
		}
		sqlString = sqlString + "  order by id desc limit " + Integer.parseInt(page) * size + "," + size;
		System.out.println(sqlString);
		if (session.getAttribute("role") != null && !session.getAttribute("role").equals("")) {
			try {
				connection = DB.GetConnect();
				ps = connection.prepareStatement(sqlString);
				rs = ps.executeQuery();
				while (rs.next()) {
					JSONObject back_operator = new JSONObject();
					back_operator.put("id", rs.getInt(1));
					back_operator.put("username", rs.getString(2));
					back_operator.put("operator_time", rs.getString(3));
					back_operator.put("last_balance",rs.getString(4) );
					back_operator.put("remarks", rs.getString(5));
				
					jArray.add(back_operator);
				}
				rs = ps.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					totol = rs.getInt(1);
				}
				back_to.put("total", totol);
				back_to.put("data", jArray);
				resp.getWriter().write(back_to.toString());

			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}
		}
	}

}
