package com.ffmsg.alipay;

public class AlipayConfigEntity {

	// 支付宝网关
	private  String gatewayUrl;

	// 应用ID
	private  String appId;

	// 商户私钥
	private  String priavateKey;
	// 支付宝公钥
	private  String publicKey;
	
	// 字符编码格式
	private  String charset;

	// 签名方式
	private  String sign_type;

	// 服务器异步通知页面路径
	private  String notify_url;

	// 页面跳转同步通知页面路径
	private  String return_url;

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPriavateKey() {
		return priavateKey;
	}

	public void setPriavateKey(String priavateKey) {
		this.priavateKey = priavateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	@Override
	public String toString() {
		return "AlipayConfigEntity [gatewayUrl=" + gatewayUrl + ", appId=" + appId + ", priavateKey=" + priavateKey
				+ ", publicKey=" + publicKey + ", charset=" + charset + ", sign_type=" + sign_type + ", notify_url="
				+ notify_url + ", return_url=" + return_url + "]";
	}


	
	
	
	
}
