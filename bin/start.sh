#!/bin/bash
dir=$(cd `dirname $0`;pwd)
echo $dir
source $dir/conf
#echo 1
#echo $jarpath
#echo $jarpack
ret=`ps -ef | grep "$jarpath/$jarpack" | grep -v grep`
echo $ret
if [ "$ret"x == "x" ];then
cd $jarpath
   nohup $path/java $JAVA_OPTS -jar $jarpath/$jarpack $sys_after >/dev/null 2>&1 &
fi

ret=`ps -ef | grep "$jarpath/$jarpack" | grep -v grep`
echo $ret
if [ "$ret"x != "x" ];
then
  echo "you have start the your service $jarpack now!"
else
  echo "Please check your conf! and re-excute the start.sh"
fi
