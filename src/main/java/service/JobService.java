package service;

import model.Job;

import java.util.List;

public interface JobService{
    //查询岗位信息
    Job getJobInfo(int jobId);
    //展示所有岗位
    List<Job> showAllJob();
    //发布岗位
    String publishJob(String jobName, String content, String requirement, double salary,
                   String unit, int number, String workTime, String location, String deadline,
                   int publisherId);
    //接受岗位
    String chooseJob(int jobId);
    //关键词查找特定岗位
    List<Job> searchJob(String keyWord);
}
