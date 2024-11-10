package store.domain.inventory;

import store.domain.Product;
import store.domain.promotion.PromotionPolicy;
import store.enums.messages.ErrorMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Inventory {
    private final List<Product> products;
    private final PromotionPolicy promotionPolicy;

    public Inventory(List<Product> products, PromotionPolicy promotionPolicy) {
        this.products = products;
        this.promotionPolicy = promotionPolicy;
    }

    public boolean isProductWithPromotion(String productName) {
        return promotionPolicy.isProductWithPromotion(productName);
    }

    public Map<String, Integer> buyPromotionProduct(String productName, int purchaseQuantity) {   
        return promotionPolicy.buyPromotionProduct(productName, purchaseQuantity);
    }

    public void buyGeneralProduct(String productName, int currentQuantity) {
        Optional<Product> productOpt = findProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
        validateSufficientQuantity(productWithoutPromotion, currentQuantity);
        productWithoutPromotion.reduceQuantity(currentQuantity);
    }

    private void validateSufficientQuantity(Product product, int requiredQuantity) {
        if (product.isNotEnoughQuantityToBuy(requiredQuantity)) {
            throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
        }
    }

    private Optional<Product> findProductWithoutPromotion(String productName) {
        return this.products.stream()
                .filter(product -> product.isSameName(productName))
                .filter(product -> !product.hasPromotion())
                .findFirst();
    }

    public int getPriceOfProductPacket(String productName, int quantity) {
        Optional<Product> productOpt = findProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();

        return productWithoutPromotion.getPriceOfOnePacket(quantity);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(this.products);
    }
}