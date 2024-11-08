package store.utilities;

public class Splitter {
    private static final String LINE_DELIMITER = ",";
    private static final String LINES_DELIMITER = "\\r?\\n";

    public static String[] splitStringLine(String line) {
        return line.split(LINE_DELIMITER);
    }

    public static String[] splitStringFileLines(String stringFile) {
        return stringFile.split(LINES_DELIMITER);
    }
}