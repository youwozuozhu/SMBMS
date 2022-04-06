package com.yajun.smbms.dao.Role;

import com.yajun.smbms.pojo.Role;
import com.yajun.smbms.utils.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImple implements RoleDao{
    //查询用户表中用户的角色
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs =null;
        String sql = "select * from smbms_role";
        ArrayList<Role> RoleList = new ArrayList<Role>();
        Role role = null;
        Object [] params = {};
        if(connection!=null)
        {
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while(rs.next())
            {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
//                role.setCreateBy(rs.getInt("createBy"));
//                role.setCreationDate(rs.getDate("creationDate"));
//                role.setModifyBy(rs.getInt("modifyBy"));
//                role.setModifyDate(rs.getDate("modifyDate"));
                RoleList.add(role);
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return RoleList;
    }
}
