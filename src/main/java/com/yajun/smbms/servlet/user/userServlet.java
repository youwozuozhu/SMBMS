package com.yajun.smbms.servlet.user;

import com.mysql.jdbc.StringUtils;
import com.yajun.smbms.pojo.User;
import com.yajun.smbms.service.user.UserServiceImple;
import com.yajun.smbms.utils.constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//实现servlet复用
public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中里面拿到用户信息
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        //得到前端新密码信息
        String newpassword = (String) req.getParameter("newpassword");
        System.out.println(StringUtils.isNullOrEmpty(newpassword));
        boolean flag = false;
        if(user!=null && StringUtils.isNullOrEmpty(newpassword))
        {
            UserServiceImple userServiceImple = new UserServiceImple();
            flag = userServiceImple.pwdModify(newpassword, user.getUserCode());
            if(flag)
            {
                req.setAttribute("message","密码修改成功，请退出，使用新密码登录");
                //因为修改了新密码，所以应该删除session 让用户重新登录
                req.getSession().removeAttribute(constants.USER_SESSION);
            }else
            {
                req.setAttribute("message","密码修改失败，请重新修改");
            }
        }else
        {
            req.setAttribute("message","新密码设置有问题");
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
