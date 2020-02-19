package com.anyu.mybatis.defaults;

import com.anyu.mybatis.configuration.Configuration;
import com.anyu.mybatis.sqlSession.SqlSession;
import com.anyu.mybatis.sqlSession.SqlSessionFactory;

/**
 * SqlSessionFactory的实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建一个新的数据库操作对象SqlSession对象
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
