package bdtc.lab2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkFiles;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@AllArgsConstructor
@Slf4j
public class NewsInteractionCounter {
    /**
     * Функция подсчёта количества разных типов взаимодействий с записями в новостной ленте
     * @param inputRDD - входной JavaRDD для анализа
     * @return результат подсчета в формате JavaRDD
     */
    public static JavaRDD<String> countInteraction(JavaRDD<String> inputRDD) {

        // Каждая строчка исходного набора данных представляет значения, разделённые запятыми.
        // Транформация mapToPair преобразует строки в объекты data-класса NewsRecord.
        // Также задаёт соответствие между recordId и объектами NewsRecord.
        JavaPairRDD<Integer,NewsRecord> pairRDD = inputRDD.mapToPair(s -> {
            String[] logFields = s.split(",");
            return new Tuple2(logFields[1],new NewsRecord(Integer.valueOf(logFields[1]),
                                                            Integer.valueOf(logFields[2]),
                                                            Integer.valueOf(logFields[3]),
                                                            Integer.valueOf(logFields[4])));
        });

        // На предыдущем эпапе в качестве ключа было выбрано поле recordId.
        // Трансформация groupByKey собирает все значения для одиковых ключей в один объект,
        // расширяющий интерфейс Iterable. Далее с помощью трансформации mapValues проходим
        // по каждому из этих Iterable и подсчитываем количество взаимодействий каждого типа
        JavaPairRDD<Integer,String> result = pairRDD.groupByKey().mapValues(recordsById -> {
            // Справочник названий типов взаимодействия(текстовый файл) был добавлен в spark контекст.
            // Для определения соответствия числовых значений типа строковым,
            // необходимо сначала этот справочник получить.
            String filename = SparkFiles.get("interaction_types");
            Map<Integer, String> numToName = getTypeNames(filename);

            // Подсчёт количества взаимодействий для каждого типа (1,2,3)
            int typeCounter[] = new int[4];
            int recordId = recordsById.iterator().next().getRecordId();
            for(NewsRecord nr: recordsById){
                typeCounter[nr.getType()] += 1;
            }

            // Формирование строки выходного файла
            String res = "recordId "+Integer.toString(recordId)+": ";
            boolean first = true;
            for(int i=1;i<=3;i++){
                if(!first && typeCounter[i]!=0){
                    res+=", ";
                }else if(typeCounter[i]!=0){
                    first=false;
                }
                if(typeCounter[i]==0) continue;
                res += numToName.get(i)+": "+Integer.toString(typeCounter[i]);
            }
            return res;

        // Далее сортировка по ключу и сбор всех строк в один partition
        }).sortByKey().coalesce(1);

        log.info("===========RESULT=========== ");
        return result.values();
    }

    /**
     * Вспомогательная функция для получения таблицы соответствия числового значения типа
     * взаимодействия и строкового значения из файла, в котором это соответствие определено.
     *
     * @param filename - имя файла, содержащего строки в формате:
     *	<номер типа взаимодействия>,<название типа взаимодействия>
     * @return отображение номера типа в его название.
     */
    static private Map<Integer,String> getTypeNames(String filename){
        Map<Integer, String> numToName = new HashMap<Integer, String>();

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data[] = myReader.nextLine().split(",");
                numToName.put(Integer.valueOf(data[0]),data[1]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return numToName;
    }

}
