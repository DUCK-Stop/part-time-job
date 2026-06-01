import enums.Identity;
import model.User;
import service.UserService;
import service.UserServiceImp;

import java.util.Scanner;

public class Main {
    private static UserService userService = new UserServiceImp();//不绑定单一实现，可指向其他可实现类
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n====== 兼职招聘系统 ======");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 退出");
            System.out.println("========================");
            System.out.print("请选择：");

            int choice = scanner.nextInt();//读取第一位数字
            scanner.nextLine(); // 消费换行

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("感谢使用，再见！");
                    return;
                }
                default -> System.out.println("无效选项，请重新选择");
            }
        }
    }

    private static void register() {
        System.out.print("请输入用户名：");
        String name = scanner.nextLine();
        System.out.print("请输入手机号：");
        String phone = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        System.out.println("请选择身份：1. 发布者  2. 求职者");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        Identity identity = (roleChoice == 1) ? Identity.Publisher : Identity.Taker;

        String result = userService.register(name, phone, password, identity);
        System.out.println(result);
    }

    private static void login() {
        System.out.print("请输入手机号：");
        String phone = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        User user = userService.login(phone, password);
        if (user == null) {
            System.out.println("手机号或密码错误");
            return;
        }

        System.out.println("登录成功！欢迎 " + user.getName());
        if (user.getIdentity() == Identity.Publisher) {
            showPublisherMenu(user);
        } else {
            showTakerMenu(user);
        }
    }

    private static void showPublisherMenu(User user) {
        while (true) {
            System.out.println("\n====== 发布者菜单 ======");
            System.out.println("1. 浏览所有岗位");
            System.out.println("2. 搜索岗位");
            System.out.println("3. 退出登录");
            System.out.println("0. 退出程序");
            System.out.println("========================");
            System.out.print("请选择：");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println("（功能待实现）");
                case 2 -> System.out.println("（功能待实现）");
                case 3 -> {
                    System.out.println("已退出登录");
                    return;
                }
                case 0 -> {
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                }
                default -> System.out.println("无效选项");
            }
        }
    }

    private static void showTakerMenu(User user) {
        while (true) {
            System.out.println("\n====== 求职者菜单 ======");
            System.out.println("1. 浏览所有岗位");
            System.out.println("2. 搜索岗位");
            System.out.println("3. 退出登录");
            System.out.println("0. 退出程序");
            System.out.println("========================");
            System.out.print("请选择：");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println("（功能待实现）");
                case 2 -> System.out.println("（功能待实现）");
                case 3 -> {
                    System.out.println("已退出登录");
                    return;
                }
                case 0 -> {
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                }
                default -> System.out.println("无效选项");
            }
        }
    }
}
