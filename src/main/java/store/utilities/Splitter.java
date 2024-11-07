package store.utilities;

public class Splitter {
    private static final String DELIMITER = ",";

    public static String[] splitStringLine(String line) {
        return line.split(DELIMITER);
    }
}