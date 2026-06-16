@echo off
set REMOTE_HOST=8.138.192.146
set REMOTE_PORT=3306
set LOCAL_PORT=3307
set SSH_USER=admin
set SSH_PASSWORD=??

echo 正在启动 SSH 隧道 (本地 %LOCAL_PORT% → 远程 %REMOTE_HOST%:%REMOTE_PORT%)...
.\plink -L %LOCAL_PORT%:127.0.0.1:%REMOTE_PORT% -pw "%SSH_PASSWORD%" %SSH_USER%@%REMOTE_HOST% -N

if errorlevel 1 (
    echo 隧道启动失败，请检查网络或密码。
    pause
) else (
    echo 隧道运行中，请保持此窗口打开。
    pause
)