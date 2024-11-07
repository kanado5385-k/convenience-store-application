package store.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import store.enums.ErrorMessage;

public class ProductFileHandler {
    private String filePath = "src/main/resources/products.md";

    public ProductFileHandler(){};

    public String readFileAsString() {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage());
        }
    } 
    
    public String showProductsToUser() {
        try {
            String content = Files.readString(Path.of(filePath));
            content = content.replace("null", "").replace("0", "재고 없다");
            return content;
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage());
        }
    }
}