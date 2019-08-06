package com.hello.QrCodeUtils;

import java.io.Serializable;

/**
 * 图形验证码校验结果DTO
 * 
 * @author wwenbo@linewell.com  
 * @since 2019年3月4日
 */
public class ImageVerifyResultDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 图形校验是否成功
	 */
	private boolean verifyPass;
	
	/**
	 * 获取的accessToken，校验成功才有值
	 */
	private String accessToken;



	public boolean isVerifyPass() {
		return verifyPass;
	}

	public void setVerifyPass(boolean verifyPass) {
		this.verifyPass = verifyPass;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	

	
}
