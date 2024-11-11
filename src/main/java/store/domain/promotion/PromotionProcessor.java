package store.domain.promotion;

import store.domain.inventory.InventoryManager;
import store.domain.inventory.Product;
import store.enums.constants.AnswerConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PromotionProcessor {
    private static final int NO_ANY_PRODUCT = 0;
    private static final int GET_ONE_FREE = 1;
    private static final int NO_ANY_PROMOTION_BOON = 0;
    private static final int ONE_PROMOTION_BOON = 1;

    private final InventoryManager inventoryManager;
    private final UserInteractionHandler userInteractionHandler;
    private final List<Promotion> promotions;
    private final Map<String, Integer> promotionProducts;

    public PromotionProcessor(InventoryManager inventoryManager, UserInteractionHandler userInteractionHandler, List<Promotion> promotions) {
        this.inventoryManager = inventoryManager;
        this.userInteractionHandler = userInteractionHandler;
        this.promotions = promotions;
        this.promotionProducts = new HashMap<>(Map.of(
        "benefit", NO_ANY_PRODUCT,
        "adjusted", NO_ANY_PRODUCT,
        "added", NO_ANY_PRODUCT
    ));
    }

    private void incrementBenefit(int value) {
        promotionProducts.put("benefit", promotionProducts.get("benefit") + value);
    }

    private void incrementAdjusted(int value) {
        promotionProducts.put("adjusted", promotionProducts.get("adjusted") + value);
    }

    private void incrementAdded(int value) {
        promotionProducts.put("added", promotionProducts.get("added") + value);
    }

    public Map<String, Integer> buyPromotionProduct(String productName, int purchaseQuantity) {
        Product productWithPromotion = getProductWithPromotion(productName);
        int promotionBoon = getPromotionBoon(productWithPromotion);
        int result = processPurchase(productName, purchaseQuantity, productWithPromotion, promotionBoon);
        incrementBenefit(result);
        return this.promotionProducts;
    }

    private Product getProductWithPromotion(String productName) {
        Optional<Product> productOpt = inventoryManager.findProductWithPromotion(productName);
        return productOpt.get();
    }

    private int processPurchase(String productName, int purchaseQuantity, Product productWithPromotion, int promotionBoon) {
        if (productWithPromotion.isSmallQuantityThanPromotionBoon(promotionBoon)) {
            return handleSmallQuantityCase(productName, purchaseQuantity, productWithPromotion);
        }
        return processPurchaseQuantity(productName, purchaseQuantity, productWithPromotion, promotionBoon);
    }

    private int getPromotionBoon(Product product) {
        String promotionName = product.getNameOfPromotion();
        Optional<Promotion> promotionOpt = promotions.stream()
                .filter(promotion -> promotion.isSamePromotionName(promotionName))
                .findFirst();
        Promotion promotion = promotionOpt.get();
        return promotion.getPromotionBoon();
    }

    private int handleSmallQuantityCase(String productName, int purchaseQuantity, Product productWithPromotion) {
        int currentQuantity = productWithPromotion.firstReduceQuantityThanCheck(purchaseQuantity);
        if (currentQuantity >= NO_ANY_PRODUCT) {
            return ONE_PROMOTION_BOON;
        }
        return handleInsufficientPromotionProduct(productName, currentQuantity);
    }

    private int handleInsufficientPromotionProduct(String productName, int currentQuantity) {
        Optional<Product> productOpt = inventoryManager.findProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
        int requiredQuantity = Math.abs(currentQuantity);
        inventoryManager.reduceProductQuantity(productWithoutPromotion, requiredQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private int processPurchaseQuantity(String productName, int purchaseQuantity,
                                        Product productWithPromotion, int promotionBoon) {
        if (isSpecialCase(purchaseQuantity, promotionBoon)) {
            return handleSpecialCase(productName, purchaseQuantity, productWithPromotion, promotionBoon);
        }
        return handleGreaterThanPromotionBoonCase(productName, purchaseQuantity, productWithPromotion, promotionBoon);
    }

    private boolean isSpecialCase(int purchaseQuantity, int promotionBoon) {
        return purchaseQuantity == promotionBoon ||
            purchaseQuantity < promotionBoon - GET_ONE_FREE ||
            purchaseQuantity == promotionBoon - GET_ONE_FREE;
    }

    private int handleSpecialCase(String productName, int purchaseQuantity,
                                Product productWithPromotion, int promotionBoon) {
        if (purchaseQuantity == promotionBoon)
            return handleEqualPromotionBoonCase(purchaseQuantity, productWithPromotion);
        if (purchaseQuantity < promotionBoon - GET_ONE_FREE)
            return handleLessThanPromotionBoonMinusOneCase(purchaseQuantity, productWithPromotion);
        return handleEqualPromotionBoonMinusOneCase(productName, purchaseQuantity, productWithPromotion);
    }

    private int handleEqualPromotionBoonCase(int purchaseQuantity, Product productWithPromotion) {
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return ONE_PROMOTION_BOON;
    }

    private int handleLessThanPromotionBoonMinusOneCase(int purchaseQuantity, Product productWithPromotion) {
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private int handleEqualPromotionBoonMinusOneCase(String productName, int purchaseQuantity,Product productWithPromotion) {
        String answer = userInteractionHandler.getValidatedAnswerForOneMoreProduct(productName);
        if (answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            productWithPromotion.reduceQuantity(purchaseQuantity + ONE_PROMOTION_BOON);
            incrementAdded(ONE_PROMOTION_BOON);
            return ONE_PROMOTION_BOON;
        }
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private int handleGreaterThanPromotionBoonCase(String productName, int purchaseQuantity,
                                               Product productWithPromotion, int promotionBoon) {
        int result = NO_ANY_PROMOTION_BOON;
        int currentPurchaseQuantity = purchaseQuantity;
        result += calculatePromotionBoons(productName, productWithPromotion, promotionBoon, currentPurchaseQuantity);
    
        return result;
    }

    private int calculatePromotionBoons(String productName, Product productWithPromotion, 
                                        int promotionBoon, int currentPurchaseQuantity) {
        int result = NO_ANY_PROMOTION_BOON;
        while (currentPurchaseQuantity > NO_ANY_PRODUCT) {
            result += ONE_PROMOTION_BOON;
            currentPurchaseQuantity = processSinglePromotionCycle(productName, productWithPromotion, 
                                                                promotionBoon, currentPurchaseQuantity, result);
        }
        return result - ONE_PROMOTION_BOON;
    }

    private int processSinglePromotionCycle(
        String productName, Product productWithPromotion, 
        int promotionBoon, int currentPurchaseQuantity, int result
    ) {
        int beforQuantity = productWithPromotion.getQuantity();                                        
        int currentQuantity = productWithPromotion.firstReduceQuantityThanCheck(promotionBoon);
        currentPurchaseQuantity -= promotionBoon; 
    
        if (currentQuantity < NO_ANY_PRODUCT) {
            handleInsufficientQuantityInLoop(
                productName, currentQuantity, beforQuantity,
                currentPurchaseQuantity, promotionBoon, productWithPromotion
            );
            return - ONE_PROMOTION_BOON;                                        
        }   
        return currentPurchaseQuantity;
    }

    private void handleInsufficientQuantityInLoop(String productName, int currentQuantity, int beforQuantity,
                                            int currentPurchaseQuantity, int promotionBoon, Product productWithPromotion) {
        currentPurchaseQuantity += promotionBoon;
        handleLackOfQuantity(productName, currentQuantity, beforQuantity, currentPurchaseQuantity, productWithPromotion);
    }

    private void handleLackOfQuantity(String productName, int currentQuantity, int beforQuantity,
                                 int currentPurchaseQuantity, Product productWithPromotion) {
        String answer = userInteractionHandler.getValidatedAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
        processLackOfQuantityAnswer(productName, currentQuantity, beforQuantity, answer, currentPurchaseQuantity, productWithPromotion);
    }

    private void processLackOfQuantityAnswer(String productName, int currentQuantity, int beforQuantity,
                                            String answer, int currentPurchaseQuantity, Product productWithPromotion) {
        if (answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            if (currentPurchaseQuantity == beforQuantity){
                return;
            }
            handleValidQuantityReduction(productName, currentQuantity, beforQuantity);
        }
        if (answer.equals(AnswerConstants.ANSWER_NO.getConstants())) {
            productWithPromotion.addQuantity(beforQuantity);
            incrementAdjusted(currentPurchaseQuantity);
        }
    }

    private void handleValidQuantityReduction(String productName, int currentQuantity, int beforQuantity) {
        Optional<Product> productOpt = inventoryManager.findProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
        int requiredQuantity = (Math.abs(currentQuantity))+ beforQuantity;
        inventoryManager.reduceProductQuantity(productWithoutPromotion, requiredQuantity);
    }
}