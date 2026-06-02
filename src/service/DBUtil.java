package service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    //三个静态常量
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    //静态代码块，加载类时自动执行
    //读取properties文件获取密码
    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("找不到 config.properties，请确认文件在项目根目录", e);
        }

        URL = props.getProperty("db.url");
        USER = props.getProperty("db.user");
        PASSWORD = props.getProperty("db.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载MySQL驱动类
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL 驱动加载失败", e);
        }//文件缺失时终止程序
    }

    public static Connection getConnection() throws SQLException {//受检异常不捕获
        return DriverManager.getConnection(URL, USER, PASSWORD);//返回Connection对象
    }
}
