package com.hello.controller;

import com.hello.QrCodeUtils.*;
import com.hello.exception.CodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class Check {


    @Autowired
    private RedisTemplate redisTemplate;


    //获取包含大小写字母和数字的随机数,长度
    private int IMAGE_CODE_TICKET_LENGTH=20;

    //base64图片格式前缀
    private String BASE64_PREX="data:image/jpg;base64,";

    //保存时间
    private int IMAGE_CODE_TICKET_EXPIRE_TIME=600;


    //正确比率
    private double RATE_VALUE_REQ=0.93;

    //uuid的存在时间
    private int IMAGE_CODE_ACCESS_TOKEN_EXPIRE_TIME=600;

    /**
     * 取抠图信息v2，效果为拼图获
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getcode")
    @ResponseBody
    private ImageVerifySourceDTO getImageVerifyInfoV2() throws Exception {

        // 生成ticket
        String ticket = getRandomString(IMAGE_CODE_TICKET_LENGTH);

        // 生成图形验证码
        VerifyImageInfoDTO verifyImageInfoDTO = createVerifyImages();
        if (verifyImageInfoDTO == null || verifyImageInfoDTO.getPosInfoDTO() == null) {
            throw new RuntimeException("获取图形验证码失败");
        }
        String cuttingImageStr = BASE64_PREX + verifyImageInfoDTO.getBase64CuttingImage();
        String cuttedImageStr = BASE64_PREX + verifyImageInfoDTO.getBase64CuttedImage();

        if (StringUtils.isEmpty(cuttingImageStr) || StringUtils.isEmpty(cuttedImageStr)) {
            throw new RuntimeException("获取图形验证码失败");
        }

        // 保存ticket和对应的正确位置到缓存
        double v = verifyImageInfoDTO.getPosInfoDTO().getxPercent();
        redisTemplate.boundValueOps(ticket).set(v,IMAGE_CODE_TICKET_EXPIRE_TIME, TimeUnit.SECONDS);



        // 返回
        ImageVerifySourceDTO dto = new ImageVerifySourceDTO();
        dto.setTicket(ticket);
        dto.setVerifySmallImage(cuttingImageStr);
        dto.setVerifyBigImage(cuttedImageStr);
        dto.setyRate(0);

        return dto;
    }



    // 校验图像验证码拖动位置的正确性
    @RequestMapping("/checkcode")
    @ResponseBody
    public ImageVerifyResultDTO getImageVerifyResult(@RequestParam String ticket,@RequestParam Double dragPos,@RequestParam Double imageWidth) throws CodeException {

        // 合规性判断
        if (StringUtils.isEmpty(ticket) || dragPos == 0 || imageWidth == 0) {
            throw new RuntimeException("参数有误");
        }

        // 核对ticket对应位置是否和传入的一致
        //服务器计算的比率
        Object o = redisTemplate.opsForValue().get(ticket);

        if (o == null) {
            throw new RuntimeException("ticket已过期");
        }
        //服务器中的比率
        Double oriRate= (Double)o;
        // 根据核对结果返回,用户拖拽比率
        double rate = (double)dragPos / imageWidth;
        double correctRate = oriRate > rate ?  rate / oriRate : oriRate / rate;
        if (correctRate < RATE_VALUE_REQ) { // 位置占比和正确值对比如果小于要求值，则返回错误
            throw new CodeException("位置有误",false);
        }

        // 清除ticket
        redisTemplate.delete(ticket);

        // 设置accessToken，保存缓存并返回
        ImageVerifyResultDTO dto = new ImageVerifyResultDTO();
        //根据需求更换UUID
        String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.boundValueOps("tokenKey").set(accessToken,IMAGE_CODE_ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        dto.setAccessToken(accessToken);
        dto.setVerifyPass(true);

        return dto;
    }

    // 创建验证码图片
    public VerifyImageInfoDTO createVerifyImages() throws Exception {

        //读取模板图,
        String path="static/img/"+(new Random().nextInt(4)+1)+".png";

        Resource resource = new ClassPathResource(path);
        BufferedImage imageTemplate = ImageIO.read(resource.getInputStream());

        //读取大图
        Resource resource1 = new ClassPathResource("static/img/m.png");
        BufferedImage imageOriginal = ImageIO.read(resource1.getInputStream());

        if (imageOriginal == null || imageTemplate == null) {
            throw new RuntimeException("背景图不存在");
        }

        // 获取位置
        VerifyImageRandomPosInfoDTO posInfoDTO = VerifyImageUtilV2.getCutoutCoordinatesDTO(imageOriginal.getWidth(),
                imageOriginal.getHeight(), imageTemplate.getWidth(), imageTemplate.getHeight());

        // 获取图片
        Map<String, BufferedImage> pictureMap = VerifyImageUtilV2.pictureTemplatesCutV2(imageTemplate, imageOriginal,
                imageTemplate.getWidth(), imageTemplate.getHeight(), posInfoDTO.getxPos(), posInfoDTO.getyPos());

        // 大图
        BufferedImage oriCopyImage = pictureMap.get("oriCopyImage");

        // 小图
        BufferedImage newImage = pictureMap.get("newImage");
        if (oriCopyImage == null || newImage == null) {
            return null;
        }

        VerifyImageInfoDTO verifyImageInfoDTO = new VerifyImageInfoDTO();
        verifyImageInfoDTO.setBase64CuttedImage(VerifyImageUtilV2.imageToBase64(oriCopyImage));
        verifyImageInfoDTO.setBase64CuttingImage(VerifyImageUtilV2.imageToBase64(newImage)); // 小图
        verifyImageInfoDTO.setPosInfoDTO(posInfoDTO);

        return verifyImageInfoDTO;

    }

    /**
     * 获取包含大小写字母和数字的随机数
     *
     * @param length 长度
     * @return
     */
    public static String getRandomString(int length){

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++){
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }
}
