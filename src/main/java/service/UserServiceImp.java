package service;

import enums.Identity;
import model.User;
import model.Publisher;
import model.Taker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImp implements UserService {

    @Override
    public String register(String name, String phoneNumber, String password, Identity identity) {
        // 第一步：查手机号是否已注册
        String checkSql = "SELECT COUNT(*) FROM users WHERE phoneNumber = ?";

        try (Connection conn = DBUtil.getConnection();//使用工具类加载驱动并连接MySQL
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {

            psCheck.setString(1, phoneNumber);//把用户输入填入第一个占位符
            ResultSet rs = psCheck.executeQuery();//将组装好的SQL发送给数据库并 查询
            rs.next();// 定位到数据行（第一行）
            int count = rs.getInt(1);//读取当前行的第一列的数字
            rs.close();//关闭rs结果集

            if (count > 0) {
                return "不可重复注册手机号";
            }
        } catch (SQLException e) {//catch数据库相关异常
            e.printStackTrace();//打印错误详情
            return "数据库异常，注册失败";
        }

        // 第二步：插入新用户
        String insertSql = "INSERT INTO users (name, phoneNumber, password, identity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {//预编译，返回可填入参数的对象

            //将值填入占位符
            psInsert.setString(1, name);
            psInsert.setString(2, phoneNumber);
            psInsert.setString(3, password);
            psInsert.setString(4, identity.name().toLowerCase());
            psInsert.executeUpdate();//写入数据表，该方法可以用来更新数据表（增删改）

            return "注册成功";
        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常，注册失败";
        }
    }

    @Override
    public User login(String phoneNumber, String password) {
        //验证账号密码
        String sql = "SELECT * FROM users WHERE phoneNumber = ? AND password = ?";//预编译sql

        try (Connection conn = DBUtil.getConnection();//
             PreparedStatement ps = conn.prepareStatement(sql)) {//将sql模板发送到MySQL，返回ps准备填入值

            ps.setString(1, phoneNumber);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();//查询，返回rs结果表

            if (rs.next()) {//如果rs不为空则获取表内数据
                int userId       = rs.getInt("userId");
                String name      = rs.getString("name");
                String phone     = rs.getString("phoneNumber");
                String pwd       = rs.getString("password");
                String identityStr = rs.getString("identity");

                rs.close();//关闭rs结果表

                if ("publisher".equalsIgnoreCase(identityStr)) {//判断身份标识
                    return new Publisher(userId, name, phone, pwd);//返回对象，值从数据库中获取
                } else {
                    return new Taker(userId, name, phone, pwd);
                }
            }
            rs.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
