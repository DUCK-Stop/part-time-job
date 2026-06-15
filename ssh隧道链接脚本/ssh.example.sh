# 请复制此文件并命名为 ssh.sh 并填入真实密码

REMOTE_HOST="8.138.192.146"
REMOTE_PORT="3306"
LOCAL_PORT="3307"
SSH_USER="admin"
SSH_PASSWORD="你的密码"

echo "正在启动 SSH 隧道 (本地 $LOCAL_PORT → 远程 $REMOTE_HOST:$REMOTE_PORT)..."
sshpass -p "$SSH_PASSWORD" ssh -L ${LOCAL_PORT}:127.0.0.1:${REMOTE_PORT} ${SSH_USER}@${REMOTE_HOST} -N

read -p "隧道已关闭。按回车键退出..."