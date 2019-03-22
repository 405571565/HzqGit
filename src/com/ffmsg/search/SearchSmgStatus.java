package com.ffmsg.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

import com.ffmsg.db.DB;
import com.ffmsg.db.DbTool;
import com.mchange.v2.c3p0.cfg.C3P0Config;

public class SearchSmgStatus extends HttpServlet {

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
		super.doPost(req, resp);
		HttpSession session = req.getSession();
		String username = req.getParameter("username");
		String start_time = req.getParameter("start_time");
		String end_time = req.getParameter("end_time");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int total = 0;
		int success = 0;
		int bad = 0;
		String sql = "select SQL_CACL_FOUND_ROWS id from send_count where 1=1";
		String sql_success = "select SQL_CACL_FOUND_ROWSa.id from send_count s,app_account a where status=100";
		JSONObject back_status = new JSONObject();
		System.out.println(username);
		System.out.println(start_time);
		System.out.println(end_time);

		if (username != null && !username.trim().equals("")) {
			sql += " and username='" + username + "'";
		}
		if (start_time != null) {
			sql += " and send_time>='" + start_time + "'";
		}
		if (end_time != null) {
			sql += " and send_time<='" + end_time + "'";
		}

		if (session.getAttribute("role") != null && session.getAttribute("role").equals(-1)) {
			try {
				con = DB.getInstance().getConnection();
				st = con.createStatement();

				rs = st.executeQuery(sql);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					total = rs.getInt(1);
				}

				rs = st.executeQuery(sql_success);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					success = rs.getInt(1);
				}
				bad = total - success;

				back_status.put("total", total);
				back_status.put("success", success);
				back_status.put("bad", bad);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(session.getAttribute("role")!=null&&session.getAttribute("role").equals("1")){
			try {
				con = DB.getInstance().getConnection();
				st = con.createStatement();

				rs = st.executeQuery(sql);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					total = rs.getInt(1);
				}

				rs = st.executeQuery(sql_success);
				rs = st.executeQuery("SELECT FOUND_ROWS()");
				while (rs.next()) {
					success = rs.getInt(1);
				}
				bad = total - success;

				back_status.put("total", total);
				back_status.put("success", success);
				back_status.put("bad", bad);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
