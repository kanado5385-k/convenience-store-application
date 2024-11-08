package store.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import store.enums.constants.FilePathConstants;
import store.enums.messages.ErrorMessage;
import store.utilities.Splitter;

public class ProductFileReader {
    public static final String PRICE_FORMAT = "%,d%s";
    private static final String HEADER_PREFIX = "- ";
    private static final String WON_SUFFIX = "원";
    private static final String QUANTITY_SUFFIX = "개";
    private static final String NO_STOCK = "재고 없음";
    private static final String NULL_STRING = "null";
    private static final String EMPTY_STRING = "";
    private static final String NEWLINE = "\n";
    private static final String ZERO_STRING = "0";
    private static final int HEADER_LINE_INDEX = 1;

    private static final int TOKEN_NAME = 0;
    private static final int TOKEN_PRICE = 1;
    private static final int TOKEN_QUANTITY = 2;
    private static final int TOKEN_PROMOTION = 3;

    private final String filePath = FilePathConstants.PRODUCT_FILE_PATH.getConstants();

    public ProductFileReader() {}

    public String readFileAsString() {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage());
        }
    }

    public String showProductsToUser() {
        List<String> lines = readProductLines();
            return lines.stream()
                .skip(HEADER_LINE_INDEX)
                .map(this::formatProductLine)
                .collect(Collectors.joining(NEWLINE));
    }

    private List<String> readProductLines() {
        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage());
        }
    }

    private String formatProductLine(String line) {
        String[] tokens = Splitter.splitStringLine(line);
        String productLine = HEADER_PREFIX + tokens[TOKEN_NAME] + " " 
            + formatPrice(tokens[TOKEN_PRICE]) + " " + formatQuantity(tokens[TOKEN_QUANTITY]);
        if (!formatPromotion(tokens[TOKEN_PROMOTION]).isEmpty()) {
            productLine += " " + formatPromotion(tokens[TOKEN_PROMOTION]);
        }
    
        return productLine;
    }

    private String formatPrice(String price) {
        int priceValue = Integer.parseInt(price);
        return String.format(PRICE_FORMAT, priceValue, WON_SUFFIX);
    }

    private String formatQuantity(String quantity) {
        if (quantity.equals(ZERO_STRING)) {
            return NO_STOCK;
        }
        return quantity + QUANTITY_SUFFIX;
    }

    private String formatPromotion(String promotion) {
        if (promotion.equals(NULL_STRING)) {
            return EMPTY_STRING;
        }
        return promotion;
    }
    
}
