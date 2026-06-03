package model;

public abstract class User {
    private static int nextId = 1;

    private int userId;
    private String name;
    private String phoneNumber;
    private String password;

    public User() {
    }

    // 全参构造——加载用户时使用
    public User(int userId, String name, String phoneNumber, String password) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        if (userId >= nextId) {
            nextId = userId + 1;
        }
    }

    // 注册构造——自动分配 userId
    public User(String name, String phoneNumber, String password) {
        this(nextId++, name, phoneNumber, password);
    }

    // 子类返回各自角色名称
    public abstract String getRoleName();

    // getter
    public int getUserId() {return userId;}
    public String getName() {return name;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getPassword() {return password;}

    // setter
    public void setName(String name) {this.name = name;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "用户id为：" + userId + "\n" +
               "用户名称为：" + name + "\n" +
               "电话号码为：" + phoneNumber + "\n" +
               "密码为:" + password + "\n" +
               "用户身份为：" + getRoleName();
    }
}
