package com.anyu.mybatis.configuration;

import java.util.Map;

/**
 * mybatis基于配置文件读取的配置类
 */
public class Configuration {
    private String Driver;
    private String Url;
    private String UserName;
    private String passWord;
    private Map<String, Mapper> Mappers;

    public Map<String, Mapper> getMappers() {
        return Mappers;
    }
    public void setMappers(Map<String, Mapper> mappers) {
        Mappers = mappers;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
