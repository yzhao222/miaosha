package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.UserModel;

public interface UserService {
    //通过用户id获取对象的方法
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telephone,String encryptPassword) throws BusinessException;
    //通过缓存获取用户对象
    UserModel getUserByIdInCache(Integer id);
}
