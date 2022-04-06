package com.yajun.smbms.dao.Role;

import com.yajun.smbms.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    //查询用户表中用户的角色
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
