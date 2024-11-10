package store.domain.promotion;

import store.domain.Product;
import store.domain.Promotion;
import store.domain.inventory.InventoryManager;

import java.util.List;

public class PromotionPolicy {
    private final PromotionValidator promotionValidator;
    private final PromotionProcessor promotionProcessor;

    public PromotionPolicy(List<Product> products, List<Promotion> promotions) {
        InventoryManager inventoryManager = new InventoryManager(products);
        UserInteractionHandler userInteractionHandler = new UserInteractionHandler();
        this.promotionValidator = new PromotionValidator(products, promotions);
        this.promotionProcessor = new PromotionProcessor(inventoryManager, userInteractionHandler, promotions);
    }

    public boolean isProductWithPromotion(String productName) {
        promotionValidator.validateProductName(productName);
        return promotionValidator.isProductWithPromotion(productName);
    }

    public List<Integer> buyPromotionProduct(String productName, int purchaseQuantity) {
        return promotionProcessor.buyPromotionProduct(productName, purchaseQuantity);
    }
}