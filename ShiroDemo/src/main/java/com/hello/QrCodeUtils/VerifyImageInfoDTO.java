package com.hello.QrCodeUtils;

import java.io.Serializable;

/**
 * 图形验证码DTO
 * 
 * @author wwenbo@linewell.com  
 * @since 2019年3月9日
 */
public class VerifyImageInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String base64CuttingImage;
	
	private String base64CuttedImage;
	
	private VerifyImageRandomPosInfoDTO posInfoDTO;

	public String getBase64CuttingImage() {
		return base64CuttingImage;
	}

	public void setBase64CuttingImage(String base64CuttingImage) {
		this.base64CuttingImage = base64CuttingImage;
	}

	public String getBase64CuttedImage() {
		return base64CuttedImage;
	}

	public void setBase64CuttedImage(String base64CuttedImage) {
		this.base64CuttedImage = base64CuttedImage;
	}

	public VerifyImageRandomPosInfoDTO getPosInfoDTO() {
		return posInfoDTO;
	}

	public void setPosInfoDTO(VerifyImageRandomPosInfoDTO posInfoDTO) {
		this.posInfoDTO = posInfoDTO;
	}
	
	
	
	

}
