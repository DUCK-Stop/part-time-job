# 兼职招聘系统

Java 14 控制台应用，基于 Maven + MySQL，通过 Shell 脚本建立 SSH 隧道连接远程数据库，实现用户注册登录、岗位的增删改查、关键词搜索等功能。

## 技术栈

| 组件 | 版本 / 说明                                      |
|------|----------------------------------------------|
| JDK | 14                                           |
| 构建工具 | Maven                |
| 数据库 | MySQL，通过JDBC 驱动连接 |
| SSH 隧道 | 运行编写的Shell 脚本（`ssh.sh`或`ssh.bat`），将服务器`3306`端口转发到本地`3307`端口  |
| 编码 | UTF-8（源码、注释、控制台交互均为中文）                       |

## 项目结构

```
.
├── pom.xml                          # Maven 配置
├── README.md
├── src/main/java/
│   ├── Main.java                    # 程序入口（控制台菜单 + 交互）
│   ├── enums/
│   │   └── Identity.java            # 身份枚举：Publisher / Taker
│   ├── model/
│   │   ├── User.java                # 用户抽象类
│   │   ├── Publisher.java           # 发布者
│   │   ├── Taker.java               # 求职者
│   │   └── Job.java                 # 岗位实体
│   └── service/
│       ├── UserService.java         # 用户服务接口
│       ├── UserServiceImp.java      # 用户服务实现（注册 / 登录）
│       ├── JobService.java          # 岗位服务接口
│       └── JobServicelmp.java       # 岗位服务实现（CRUD + 搜索）
├── src/main/resources/
│   ├── config.example.properties    # 数据库配置模板
│   └── config.properties            # 真实配置（gitignore）
└── ssh隧道链接脚本/
    ├── ssh.example.sh               # SSH 隧道脚本模板[linux]
    ├── ssh.sh                       # 真实脚本[linux]（gitignore）
    ├── ssh.example.bat              # SSH 隧道脚本模板[windows]
    └── ssh.bat                      # 真实脚本[windows]（gitignore）
```

## 快速开始

### 1. 环境准备

- JDK 14+
- Maven 3.6+
- MySQL，本项目已经配置好阿里云服务器：`8.138.192.146`（本地或远程均可；远程需通过 SSH 隧道脚本连接）

### 2. 配置数据库
将`config.example.properties`重命名为`config.properties`，并填入MySQL数据库连接信息：

```properties
db.url=jdbc:mysql://127.0.0.1:3307/part_time_job?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
db.user=root
db.password=你的密码
```

> 使用 SSH 隧道连接服务器的MySQL数据库时，本地转发端口为 `3307`，如果连接本地MySQL数据库则端口号为`3306`

### 3. （连接云服务器时）建立 SSH 隧道
#### Linux
```
将ssh.example.sh改名为ssh.sh填入数据库密码后右键运行
```

#### Windows
```
将ssh.example.bat改名为ssh.bat填入数据库密码后双击运行
```

隧道会将远端 `3306` 转发到本地 `3307`。

### 4. 编译 & 运行

```bash
# 编译
mvn compile

# 打包（含依赖的 fat JAR）
mvn package

# 运行
java -jar target/part-time-job-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 功能清单

### 用户系统

| 功能 | 说明 |
|------|------|
| 注册 | 输入用户名、手机号、密码，选择身份（发布者 / 求职者） |
| 登录 | 手机号 + 密码登录，根据身份进入对应菜单 |

### 发布者菜单

| 序号 | 功能 | 对应方法 |
|:---:|------|------|
| 1 | 浏览所有岗位 | `JobService.showAllJob()` |
| 2 | 发布岗位 | `JobService.publishJob(...)` |
| 3 | 搜索岗位 | `JobService.searchJob(keyword)` |
| 4 | 修改岗位 | `JobService.updateJob(...)` |
| 5 | 删除岗位 | `JobService.deleteJob(jobId, publisherId)` |
| 0 | 退出程序 | — |

- **修改岗位**：需输入岗位 ID，然后重新录入全部字段
- **删除岗位**：校验发布者身份，仅允许删除自己发布的岗位
- **搜索岗位**：检索岗位名称和岗位内容

### 求职者菜单

| 序号 | 功能 | 对应方法 |
|:---:|------|------|
| 1 | 浏览所有岗位 | `JobService.showAllJob()` |
| 2 | 搜索岗位 | `JobService.searchJob(keyword)` |
| 3 | 退出登录 | — |
| 0 | 退出程序 | — |

## 设计说明

### 分层架构

```
Main（UI 层：控制台交互）
   ↓ 调用接口
JobService / UserService（接口：定义契约）
   ↓ 多态实现
JobServicelmp / UserServiceImp（业务层：JDBC 操作）
   ↓ 连接获取
DBUtil（工具层：读取配置、建立连接）
```

### 权限控制

- 删除 / 修改岗位均校验 `publisherId`，仅发布者本人可操作
- 求职者菜单不开放发布、修改、删除入口

### 已知待实现

- `JobService.chooseJob(int jobId)` —— 接单功能（数据库中存在`applications` 表，储存JobId和takerId的对应关系），当前返回 `"待实现"`

## 常见问题

**Q:如何连接SSH隧道?**

A：检查`ssh.sh(linux)或ssh.bat(windows)`中的服务器ip和端口号是否正确，是否填入服务器密码**

**Q: 运行报错 `Communications link failure`？**

A: 检查 `config.properties` 中的数据库地址和端口是否正确，是否填入数据库密码。