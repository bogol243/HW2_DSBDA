#!/bin/bash

export PATH=$PATH:~/bigdata/sqoop-1.4.7.bin__hadoop-2.6.0/bin

echo $PATH
export SPARK_HOME=~/bigdata/spark-2.3.1-bin-hadoop2.7
export PATH=$PATH:$SPARK_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
sqoop import --connect 'jdbc:postgresql://127.0.0.1:5432/'"$1"'?ssl=false' --username 'postgres' --password '1234' --table 'records' --target-dir 'records'
