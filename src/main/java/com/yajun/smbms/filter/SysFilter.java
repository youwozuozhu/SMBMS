package com.yajun.smbms.filter;

import com.yajun.smbms.utils.constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if(req.getSession().getAttribute(constants.USER_SESSION)==null)
        {
            resp.sendRedirect("/error.jsp");
        }
        filterChain.doFilter(req,resp);

    }

    public void destroy() {

    }
}
