package com.ffmsg.alipay;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ffmsg.tools.Tools;


/**
 * 生成支付宝订单
 * 
 * @author ddc
 *
 */
public class AliPayCreateOrder extends HttpServlet {

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
		
		int status = -1;
		String error = "";
		String result = "";
		try {
			JSONObject param = Tools.GetRequestParam(req);
			
			Tools.saveLogging(req, "请求参数："+param);
			
			int total_amount = param.getInt("Amount");
			String userNameString = param.getString("userName");
			String sign = param.getString("sign");
			String return_url = param.has("return_url")?param.getString("return_url"):AliPayTools.AliPayConfig.getReturn_url();
			
			if(Tools.checkSign(userNameString, total_amount, sign)){
				AlipayClient alipayClient = new DefaultAlipayClient(AliPayTools.AliPayConfig.getGatewayUrl(), 
						AliPayTools.AliPayConfig.getAppId(), 
						AliPayTools.AliPayConfig.getPriavateKey(), "json", AliPayTools.AliPayConfig.getCharset(), 
						AliPayTools.AliPayConfig.getPublicKey(), AliPayTools.AliPayConfig.getSign_type());

				//设置请求参数
				AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
				alipayRequest.setReturnUrl(return_url);
				alipayRequest.setNotifyUrl(AliPayTools.AliPayConfig.getNotify_url());

				String out_trade_no = System.currentTimeMillis()+Tools.GetRandomForInt6()+"";
				String subject = "短信充值";
				alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
						+ "\"total_amount\":\""+ total_amount +"\"," 
						+ "\"subject\":\""+ subject +"\"," 
						+ "\"body\":\""+ userNameString +"\"," 
						+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
				//请求
				result = alipayClient.pageExecute(alipayRequest).getBody();
				status = 1;
			}else{
				status = -1;
				error = "签名异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			error = e.toString();
			
			Tools.SaveErrorLog(e);
		}
		
		try {
			JSONObject resultObject = new JSONObject();
			resultObject.put("status", status);
			resultObject.put("error", error);
			resultObject.put("result", result);
			
			Tools.saveLogging(req, "接口调用结束："+resultObject);
			
			resp.getWriter().write(resultObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
