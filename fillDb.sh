
NUM_LINES=$2
RECORDID_BEGIN=0
RECORDID_END=$(expr $NUM_LINES / 10)
USERID_BEGIN=0
USERID_END=$(expr $NUM_LINES / 10)
TIMESTAMP_BEGIN=$(date +%s)
TIMESTAMP_END=$(expr $TIMESTAMP_BEGIN + 5 \* 86400)
# Генерируем входные данные и добавляем их в таблицу
for i in $(eval echo {1..$NUM_LINES})
do
	RECORDID=$(shuf -i $RECORDID_BEGIN-$RECORDID_END -n 1)
	USERID=$(shuf -i $USERID_BEGIN-$USERID_END -n 1)
	TIMESTAMP=$(shuf -i $TIMESTAMP_BEGIN-$TIMESTAMP_END -n 1)
	TYPEID=$(shuf -i 1-3 -n 1)
	sudo -u postgres -H -- psql -d $1 -c 'INSERT INTO records (recordId, userId, timestamp, recordType) values ('$RECORDID','$USERID','$TIMESTAMP','$TYPEID');'
done

for i in {1..5}
do
	echo "$i"
done
