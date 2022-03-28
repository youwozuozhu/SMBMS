package com.yajun.smbms.service.user;

import com.yajun.smbms.pojo.User;

public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    //用户密码修改
    public boolean pwdModify(String userPassword,String userCode );
}
