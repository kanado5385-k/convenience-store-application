package store.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import store.utilities.Parser;
import store.utilities.Splitter;

public class Inventory {
    private final List<Product> products;
    private final List<Promotion> promotion;

    public Inventory(String productInformation, String promotionInformation){
        this.products = formatProductLine(productInformation);
        this.promotion = formatPromotionLine(promotionInformation);
    }


    private List<Product> formatProductLine(String productInformation) {
    return Arrays.stream(Splitter.splitStringFileLines(productInformation))
        .filter(line -> !isSkippableProductLine(line))
        .map(this::parseProduct)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }
    
    private boolean isSkippableProductLine(String line) {
        return line.trim().isEmpty() || line.startsWith("name,price,quantity,promotion");
    }
    
    private Product parseProduct(String line) {
        String[] fields = line.split(",");
        if (fields.length != 4) {
            return null;
        }
        Integer price = Parser.parseNumberToInt(fields[1]);
        Integer quantity = Parser.parseNumberToInt(fields[2]);

        return new Product(fields[0], price, quantity, getPromotionName(fields[3]));
    }
    
    private String getPromotionName(String boon) {
        if ("null".equals(boon)) {
            return null;
        }
        return boon;
    }

    private List<Promotion> formatPromotionLine(String promotionInformation) {
        return Arrays.stream(Splitter.splitStringFileLines(promotionInformation))
            .filter(line -> !isSkippablePromotionLine(line))
            .map(this::parsePromotion)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    private boolean isSkippablePromotionLine(String line) {
        return line.trim().isEmpty() || line.startsWith("name,buy,get,start_date,end_date");
    }
    
    private Promotion parsePromotion(String line) {
        String[] fields = line.split(",");
        if (fields.length != 5) {
            return null;
        }
        Integer get = Parser.parseNumberToInt(fields[2]);
        return new Promotion(fields[0],get, fields[3], fields[4]);
    }
}
