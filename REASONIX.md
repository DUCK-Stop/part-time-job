# REASONIX.md

## Stack
- **Java 14** — source/target both set to 14 in pom.xml
- **Maven** — build tool with assembly plugin for fat JAR
- **MySQL** — via `mysql-connector-j` 9.2.0 JDBC driver
- **JSch** — SSH tunnel support (`com.github.mwiede:jsch` 0.2.23)

## Layout
- `src/main/java/` — all source; packages: `enums/`, `model/`, `service/`, root `Main.java`
- `src/main/resources/` — runtime config (`config.properties` gitignored, `config.example.properties` committed)
- `ssh隧道链接脚本/` — SSH tunnel helper scripts (`ssh.sh` gitignored, `ssh.example.sh` committed)
- `target/` — Maven build output (gitignored)
- `.idea/` — IntelliJ IDEA project config (gitignored)

## Commands
```
# compile
mvn compile

# package (fat JAR with dependencies)
mvn package

# run
java -jar target/part-time-job-1.0-SNAPSHOT-jar-with-dependencies.jar
```

No test runner configured — there is no `src/test` directory and no test dependencies in pom.xml.

## Conventions
- **Service layer**: interface + single implementation (`UserService` → `UserServiceImp`, `JobService` → `JobServicelmp`). Note the typo `Servicelmp` (lowercase L) — not `ServiceImpl`.
- **Model hierarchy**: abstract `User` extended by `Publisher` and `Taker`; identity selected via `enums.Identity` enum.
- **ID generation**: auto-increment `userId`/`jobId` managed by static `nextId` counters in model classes, not DB `AUTO_INCREMENT`.
- **Encoding**: project-wide UTF-8, Chinese comments and UI strings throughout.
- **Java 14 features**: switch expressions (`case 1 ->`) used in `Main.java`.
- **Config loading**: `DBUtil` reads `config.properties` from classpath in a static initializer.

## Watch out for
- `config.properties` is **gitignored** — copy `config.example.properties` and fill in real DB credentials before running.
- `ssh.sh` is **gitignored** — copy `ssh.example.sh` to set up the SSH tunnel (forwards remote `3306` → local `3307`).
- No tests exist anywhere in the project. Adding tests requires creating `src/test/java/` and adding a test dependency to pom.xml.
- The `Main` class is in the **default (root) package** — the assembly plugin references it as `mainClass>Main</mainClass>` without a package prefix.
