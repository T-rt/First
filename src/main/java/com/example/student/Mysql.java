package com.example.student;

import java.sql.*;

public class Mysql {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/student?useSSL=false&serverTimezone=UTC";
	static final String USER = "root";
	static final String PASS = "root00";
	Connection conn = null;
	Statement stmt = null;
	public int MysqlGet(String id) {
	try {
		// 注册 JDBC 驱动
		Class.forName(JDBC_DRIVER);

		// 打开链接
//		System.out.println("连接数据库...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		 PreparedStatement pst = null; 
		ResultSet ret= null; 
		// 执行查询
//		System.out.println(" 实例化Statement对象...");
		stmt = conn.createStatement();
		String sql;
		System.out.println("1");
		sql = "select　count(*) from stu where id= "+id;
		System.out.println("2");
//		ResultSet rs = stmt.executeQuery(sql);
		 pst = conn.prepareStatement(sql);
		 System.out.println(pst);
//		 rs = pst.executeQuery();
		 ResultSet rs = stmt.executeQuery(sql);
		System.out.println(rs);
//		 展开结果集数据库
		while (rs.next()) {
//
//			// 通过字段检索
//			int id = rs.getInt("id");
//			// 输出数据
////			System.out.print("ID: " + id);
//			return id;
			String uid = rs.getString(1);  
			System.out.println(uid);
		}
		// 完成后关闭
		rs.close();
		stmt.close();
		conn.close();
	} catch (SQLException se) {
		// 处理 JDBC 错误
		se.printStackTrace();
	} catch (Exception e) {
		// 处理 Class.forName 错误
		e.printStackTrace();
	} finally {
		// 关闭资源
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
		} // 什么都不做
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	return 0;
	}

	public static void main(String[] args) {
		Mysql m =new Mysql();
		System.out.println(m.MysqlGet("123"));
	}
}
