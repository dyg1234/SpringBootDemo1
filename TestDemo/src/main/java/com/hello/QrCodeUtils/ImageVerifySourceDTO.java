package com.hello.QrCodeUtils;

import java.io.Serializable;

/**
 * 图形验证码来源信息DTO
 * 
 * @author wwenbo@linewell.com  
 * @since 2019年3月4日
 */
public class ImageVerifySourceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 校验票据，用于匹配前端请求
	 */
	private String ticket;
	
	/**
	 * 用于校验的大图（原图中去除小图）
	 */
	private String verifyBigImage;
	
	/**
	 * 用于校验的小图
	 */
	private String verifySmallImage;
	
	/**
	 * 小图底部所在大图的y轴坐标和大图高度的比例
	 */
	private double yRate;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getVerifyBigImage() {
		return verifyBigImage;
	}

	public void setVerifyBigImage(String verifyBigImage) {
		this.verifyBigImage = verifyBigImage;
	}

	public String getVerifySmallImage() {
		return verifySmallImage;
	}

	public void setVerifySmallImage(String verifySmallImage) {
		this.verifySmallImage = verifySmallImage;
	}

	public double getyRate() {
		return yRate;
	}

	public void setyRate(double yRate) {
		this.yRate = yRate;
	}

}
