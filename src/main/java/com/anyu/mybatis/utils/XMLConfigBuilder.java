package com.anyu.mybatis.utils;

import com.anyu.mybatis.annotations.Select;
import com.anyu.mybatis.configuration.Configuration;
import com.anyu.mybatis.configuration.Mapper;
import com.anyu.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLConfigBuilder {
    /**
     *
     * @param config
     * @return
     * @throws DocumentException
     */
    public static Configuration loadConfiguration(InputStream config)  {
        Configuration configuration = null;
        try{
            //定义封装连接信息的配置的对象
            configuration = new Configuration();
            //1.获取SAXReader对象
            SAXReader reader = new SAXReader();
            //2.根据字节输入流获取Document对象
            Document document = reader.read(config);
            //3.获取根节点
            Element root = document.getRootElement();
            //4.获取property节点数据
            List<Element> propertyElements = root.selectNodes("//property");
            //5.遍历节点
            for (Element propertyElement : propertyElements) {
                //判断节点是数据库那部分信息
                //去除name值
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)) {
                    //表示驱动
                    //获取property的value
                    String driver = propertyElement.attributeValue("value");
                    configuration.setDriver(driver);
                }
                if ("url".equals(name)) {
                    String url = propertyElement.attributeValue("value");
                    configuration.setUrl(url);
                }
                if ("username".equals(name)) {
                    String username = propertyElement.attributeValue("value");
                    configuration.setUserName(username);
                }
                if ("password".equals(name)) {
                    String password = propertyElement.attributeValue("value");
                    configuration.setPassWord(password);
                }
            }
            //解析mappers下的mapper并判断是注解还是xml配置
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            for (Element mapperElement : mapperElements) {
                Attribute attribute = mapperElement.attribute("resource");
                //如果attribute属性为空则使用的注解，不为空使用的xml
                if (attribute != null) {
                    System.out.println("使用xml配置dao接口");
                    //获取dao接口的位置
                    String daoMapperPath = attribute.getValue();
                    //将映射配置文件中的内容取出来放入map中保存
                    Map<String, Mapper> mappers = loadMapperConfiguration(daoMapperPath);
                    configuration.setMappers(mappers);
                } else {
                    System.out.println("使用注解配置dao接口");
                    String daoClassPass = mapperElement.attributeValue("class");
                    System.out.println(daoClassPass);
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPass);
                    configuration.setMappers(mappers);
                }
            }
        }catch (Exception e){
            System.out.println("based on function mybatis/xmlConfigBuilder/loadConfiguration");
        }
        return configuration;
    }
    /**
     * 根据xml配置存放读取的dao接口属性
     *
     * @param mapperPath
     * @return
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) {
        InputStream inputStream = null;
        //创建一个map容器存放mapper对象
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        try {
            //1.根据字节获取输入流
            inputStream = Resources.getResourceAsStream(mapperPath);
            //2.根据字节输入流Document对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            //3.h获取根节点
            Element root = document.getRootElement();
            //4.获取节点的namespace属性值(map中的key)
            String nameSpace = root.attributeValue("namespace");
            //5.获取所有select节点
            List<Element> selectElements=root.selectNodes("//select");
            //6.遍历select节点
            for (Element elementElement: selectElements) {
                //取出id 属性点的值
                String id=elementElement.attributeValue("id");
                //取出resulttype的值
                String resultType=elementElement.attributeValue("resultType");
                //取出文本内容
                String qeryString=elementElement.getText();
                //组装key值
                String key=nameSpace+"."+id;
                //创建value
                Mapper mapper=new Mapper();
                mapper.setQueryString(qeryString);
                mapper.setResultType(resultType);
                //吧key和mapper存入mappers中
                mappers.put(key,mapper);
            }
        } catch (Exception e) {
            System.out.println("based on xml");
            System.out.println("创建mappers失败");
            System.out.println("where:mybatis/utils/xmlConfigBuilder/loadMapperConfiguration");

        }
        return mappers;
    }

    /**
     * 根据annotation配置读取dao接口属性
     *
     * @param daoClassPath
     * @return
     */
    private static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) {
        //创建一个map容器存放mapper对象
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        try {
            //1.根据全限定类名得到接口名
            Class daoMainClass = Class.forName(daoClassPath);
            //2.根据接口获取所有方法
            Method[] methods = daoMainClass.getMethods();
            //3.遍历Method数组
            for (Method method : methods) {
                //方法数组是否有Select注解
                Boolean isSelect = method.isAnnotationPresent(Select.class);
                if (isSelect) {
                    //创建Mapper对象存储方法返回值，sql语句
                    Mapper mapper = new Mapper();
                    //取出注解的value值
                    Select selectAnno = method.getAnnotation(Select.class);
                    String sqlString = selectAnno.value();
                    mapper.setQueryString(sqlString);
                    //获取方法的返回值，要求带有泛型信息
                    Type type = method.getGenericReturnType();
                    //判断type是不是参数化的类型
                    if (type instanceof ParameterizedType) {
                        //强转
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        //得到参数类型的实际参数
                        Type[] types = parameterizedType.getActualTypeArguments();
                        //取出第一个
                        Class domainClass = (Class) types[0];
                        //获取doMainClass的类名
                        String resultType = domainClass.getName();
                        //给mapper赋值
                        mapper.setResultType(resultType);
                    }
                    //组装key
                    //获取方法名
                    String methodName = method.getName();
                    String className = method.getDeclaringClass().getName();
                    String key = className + "." + methodName;
                    //mappers赋值
                    mappers.put(key, mapper);
                }
            }
            return mappers;
        } catch (Exception e) {
            System.out.println("where:mybatis/utils/xmlConfigBuilder/loadMapperAnnotation");
        }
        return mappers;
    }


}
