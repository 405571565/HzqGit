package com.ffmsg.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.json.JSONObject;

import com.ffmsg.tools.Tools;

public class MsgFilter implements Filter {

	private String[] execludedurl;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		arg1.setContentType("text/html;charset=UTF-8");
		arg1.setCharacterEncoding("UTF-8");
		arg0.setCharacterEncoding("UTF-8");
		JSONObject jsonObject = new JSONObject();
		int status = 0;
		String msg = "";
		ResourceBundle path = ResourceBundle.getBundle("config");
		String[] url = path.getString("url").split(",");
		boolean b = false;
		HttpServletRequest request1 = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		JSONObject request = GetDate(request1);
		HttpSession session = request1.getSession();
		for (String urlString : url) {
			if (request1.getServletPath().contains(urlString)) {
				b = true;
				break;
			}
		}
		if (b) {
			arg2.doFilter(arg0, arg1);
		} else {
			if (session != null && session.getAttribute("role") != null) {
				arg2.doFilter(arg0, arg1);
			} else {
				status = -100;
				msg = "请先登录";
				try {
					jsonObject.put("status", status);
					jsonObject.put("msg", msg);
				} catch (Exception e) {
					e.printStackTrace();
					Tools.SaveErrorLog(e);
				}
				response.getWriter().write(jsonObject.toString());
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	public JSONObject GetDate(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			Enumeration enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				String value = request.getParameter(name);
				jsonObject.put(name, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Tools.SaveErrorLog(e);
		}
		Tools.saveLogging(null, request.getServletPath() + "，接口請求參數:" + jsonObject.toString());
		System.out.println(request.getServletPath() + "，接口請求參數:" + jsonObject.toString());
		return jsonObject;
	}

}
