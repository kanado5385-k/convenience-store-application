package store.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import store.domain.Product;
import store.enums.constants.FilePathConstants;
import store.enums.messages.ErrorMessage;

public class ProductFileWriter {
    private static final String HEADER = "name,price,quantity,promotion";
    private static final int INDEX_OF_HEADER = 0;
    private static final String DELIMITER = ",";


    private final String filePath = FilePathConstants.PRODUCT_FILE_PATH.getConstants();

    public void writeProductsToFile(List<Product> products) {
        List<String> lines = prepareLines(products);
        writeLinesToFile(lines);
    }

    private List<String> prepareLines(List<Product> products) {
        List<String> productLines = formatProducts(products);
        return addHeader(productLines);
    }

    private List<String> formatProducts(List<Product> products) {
        return products.stream()
            .map(this::formatProduct)
            .toList();
    }

    private String formatProduct(Product product) {
        return String.join(DELIMITER,
            product.getName(),
            product.getPrice().toString(),
            product.getQuantity().toString(),
            product.getNameOfPromotion());
    }

    private List<String> addHeader(List<String> productLines) {
        productLines.add(INDEX_OF_HEADER, HEADER);
        return productLines;
    }

    private void writeLinesToFile(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage(), e);
        }
    }
}