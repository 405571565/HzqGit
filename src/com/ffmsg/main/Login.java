package com.ffmsg.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Session;
import javax.persistence.MapKeyColumn;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.ffmsg.db.DB;
import com.ffmsg.tools.Tools;

public class Login extends HttpServlet {

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
		String username = req.getParameter("username");
		String pwd = req.getParameter("password");
		Connection con = null;
		Statement ps = null;
		ResultSet rs = null;
		HttpSession session = req.getSession(true);
		boolean status = false;
		JSONObject back_login = new JSONObject();
		String msg = "";

		System.out.println("1--------username:" + username);
		try {
			// 判断map是否含有登录用户 为空就从数据库取全部数据 防止频繁访问数据库
			if (!Tools.Usersmap.containsKey(username)) {
				con = DB.getInstance().getConnection();
				String sqlString = "select username,password ,role,balance,other_balance from app_account";// 获取表中app用户账号密码
				ps = con.createStatement();
				rs = ps.executeQuery(sqlString);
			
				while (rs.next()) {// 将user信息放入map
					Tools.Usersmap.put(rs.getString(1), rs.getString(2)); // key:账号
					
					Tools.Balancemap.put(rs.getString(1), rs.getInt(4));
					Tools.Adminsmap.put(rs.getString(1), rs.getString(3));
					Tools.OtherBalancemap.put(rs.getString(1), rs.getInt(5));
				}

				if (Tools.Usersmap.get(username) != null && Tools.Usersmap.get(username).equals(pwd)) {
					status = true;
					msg = "成功";
					back_login.put("status", status);
					back_login.put("msg", msg);
					System.out.println(status);
					if (Tools.Adminsmap.get(username).trim().equals("1")) {
						System.out.println("3");
						session.setAttribute("role", "1");
						back_login.put("role", session.getAttribute("role"));
					} else {
						System.out.println("4");
						session.setAttribute("role", "-1");
						back_login.put("role", session.getAttribute("role"));
					}

				} else {
					System.out.println("5");
					status = false;
					msg = "请检查账号密码是否正确";
					back_login.put("status", status);
					back_login.put("msg", msg);

				}

			} 
			// map有该用户信息
			else if (Tools.Usersmap.containsKey(username)&&Tools.Usersmap.get(username).equals(pwd)) {
				System.out.println("6");
				status = true;
				msg = "成功";
				back_login.put("status", status);
				back_login.put("msg", msg);

				if (Tools.Usersmap.containsKey(username)&&Tools.Adminsmap.get(username).trim().equals("1")) {
					System.out.println("7");
					session.setAttribute("role", "1");
					back_login.put("role", session.getAttribute("role"));
				} else {
					System.out.println("8");
					session.setAttribute("role", "-1");
					back_login.put("role", session.getAttribute("role"));
				}

			} else {
				status = false;
				msg = "请检查账号密码是否正确";
				back_login.put("status", status);
				back_login.put("msg", msg);

				Tools.saveLogging(req, "Login----------账号或者密码错误");
			}

			// 判断成功与否跳转的页面
			if (status) {
				resp.getWriter().write(back_login.toString());
			} else {
				resp.getWriter().write(back_login.toString());
				Tools.saveLogging(req,
						"Login------------------账号或者密码错误username----" + username + "---password---" + pwd);
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
