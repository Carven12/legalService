#!/bin/bash
dir=$(cd `dirname $0`;pwd)
source $dir/conf
pid=`ps -ef| grep "$jarpath/$jarpack" | grep -v grep |awk '{print $2}'`

#原有标准化脚本,kill一次,如果失败,将会kill -9 统计计数服务不能强杀,数据会出问题,调整为下面循环kill
while [ ! -z $pid ]
do
    echo "shutting down $jarpath/$jarpack  which pid is $pid"
    kill $pid
    sleep 1
        pid=`ps -ef| grep "$jarpath/$jarpack" | grep -v grep |awk '{print $2}'`
done

echo "you have stop the your service $jarpack now!"
