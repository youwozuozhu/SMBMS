package com.yajun.smbms.service.user;

import com.yajun.smbms.dao.user.UserDao;
import com.yajun.smbms.dao.user.UserDaoImple;
import com.yajun.smbms.pojo.User;
import com.yajun.smbms.utils.BaseDao;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
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

    public User getUserById(String uid) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            user = userdao.getUserById(connection, uid);
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return user;
    }

    public boolean modifyUserById(String uid,User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if(userdao.modifyUserById(connection, uid, user)>0)
            {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public boolean delUserById(String uid) {
        boolean flag = false;

        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int i = userdao.delUserById(connection, uid);
            System.out.println("****i*****"+i);
            if(i>0)
            {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public boolean addUser(User user) {
        boolean flag = false;

        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int updaterow = userdao.addUser(connection, user);
            connection.commit();
            if(updaterow>0)
            {
                flag = true;
                System.out.println("添加成功！");
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public User getUserByUserCode(String userCode) {
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
        int count = userServiceImple.getUserCount(null, 0);
        System.out.println(count);
    }
    @Test
    public void test4()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        List<User> list = userServiceImple.getUserList(null, 0, 1, 16);
        for (int i = 0; i <list.size() ; i++) {
            System.out.println(list.get(i).getUserName());
        }
        //System.out.println(list.size());
    }
    @Test
    public void test5()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.getUserById("12");
        System.out.println(user.getUserName());
    }
    @Test
    public void test6()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        boolean b = userServiceImple.modifyUserById("12", new User());
        System.out.println(b);
    }

    @Test
    public void test7()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.getUserByUserCode("admin");
        System.out.println(user);
    }
    @Test
    public void  test8()
    {
        UserServiceImple userServiceImple = new UserServiceImple();
        boolean b = userServiceImple.delUserById("16");
        System.out.println(b);
    }
}
