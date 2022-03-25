package com.yajun.smbms.service.user;

import com.yajun.smbms.pojo.User;

public interface UserService {
    //用户登录
    public User login(String userCode,String password);
}
