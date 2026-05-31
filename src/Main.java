import enums.Identity;
import model.User;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(1,"DUCKSTOP","123456789","123", Identity.Taker);
        user1.setName("danking");
        System.out.println("修改后" + user1);
    }
}
