package com.hello.QrCodeUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class UUIDUtil {

    /* 从当前Web容器的JAVA_OPTIONS中，获取JVM的配置：当前实例名 */
    private static final String INSTANCE_NAME = System.getProperty("instance.name");
    /* 实例名脱敏后的值 */
    private static String INSTANCE_NAME_BY_NUM = null;
    /* 计数器。AtomicInteger是java.util.concurrent下的类，JDK的算法工程师会避免并发问题 */
    private static AtomicInteger CNT = new AtomicInteger(0);

    /**
     * 初始化INSTANCE_NAME_BY_NUM。需考虑并发
     */
    private synchronized static void initInstanceNameByNum() {
        if (null != INSTANCE_NAME_BY_NUM) {
            return;
        }
        char[] chars = INSTANCE_NAME.toUpperCase().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append((int) c);
        }
        INSTANCE_NAME_BY_NUM = sb.toString();
    }

    /**
     * 生成分布式的UUID
     *
     * @return
     */
    public static String getConcurrentUUID() {
        if (null == INSTANCE_NAME) {
            return "The JVM option is null, named 'instance.name'";
        }
        if (null == INSTANCE_NAME_BY_NUM) {
            initInstanceNameByNum();
        }
        StringBuilder uuid = new StringBuilder();
        uuid.append(INSTANCE_NAME_BY_NUM);
        uuid.append(System.currentTimeMillis());
        uuid.append(CNT.incrementAndGet());
        return uuid.toString();
    }
}
