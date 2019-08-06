package com.hello.Base64_InputStream_BufferedImage;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片文件转Base64、InputStream、BufferedImage
 */
public class Utils {
    //将本地图片文件转成Base64字符串，需要一个参数：本地图片路径

    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        byte[] data = null;


        // 读取图片字节数组

        try {

            InputStream in = new FileInputStream(imgFilePath);

            data = new byte[in.available()];

            in.read(data);

            in.close();

        } catch (IOException e) {

            e.printStackTrace();

        }


        // 对字节数组Base64编码

        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串

    }

    //本地图片转成InputStream流对象。需要一个参数，base64编码的字符串，可由步骤一得到。

    public static InputStream BaseToInputStream(String base64string) {

        ByteArrayInputStream stream = null;

        try {

            BASE64Decoder decoder = new BASE64Decoder();

            byte[] bytes1 = decoder.decodeBuffer(base64string);

            stream = new ByteArrayInputStream(bytes1);

        } catch (Exception e) {

// TODO: handle exception

        }

        return stream;

    }

    //本地图片转成BufferedImage流对象。需要一个参数，base64编码的字符串，可由步骤一得到。

    public static BufferedImage GetBufferedImage(String base64string) {

        BufferedImage image = null;

        try {

            InputStream stream = BaseToInputStream(base64string);

            image = ImageIO.read(stream);

            System.out.println(">>>" + image.getWidth() + "," + image.getHeight() + "<<<");

        } catch (IOException e) {

            e.printStackTrace();

        }

        return image;

    }

    //将Base64String转成图片文件，需要两个参数：参数一：Base64编码的字符串，参数二：图片文件保存的路径。

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片

        if (imgStr == null) // 图像数据为空

            return false;

        BASE64Decoder decoder = new BASE64Decoder();

        try {

            // Base64解码

            byte[] bytes = decoder.decodeBuffer(imgStr);

            for (int i = 0; i < bytes.length; ++i) {

                if (bytes[i] < 0) {// 调整异常数据

                    bytes[i] += 256;

                }

            }

            // 生成jpeg图片

            OutputStream out = new FileOutputStream(imgFilePath);

            out.write(bytes);

            out.flush();

            out.close();

            return true;

        } catch (Exception e) {

            return false;

        }

    }
}
