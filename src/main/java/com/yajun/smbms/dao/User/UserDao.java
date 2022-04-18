package com.yajun.smbms.dao.User;

import com.yajun.smbms.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode);

    //修改当前用户的密码
    public int pwdModify(Connection connection,String userPassword,String userCode);

    //根据用户名或者用户角色查询用户表中所有用户的数量
    public int getUserCount(Connection connection,String userName,int roleCode);
    //根据条件username或者角色 查询用户表中所有的用户
    public List<User> getUserList(Connection connection,String userName,int roleCode,int currentPageNo,int pageSize);
    //查询用户信息直接在界面显示
    public User getUserById(Connection connection,String uid);
    //用户管理-修改信息
    public int modifyUserById(Connection connection,String uid,User user);
    //用户管理-删除用户信息
    public int delUserById(Connection connection,String uid);
    //用户管理-增加新用户
    public int addUser(Connection connection,User user);
}
