package com.ffmsg.search;

import java.io.IOException;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.ffmsg.tools.Tools;

public class LeaveLogin extends HttpServlet {

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
		HttpSession session = req.getSession();
		JSONObject back_LeaveLogin = new JSONObject();
		try {		
			session.invalidate();	
			resp.getWriter().write("true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Tools.SaveErrorLog(e);
			e.printStackTrace();
		}

	}

}
