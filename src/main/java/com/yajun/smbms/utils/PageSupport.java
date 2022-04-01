package com.yajun.smbms.utils;

public class PageSupport {
    //当前页码
    private int currentPageNo = 1;
    //页码容量
    private int pageSize = 0;
    //信息总数量
    private int totalCount = 0;
    //总页数 totalcount/pagesize(+1)
    private int totalPageCount = 1;

    public int getCurrentPageNo() {
        return currentPageNo;
    }
//OOP三大特征：封装（属性私有、get/set、在set中限定一些不安全的情况） 多态 继承
    public void setCurrentPageNo(int currentPageNo) {
        if(currentPageNo>0)
        {
            this.currentPageNo = currentPageNo;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
