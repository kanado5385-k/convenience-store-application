package store.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Splitter {
    private static final String LINE_DELIMITER = ",";
    private static final String LINES_DELIMITER = "\\r?\\n";
    private static final String START = "[";
    private static final String SEPARATOR = "-";
    private static final String END = "]";

    private static final int NEXT_INDEX = 1;

    public static List<String> splitStringLine(String line) {
        return Arrays.asList(line.split(LINE_DELIMITER));
    }

    public static List<String> splitStringFileLines(String stringFile) {
        return Arrays.asList(stringFile.split(LINES_DELIMITER));
    }

    public static List<String> splitStringOrder(String line) {
        return List.of(line.split(LINE_DELIMITER));
    }

    public static List<String> splitOneOrder(String order) {
        String product = splitProductAndQuantity(order.indexOf(START) + NEXT_INDEX, order.indexOf(SEPARATOR), order);
        String quantity = splitProductAndQuantity(order.indexOf(SEPARATOR) + NEXT_INDEX, order.indexOf(END), order);
        List<String> productAndQuantity = new ArrayList<>();
        productAndQuantity.add(product);
        productAndQuantity.add(quantity);
    
        return productAndQuantity;
    }

    private static String splitProductAndQuantity(int startIndex, int endIndex, String order) {
        return order.substring(startIndex, endIndex);
    }
}