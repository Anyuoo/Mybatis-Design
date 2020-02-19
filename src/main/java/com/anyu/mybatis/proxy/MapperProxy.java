package com.anyu.mybatis.proxy;

import com.anyu.mybatis.configuration.Mapper;
import com.anyu.mybatis.utils.Executor;
import com.mysql.jdbc.Connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy implements InvocationHandler {
    private Map<String,Mapper> mappers;
    private Connection connection;
    public MapperProxy(Map<String, Mapper> mappers,Connection connection) {
        this.mappers = mappers;
        this.connection=connection;
    }

    /**
     * 用于方法增强,执行方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获得方法所在累的名称
        String className=method.getDeclaringClass().getName();
        //获得方法名
        String methodName=method.getName();
        //组合key
        String key=className+"."+methodName;
        //根据key获得mappers中的值
        Mapper mapper=mappers.get(key);
        //判断mapper是否存在
        if (null==mapper){
            throw new IllegalArgumentException("传入参数key不合法");
        }
        //调用工具类查询所有
       return new Executor().selectList(mapper,connection);
    }
}
