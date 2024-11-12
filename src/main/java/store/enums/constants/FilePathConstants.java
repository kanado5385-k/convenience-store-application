package store.enums.constants;

public enum FilePathConstants implements CommonConstants {
    PRODUCT_FILE_PATH("src/main/resources/products.md"),
    PROMOTION_FILE_PATH("src/main/resources/promotions.md");

    private final String constants;

    FilePathConstants(String constants) {
        this.constants = constants;
    }

    @Override
    public String getConstants() {
        return constants;
    }
}
