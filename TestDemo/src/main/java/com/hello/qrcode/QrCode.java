package com.hello.qrcode;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class QrCode {
    /**
     * 对图片裁剪，并把裁剪后的图片返回 。
     */
    private BufferedImage getMarkImage(BufferedImage image, int x, int y, int length, int width)throws IOException {

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
            Iterator<ImageReader> it= ImageIO.getImageReadersByFormatName("png");
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




    /**
     *
     * @param targetLength 原图的长度
     * @param targetWidth  原图的宽度
     * @param x            裁剪区域的x坐标
     * @param y            裁剪区域的y坐标
     * @param length        抠图的长度
     * @param width        抠图的宽度
     * @return
     */
    private int [][] getCutAreaData(int targetLength,int targetWidth,int x,int y ,int length,int width){
        int[][] data = new int[targetLength][targetWidth];
        for (int i=0;i<targetLength;i++){//1280
            for(int j=0;j<targetWidth;j++){//720
                if(i<x+length&&i>=x&&j<y+width&&j>y){
                    data[i][j]=1;
                }else {
                    data[i][j]=0;
                }
            }
        }
        return data;
    }


    public static void cutByTemplate(BufferedImage oriImage,int[][] templateImage){

        for (int i = 0; i < oriImage.getWidth(); i++) {
            for (int j = 0; j < oriImage.getHeight(); j++) {
                int rgb = templateImage[i][j];
                // 原图中对应位置变色处理

                int rgb_ori = oriImage.getRGB(i,  j);

                if (rgb == 1) {
                    //颜色处理
                    int r = (0xff & rgb_ori);
                    int g = (0xff & (rgb_ori >> 8));
                    int b = (0xff & (rgb_ori >> 16));
                    int Gray = (r*2 + g*5 + b*1) >> 3;



                    //原图对应位置颜色变化
                    oriImage.setRGB( i, j, Gray);
                }
            }
        }
    }



    private String imageToBase64(BufferedImage image) throws  Exception{
        byte[] imagedata = null;
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        ImageIO.write(image,"png",bao);
        imagedata=bao.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String BASE64IMAGE=encoder.encodeBuffer(imagedata).trim();
        BASE64IMAGE = BASE64IMAGE.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return BASE64IMAGE;
    }
    private BufferedImage base64StringToImage(String base64String) {
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
