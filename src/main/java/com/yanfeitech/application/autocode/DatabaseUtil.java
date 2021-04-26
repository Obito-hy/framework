package com.yanfeitech.application.autocode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Title: DatabaseUtil
 * </p>
 * <p>
 * Description: 自动生成代码工具与配置类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */

@Component
public class DatabaseUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

	public static final String PACKAGE = "com.yanfeitech.application";

	// 数据库名 修改此处
	public static final String SCHEMA = "aviation_teach";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";

	public static final String URL = "jdbc:mysql://127.0.0.1:3306/" + SCHEMA
			+ "?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";

	public static final int UNDERLINE_INDEX = 0;

	/**
	 * 获取数据库连接
	 *
	 * @param url      数据库连接的URL
	 * @param username 用户名
	 * @param password 密码
	 * @return Connection
	 */
	public static Connection getConnection(String url, String username, String password) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			LOGGER.error("get connection failure", e);
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 *
	 * @param conn 数据库连接
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("close connection failure", e);
			}
		}
	}

	/**
	 * 获取数据库下的所有表名
	 *
	 * @param url      数据库连接的URL
	 * @param username 用户名
	 * @param password 密码
	 * @return 表名称集合
	 */
	public static List<String> getTableNames(String url, String username, String password) {
		return getTableNames(url, username, password, null);
	}

	/**
	 * 获取数据库下的所有表名
	 *
	 * @param url              数据库连接的URL
	 * @param username         用户名
	 * @param password         密码
	 * @param tableNamePattern 表名称的匹配字符串
	 * @return 表名称集合
	 */
	public static List<String> getTableNames(String url, String username, String password, String tableNamePattern) {
		List<String> tableNames = new ArrayList<>();
		Connection conn = getConnection(url, username, password);
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = '" + SCHEMA + "'";

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableNames.add(rs.getString(1).toLowerCase());
			}
			rs.close();
		} catch (SQLException e) {
			LOGGER.error("getTableNames failure", e);
		} finally {
			closeConnection(conn);
		}
		return tableNames;
	}

	/**
	 * 返回注释信息
	 *
	 * @param all
	 * @return
	 */

	public static String parse(String all) {
		String comment = null;
		int index = all.indexOf("COMMENT='");
		if (index < 0) {
			return "";
		}
		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);
		return comment;
	}

	public static void main(String[] args) {
		HibernateCodeGeneratorForDao daoGenerator = new HibernateCodeGeneratorForDao();
		daoGenerator.generatorEntities("%");
		HibernateCodeGeneratorForService serviceGenerator = new HibernateCodeGeneratorForService();
		serviceGenerator.generatorEntities("%");
		HibernateCodeGeneratorForController controllerCodeGenerator = new HibernateCodeGeneratorForController();
		controllerCodeGenerator.generatorEntities("%");
	}
}