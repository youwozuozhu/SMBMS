package com.yajun.smbms.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String username;
    private static String password;
    private static String url;
//静态代码块，类加载的时候就初始化了
    static
    {
            //提供类加载器读取响应的资源
        Properties properties = new Properties();

        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver = properties.getProperty("driver");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            url = properties.getProperty("url");


    }

    //获取数据的链接
    public static Connection getConnection()
    {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet,String sql,Object[] params)
    {
        resultSet=null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i <params.length ; i++) {
                preparedStatement.setObject(i+1,params[i]);
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    //编写增删改公共类
    public static int execute(Connection connection,PreparedStatement preparedStatement,String sql,Object[] params)
    {
        int updaterows = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i <params.length ; i++) {
                preparedStatement.setObject(i+1,params[i]);
            }
            updaterows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updaterows;
    }
    //释放资源
    public static boolean closeResources(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet)
    {
        boolean flag =true;
        if(resultSet!=null)
        {
            try {
                resultSet.close();
                //GC 回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag =false;
            }
        }
        if(preparedStatement!=null)
        {
            try {
                preparedStatement.close();
                preparedStatement =null;
            } catch (SQLException e) {
                flag = false;
                e.printStackTrace();
            }
        }
        if(connection!=null)
        {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                flag =false;
                e.printStackTrace();
            }
        }
        return flag;
    }
}
