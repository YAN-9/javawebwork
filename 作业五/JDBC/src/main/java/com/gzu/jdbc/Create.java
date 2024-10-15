package com.gzu.jdbc;

import java.sql.*;

public class Create {
    // 数据库连接的 URL，指定时区和字符编码
    static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
    // 数据库用户名
    static final String USER = "root";
    // 数据库密码
    static final String PASSWORD = "123456";
    // 插入数据的 SQL 语句
    static final String SQL = "INSERT INTO teacher(id, name, course, birthday) VALUES(?,?,?,?)";

    public static void main(String[] args) {
        try {
            // 加载 MySQL JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // 如果驱动加载失败，抛出运行时异常
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {
            // 关闭自动提交，开始事务
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(SQL);) {
                // 设置 SQL 语句中的参数
                ps.setInt(1, 1);  // 设置第一个参数，id 为 1
                ps.setString(2, "张三");  // 设置第二个参数，name 为 "张三"
                ps.setString(3, "高等数学");  // 设置第三个参数，course 为 "高等数学"
                ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));  // 设置第四个参数，birthday 为当前日期
                // 执行插入操作
                ps.executeUpdate();
                // 提交事务
                conn.commit();
            } catch (SQLException e) {
                // 如果出现 SQL 异常，回滚事务
                conn.rollback();
                e.printStackTrace();
            } finally {
                // 恢复自动提交模式
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
