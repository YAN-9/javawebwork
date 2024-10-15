package com.gzu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    // 数据库连接的 URL，指定时区和字符编码
    static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
    // 数据库用户名
    static final String USER = "root";
    // 数据库密码
    static final String PASSWORD = "123456";
    // 更新数据的 SQL 语句
    static final String SQL = "UPDATE teacher SET name = ? WHERE id = ?";

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
                // 设置 SQL 语句中的参数，将 id 为 1 的记录的 name 更新为 "王五"
                ps.setString(1, "王五");
                ps.setInt(2, 1);
                // 执行更新操作
                ps.executeUpdate();
                // 提交事务
                conn.commit();
            } catch (SQLException e) {
                // 如果发生 SQL 异常，回滚事务
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 捕获并处理连接或 SQL 操作中的异常
        }
    }
}
