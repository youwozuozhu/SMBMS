package com.yajun.smbms.dao.user;

import com.yajun.smbms.pojo.User;

import java.sql.Connection;

public interface userDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode);
}
