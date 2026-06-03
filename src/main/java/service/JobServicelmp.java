package service;

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
        String sql = "SELECT * FROM jobs WHERE jobId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

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

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Job> showAllJob() {
        String sql = "SELECT * FROM jobs";
        List<Job> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
            return "数据库异常，发布失败";
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

}
