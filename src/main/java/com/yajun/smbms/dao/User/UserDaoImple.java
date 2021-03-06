package com.yajun.smbms.dao.User;

import com.mysql.jdbc.StringUtils;
import com.yajun.smbms.utils.BaseDao;
import com.yajun.smbms.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImple implements UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        User user =null;
        String sql = "select * from smbms_user where userCode=?";
        Object[] params = {userCode};
        try {
            rs = BaseDao.execute(connection, pst, rs, sql, params);
            if(rs.next())
            {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreateDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResources(null,pst,rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    //修改当前用户的密码
    public int pwdModify(Connection connection, String userPassword,String userCode) {
        PreparedStatement pst = null;
        int updaterow=0;
        if(connection!=null)
        {
            Object [] parmas = {userPassword,userCode};
            String sql = "update smbms_user set userPassword = ? where userCode = ?";
            updaterow = BaseDao.execute(connection, pst, sql, parmas);
            BaseDao.closeResources(null,pst,null);
        }

        return updaterow;
    }
    //根据用户名或者用户角色查询用户表中所有用户的数量
    public int getUserCount(Connection connection, String userName, int roleCode) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        StringBuffer sqlBuffer = new StringBuffer();
        ArrayList<Object> list = new ArrayList<Object>();
        int count =0;
        if(connection!=null)
        {
            sqlBuffer.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");
            if(!StringUtils.isNullOrEmpty(userName))
            {
                sqlBuffer.append(" and u.userName like ? ");
                list.add("%"+userName+"%");

            }
            if(roleCode>0&&roleCode<4)
            {
                sqlBuffer.append(" and r.id like ?");
                list.add(roleCode);
            }
           // System.out.println(sqlBuffer);
        }
        Object[] params = list.toArray();

        rs = BaseDao.execute(connection, pst, rs, sqlBuffer.toString(), params);
        try {
            if(rs.next())
            {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(null,pst,rs);
            return count;
        }
    }
    //根据条件username或者角色 查询用户表中所有的用户
    public List<User> getUserList(Connection connection, String userName, int roleCode, int currentPageNo, int pageSize) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        StringBuffer sqlBuffer = new StringBuffer();
        ArrayList<User> usersList = new ArrayList<User>();
        ArrayList<Object> listParams = new ArrayList<Object>();
        User user = null;
        try {
            sqlBuffer.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where r.id=u.userRole");
            if(connection != null)
            {
                if(!StringUtils.isNullOrEmpty(userName))
                {
                    sqlBuffer.append(" and u.userName like ?");
                    listParams.add("%"+userName+"%");
                }
                if(roleCode>0&&roleCode<4)
                {
                    sqlBuffer.append(" and r.id like ?");
                    listParams.add(roleCode);
                }
                sqlBuffer.append(" order by u.creationDate DESC limit ?,?");
                //mysql分页的index是从0开始的
                currentPageNo = (currentPageNo-1)*pageSize;
                listParams.add(currentPageNo);//从哪一个下标开始
                listParams.add(pageSize);//从currentPageNo连续取几个
            }
            //System.out.println(sqlBuffer);
            Object[] params = listParams.toArray();
//            for (int i = 0; i < params.length; i++) {
//                System.out.println(params[i]);
//            }
            rs = BaseDao.execute(connection, pstm, rs, sqlBuffer.toString(), params);
            while(rs.next())
            {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                //根据用户生日计算年龄
                //user.setAge(rs.getDate("birthday"));
                //数据库表格里面没有 后面添加的根据userRole确定用户角色名称
                user.setUserRoleName(rs.getString("userRoleName"));
                user.setUserRole(rs.getInt("userRole"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(null,pstm,rs);
        }
        return  usersList;
    }
    //根据用户UID查看用户信息
    public User getUserById(Connection connection, String uid) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where r.id=u.userRole and u.id = ?";
        Object [] params = {uid};
        User user=null;
        rs = BaseDao.execute(connection, pstm, rs, sql, params);
        try {
            if(rs.next())
            {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getTimestamp("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                user.setCreatedBy(rs.getInt("createdBy"));
                //User.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(null,pstm,rs);
            return user;
        }
    }
    //根据用户UID修改用户信息
    public int modifyUserById(Connection connection, String uid,User user) {
        PreparedStatement pstm = null;
        String sql = "update smbms_user set userName=?,gender = ?,birthday = ?,phone = ?,address = ?,userRole = ? where id = ?";
        Object [] params = {user.getUserName(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),uid};
        int delUserRow = 0;
        try {
            delUserRow = BaseDao.execute(connection, pstm, sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        BaseDao.closeResources(null,pstm,null);
        return delUserRow;
    }
    //根据用户UID删除用户信息
    public int delUserById(Connection connection, String uid) {
        PreparedStatement pstm = null;
        String sql = "delete from smbms_user where id = ?";
        Object [] params = {uid};
        int delUserRow = 0;
        try {
            delUserRow = BaseDao.execute(connection, pstm, sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        BaseDao.closeResources(null,pstm,null);
        return delUserRow;
    }

    public int addUser(Connection connection, User user) {
        PreparedStatement pstm = null;
        String sql = "insert into smbms_user(usercode, username, userpassword, gender, birthday, phone, address, userrole, createdby, creationdate, modifyby, modifydate)\n" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                           user.getGender(),user.getBirthday(),user.getPhone(),
                           user.getAddress(),user.getUserRole(),user.getCreatedBy(),
                           user.getCreateDate(),user.getModifyBy(),user.getModifyDate()};
        int addUpdateRows = 0;
        try {
            addUpdateRows = BaseDao.execute(connection, pstm, sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(null,pstm,null);
        }
        return addUpdateRows;
    }
}
