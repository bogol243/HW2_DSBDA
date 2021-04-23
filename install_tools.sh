#!/bin/bash
TOOLS_DIR=tools
[ ! -d tools/ ] && mkdir tools

cd $TOOLS_DIR

wget http://apache-mirror.rbc.ru/pub/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz
tar xvzf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz

wget https://jdbc.postgresql.org/download/postgresql-42.2.5.jar
cp postgresql-42.2.5.jar sqoop-1.4.7.bin__hadoop-2.6.0/lib/

wget https://archive.apache.org/dist/spark/spark-2.3.1/spark-2.3.1-bin-hadoop2.7.tgz
tar xvzf spark-2.3.1-bin-hadoop2.7.tgz



