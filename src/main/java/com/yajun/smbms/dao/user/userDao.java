package com.yajun.smbms.dao.user;

import com.yajun.smbms.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface userDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode);

    //修改当前用户的密码
    public int pwdModify(Connection connection,String userPassword,String userCode);

    //根据用户名或者用户角色查询用户表中所有用户的数量
    public int getUserCount(Connection connection,String userName,int roleCode);
    //查询用户表中所有的用户
    //public List<User> queryUsers(Connection connection,String );
}
