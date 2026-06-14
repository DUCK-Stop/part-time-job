package service;

import model.Job;

import java.util.List;

public interface JobService{
    //发布岗位
    String publishJob(String jobName, String content, String requirement, double salary,
                   String unit, int number, String workTime, String location, String deadline,
                   int publisherId);
    //删除岗位
    String deleteJob(int jobId,int publisherId);
    //修改岗位
    String updateJob(String jobName, String content, String requirement, double salary,
                   String unit, int number, String workTime, String location, String deadline,
                   int jobId,int publisherId);
    //展示所有岗位
    List<Job> showAllJob();
    //查询岗位信息
    Job getJobInfo(int jobId);
    //关键词查找特定岗位
    List<Job> searchJob(String keyWord);
    //接受岗位（未实现）
    String chooseJob(int jobId);
}
