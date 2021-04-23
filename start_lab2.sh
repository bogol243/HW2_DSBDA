#!/bin/bash

DBNAME=lab2db

export PATH=$PATH:~/bigdata/sqoop-1.4.7.bin__hadoop-2.6.0/bin
export SPARK_HOME=~/bigdata/spark-2.3.1-bin-hadoop2.7
export PATH=$PATH:$SPARK_HOME/bin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export YARN_CONF_DIR=$HADOOP_HOME/etc/hadoop

yes |sudo rm -r out/
hdfs dfs -rm -r records/
hdfs dfs -rm -r out/

source setup_database.sh $DBNAME

N_LINES=50
echo "filling database $DBNAME with $N_LINES lines... "
source fillDb.sh $DBNAME $N_LINES > /dev/null

sqoop import --connect 'jdbc:postgresql://127.0.0.1:5432/'"$DBNAME"'?ssl=false' --username 'postgres' --password '1234' --table 'records' --target-dir 'records'

spark-submit --class bdtc.lab2.SparkRDDApplication --master yarn --deploy-mode cluster --executor-memory 1g --files interaction_types --name newstypecount --conf "spark.app.id=SparkRDDApplication"  target/lab2-1.0-SNAPSHOT-jar-with-dependencies.jar hdfs://127.0.0.1:9000/user/dm/records/ out

hdfs dfs -get out/

N=10
echo "First $N lines in result:"
head -n $N out/part-00000
