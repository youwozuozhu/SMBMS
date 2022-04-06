package com.yajun.smbms.service.user;

import com.yajun.smbms.dao.user.UserDao;
import com.yajun.smbms.dao.user.UserDaoImple;
import com.yajun.smbms.pojo.User;
import com.yajun.smbms.utils.BaseDao;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class UserServiceImple implements UserService {

    //业务层都会调用Dao层，所以需要引入Dao层
    //当业务层被实例化后
    private UserDao userdao;

    public UserServiceImple()
    {
        userdao = new UserDaoImple();
    }

    //userCode password 是前端传过来的
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

    public boolean pwdModify(String userPassword, String userCode) {
        Connection connection = null;
        boolean flag = false;
        //int i =0;
        try {
            connection = BaseDao.getConnection();
            if(userdao.pwdModify(connection, userPassword, userCode)>0)
            {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public int getUserCount(String userName, int roleCode) {
        Connection connection = null;
        int count =0;
        try {
            connection = BaseDao.getConnection();
            count = userdao.getUserCount(connection, userName, roleCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
            return count;
        }
    }

    public List<User> getUserList(String userName, int roleCode, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userdao.getUserList(connection, userName, roleCode, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return userList;
    }




    @Test
    public void test1()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.login("admin", "1234567");
        System.out.println(user.getAddress());
    }
    @Test
    public void test2()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        boolean flag = userServiceImple.pwdModify("12345679", "admin");
        System.out.println(flag);
    }
    @Test
    public void test3()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        int count = userServiceImple.getUserCount(null, 3);
        System.out.println(count);
    }
    @Test
    public void test4()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        List<User> list = userServiceImple.getUserList(null, 3, 0, 16);
        for (int i = 0; i <list.size() ; i++) {
            System.out.println(list.get(i).getUserName());
        }
        System.out.println(list.size());
    }

}
