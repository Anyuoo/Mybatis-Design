package com.anyu.mybatis.sqlSession;

/**
 * 自定义mybatis
 * 可以实现dao层的数据库交互的核心类
 * 可以创建dao接口的代理对象
 */
public interface SqlSession {
    /**
     * @param daoInterfaceClass dao接口的类
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}
