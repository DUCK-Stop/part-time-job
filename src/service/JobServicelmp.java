package service;

import model.Job;
import model.User;

import java.util.List;
import java.util.ArrayList;
public class JobServicelmp implements JobService {
    //创建列表储存岗位
    private List<Job> jobs = new ArrayList<>();

    @Override
    public Job getJobInfo(int jobId)
    {
        for(Job job:jobs)
        {
            if(jobId==job.getJobId())
            {
                return job;//查找id后返回job对象
            }
        }
        return null;
    }

    @Override
    public List<Job> showAllJob()
    {
        //判断是否为空
        if(jobs.isEmpty()){
            return null;
        }
        return jobs;
    }

    @Override
    public String publishJob(String jobName, String content, String requirement, double salary,
                             String unit, int number, String workTime, String location, String deadline,
                             int publisherId)
    {
        Job job = new Job(jobName, content, requirement, salary, unit, number, workTime, location, deadline, publisherId);
        jobs.add(job);
        return "已发布完成!";
    }

    @Override
    public String chooseJob(int jobID)
    {
        return "待实现";
    }

    @Override
    public List<Job> searchJob(String keyWord)
    {
        //新建列表储存搜索结果
        List<Job> result = new ArrayList<>();

        for(Job job:jobs)
        {
            //搜索岗位名称和岗位描述
            if(job.getJobName().contains(keyWord)||
                    job.getContent().contains(keyWord))
            {
                result.add(job);
            }
        }
        return result;
    }

}
