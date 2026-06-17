package gui;

import enums.Identity;
import model.Job;
import model.Publisher;
import model.Taker;
import model.User;
import service.JobService;
import service.JobServicelmp;
import service.UserService;
import service.UserServiceImp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class SwingApp extends JFrame {

    private final UserService userService = new UserServiceImp();
    private final JobService jobService = new JobServicelmp();

    private User currentUser;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    // 各面板引用
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private PublisherPanel publisherPanel;
    private TakerPanel takerPanel;

    public SwingApp() {
        setTitle("兼职招聘系统");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 居中

        buildCards();
        add(cardPanel);

        cardLayout.show(cardPanel, "login");
    }

    private void buildCards() {
        loginPanel = new LoginPanel();
        registerPanel = new RegisterPanel();
        publisherPanel = new PublisherPanel();
        takerPanel = new TakerPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(publisherPanel, "publisher");
        cardPanel.add(takerPanel, "taker");
    }

    // ==================== 导航 ====================

    private void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }

    // ==================== 登录面板 ====================

    private class LoginPanel extends JPanel {
        private final JTextField phoneField = new JTextField(15);
        private final JPasswordField passwordField = new JPasswordField(15);
        private final JButton loginBtn = new JButton("登录");
        private final JButton toRegisterBtn = new JButton("去注册");

        LoginPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);

            JLabel title = new JLabel("兼职招聘系统", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(title, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("手机号："), gbc);
            gbc.gridy = 1; gbc.gridx = 1; add(phoneField, gbc);
            gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("密  码："), gbc);
            gbc.gridy = 2; gbc.gridx = 1; add(passwordField, gbc);

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            btnRow.add(loginBtn);
            btnRow.add(toRegisterBtn);
            gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
            add(btnRow, gbc);

            // 事件
            loginBtn.addActionListener(e -> doLogin());
            passwordField.addActionListener(e -> doLogin());
            toRegisterBtn.addActionListener(e -> showCard("register"));
        }

        private void doLogin() {
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入手机号和密码", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = userService.login(phone, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "手机号或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser = user;
            phoneField.setText("");
            passwordField.setText("");

            if (user instanceof Publisher) {
                publisherPanel.refreshTable();
                showCard("publisher");
            } else {
                takerPanel.refreshTable();
                showCard("taker");
            }
        }
    }

    // ==================== 注册面板 ====================

    private class RegisterPanel extends JPanel {
        private final JTextField nameField = new JTextField(15);
        private final JTextField phoneField = new JTextField(15);
        private final JPasswordField passwordField = new JPasswordField(15);
        private final JRadioButton publisherRadio = new JRadioButton("发布者", true);
        private final JRadioButton takerRadio = new JRadioButton("求职者");
        private final JButton registerBtn = new JButton("注册");
        private final JButton backBtn = new JButton("返回登录");

        RegisterPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);

            JLabel title = new JLabel("用户注册", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(title, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("用户名："), gbc);
            gbc.gridy = 1; gbc.gridx = 1; add(nameField, gbc);
            gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("手机号："), gbc);
            gbc.gridy = 2; gbc.gridx = 1; add(phoneField, gbc);
            gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("密  码："), gbc);
            gbc.gridy = 3; gbc.gridx = 1; add(passwordField, gbc);

            // 身份选择
            ButtonGroup group = new ButtonGroup();
            group.add(publisherRadio);
            group.add(takerRadio);
            JPanel radioRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            radioRow.add(new JLabel("身份："));
            radioRow.add(publisherRadio);
            radioRow.add(takerRadio);
            gbc.gridy = 4; gbc.gridx = 1; add(radioRow, gbc);

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            btnRow.add(registerBtn);
            btnRow.add(backBtn);
            gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
            add(btnRow, gbc);

            registerBtn.addActionListener(e -> doRegister());
            backBtn.addActionListener(e -> showCard("login"));
        }

        private void doRegister() {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写全部字段", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Identity identity = publisherRadio.isSelected() ? Identity.Publisher : Identity.Taker;
            String result = userService.register(name, phone, password, identity);
            JOptionPane.showMessageDialog(this, result);

            if (result.contains("成功")) {
                nameField.setText("");
                phoneField.setText("");
                passwordField.setText("");
                publisherRadio.setSelected(true);
                showCard("login");
            }
        }
    }

    // ==================== 发布者面板 ====================

    private class PublisherPanel extends JPanel {
        private final DefaultTableModel tableModel;
        private final JTable table;
        private final JTextField searchField = new JTextField(15);
        private final JButton refreshBtn = new JButton("刷新");
        private final JButton searchBtn = new JButton("搜索");
        private final JButton publishBtn = new JButton("发布岗位");
        private final JButton updateBtn = new JButton("修改岗位");
        private final JButton deleteBtn = new JButton("删除岗位");
        private final JButton logoutBtn = new JButton("退出登录");
        private final JLabel userLabel = new JLabel();

        PublisherPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // 顶部：用户信息 + 退出
            JPanel topBar = new JPanel(new BorderLayout());
            userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            topBar.add(userLabel, BorderLayout.WEST);
            topBar.add(logoutBtn, BorderLayout.EAST);
            add(topBar, BorderLayout.NORTH);

            // 中央：岗位表格
            String[] cols = {"单号", "名称", "内容", "要求", "薪酬", "单位", "人数", "工作时间", "地点", "截止", "发布者ID"};
            tableModel = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            table = new JTable(tableModel);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // 底部：操作按钮
            JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            bottomBar.add(new JLabel("关键词："));
            bottomBar.add(searchField);
            bottomBar.add(searchBtn);
            bottomBar.add(refreshBtn);
            bottomBar.add(publishBtn);
            bottomBar.add(updateBtn);
            bottomBar.add(deleteBtn);
            add(bottomBar, BorderLayout.SOUTH);

            // 事件
            refreshBtn.addActionListener(e -> refreshTable());
            searchBtn.addActionListener(e -> doSearch());
            searchField.addActionListener(e -> doSearch());
            publishBtn.addActionListener(e -> openJobForm(null));
            updateBtn.addActionListener(e -> updateSelectedJob());
            deleteBtn.addActionListener(e -> deleteSelectedJob());
            logoutBtn.addActionListener(e -> doLogout());
        }

        void refreshTable() {
            userLabel.setText("当前用户：" + currentUser.getName() + "（发布者）");
            tableModel.setRowCount(0);
            List<Job> jobs = jobService.showAllJob();
            if (jobs != null) {
                for (Job j : jobs) {
                    tableModel.addRow(new Object[]{
                            j.getJobId(), j.getJobName(), j.getContent(), j.getRequirement(),
                            j.getSalary(), j.getUnit(), j.getNumber(), j.getWorkTime(),
                            j.getLocation(), j.getDeadline(), j.getPublisherId()
                    });
                }
            }
        }

        private void doSearch() {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) { refreshTable(); return; }
            tableModel.setRowCount(0);
            List<Job> jobs = jobService.searchJob(kw);
            if (jobs != null) {
                for (Job j : jobs) {
                    tableModel.addRow(new Object[]{
                            j.getJobId(), j.getJobName(), j.getContent(), j.getRequirement(),
                            j.getSalary(), j.getUnit(), j.getNumber(), j.getWorkTime(),
                            j.getLocation(), j.getDeadline(), j.getPublisherId()
                    });
                }
            }
        }

        private void openJobForm(Job prefill) {
            JobFormDialog dialog = new JobFormDialog(SwingApp.this, prefill);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                String result;
                if (prefill == null) {
                    // 发布
                    result = jobService.publishJob(
                            dialog.getNameText(), dialog.getContentText(), dialog.getRequirementText(),
                            dialog.getSalaryValue(), dialog.getUnitText(), dialog.getNumberValue(),
                            dialog.getWorkTimeText(), dialog.getLocationText(), dialog.getDeadlineText(),
                            currentUser.getUserId());
                } else {
                    // 修改
                    result = jobService.updateJob(
                            dialog.getNameText(), dialog.getContentText(), dialog.getRequirementText(),
                            dialog.getSalaryValue(), dialog.getUnitText(), dialog.getNumberValue(),
                            dialog.getWorkTimeText(), dialog.getLocationText(), dialog.getDeadlineText(),
                            prefill.getJobId(), currentUser.getUserId());
                }
                JOptionPane.showMessageDialog(this, result);
                if (result.contains("成功")) {
                    refreshTable();
                }
            }
        }

        private void updateSelectedJob() {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先在表格中选择要修改的岗位", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int jobId = (int) tableModel.getValueAt(row, 0);
            Job job = jobService.getJobInfo(jobId);
            if (job == null) {
                JOptionPane.showMessageDialog(this, "岗位不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (job.getPublisherId() != currentUser.getUserId()) {
                JOptionPane.showMessageDialog(this, "只能修改自己发布的岗位", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            openJobForm(job);
        }

        private void deleteSelectedJob() {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先在表格中选择要删除的岗位", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int jobId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要删除单号为 " + jobId + " 的岗位吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String result = jobService.deleteJob(jobId, currentUser.getUserId());
            JOptionPane.showMessageDialog(this, result);
            refreshTable();
        }

        private void doLogout() {
            currentUser = null;
            tableModel.setRowCount(0);
            searchField.setText("");
            showCard("login");
        }
    }

    // ==================== 求职者面板 ====================

    private class TakerPanel extends JPanel {
        private final DefaultTableModel tableModel;
        private final JTable table;
        private final JTextField searchField = new JTextField(15);
        private final JButton refreshBtn = new JButton("刷新");
        private final JButton searchBtn = new JButton("搜索");
        private final JButton logoutBtn = new JButton("退出登录");
        private final JLabel userLabel = new JLabel();

        TakerPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // 顶部
            JPanel topBar = new JPanel(new BorderLayout());
            userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            topBar.add(userLabel, BorderLayout.WEST);
            topBar.add(logoutBtn, BorderLayout.EAST);
            add(topBar, BorderLayout.NORTH);

            // 中央
            String[] cols = {"单号", "名称", "内容", "要求", "薪酬", "单位", "人数", "工作时间", "地点", "截止", "发布者ID"};
            tableModel = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            table = new JTable(tableModel);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            add(new JScrollPane(table), BorderLayout.CENTER);

            // 底部
            JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            bottomBar.add(new JLabel("关键词："));
            bottomBar.add(searchField);
            bottomBar.add(searchBtn);
            bottomBar.add(refreshBtn);
            add(bottomBar, BorderLayout.SOUTH);

            refreshBtn.addActionListener(e -> refreshTable());
            searchBtn.addActionListener(e -> doSearch());
            searchField.addActionListener(e -> doSearch());
            logoutBtn.addActionListener(e -> doLogout());
        }

        void refreshTable() {
            userLabel.setText("当前用户：" + currentUser.getName() + "（求职者）");
            tableModel.setRowCount(0);
            List<Job> jobs = jobService.showAllJob();
            if (jobs != null) {
                for (Job j : jobs) {
                    tableModel.addRow(new Object[]{
                            j.getJobId(), j.getJobName(), j.getContent(), j.getRequirement(),
                            j.getSalary(), j.getUnit(), j.getNumber(), j.getWorkTime(),
                            j.getLocation(), j.getDeadline(), j.getPublisherId()
                    });
                }
            }
        }

        private void doSearch() {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) { refreshTable(); return; }
            tableModel.setRowCount(0);
            List<Job> jobs = jobService.searchJob(kw);
            if (jobs != null) {
                for (Job j : jobs) {
                    tableModel.addRow(new Object[]{
                            j.getJobId(), j.getJobName(), j.getContent(), j.getRequirement(),
                            j.getSalary(), j.getUnit(), j.getNumber(), j.getWorkTime(),
                            j.getLocation(), j.getDeadline(), j.getPublisherId()
                    });
                }
            }
        }

        private void doLogout() {
            currentUser = null;
            tableModel.setRowCount(0);
            searchField.setText("");
            showCard("login");
        }
    }

    // ==================== 岗位表单对话框（发布 / 修改共用） ====================

    private class JobFormDialog extends JDialog {
        private boolean confirmed = false;

        private final JTextField nameField = new JTextField(20);
        private final JTextField contentField = new JTextField(20);
        private final JTextField requirementField = new JTextField(20);
        private final JTextField salaryField = new JTextField(20);
        private final JTextField unitField = new JTextField(20);
        private final JTextField numberField = new JTextField(20);
        private final JTextField workTimeField = new JTextField(20);
        private final JTextField locationField = new JTextField(20);
        private final JTextField deadlineField = new JTextField(20);

        JobFormDialog(Frame owner, Job prefill) {
            super(owner, prefill == null ? "发布岗位" : "修改岗位", true);
            setLayout(new BorderLayout(10, 10));

            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 4, 4, 4);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            addRow(form, gbc, 0, "岗位名称：", nameField);
            addRow(form, gbc, 1, "工作内容：", contentField);
            addRow(form, gbc, 2, "岗位要求：", requirementField);
            addRow(form, gbc, 3, "薪酬(/h)：", salaryField);
            addRow(form, gbc, 4, "单位：", unitField);
            addRow(form, gbc, 5, "招聘人数：", numberField);
            addRow(form, gbc, 6, "工作时间：", workTimeField);
            addRow(form, gbc, 7, "工作地点：", locationField);
            addRow(form, gbc, 8, "截止时间：", deadlineField);

            // 预填数据（修改时）
            if (prefill != null) {
                nameField.setText(prefill.getJobName());
                contentField.setText(prefill.getContent());
                requirementField.setText(prefill.getRequirement());
                salaryField.setText(String.valueOf(prefill.getSalary()));
                unitField.setText(prefill.getUnit());
                numberField.setText(String.valueOf(prefill.getNumber()));
                workTimeField.setText(prefill.getWorkTime());
                locationField.setText(prefill.getLocation());
                deadlineField.setText(prefill.getDeadline());
            }

            add(form, BorderLayout.CENTER);

            // 底部按钮
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
            JButton okBtn = new JButton("确定");
            JButton cancelBtn = new JButton("取消");
            btnRow.add(okBtn);
            btnRow.add(cancelBtn);
            add(btnRow, BorderLayout.SOUTH);

            okBtn.addActionListener(e -> {
                if (validateForm()) {
                    confirmed = true;
                    dispose();
                }
            });
            cancelBtn.addActionListener(e -> dispose());

            pack();
            setLocationRelativeTo(owner);
        }

        private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JTextField field) {
            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
            form.add(new JLabel(label), gbc);
            gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1;
            form.add(field, gbc);
        }

        private boolean validateForm() {
            if (nameField.getText().trim().isEmpty()
                    || contentField.getText().trim().isEmpty()
                    || requirementField.getText().trim().isEmpty()
                    || salaryField.getText().trim().isEmpty()
                    || unitField.getText().trim().isEmpty()
                    || numberField.getText().trim().isEmpty()
                    || workTimeField.getText().trim().isEmpty()
                    || locationField.getText().trim().isEmpty()
                    || deadlineField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写全部字段", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Double.parseDouble(salaryField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "薪酬请输入数字", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Integer.parseInt(numberField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "人数请输入整数", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        }

        boolean isConfirmed() { return confirmed; }
        String getNameText() { return nameField.getText().trim(); }
        String getContentText() { return contentField.getText().trim(); }
        String getRequirementText() { return requirementField.getText().trim(); }
        double getSalaryValue() { return Double.parseDouble(salaryField.getText().trim()); }
        String getUnitText() { return unitField.getText().trim(); }
        int getNumberValue() { return Integer.parseInt(numberField.getText().trim()); }
        String getWorkTimeText() { return workTimeField.getText().trim(); }
        String getLocationText() { return locationField.getText().trim(); }
        String getDeadlineText() { return deadlineField.getText().trim(); }
    }

    // ==================== 入口 ====================

    public static void main(String[] args) {
        // 使用 Nimbus 跨平台 L&F（避免 Linux 下 GTK 警告和崩溃）
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // 回退到默认 Metal L&F
        }
        SwingUtilities.invokeLater(() -> new SwingApp().setVisible(true));
    }
}
