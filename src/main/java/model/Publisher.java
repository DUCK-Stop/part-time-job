package model;

public class Publisher extends User {
    public Publisher() {
    }

    public Publisher(int userId, String name, String phoneNumber, String password) {
        super(userId, name, phoneNumber, password);
    }

    public Publisher(String name, String phoneNumber, String password) {
        super(name, phoneNumber, password);
    }

    @Override
    public String getRoleName() {
        return "发布者";
    }
}
