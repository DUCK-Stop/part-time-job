package model;
import enums.Identity;
public class User {
    private static int nextId = 1;

    private int userId;
    private String name;
    private String phoneNumber;
    private String password;
    private Identity identity;

    public User() {
    }

    // 全参构造—加载用户时使用
    public User(int userId, String name, String phoneNumber, String password, Identity identity) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.identity = identity;
        if (userId >= nextId) {
            nextId = userId + 1;   // 保证 nextId 不落后于文件中的最大 ID
        }
    }

    // 注册构造——自动分配 userId
    public User(String name, String phoneNumber, String password, Identity identity) {
        this(nextId++, name, phoneNumber, password, identity);
    }

    // getter
    public int getUserId() {return userId;}
    public String getName() {return name;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getPassword() {return password;}
    public Identity getIdentity(){return identity;}

    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setPassword(String password) {this.password = password;}
    public void setIdentity(Identity identity) {this.identity = identity;}


    @Override
    public String toString() {
        return "用户id为："+userId+"\n"+"用户名称为："+name+"\n"+"电话号码为："+phoneNumber+"\n"+"密码为:"+password+"\n"+"用户身份为："+identity;
    }
}
