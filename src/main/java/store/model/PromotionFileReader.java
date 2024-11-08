package store.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import store.enums.constants.FilePathConstants;
import store.enums.messages.ErrorMessage;

public class PromotionFileReader {
    private final String filePath = FilePathConstants.PROMOTION_FILE_PATH.getConstants();

    public PromotionFileReader() {}

    public String readFileAsString() {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.PROBLEM_WITH_FILE.getMessage());
        }
    }
}
