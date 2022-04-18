package com.yajun.smbms.servlet.User;

import com.yajun.smbms.pojo.User;
import com.yajun.smbms.service.User.UserService;
import com.yajun.smbms.service.User.UserServiceImple;
import com.yajun.smbms.utils.constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginServlet  extends HttpServlet {
    //servlet控制层调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("loginservlet start ....");
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //与数据库中的密码进行对比，调用业务层
        UserService userService = new UserServiceImple();
        User user = userService.login(userCode, userPassword);//这里已经把登录的人给查出来了
        //这里需要将前端获取的密码与后端密码进行对比
        if(user!=null && userPassword.equals(user.getUserPassword()))
        {
               //查有此人，一般登录成功后会把用户的信息放到session中，以便后面使用
            req.getSession().setAttribute(constants.USER_SESSION,user);
            //跳转到内部界面
            resp.sendRedirect("jsp/frame.jsp");
        }
        else
        {
            //查无此人 无法登录转发回登录界面，顺便提示用户名或者密码错误
            req.setAttribute("error","用户名或者密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
