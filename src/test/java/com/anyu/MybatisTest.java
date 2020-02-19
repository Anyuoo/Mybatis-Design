package com.anyu;

import com.anyu.dao.IUserDao;
import com.anyu.entity.User;
import com.anyu.mybatis.io.Resources;
import com.anyu.mybatis.sqlSession.SqlSession;
import com.anyu.mybatis.sqlSession.SqlSessionFactory;
import com.anyu.mybatis.sqlSession.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    public static void main(String[] args) throws IOException {
        //1.读取配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(in);
        //3.使用工厂产生SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.使用session创建Dao接口的代理对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        //5.使用代理对象执行方法
        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.printf("name" + user.getUsername());
            System.out.println(user.getAddress());
            System.out.println(user.getBirthday());
        }
        //6.释放资源
        sqlSession.close();
        in.close();
    }
}
