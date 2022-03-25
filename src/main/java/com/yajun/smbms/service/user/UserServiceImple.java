package com.yajun.smbms.service.user;

import com.yajun.smbms.dao.BaseDao;
import com.yajun.smbms.dao.user.userDao;
import com.yajun.smbms.dao.user.userDaoImple;
import com.yajun.smbms.pojo.User;
import org.junit.Test;

import java.sql.Connection;

public class UserServiceImple implements UserService {

    //业务层都会调用Dao层，所以需要引入Dao层
    //当业务层被实例化后
    private userDao userdao;

    public UserServiceImple() {
        userdao = new userDaoImple();
    }

    //userCode pawword 是前端传过来的
    public User login(String userCode, String password) {

        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userdao.getLoginUser(connection, userCode);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return user;
    }


    @Test
    public void test()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.login("admin", "1234567");
        System.out.println(user.getAddress());
    }
}
