package com.yajun.smbms.dao.user;

import com.mysql.jdbc.StringUtils;
import com.yajun.smbms.utils.BaseDao;
import com.yajun.smbms.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDaoImple implements userDao{

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

    public int getUserCount(Connection connection, String userName, int roleCode) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        StringBuffer sqlBuffer = new StringBuffer();
        ArrayList<Object> list = new ArrayList<Object>();
        int count =0;
        if(connection!=null)
        {
            sqlBuffer.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");
            if(StringUtils.isNullOrEmpty(userName))
            {
                sqlBuffer.append(" and u.userName like ? ");
                list.add("%"+userName+"%");

            }else if(roleCode>0)
            {
                sqlBuffer.append(" and r.id like ?");
                list.add(roleCode);
            }
            System.out.println(sqlBuffer);
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

//    public List<User> queryUsers(Connection connection) {
//
//        String sql = "select userCode,userName,gender,birthday,userRole from smbms_user";
//        BaseDao.execute(connection,pst,rs,sql,params);
//        return null;
//    }
}
