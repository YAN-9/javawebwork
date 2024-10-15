package com.gzu.jdbc;

import java.sql.*;

public class Retrieve {
    // 数据库连接的 URL，指定时区和字符编码
    static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
    // 数据库用户名
    static final String USER = "root";
    // 数据库密码
    static final String PASSWORD = "123456";
    // 查询数据的 SQL 语句，根据 id 过滤记录
    static final String SQL = "SELECT id, name, course, birthday FROM teacher WHERE id >= ?";

    public static void main(String[] args) {

        try {
            // 加载 MySQL JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // 如果驱动加载失败，抛出运行时异常
            throw new RuntimeException(e);
        }
        // 建立数据库连接，并创建 PreparedStatement
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(SQL);
        ) {
            // 设置 SQL 语句中的参数，查询 id >= 20 的记录
            ps.setInt(1, 1);

            // 执行查询操作，获取结果集
            try (ResultSet rs = ps.executeQuery()) {
                // 输出查询结果
                while (rs.next()) {
                    // 获取并打印每一行的数据（id, name, course, birthday）
                    System.out.println(rs.getObject(1) + " " + rs.getObject(2) + " " + rs.getObject(3) + " " + rs.getObject(4));
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 如果查询时发生异常，打印异常信息
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 如果连接数据库或执行 SQL 语句时发生异常，打印异常信息
        }
    }
}
