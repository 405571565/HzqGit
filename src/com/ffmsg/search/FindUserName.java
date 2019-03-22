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

import com.ffmsg.db.DB;
import com.ffmsg.tools.Tools;

public class FindUserName extends HttpServlet {

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
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "select username from app_account where role=-1";
		JSONObject back_user = new JSONObject();
		if (session.getAttribute("role") != null && !session.getAttribute("role").toString().trim().equals("")) {

			try {
				connection = DB.GetConnect();
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
				}
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}
		} 
	}

}
