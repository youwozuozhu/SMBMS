package com.yajun.smbms.service.Role;

import com.yajun.smbms.dao.Role.RoleDao;
import com.yajun.smbms.dao.Role.RoleDaoImple;
import com.yajun.smbms.pojo.Role;
import com.yajun.smbms.utils.BaseDao;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImple implements RoleService {
    private RoleDao roledao;

    public RoleServiceImple() {
        roledao = new RoleDaoImple();
    }

    public List<Role> getRoleList() throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<Role> roleList = roledao.getRoleList(connection);
        BaseDao.closeResources(connection,null,null);
        return roleList;
    }

    @Test
    public void test()
    {
        RoleServiceImple roleServiceImple = new RoleServiceImple();
        List<Role> roleList = null;
        try {
            roleList = roleServiceImple.getRoleList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <roleList.size() ; i++) {
            System.out.println(roleList.get(i).getRoleName());
        }
        //System.out.println(roleList.size());
    }
}
