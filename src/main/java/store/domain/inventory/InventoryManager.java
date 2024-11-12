package store.domain.inventory;

import store.enums.messages.ErrorMessage;

import java.util.List;
import java.util.Optional;

public class InventoryManager {
    private final List<Product> products;

    public InventoryManager(List<Product> products) {
        this.products = products;
    }

    public void reduceProductQuantity(Product product, int quantity) {
        if (product.isNotEnoughQuantityToBuy(quantity)) {
            throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
        }
        product.reduceQuantity(quantity);
    }

    public Optional<Product> findProductWithPromotion(String productName) {
        return products.stream()
            .filter(product -> product.isSameName(productName))
            .filter(Product::hasPromotion)
            .findFirst();
    }

    public Optional<Product> findProductWithoutPromotion(String productName) {
        return products.stream()
            .filter(product -> product.isSameName(productName))
            .filter(product -> !product.hasPromotion())
            .findFirst();
    }
}