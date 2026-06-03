package model;

public class Taker extends User {
    public Taker() {
    }

    public Taker(int userId, String name, String phoneNumber, String password) {
        super(userId, name, phoneNumber, password);
    }

    public Taker(String name, String phoneNumber, String password) {
        super(name, phoneNumber, password);
    }

    @Override
    public String getRoleName() {
        return "求职者";
    }
}
