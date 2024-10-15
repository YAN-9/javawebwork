package com.gzu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchInsertion {
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
        // 建立数据库连接
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {
            // 关闭自动提交，开始事务
            conn.setAutoCommit(false);
            // 创建 PreparedStatement 预编译 SQL 语句
            try (PreparedStatement ps = conn.prepareStatement(SQL);) {
                // 设置参数并执行批处理插入
                for (int i = 0; i < 500; i++) {
                    ps.setInt(1, 1 + i);  // 设置id为 1+i
                    ps.setString(2, "name" + (i + 1));  // 设置name为 "name" + (i+1)
                    ps.setString(3, "course" + (i + 1));  // 设置course为 "course" + (i+1)
                    ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));  // 设置birthday为当前日期
                    // 将该条数据添加到批处理
                    ps.addBatch();
                    // 每 100 条记录执行一次批处理，减少数据库压力
                    if (i % 100 == 0) {
                        ps.executeBatch();  // 执行批处理
                        ps.clearBatch();  // 清空批处理
                        System.out.println("完成第" + (i / 100 + 1) + "次批处理");
                    }
                }
                // 执行最后剩下的批处理
                ps.executeBatch();
                // 提交事务
                conn.commit();
                System.out.println("完成批量插入数据");
            } catch (SQLException e) {
                // 如果出现异常，回滚事务
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
