package org.example.dao;

import org.example.dataobject.UserDO;

public interface UserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);
    UserDO selectByTelephone(String telephone);
    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}