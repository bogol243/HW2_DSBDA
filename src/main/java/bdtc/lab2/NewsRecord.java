package bdtc.lab2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
/**
 * Data-класс для хранения информации о единичном взаимодействии с записью в новостной ленте.
 * @author Dmitry Bogoslovsky
 * @version 2.0
 */
public class NewsRecord implements Serializable {
    /** Уникальный идентификатор записи */
    private int recordId;
    /** Идентификатор пользователя, провзаимодействовавшего с записью*/
    private int userId;
    /** Время взаимодействия */
    private int timestamp;
    /** Тип взаимодействия */
    private int type;
}
