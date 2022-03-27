package com.yajun.smbms.dao.user;

import com.yajun.smbms.pojo.User;

import java.sql.Connection;

public interface userDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode);

    //修改当前用户的密码
    public int pwdModify(Connection connection,String userCode);
}
