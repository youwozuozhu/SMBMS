package com.yajun.smbms.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.yajun.smbms.pojo.Role;
import com.yajun.smbms.pojo.User;
import com.yajun.smbms.service.role.RoleServiceImple;
import com.yajun.smbms.service.user.UserServiceImple;
import com.yajun.smbms.utils.PageSupport;
import com.yajun.smbms.utils.constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

//实现servlet复用
public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd") && method!=null)
        {
            this.updatePwd(req,resp);
        }else if(method.equals("pwdmodify") && method!=null)
        {
            this.queryOldpassword(req,resp);
        }else if(method.equals("query")&& method != null)//此处的method要看前端传过来的参数
        {
            this.queryUserList(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //密码修改
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException
    {
        //从session中里面拿到用户信息
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        //得到前端新密码信息
        String newpassword = (String) req.getParameter("newpassword");
        //System.out.println(StringUtils.isNullOrEmpty(newpassword));
        boolean flag = false;
        if(user!=null && !StringUtils.isNullOrEmpty(newpassword))
        {
            UserServiceImple userServiceImple = new UserServiceImple();
            flag = userServiceImple.pwdModify(newpassword, user.getUserCode());
            System.out.println(flag);
            if(flag)
            {
                req.setAttribute("message","密码修改成功，请退出，使用新密码登录");
                //因为修改了新密码，所以应该删除session 让用户重新登录
                req.getSession().removeAttribute(constants.USER_SESSION);
                req.getRequestDispatcher("/error.jsp").forward(req,resp);
            }else
            {
                req.setAttribute("message","密码修改失败，请重新修改");
            }
        }else
        {
            req.setAttribute("message","新密码设置有问题");
        }
        //req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
    }

    public void queryOldpassword(HttpServletRequest req, HttpServletResponse resp)throws ServletException,IOException
    {
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        //从session中拿到用户老旧密码 也就是从数据库中取出密码
        String userPassword = user.getUserPassword();
        //System.out.println(userPassword);
        //从前端输入框拿到的老旧密码
        String oldpassword = (String) req.getParameter("oldpassword");
        //System.out.println(oldpassword);
        //万能map 存放结果集
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(user==null)
        {
            //session生效或者过期了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword))
        {
            resultMap.put("result","error");
        }else if(!oldpassword.equals(userPassword))
        {
            resultMap.put("result","false");
        }else
        {
            resultMap.put("result","true");
        }

        //设置响应头的类型为json格式
        resp.setContentType("application/json");
        PrintWriter printwriter = resp.getWriter();
        //resultMap = ["result","true","result","false"]
        //将map类型转换为json格式
        printwriter.write(JSONObject.toJSONString(resultMap));
        printwriter.flush();
        printwriter.close();
    }

    //重难点 
    public void queryUserList(HttpServletRequest req,HttpServletResponse resp)  {
        //从前端获取数据
        String queryname = req.getParameter("queryname");
        String temp = (req.getParameter("queryUserRole"));
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //第一次请求，一定是第一页，并且页面大小是固定的
        UserServiceImple userServiceImple = new UserServiceImple();
        RoleServiceImple roleServiceImple = new RoleServiceImple();
        int currentPageNo = 1;
        if(queryname == null)
        {
            queryname = "";
        }
        if(temp!=null && !temp.equals(""))
        {
            queryUserRole = Integer.parseInt(temp);//给查询用户角色赋值 0 1 2 3
        }
        if(pageIndex!=null)
        {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户总数(分页，上一页 下一页的情况)
        int totalcount = userServiceImple.getUserCount(queryname, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(constants.PAGE_SIZE);
        pageSupport.setTotalCount(totalcount);

        //控制首页和尾页 总页数
        int totalPageCount = totalcount/constants.PAGE_SIZE;
        System.out.println("-------"+totalPageCount);
        if(currentPageNo<1)
        {
            currentPageNo = 1;
        }else if(currentPageNo>totalcount)
        {
            currentPageNo = totalPageCount;
        }

        List<User> userList = userServiceImple.getUserList(queryname, queryUserRole, currentPageNo, constants.PAGE_SIZE);
        req.setAttribute("userList",userList);

        List<Role> roleList = null;
        try {
            roleList = roleServiceImple.getRoleList();
            req.setAttribute("roleList",roleList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("totalCount",totalcount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryname);
        req.setAttribute("queryUserRole",queryUserRole);
        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
