package bdtc.lab2;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

/**
 * Считает количество событий syslog разного уровная log level по часам.
 */
@Slf4j
public class SparkRDDApplication {

    /**
     * @param args - args[0]: входной файл, args[1] - выходная папка
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Usage: java -jar SparkSQLApplication.jar input.file outputDirectory");
        }

        log.info("Appliction started!");
        log.debug("Application started");
        SparkSession ss = SparkSession
                .builder()
                .master("yarn")
                .appName("SparkRDDApplication")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(ss.sparkContext());;

        // Добавление файла в spark-контекст
        sc.addFile("interaction_types");

        JavaRDD<String> rdd = sc.textFile(args[0]);
        log.info("===============COUNTING...================");
        JavaRDD<String> result = NewsInteractionCounter.countInteraction(rdd);
        log.info("============SAVING FILE TO " + args[1] + " directory============");
        result.saveAsTextFile(args[1]);
    }
}
