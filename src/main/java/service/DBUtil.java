package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DBUtil {
    //三个静态常量
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    //静态代码块，加载类时自动执行
    //读取properties文件获取密码
    static {
        Properties props = new Properties();
        // 优先读取 JAR 外部同目录的 config.properties
        File externalFile = new File("config.properties");
        if (externalFile.exists()) {
            try (FileInputStream fis = new FileInputStream(externalFile)) {
                props.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("读取外部 config.properties 失败", e);
            }
        } else {
            // 回退到 classpath 内的配置（开发时用）
            try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (is == null) {
                    throw new RuntimeException("找不到 config.properties，请将 config.example.properties 改名为 config.properties 并填写配置");
                }
                props.load(is);
            } catch (IOException e) {
                throw new RuntimeException("读取 config.properties 失败", e);
            }
        }

        // 建立 SSH 隧道
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(
                props.getProperty("ssh.user"),
                props.getProperty("ssh.host"),
                Integer.parseInt(props.getProperty("ssh.port"))
            );
            session.setPassword(props.getProperty("ssh.password"));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setServerAliveInterval(30000);
            session.setPortForwardingL(
                Integer.parseInt(props.getProperty("ssh.localPort")),
                props.getProperty("ssh.remoteHost"),
                Integer.parseInt(props.getProperty("ssh.remotePort"))
            );
        } catch (Exception e) {
            throw new RuntimeException("SSH 隧道建立失败", e);
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
