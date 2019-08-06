package com.hello.QrCodeUtils;

import java.io.Serializable;

/**
 * 图形验证码随机位置DTO
 * 
 * @author wwenbo@linewell.com  
 * @since 2019年3月9日
 */
public class VerifyImageRandomPosInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int xPos;
	
	private int yPos;
	
	private double xPercent;
	
	private double yPercent;

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public double getxPercent() {
		return xPercent;
	}

	public void setxPercent(double xPercent) {
		this.xPercent = xPercent;
	}

	public double getyPercent() {
		return yPercent;
	}

	public void setyPercent(double yPercent) {
		this.yPercent = yPercent;
	}
	
}
