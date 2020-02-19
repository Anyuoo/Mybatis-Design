package com.anyu.mybatis.utils;

import com.anyu.mybatis.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * 创建数据源的工具类
 */
public class DataSourceUtil {
    public static Connection getConnection(Configuration configuration){
        try {
           Class.forName(configuration.getDriver());
            return DriverManager.getConnection(configuration.getUrl(),configuration.getUserName(),configuration.getPassWord());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
 }
