package store.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import store.enums.messages.ErrorMessage;

public class Parser {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static LocalDate parseToDateFormat(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static Integer parseNumberToInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(ErrorMessage.INVALID_INT_NUMBER.getMessage());
        }
    }
}
