package store.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Parser {
    public static LocalDate parseToDateFormat(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
