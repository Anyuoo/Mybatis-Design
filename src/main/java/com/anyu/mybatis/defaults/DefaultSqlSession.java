package com.anyu.mybatis.defaults;

import com.anyu.mybatis.configuration.Configuration;
import com.anyu.mybatis.proxy.MapperProxy;
import com.anyu.mybatis.sqlSession.SqlSession;
import com.anyu.mybatis.utils.DataSourceUtil;
import com.mysql.jdbc.Connection;

import java.lang.reflect.Proxy;

/**
 * SqlSession的实现类
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Connection connection;
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.connection= (Connection) DataSourceUtil.getConnection(configuration);
    }

    /**
     * 创建代理对象
     * @param daoInterfaceClass dao接口的类
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> daoInterfaceClass) {
       return (T) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),new Class[]{daoInterfaceClass},new MapperProxy(configuration.getMappers(),connection));
    }

    /**
     * 释放资源
     */
    public void close() {
        if (null!=connection){
            try{
                connection.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
