package service;

import enums.Identity;
import model.User;
import java.util.List;
import java.util.ArrayList;

public class UserServiceImp implements UserService {

    //创建列表储存用户
    private List<User> users = new ArrayList<>();

    @Override
    public String register(String name, String phoneNumber, String password, Identity identity)
    {
        for(User user:users)
        {
            if(phoneNumber.equals(user.getPhoneNumber()))
            {
                return "不可重复注册手机号";
            }
        }
        //检验电话号码无重复后创建用户
        User user = new User(name, phoneNumber, password, identity);
        //添加到users列表中
        users.add(user);


        return "注册成功";
    }

    @Override
    public User login(String phoneNumber, String password) {
        //列表为空时直接返回
        if (users == null || users.isEmpty()) {
            return null;
        }

        for (User user : users) {
            // 手机号和密码都匹配时返回该用户
            if (phoneNumber.equals(user.getPhoneNumber())
                    && password.equals(user.getPassword())) {
                return user;
            }
        }
        return null; // 未找到
    }
}
