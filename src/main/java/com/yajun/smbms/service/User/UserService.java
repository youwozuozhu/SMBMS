package com.yajun.smbms.service.User;

import com.yajun.smbms.pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    //用户密码修改
    public boolean pwdModify(String userPassword,String userCode );
    //根据用户名或者用户角色查询用户表中所有用户的数量
    public int getUserCount(String userName,int roleCode);
    //根据条件username或者角色 查询用户表中所有的用户
    public  List<User> getUserList(String userName,int roleCode,int currentPageNo,int pageSize);
    //根据UID查询用户信息
    public User getUserById(String uid);
    //根据UID修改用户信息
    public boolean modifyUserById(String uid,User user);
    //根据UID删除用户信息
    public boolean delUserById(String uid);
    //用户管理-添加用户
    public boolean addUser(User user);
    //用户管理-添加用户-根据usercode查询是否存在
    public User getUserByUserCode(String userCode);
}
