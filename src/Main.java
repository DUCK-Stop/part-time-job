import enums.Identity;
import model.User;
import model.Job;
import model.Publisher;
import service.UserService;
import service.UserServiceImp;
import service.JobService;
import service.JobServicelmp;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserServiceImp();
    private static JobService jobService = new JobServicelmp();
    private static Scanner scanner = new Scanner(System.in);

   public static void main(String[] args) {
        while (true) {
            System.out.println("\n====== 兼职招聘系统 ======");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 退出");
            System.out.println("========================");
            System.out.print("请选择：");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
        if (user instanceof Publisher) {
            showPublisherMenu(user);
        } else {
            showTakerMenu(user);
        }
    }

    private static void showPublisherMenu(User user) {
        while (true) {
            System.out.println("\n====== 发布者菜单 ======");
            System.out.println("1. 浏览所有岗位");
            System.out.println("2. 发布岗位");
            System.out.println("3. 搜索岗位");
            System.out.println("4. 退出登录");
            System.out.println("0. 退出程序");
            System.out.println("========================");
            System.out.print("请选择：");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAllJobs();
                case 2 -> publishJob(user);
                case 3 -> searchJobs();
                case 4 -> {
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
                case 1 -> showAllJobs();
                case 2 -> searchJobs();
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

    private static void showAllJobs() {
        List<Job> jobs = jobService.showAllJob();
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("暂无岗位信息");
            return;
        }
        for (Job job : jobs) {
            System.out.println(job);
            System.out.println("------------------------");
        }
    }

    private static void publishJob(User user) {
        System.out.print("请输入岗位名称：");
        String name = scanner.nextLine();
        System.out.print("请输入工作内容：");
        String content = scanner.nextLine();
        System.out.print("请输入岗位要求：");
        String requirement = scanner.nextLine();
        System.out.print("请输入薪酬（/h）：");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入单位：");
        String unit = scanner.nextLine();
        System.out.print("请输入招聘人数：");
        int number = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入工作时间：");
        String workTime = scanner.nextLine();
        System.out.print("请输入工作地点：");
        String location = scanner.nextLine();
        System.out.print("请输入截止时间：");
        String deadline = scanner.nextLine();

        String result = jobService.publishJob(name, content, requirement, salary,
                unit, number, workTime, location, deadline, user.getUserId());
        System.out.println(result);
    }

    private static void searchJobs() {
        System.out.print("请输入关键词：");
        String keyword = scanner.nextLine();
        List<Job> result = jobService.searchJob(keyword);
        if (result == null || result.isEmpty()) {
            System.out.println("未找到匹配的岗位");
            return;
        }
        for (Job job : result) {
            System.out.println(job);
            System.out.println("------------------------");
        }
    }
}
