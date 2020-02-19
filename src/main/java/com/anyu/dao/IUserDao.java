package com.anyu.dao;


import com.anyu.entity.User;


import java.util.List;

/***
 * 用户的持久层接口
 */
public interface IUserDao {
    /**
     * 查询所有操作
     *
     * @return
     */
    List<User> findAll();
}
