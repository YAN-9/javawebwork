package com.gzu.jdbc;

import java.sql.*;

public class ScrollableResultDet {
    // 数据库连接的 URL，指定时区和字符编码
    static final String URL = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
    // 数据库用户名
    static final String USER = "root";
    // 数据库密码
    static final String PASSWORD = "123456";
    // 查询数据的 SQL 语句，查询 id 大于某个值的记录
    static final String SQL = "SELECT id, name, course, birthday FROM teacher WHERE id > ?";

    public static void main(String[] args) {
        // 建立数据库连接，并创建 PreparedStatement
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             // 创建可滚动的、只读的结果集
             PreparedStatement ps = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ) {
            // 设置 SQL 查询参数，将 id 大于 40 的记录作为查询条件
            ps.setInt(1, 40);
            // 执行查询，获取结果集
            try (ResultSet rs = ps.executeQuery()) {
                // 移动到倒数第二行
                rs.absolute(-2);
                // 获取并输出当前行的 id, name, course, birthday
                System.out.println(rs.getInt("id") + " " + rs.getString("name")
                        + " " + rs.getString("course") + " " + rs.getDate("birthday"));
            } catch (SQLException e) {
                e.printStackTrace();  // 捕获并处理查询中的异常
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 捕获并处理连接中的异常
        }
    }
}
