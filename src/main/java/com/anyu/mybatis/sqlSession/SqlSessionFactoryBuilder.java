package com.anyu.mybatis.sqlSession;

import com.anyu.mybatis.configuration.Configuration;
import com.anyu.mybatis.defaults.DefaultSqlSessionFactory;
import com.anyu.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 *
 */
public class SqlSessionFactoryBuilder {
    /**根据参数的字节输入流来构建SqlSessionFactory工厂
     * @param config 配置文件的字符流
     * @return
     */
    public SqlSessionFactory build(InputStream config) {
        Configuration configuration= XMLConfigBuilder.loadConfiguration(config);
        return new DefaultSqlSessionFactory(configuration);
    }
}
