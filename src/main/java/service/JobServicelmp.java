package service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class JobServicelmp implements JobService {

    @Override
    public Job getJobInfo(int jobId) {
        //sql语句：查询jobs表中的jobId字段，使用占位符
        String sql = "SELECT * FROM jobs WHERE jobId = ?";

        try (Connection conn = DBUtil.getConnection();//链接数据库
             PreparedStatement ps = conn.prepareStatement(sql)) {//预编译sql语句

            ps.setInt(1, jobId);//将jobId填入占位符
            ResultSet rs = ps.executeQuery();//通过jobId执行查询并返回结果集rs

            //如果rs有数据，创建job对象并填入rs结果集里的数据
            if (rs.next()) {
                Job job = new Job(
                        rs.getInt("jobId"),
                        rs.getString("jobName"),
                        rs.getString("jobContent"),
                        rs.getString("requirement"),
                        rs.getDouble("salary"),
                        rs.getString("unit"),
                        rs.getInt("number"),
                        rs.getString("workTime"),
                        rs.getString("location"),
                        rs.getString("deadline"),
                        rs.getInt("publisherId")
                );
                rs.close();
                return job;
            }

            rs.close();
            return null;

        } catch (SQLException e) {//catch数据库相关异常
            e.printStackTrace();//打印到控制台
            return null;
        }
    }

    @Override
    public List<Job> showAllJob() {
        String sql = "SELECT * FROM jobs";//查询 jobs表
        List<Job> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();//连接数据库
             PreparedStatement ps = conn.prepareStatement(sql)) {//预编译sql语句

            ResultSet rs = ps.executeQuery();//执行查询并返回结果集

            while (rs.next()) {//循环创建job对象
                Job job = new Job(
                        rs.getInt("jobId"),
                        rs.getString("jobName"),
                        rs.getString("jobContent"),
                        rs.getString("requirement"),
                        rs.getDouble("salary"),
                        rs.getString("unit"),
                        rs.getInt("number"),
                        rs.getString("workTime"),
                        rs.getString("location"),
                        rs.getString("deadline"),
                        rs.getInt("publisherId")
                );
                result.add(job);
            }

            rs.close();

            if (result.isEmpty()) {
                return null;
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String publishJob(String jobName, String content, String requirement, double salary,
                             String unit, int number, String workTime, String location, String deadline,
                             int publisherId) {
        String sql = "INSERT INTO jobs (jobName, jobContent, requirement, salary, unit, number, workTime, location, deadline, publisherId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jobName);
            ps.setString(2, content);
            ps.setString(3, requirement);
            ps.setDouble(4, salary);
            ps.setString(5, unit);
            ps.setInt(6, number);
            ps.setString(7, workTime);
            ps.setString(8, location);
            ps.setString(9, deadline);
            ps.setInt(10, publisherId);
            ps.executeUpdate();

            return "已发布完成!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常，发布失败!";
        }
    }

    @Override
    public String updateJob(String jobName, String content, String requirement, double salary,
                             String unit, int number, String workTime, String location, String deadline,
                             int jobId,int publisherId) {
        String sql = "UPDATE jobs SET jobName=?, jobContent=?, requirement=?, salary=?, unit=?, number=?, workTime=?, location=?, deadline=? WHERE jobId=? AND publisherId=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jobName);
            ps.setString(2, content);
            ps.setString(3, requirement);
            ps.setDouble(4, salary);
            ps.setString(5, unit);
            ps.setInt(6, number);
            ps.setString(7, workTime);
            ps.setString(8, location);
            ps.setString(9, deadline);
            ps.setInt(10, jobId);
            ps.setInt(11,publisherId);

            //有权限检验
            int r = ps.executeUpdate();
            if (r > 0) {
                return "已修改完成!";
            }else {
                return "修改失败！岗位不存在或非发布人";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常，修改失败!";
        }
    }

    @Override
    public String chooseJob(int jobID) {
        return "待实现";
    }

    @Override
    public List<Job> searchJob(String keyWord) {
        String sql = "SELECT * FROM jobs WHERE jobName LIKE ? OR jobContent LIKE ?";
        List<Job> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyWord + "%");
            ps.setString(2, "%" + keyWord + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Job job = new Job(
                        rs.getInt("jobId"),
                        rs.getString("jobName"),
                        rs.getString("jobContent"),
                        rs.getString("requirement"),
                        rs.getDouble("salary"),
                        rs.getString("unit"),
                        rs.getInt("number"),
                        rs.getString("workTime"),
                        rs.getString("location"),
                        rs.getString("deadline"),
                        rs.getInt("publisherId")
                );
                result.add(job);
            }

            rs.close();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public String deleteJob(int jobId,int publisherId) {
        String sql = "DELETE FROM jobs WHERE jobId = ? AND publisherId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            //填入占位符
            ps.setInt(1, jobId);
            ps.setInt(2, publisherId);

            //检测数据库是否更新成功（因为有条件判断）
            int r = ps.executeUpdate();
            if (r > 0) {
                return "删除成功";
            } else {
                return "删除失败！岗位不存在或非发布人";
            }
        }
            catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常，删除失败";
        }
    }
}