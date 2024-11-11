package store.domain.promotion;

import store.domain.inventory.Product;
import store.enums.messages.ErrorMessage;

import java.util.List;
import java.util.Optional;

public class PromotionValidator {
    private final List<Product> products;
    private final List<Promotion> promotions;

    public PromotionValidator(List<Product> products, List<Promotion> promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public void validateProductName(String productName) {
        boolean productExists = products.stream()
                .anyMatch(product -> product.isSameName(productName));

        if (!productExists) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_PRODUCT_NAME.getMessage());
        }
    }

    public boolean isProductWithPromotion(String productName) {
        Optional<Product> productOpt = findProductWithPromotion(productName);
        return productOpt.isPresent() && hasSufficientPromotionQuantity(productOpt.get())
            && isPromotionDateValid(productOpt.get());
    }

    private Optional<Product> findProductWithPromotion(String productName) {
        return products.stream()
            .filter(product -> product.isSameName(productName))
            .filter(Product::hasPromotion)
            .findFirst();
    }

    private boolean hasSufficientPromotionQuantity(Product product) {
        return product.isEnoughQuantity();
    }

    private boolean isPromotionDateValid(Product product) {
        String promotionName = product.getNameOfPromotion();
        Optional<Promotion> promotionOpt = promotions.stream()
            .filter(promotion -> promotion.isSamePromotionName(promotionName))
            .findFirst();

        return promotionOpt.isPresent() && promotionOpt.get().isBetweenStartAndEndDate();
    }
}