#!/bin/sh
cd /var/app/googlemap
echo "圧縮ファイル解凍"
unzip googlemap-1.0-SNAPSHOT.zip
echo "圧縮ファイル削除"
rm -f googlemap-1.0-SNAPSHOT.zip
echo "activatorのプロセスを確認"
ps -ef | grep java | grep googlemap
if [ $? -eq 0 ]; then
echo "プロセス終了"
killingpid=`ps -ef | grep java | grep googlemap | awk '{ print $2 }'`
kill -9 $killingpid
fi
ls /var/app/googlemap/googlemap-1.0-SNAPSHOT/RUNNING_PID
if [ $? -eq 0 ]; then
echo "RUNNING_PID削除"
rm -f /var/app/googlemap/googlemap-1.0-SNAPSHOT/RUNNING_PID
fi
echo "起動"
./googlemap-1.0-SNAPSHOT/bin/googlemap &
