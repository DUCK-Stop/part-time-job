package service;

import enums.Identity;
import model.User;

public interface UserService {
    // 注册方法（根据 Identity 创建 Publisher 或 Taker）
    String register(String name, String phoneNumber, String password, Identity identity);

    // 登录方法
    User login(String phoneNumber, String password);
}
