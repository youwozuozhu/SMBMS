package com.yajun.smbms.dao.user;

import com.yajun.smbms.dao.BaseDao;
import com.yajun.smbms.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            String sql = "update smbms_user set userPassword = ? where usercode = ?";
            updaterow = BaseDao.execute(connection, pst, sql, parmas);
            BaseDao.closeResources(null,pst,null);
        }

        return updaterow;
    }
}
