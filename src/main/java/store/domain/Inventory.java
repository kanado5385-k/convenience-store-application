package store.domain;

import java.util.List;
import java.util.stream.Collectors;

import store.enums.constants.AnswerConstants;
import store.enums.messages.ErrorMessage;
import store.utilities.Validator;
import store.view.input.InputViewOfPromotionIssue;

public class Inventory {
    private static final int GET_ONLY_ONE = 0;
    private static final int NO_ANY_PRODUCT = 0;
    private static final int GET_ONE_FREE = 1;
    private static final int NO_ANY_PROMOTION_BOON = 0;
    private static final int ONE_PROMOTION_BOON = 1;

    private final List<Product> products;
    private final List<Promotion> promotions;

    public Inventory(List<Product> products, List<Promotion> promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public boolean isProductWithPromotion(String productName) {
        List<Product> productsWithPromotion = findProductWithPromotion(productName);
        if (!productsWithPromotion.isEmpty()) {
            return isEnoughQuantityOfPromotionProduct(productsWithPromotion)
                    && isValidDateOfPromotion(productsWithPromotion);
        }
        return false;
    }

    private boolean isEnoughQuantityOfPromotionProduct(List<Product> products) {
        Product product = products.get(GET_ONLY_ONE);
        return product.isEnoughQuantity();
    }

    private List<Product> findProductWithPromotion(String productName) {
        return this.products.stream()
                .filter(product -> product.isSameName(productName))
                .filter(Product::hasPromotion)
                .collect(Collectors.toList());
    }

    private boolean isValidDateOfPromotion(List<Product> products) {
        Product product = products.get(GET_ONLY_ONE);
        String promotionName = product.getNameOfPromotion();
        List<Promotion> promotions = this.promotions.stream()
                .filter(promotion -> promotion.isSamePromotionName(promotionName))
                .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);
        return promotion.isBetweenStartAndEndDate();
    }

    public int buyPromotionProduct(String productName, int purchaseQuantity) {
        Product productWithPromotion = getProductWithPromotion(productName);
        int promotionBoon = getPromotionBoon(productName);

        if (productWithPromotion.isSmallQuantityThanPromotionBoon(promotionBoon)) {
            return handleSmallQuantityCase(productName, purchaseQuantity, productWithPromotion);
        }
        return processPurchaseQuantity(productName, purchaseQuantity, productWithPromotion, promotionBoon);
    }

    private int processPurchaseQuantity(String productName, int purchaseQuantity,
                                        Product productWithPromotion, int promotionBoon) {
        if (purchaseQuantity == promotionBoon) {
            return handleEqualPromotionBoonCase(purchaseQuantity, productWithPromotion);
        }
        if (purchaseQuantity < promotionBoon - GET_ONE_FREE) {
            return handleLessThanPromotionBoonMinusOneCase(purchaseQuantity, productWithPromotion);
        }
        if (purchaseQuantity == promotionBoon - GET_ONE_FREE) {
            return handleEqualPromotionBoonMinusOneCase(productName, purchaseQuantity, productWithPromotion);
        }
        return handleGreaterThanPromotionBoonCase(productName, purchaseQuantity, productWithPromotion, promotionBoon);
    }

    private Product getProductWithPromotion(String productName) {
        List<Product> productsWithPromotion = findProductWithPromotion(productName);
        return productsWithPromotion.get(GET_ONLY_ONE);
    }

    private int getPromotionBoon(String productName) {
        Product product = getProductWithPromotion(productName);
        String promotionName = product.getNameOfPromotion();
        List<Promotion> promotions = this.promotions.stream()
                .filter(promotion -> promotion.isSamePromotionName(promotionName))
                .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);
        return promotion.getPromotionBoon();
    }

    private int handleSmallQuantityCase(String productName, int purchaseQuantity, Product productWithPromotion) {
        int currentQuantity = productWithPromotion.firstReduceQuantityThanCheck(purchaseQuantity);
        if (currentQuantity >= NO_ANY_PRODUCT) {
            return 1;
        }
        return handleInsufficientPromotionProduct(productName, currentQuantity);
    }

    private int handleInsufficientPromotionProduct(String productName, int currentQuantity) {
        Product productWithoutPromotion = getProductWithoutPromotion(productName);
        int requiredQuantity = Math.abs(currentQuantity);
        validateSufficientQuantity(productWithoutPromotion, requiredQuantity);
        productWithoutPromotion.reduceQuantity(requiredQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private void validateSufficientQuantity(Product product, int requiredQuantity) {
        if (product.isNotEnoughQuantityToBuy(requiredQuantity)) {
            throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
        }
    }

    private int handleEqualPromotionBoonCase(int purchaseQuantity, Product productWithPromotion) {
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return ONE_PROMOTION_BOON;
    }

    private int handleLessThanPromotionBoonMinusOneCase(int purchaseQuantity, Product productWithPromotion) {
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private int handleEqualPromotionBoonMinusOneCase(String productName, int purchaseQuantity,
                                                     Product productWithPromotion) {
        String answer = getValidatedAnswerForOneMoreProduct(productName);
        if (answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            productWithPromotion.reduceQuantity(purchaseQuantity + ONE_PROMOTION_BOON);
            return ONE_PROMOTION_BOON;
        }
        productWithPromotion.reduceQuantity(purchaseQuantity);
        return NO_ANY_PROMOTION_BOON;
    }

    private String getValidatedAnswerForOneMoreProduct(String productName) {
        while (true) {
            try {
                String answer = InputViewOfPromotionIssue.readAnswerToOneMoreProduct(productName);
                Validator.validateAnswer(answer);
                return answer;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int handleGreaterThanPromotionBoonCase(String productName, int purchaseQuantity,
                                                   Product productWithPromotion, int promotionBoon) {
        int result = NO_ANY_PROMOTION_BOON;
        int currentPurchaseQuantity = purchaseQuantity;

        while (currentPurchaseQuantity > NO_ANY_PRODUCT) {
            int currentQuantity = productWithPromotion.firstReduceQuantityThanCheck(promotionBoon);
            currentPurchaseQuantity -= promotionBoon;

            if (currentQuantity < NO_ANY_PRODUCT) {
                return handleInsufficientQuantityInLoop(productName, currentQuantity, currentPurchaseQuantity, result);
            }
            result = updateResultForExcessQuantity(productName, currentPurchaseQuantity, result, productWithPromotion, promotionBoon);
        }

        return result;
    }

    private int updateResultForExcessQuantity(String productName, int currentPurchaseQuantity,
                                              int result, Product productWithPromotion, int promotionBoon) {
        if (currentPurchaseQuantity < NO_ANY_PRODUCT) {
            return handleExcessQuantity(productName, currentPurchaseQuantity, productWithPromotion, promotionBoon, result);
        }
        return result + ONE_PROMOTION_BOON;
    }

    private int handleInsufficientQuantityInLoop(String productName, int currentQuantity,
                                                 int currentPurchaseQuantity, int result) {
        currentPurchaseQuantity += GET_ONE_FREE;
        handleLackOfQuantity(productName, currentQuantity, currentPurchaseQuantity);
        return result;
    }

    private int handleExcessQuantity(String productName, int currentPurchaseQuantity,
                                     Product productWithPromotion, int promotionBoon, int result) {
        productWithPromotion.addQuantity(promotionBoon);
        result += buyPromotionProduct(productName, currentPurchaseQuantity + promotionBoon);
        return result;
    }

    private void handleLackOfQuantity(String productName, int currentQuantity, int currentPurchaseQuantity) {
        String answer = getValidatedAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
        processLackOfQuantityAnswer(productName, currentQuantity, answer);
    }

    private void processLackOfQuantityAnswer(String productName, int currentQuantity, String answer) {
        if (answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            handleValidQuantityReduction(productName, currentQuantity);
        }
    }

    private void handleValidQuantityReduction(String productName, int currentQuantity) {
        Product productWithoutPromotion = getProductWithoutPromotion(productName);
        int requiredQuantity = Math.abs(currentQuantity) + GET_ONE_FREE;
        validateSufficientQuantity(productWithoutPromotion, requiredQuantity);
        productWithoutPromotion.reduceQuantity(requiredQuantity);
    }

    private String getValidatedAnswerToLackOfQuantity(String productName, int currentPurchaseQuantity) {
        while (true) {
            try {
                String answer = InputViewOfPromotionIssue.readAnswerToLackOfQuantity(
                        productName, currentPurchaseQuantity);
                Validator.validateAnswer(answer);
                return answer;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Product getProductWithoutPromotion(String productName) {
        List<Product> productsWithoutPromotion = findProductWithoutPromotion(productName);
        return productsWithoutPromotion.get(GET_ONLY_ONE);
    }

    private List<Product> findProductWithoutPromotion(String productName) {
        return this.products.stream()
                .filter(product -> product.isSameName(productName))
                .filter(product -> !product.hasPromotion())
                .collect(Collectors.toList());
    }
}
