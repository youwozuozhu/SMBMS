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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.SimpleFormatter;

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
        }else if(method.equals("view") && method != null)
        {
            this.viewUserList(req,resp,"/jsp/userview.jsp");
        }else if(method.equals("modify") && method != null)
        {
            this.goModifyUser(req,resp);
        }else if(method.equals("modifyexe")&& method !=null)
        {
            this.modifyUserById(req,resp);
        }else if(method.equals("getrolelist")&& method !=null)
        {
            this.getRoleList(req,resp);
        }else if(method.equals("ucexist")&& method !=null)
        {
            this.userCodeExist(req,resp);
        }
        else if(method.equals("add")&& method !=null)
        {
            this.addUser(req,resp);
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
    //用户管理- 查询用户列表
    public void queryUserList(HttpServletRequest req,HttpServletResponse resp)  {
        //从前端获取数据
        String queryname = req.getParameter("queryname");
        String temp = (req.getParameter("queryUserRole"));
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //第一次请求，一定是第一页，并且页面大小是固定的
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

        UserServiceImple userServiceImple = new UserServiceImple();
        //获取用户总数(分页，上一页 下一页的情况)
        int totalcount = userServiceImple.getUserCount(queryname, queryUserRole);
        //System.out.println("totalcount:"+totalcount);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setTotalCount(totalcount);
        pageSupport.setCurrentPageNo(currentPageNo);
       // pageSupport.setPageSize(constants.PAGE_SIZE);

        //控制首页和尾页 总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        //System.out.println("totalPageCount:"+totalPageCount);
        if(currentPageNo<1)
        {
            currentPageNo = 1;
        }else if(currentPageNo>totalPageCount)
        {
            currentPageNo = totalPageCount;
        }
        //用户列表
        List<User> userList = userServiceImple.getUserList(queryname, queryUserRole, currentPageNo, constants.PAGE_SIZE);
//        for (int i = 0; i <userList.size() ; i++) {
//            System.out.println(userList.get(i).getUserName());
//        }
        req.setAttribute("userList",userList);
        //角色列表
        RoleServiceImple roleServiceImple = new RoleServiceImple();
        List<Role> roleList = null;
        try {
            roleList = roleServiceImple.getRoleList();
            req.setAttribute("roleList",roleList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("totalCount",totalcount);//查询记录总条数
        req.setAttribute("currentPageNo",currentPageNo);//当前页码数
        req.setAttribute("totalPageCount",totalPageCount);//总页数
        req.setAttribute("queryUserName",queryname);
        req.setAttribute("queryUserRole",queryUserRole);
        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户管理-点击查看用户信息
    public void viewUserList(HttpServletRequest req,HttpServletResponse resp,String url)
    {
        String uid = req.getParameter("uid");
        //System.out.println("--------"+uid);
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.getUserById(uid);
        //System.out.println("====="+user.getUserCode());
        req.setAttribute("user",user);
        //下面赋值属性不可行
//        req.setAttribute("user.userCode",user.getUserCode());
//        req.setAttribute("user.userName",user.getUserName());
        try {
            req.getRequestDispatcher(url).forward(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户管理-点击修改用户信息
    public void goModifyUser(HttpServletRequest req,HttpServletResponse resp)
    {
        User user = (User) req.getSession().getAttribute(constants.USER_SESSION);
        if(user==null)
        {
            try {
                req.getRequestDispatcher("/login.jsp").forward(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String uid = req.getParameter("uid");
        if(StringUtils.isNullOrEmpty(uid))
        {
            //uid 不合法 为空/null
            return;
        }else
        {
            User user2= new UserServiceImple().getUserById(uid);
            System.out.println(user2.getId());
            System.out.println("user2.userrole"+user2.getUserRoleName());
            req.setAttribute("user",user2);
            try {
                req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void modifyUserById(HttpServletRequest req,HttpServletResponse resp)
    {
        String uid = req.getParameter("uid");
        System.out.println("++++++++++"+uid);
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = new User();
        user.setUserName(req.getParameter("userName"));
        System.out.println(req.getParameter("gender"));
        user.setGender(Integer.valueOf(req.getParameter("gender")));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("birthday")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        System.out.println(req.getParameter("userRole"));
        user.setUserRoleName(Integer.valueOf(req.getParameter("userRole")));
       // user.setUserRole(Integer.valueOf(req.getParameter("userRole")));
        boolean b = userServiceImple.modifyUserById(uid, user);
        System.out.println("_______"+b);
        if(b)
        {
            try {
                resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=view&uid="+uid);
                //req.getRequestDispatcher(url).forward(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            try {
                req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //用户管理-删除用户信息
    public void delUserById(HttpServletRequest req,HttpServletResponse resp)
    {

    }
    //用户管理-新增用户
    public void addUser(HttpServletRequest req,HttpServletResponse resp)
    {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        //user.setUserRoleName(Integer.parseInt(userRole));
        user.setCreatedBy(((User)req.getSession().getAttribute(constants.USER_SESSION)).getId());
        user.setCreateDate(new Date());
        user.setModifyDate(null);
        user.setModifyBy(null);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserServiceImple userServiceImple = new UserServiceImple();
        boolean flag = userServiceImple.addUser(user);
        if(flag)
        {
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            try {
                req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //用户管理-新增用户 判断新增用户与数据库的Usercode是否重复
    public void userCodeExist(HttpServletRequest req,HttpServletResponse resp)
    {
        String userCode = req.getParameter("userCode");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        UserServiceImple userServiceImple = new UserServiceImple();
        User user = userServiceImple.getUserByUserCode(userCode);
       // System.out.println("flag==="+flag);
        if(user!=null)
        {
            resultMap.put("userCode","exist");
            //System.out.println("账号已经存在,不能使用！");
        }else
        {
            resultMap.put("userCode","notexist");
           // System.out.println("账号不存在，可以使用！");
        }

        resp.setContentType("application/json");
        try {
            resp.getWriter().write(JSONArray.toJSONString(resultMap));
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //用户管理-新增用户 获取角色列表的请求
    // 获取角色列表的请求
    public void getRoleList(HttpServletRequest req,HttpServletResponse resp)
    {
        RoleServiceImple roleServiceImple = new RoleServiceImple();
        List<Role> roleList = null;
        try {
            roleList = roleServiceImple.getRoleList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //把roleList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }
}
