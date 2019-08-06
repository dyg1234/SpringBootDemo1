package com.hello.QrCodeUtils;


import org.apache.commons.lang3.RandomUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * 滑块验证工具类，抠图效果为拼图形状
 * 
 * @author wwenbo@linewell.com  
 * @since 2019年3月11日
 */
public class VerifyImageUtilV2 {

    public static void main(String[] args){
        try {
            //小图的长宽
            String imgPath = "d://sm.jpg";
            BufferedImage imageTemplate = ImageIO.read(new FileInputStream(imgPath));

            //大图
            String imgPath1 = "d://m.jpg";
            BufferedImage imageOriginal = ImageIO.read(new FileInputStream(imgPath1));

            Map<String, BufferedImage> img = VerifyImageUtilV2.pictureTemplatesCutV2(imageTemplate, imageOriginal, 0, 0, 40, 70);

            BufferedImage newImage = img.get("newImage");
            BufferedImage oriCopyImage = img.get("oriCopyImage");


            //处理后小图
            String s = VerifyImageUtilV2.imageToBase64(newImage);
            //处理后大图
            String s1 = VerifyImageUtilV2.imageToBase64(oriCopyImage);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	
    /**
     * 根据模板切图
     *
     * @param imageTemplate
     * @param imageOriginal
     * @param xPos
     * @param yPos
     * @return
     * @throws Exception
     */
    public static Map<String, BufferedImage> pictureTemplatesCutV2(BufferedImage imageTemplate, 
    		BufferedImage imageOriginal, // String templateType, String targetType,
    		int templateWidth, int templateHeight, int xPos, int yPos) throws Exception {
        
    	Map<String, BufferedImage> pictureMap = new HashMap<String, BufferedImage>();
    	
        // 模板图
        templateWidth = imageTemplate.getWidth();
        templateHeight = imageTemplate.getHeight();
        
        // 最终图像
        BufferedImage newImage = new BufferedImage(templateWidth, templateHeight, imageTemplate.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setBackground(Color.white);
        
        // 设置透明色
        newImage = graphics.getDeviceConfiguration().createCompatibleImage(templateWidth, templateHeight, Transparency.TRANSLUCENT);
        graphics.dispose();
        graphics = newImage.createGraphics();

        int bold = 5;
        
        // 获取感兴趣的目标区域
        BufferedImage targetImageNoDeal = getSquareCuttingImage(imageOriginal, xPos, yPos, templateWidth, templateHeight);

        // 根据模板图片抠图
        newImage = DealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        pictureMap.put("newImage", newImage);

        // 源图生成遮罩
        BufferedImage oriCopyImage = DealOriPictureByTemplateV2(imageOriginal, imageTemplate, xPos, yPos);
        pictureMap.put("oriCopyImage", oriCopyImage);
        return pictureMap;
    }
    
    /**
     * 抠图后原图生成
     *
     * @param oriImage
     * @param templateImage
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    private static BufferedImage DealOriPictureByTemplateV2(BufferedImage oriImage, BufferedImage templateImage, int x,
                                                   int y) throws Exception {
        // 源文件备份图像矩阵 支持alpha通道的rgb图像
        BufferedImage ori_copy_image = new BufferedImage(oriImage.getWidth(), oriImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);

        //copy 源图做不透明处理
        for (int i = 0; i < oriImageData.length; i++) {
            for (int j = 0; j < oriImageData[0].length; j++) {
                int rgb = oriImage.getRGB(i, j);
                int r = (0xff & rgb);
                int g = (0xff & (rgb >> 8));
                int b = (0xff & (rgb >> 16));
                //无透明处理
                rgb = r + (g << 8) + (b << 16) + (255 << 24);
                ori_copy_image.setRGB(i, j, rgb);
            }
        }

        for (int i = 0; i < templateImageData.length; i++) {
            for (int j = 0; j < templateImageData[0].length - 5; j++) {
                int rgb = templateImage.getRGB(i, j);
                //对源文件备份图像(x+i,y+j)坐标点进行透明处理
                if (rgb != 16777215 && rgb <= 0) {
                    int rgb_ori = ori_copy_image.getRGB(x + i, y + j);
                    int r = (0xff & rgb_ori);
                    int g = (0xff & (rgb_ori >> 8));
                    int b = (0xff & (rgb_ori >> 16));
                    rgb_ori = r + (g << 8) + (b << 16) + (50 << 24);
                    ori_copy_image.setRGB(x + i, y + j, rgb_ori);
                    
                } else {
                    //do nothing
                }
            }
        }
        
        return ori_copy_image;
    }


    /**
     * 根据模板图片抠图
     *
     * @param oriImage
     * @param templateImage
     * @return
     */

    private static BufferedImage DealCutPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage,
                                                          BufferedImage targetImage) throws Exception {
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);
        
        // 模板图像宽度

        for (int i = 0; i < templateImageData.length; i++) {
            // 模板图片高度
            for (int j = 0; j < templateImageData[0].length; j++) {
                // 如果模板图像当前像素点不是白色 copy源文件信息到目标图片中
                int rgb = templateImageData[i][j];
                if (rgb != 16777215 && rgb <= 0) {
                	
                    targetImage.setRGB(i, j, oriImageData[i][j]);
                }
            }
        }
        
        return targetImage;
    }


    /**
     * 生成图像矩阵
     *
     * @param
     * @return
     * @throws Exception
     */
    private static int[][] getData(BufferedImage bimg) throws Exception {
    	
        int[][] data = new int[bimg.getWidth()][bimg.getHeight()];
        for (int i = 0; i < bimg.getWidth(); i++) {
            for (int j = 0; j < bimg.getHeight(); j++) {
                data[i][j] = bimg.getRGB(i, j);
            }
        }
        return data;
    }

    
    /**
     * 随机生成抠图坐标
     */
    public static VerifyImageRandomPosInfoDTO getCutoutCoordinatesDTO(int oriWidth, int oriHeight,
    		int templateWidth, int templateHeight) {

    	int X = 0;
    	int Y = 0;
    	double xPercent =0;
    	double yPercent =0;
    	
    	Random random = new Random();
        int widthDifference = oriWidth - templateWidth;
        int heightDifference = oriHeight - templateHeight;

        int startX = (templateWidth * 2 < oriWidth - templateWidth) ? templateWidth * 2 : templateWidth;
        if (widthDifference <= 0) {
        	X = startX;

        } else {
        	X = RandomUtils.nextInt(startX, oriWidth - templateWidth);
        }

        if (heightDifference <= 0) {
        	Y = 0;
        } else {
        	Y = random.nextInt(oriHeight - templateHeight);
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        xPercent = Float.parseFloat(numberFormat.format((float) X / (float) oriWidth));
        yPercent = Float.parseFloat(numberFormat.format((float) Y / (float) oriHeight));
        
        VerifyImageRandomPosInfoDTO dto = new VerifyImageRandomPosInfoDTO();
        dto.setxPos(X);
        dto.setyPos(Y);
        dto.setxPercent(xPercent);
        dto.setyPercent(yPercent);
        return dto;
    }
    
    public static String imageToBase64(BufferedImage image) throws  Exception{
    	
        byte[] imagedata = null;
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        ImageIO.write(image,"png",bao);
        imagedata=bao.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String BASE64IMAGE=encoder.encodeBuffer(imagedata).trim();
        BASE64IMAGE = BASE64IMAGE.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return BASE64IMAGE;
    }
    
	public static BufferedImage getSquareCuttingImage(BufferedImage image, int x, int y, int length, int width)throws IOException {
		
	    InputStream is =  null ;
	    ImageInputStream iis = null ;
	
	    try {
	 
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ImageIO.write(image, "png", os);
	        is = new ByteArrayInputStream(os.toByteArray());
	        
		    /*
		    * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
		    * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
		    *（例如 "jpeg" 或 "tiff"）等 。
		    */
	        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("png");
	        ImageReader reader = it.next();
	        
	        // 获取图片流
	        iis = ImageIO.createImageInputStream(is);
		
		    /*
		    * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索'。
		    * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
		    * 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
		    */
		    reader.setInput(iis, true ) ;
		
		    /*
		    * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
		    * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
		    * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
		    * ImageReadParam 的实例。
		    */
		    ImageReadParam param = reader.getDefaultReadParam();
		
		    /*
		    * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
		    * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
		    */
		    Rectangle rect =  new Rectangle(x, y, length, width);
		
		    // 提供一个 BufferedImage，将其用作解码像素数据的目标。
		    param.setSourceRegion(rect);
		
		    /*
		    * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
		    * 它作为一个完整的 BufferedImage 返回。
		    */
		    BufferedImage bi=reader.read(0,param);
	        return bi;
		
	    } finally {
	        if (is != null )
	            is.close() ;
	        if (iis != null )
	            iis.close();
	    }
	}
	
    public static BufferedImage base64StringToImage(String base64String) {
    	
        try {
            BASE64Decoder decoder=new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}


