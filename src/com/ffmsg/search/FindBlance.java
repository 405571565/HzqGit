package com.ffmsg.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ffmsg.db.DB;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;

public class FindBlance extends HttpServlet {

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
		String username = req.getParameter("username");
		String passowpd = req.getParameter("password");
		System.out.println(username);
		System.out.println(passowpd);	
		JSONObject back_balance = new JSONObject();
		int stutas = -1;
		String msg = "";
		try {
			JSONObject jsonObject = DbTool.findapp(username, passowpd);
			if ( jsonObject.length() > 0&&jsonObject != null ) {
				stutas = 1;
				msg = "成功";
				back_balance.put("balance", jsonObject.get("balance"));
				back_balance.put("stutas", stutas);
				back_balance.put("msg", msg);
			} else {
				stutas = -1;
				msg = "账号或者密码错误";
				back_balance.put("stutas", stutas);
				back_balance.put("msg", msg);
				Tools.saveLogging(req, "FindBlance-----------------" + msg + " " + username + " " + passowpd);
			}
			resp.getWriter().write(back_balance.toString());
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
		}
	}

}
