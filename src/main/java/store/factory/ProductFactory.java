package store.factory;

import store.domain.Product;
import store.utilities.Parser;
import store.utilities.Splitter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductFactory {
    private static final String HEADER_LINE = "name,price,quantity,promotion";
    private static final int EXPECTED_FIELD_COUNT = 4;
    private static final String NO_PROMOTION = "null";

    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    public List<Product> createProducts(String productInformation) {
        return Arrays.stream(Splitter.splitStringFileLines(productInformation))
            .filter(line -> !isSkippableProductLine(line))
            .map(this::parseProduct)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private boolean isSkippableProductLine(String line) {
        return line.trim().isEmpty() || line.startsWith(HEADER_LINE);
    }

    private Product parseProduct(String line) {
        String[] fields = Splitter.splitStringLine(line);
        if (fields.length != EXPECTED_FIELD_COUNT) {
            return null;
        }
        Integer price = Parser.parseNumberToInt(fields[PRICE_INDEX]);
        Integer quantity = Parser.parseNumberToInt(fields[QUANTITY_INDEX]);
        return new Product(fields[NAME_INDEX], price, quantity, getPromotionName(fields[PROMOTION_INDEX]));
    }

    private String getPromotionName(String promotion) {
        if (NO_PROMOTION.equals(promotion)) {
            return null;
        }
        return promotion;
    }
}