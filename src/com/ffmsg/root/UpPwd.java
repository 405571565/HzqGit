package com.ffmsg.root;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Tools;

public class UpPwd extends HttpServlet {

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
		HttpSession session = req.getSession(true);
		System.out.println("session.getAttribute:" + session.getAttribute("role"));
		int status = -1;
		String msg = "";
		JSONObject back_uppwd = new JSONObject();
		String username = req.getParameter("username");
		String newpassword = req.getParameter("newpassword");
		String oldpassword = req.getParameter("oldpassword");
		System.out.println(username);
		System.out.println(newpassword);
		System.out.println(oldpassword);

			try {
                JSONObject pwd= DbTool.findapp(username, oldpassword);
                System.out.println(pwd.toString());
               if(pwd.length()>0&&!pwd.get("password").toString().trim().equals("")){ 
				boolean b = DbTool.isuppassword(newpassword, username, oldpassword);// 更改密码
				status = 1;
				msg = "true";
				back_uppwd.put("status", status);
				back_uppwd.put("msg", msg);
                 Tools.Usersmap.put(username, newpassword);
				resp.getWriter().write(back_uppwd.toString());
               }else {
            	   status = -1;
   				msg = "请输入正确密码";
   				back_uppwd.put("status", status);
   				back_uppwd.put("msg", msg);
   				resp.getWriter().write(back_uppwd.toString());
			}
			} catch (Exception e) {
				Tools.SaveErrorLog(e);
				e.printStackTrace();
			}

		
	}
}
