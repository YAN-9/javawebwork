package com.gzu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    // 数据库连接的 URL，指定时区和字符编码
    static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
    // 数据库用户名
    static final String USER = "root";
    // 数据库密码
    static final String PASSWORD = "123456";
    // 删除数据的 SQL 语句
    static final String SQL = "DELETE FROM teacher WHERE id = ?";

    public static void main(String[] args) {
        try {
            // 加载 MySQL JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // 如果驱动加载失败，抛出运行时异常
            throw new RuntimeException(e);
        }
        // 建立数据库连接
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {
            // 关闭自动提交，开始事务
            conn.setAutoCommit(false);
            // 创建 PreparedStatement 预编译 SQL 语句
            try (PreparedStatement ps = conn.prepareStatement(SQL);) {
                // 设置 SQL 语句中的参数，删除 name 为 "张三" 的记录
                ps.setInt(1, 1);

                // 执行删除操作
                ps.executeUpdate();
                // 提交事务
                conn.commit();
            } catch (SQLException e) {
                // 如果出现 SQL 异常，回滚事务
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
