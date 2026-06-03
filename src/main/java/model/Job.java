package model;

public class Job {
    private static int nextId = 1;

    private int jobId;
    private String jobName;
    private String jobContent;
    private String requirement;
    private double salary;
    private String unit;
    private int number;
    private String workTime;
    private String location;
    private String deadline;
    private int publisherId;

    public Job() {
    }

    // 全参构造——加载岗位时使用
    public Job(int jobId, String jobName, String content, String requirement, double salary,
               String unit, int number, String workTime, String location, String deadline,
               int publisherId) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobContent = content;
        this.requirement = requirement;
        this.salary = salary;
        this.unit = unit;
        this.number = number;
        this.workTime = workTime;
        this.location = location;
        this.deadline = deadline;
        this.publisherId = publisherId;
        if (jobId >= nextId) {
            nextId = jobId + 1;
        }
    }

    // 发布构造——自动分配 jobId
    public Job(String jobName, String content, String requirement, double salary,
               String unit, int number, String workTime, String location, String deadline,
               int publisherId) {
        this(nextId++, jobName, content, requirement, salary, unit, number, workTime,
             location, deadline, publisherId);
    }

    // getter
    public int getJobId() { return jobId; }
    public String getJobName() { return jobName; }
    public String getContent() { return jobContent; }
    public String getRequirement() { return requirement; }
    public double getSalary() { return salary; }
    public String getUnit() { return unit; }
    public int getNumber() { return number; }
    public String getWorkTime() { return workTime; }
    public String getLocation() { return location; }
    public String getDeadline() { return deadline; }
    public int getPublisherId() { return publisherId; }

    // setter
    public void setJobName(String jobName) { this.jobName = jobName; }
    public void setContent(String content) { this.jobContent = content; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setNumber(int number) { this.number = number; }
    public void setWorkTime(String workTime) { this.workTime = workTime; }
    public void setLocation(String location) { this.location = location; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

    @Override
    public String toString() {
        return "单号：" + jobId + "\n" +
               "名称：" + jobName + "\n" +
               "内容：" + jobContent + "\n" +
               "要求：" + requirement + "\n" +
               "薪酬：" + salary + "\n" +
               "单位：" + unit + "\n" +
               "人数：" + number + "\n" +
               "工作时间：" + workTime + "\n" +
               "地点：" + location + "\n" +
               "截止时间：" + deadline + "\n" +
               "发布者id：" + publisherId;
    }
}
