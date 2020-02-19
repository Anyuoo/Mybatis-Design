package com.anyu.mybatis.sqlSession;

public interface SqlSessionFactory {
    /**
     * 创建一个新的sqlSession对象
     *
     * @return
     */
    public SqlSession openSession();
}
