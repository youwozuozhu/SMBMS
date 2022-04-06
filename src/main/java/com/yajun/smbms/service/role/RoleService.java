package com.yajun.smbms.service.role;

import com.yajun.smbms.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    //查询用户表中用户的角色
    public List<Role> getRoleList() throws SQLException;
}
