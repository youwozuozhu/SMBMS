package com.yajun.smbms.service.user;

import com.yajun.smbms.pojo.User;
import java.util.ArrayList;
import java.util.List;
import com.yajun.smbms.pojo.User;
public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    //用户密码修改
    public boolean pwdModify(String userPassword,String userCode );
    //根据用户名或者用户角色查询用户表中所有用户的数量
    public int getUserCount(String userName,int roleCode);
    //根据条件username或者角色 查询用户表中所有的用户
    public  List<User> getUserList(String userName,int roleCode,int currentPageNo,int pageSize);
}
