package com.ffmsg.alipay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.ffmsg.db.DbTool;
import com.ffmsg.tools.Config;
import com.ffmsg.tools.Tools;

/**
 * 支付宝回调
 * 
 * @author ddc
 *
 */
public class AlipayBack extends HttpServlet {

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
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		try {
			JSONObject param = Tools.GetRequestParam(req);
			
			Tools.saveLogging(req, "收到请求："+param.toString());
			
			Map<String, String> paramsMap = Tools.JSONToMap(param);

			boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AliPayTools.AliPayConfig.getPublicKey(), 
					AliPayTools.AliPayConfig.getCharset(), AliPayTools.AliPayConfig.getSign_type()); //调用SDK验证签名
			if(signVerified){
				//支付成功
				String userName = param.getString("body");
				double amount = Double.valueOf(param.getString("buyer_pay_amount"));//充值金额
				double price = Double.valueOf(Config.getInstance().getValue("MessagePrice")); //单价
				int msgNum = (int) (amount/price);

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("username", userName);
				paramMap.put("msgNum", msgNum);
				paramMap.put("remarks", param.getString("trade_no"));
				paramMap.put("type", 1);//支付宝充值

				DbTool.UpdateUserBalance(paramMap);

				resp.getWriter().write("success");

				// 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
			}else{
				// 验签失败则记录异常日志，并在response中返回failure.
				
				Tools.saveLogging(null, "支付宝签名校验失败--");
				resp.getWriter().write("failure");
			}
		} catch (Exception e) {
			e.printStackTrace();

			Tools.SaveErrorLog(e);
		}
	}


}
