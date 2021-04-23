package bdtc.lab2;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static bdtc.lab2.NewsInteractionCounter.countInteraction;
import static org.junit.Assert.assertEquals;

public class SparkTest {

    SparkSession ss = SparkSession
            .builder()
            .master("local")
            .appName("SparkSQLApplication")
            .getOrCreate();

    JavaSparkContext sc = new JavaSparkContext(ss.sparkContext());
    /**
     * Присутствующие идентификаторы подсчитываются
     */
    @Test
    public void testCountPresent(){

        sc.addFile("interaction_types");
        //format: interactionId,recordId,userId,timestamp,type
        String lines[] = {"1,1,1,0,1","2,1,2,0,1","3,1,3,0,1"};
        String expected = "recordId 1: opened and read: 3";
        JavaRDD<String> data = sc.parallelize(Arrays.asList(lines));
        JavaRDD<String> result = countInteraction(data);
        List<String> stringList = result.collect();

        assertEquals(expected, stringList.iterator().next());
    }
}