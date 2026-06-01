package service;

import enums.Identity;
import model.User;
import model.Publisher;
import model.Taker;
import java.util.List;
import java.util.ArrayList;

public class UserServiceImp implements UserService {

    private List<User> users = new ArrayList<>();

    @Override
    public String register(String name, String phoneNumber, String password, Identity identity) {
        for (User user : users) {
            if (phoneNumber.equals(user.getPhoneNumber())) {
                return "不可重复注册手机号";
            }
        }

        User user;
        if (identity == Identity.Publisher) {
            user = new Publisher(name, phoneNumber, password);
        } else {
            user = new Taker(name, phoneNumber, password);
        }

        users.add(user);
        return "注册成功";
    }

    @Override
    public User login(String phoneNumber, String password) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        for (User user : users) {
            if (phoneNumber.equals(user.getPhoneNumber())
                    && password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
