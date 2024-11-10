package store.controller;

import store.model.ProductFileReader;
import store.model.ProductFileWriter;
import store.model.PromotionFileReader;

public class ModelFactory {

    public ProductFileReader createProductFileReader() {
        return new ProductFileReader();
    }

    public ProductFileWriter createProductFileWriter() {
        return new ProductFileWriter();
    }

    public PromotionFileReader createPromotionFileReader() {
        return new PromotionFileReader();
    }
}
