package store.enums;

public enum FilePathConstants implements CommonConstants {
    FILE_PATH("src/main/resources/products.md");

    private final String constants;

    FilePathConstants(String constants) {
        this.constants = constants;
    }

    @Override
    public String getConstants() {
        return constants;
    }
}
