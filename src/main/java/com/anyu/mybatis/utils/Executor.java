package com.anyu.mybatis.utils;

import com.anyu.mybatis.configuration.Mapper;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/***
 * 查询所有sql语句值行
 */
public class Executor {
    public <E> List<E> selectList(Mapper mapper, Connection connection){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
//            取出mapper中的数据
            String queryString=mapper.getQueryString();
            String resultType=mapper.getResultType();
            //结果集的实体类
            Class doMainClass=Class.forName(resultType);
            //获取prepareStatement对象
            preparedStatement= (PreparedStatement) connection.prepareStatement(queryString);
            //执行sql语句
            resultSet=preparedStatement.executeQuery();
            //封装结果集
            List<E> list=new ArrayList<E>();
            while (resultSet.next()){
                //实例化要封装的实体类对象
                E obj=(E)doMainClass.newInstance();
                //取出结果集的元信息result
                ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
                //取出总列数
                int columnCount =resultSetMetaData.getColumnCount();
                for (int i = 1; i < columnCount; i++) {
                    //获取每列的名称，列明序列号从1开始
                    String colName=resultSetMetaData.getColumnName(i);
                    //根据得到的列名，获取每列值
                    Object colValue=resultSet.getObject(colName);
                    //给obj赋值，使用Java的内省机制(借助PropertyDescriptor实现属性的封装)
                    PropertyDescriptor propertyDescriptor=new PropertyDescriptor(colName,doMainClass);
                    //获取他写入的方法
                    Method writeMethod=propertyDescriptor.getWriteMethod();
                    //把获取的列值，给对象赋值
                    writeMethod.invoke(obj,colValue);
                }
                //把赋值好的对象加入集合
                list.add(obj);
            }
            return list;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            release(preparedStatement,resultSet);
        }
    }

    private void release(PreparedStatement preparedStatement,ResultSet resultSet){
        if (null!=resultSet){
            try {
                resultSet.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (null!=preparedStatement){
            try {
                preparedStatement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
