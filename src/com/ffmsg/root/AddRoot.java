package com.ffmsg.root;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.ffmsg.db.DB;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;
import com.gargoylesoftware.htmlunit.Cache;

import netscape.javascript.JSObject;

public class AddRoot extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		Connection connection = null;
		PreparedStatement ps = null;
		JSONObject back_addroot = new JSONObject();
		int status = -1;
		String msg = "";
		HttpSession session = req.getSession();	
			try {
				JSONObject jsonObject = DbTool.findapp(username, password);
				if (jsonObject == null && jsonObject.length() < 0) {
					connection = DB.GetConnect();
					String sql = "insert into app_account(username,password,add_time,role) value(?,?,?,?)";
					ps = connection.prepareStatement(sql);
					ps.setString(1, username);
					ps.setString(2, password);
					ps.setString(3, DbTool.dateFormat.format(new Date()));
					ps.setInt(4, 1);
					ps.execute();
					status = 1;
					msg = "ok";
					back_addroot.put("status", status);
					back_addroot.put("msg", msg);
					System.out.println("成功");
					resp.getWriter().write(back_addroot.toString());
				} else {
					status = -1;
					msg = "账号已存在";
					back_addroot.put("status", status);
					back_addroot.put("msg", msg);
					System.out.println("成功");
					resp.getWriter().write(back_addroot.toString());
					Tools.saveLogging(req, "AddRoot------------------" + msg);
				}
			} catch (Exception e) {
				Tools.logger.error(e);
				e.printStackTrace();
			}	
	}
}
