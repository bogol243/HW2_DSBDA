#!/bin/bash

sudo service postgresql start
sudo -u postgres psql -c 'drop database if exists '"$1"';'
sudo -u postgres psql -c 'create database '"$1"';'
sudo -u postgres -H -- psql -d lab2db -c 'CREATE TABLE records (interactionId BIGSERIAL PRIMARY KEY, recordId integer, userId integer, timestamp VARCHAR(20), recordType smallint);'

