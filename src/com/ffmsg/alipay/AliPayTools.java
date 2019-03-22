package com.ffmsg.alipay;

import java.util.ResourceBundle;

import com.ffmsg.tools.Tools;

public class AliPayTools {
	
	public static AlipayConfigEntity AliPayConfig;

	static{
		try {
			ResourceBundle param = ResourceBundle.getBundle("alipay_config");

			AliPayConfig = new AlipayConfigEntity();
			
			AliPayConfig.setGatewayUrl(param.getString("alipay.gatewayUrl"));
			AliPayConfig.setAppId(param.getString("alipay.appId"));
			AliPayConfig.setPriavateKey(param.getString("alipay.priavateKey"));
			AliPayConfig.setPublicKey(param.getString("alipay.publicKey"));
			AliPayConfig.setCharset(param.getString("alipay.charset"));
			AliPayConfig.setSign_type(param.getString("alipay.sign_type"));
			AliPayConfig.setNotify_url(param.getString("alipay.notify_url"));
			AliPayConfig.setReturn_url(param.getString("alipay.return_url"));

			Tools.saveLogging(null, "初始化支付宝支付参数:"+AliPayConfig.toString());
		} catch (Exception e) {
			e.printStackTrace();

			Tools.SaveErrorLog(e);
		}
	}
	
	
}
