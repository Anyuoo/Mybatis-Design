package com.anyu.mybatis.io;

import java.io.InputStream;

/**
 * 类加载器读取配置文件的类
 */
public class Resources {
    /**
     * 根据参数获得一个字节码流
     *
     * @param filePath 配置文件路径
     * @return 返回字节码流
     */
    public static InputStream getResourceAsStream(String filePath) {
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}
