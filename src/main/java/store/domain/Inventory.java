package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import store.enums.constants.AnswerConstants;
import store.enums.messages.ErrorMessage;
import store.utilities.Validator;
import store.view.input.InputViewOfPromotionIssue;

public class Inventory {
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
        isValidateNameOfProduct(productName);
        Optional<Product> productOpt = findProductWithPromotion(productName);
        return productOpt.isPresent() && isEnoughQuantityOfPromotionProduct(productOpt.get())
                && isValidDateOfPromotion(productOpt.get());
    }

    private void isValidateNameOfProduct(String productName) {
        boolean productExists = this.products.stream()
            .anyMatch(product -> product.isSameName(productName));

        if (!productExists) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_PRODUCT_NAME.getMessage());
        }
    }

    private boolean isEnoughQuantityOfPromotionProduct(Product product) {
        return product.isEnoughQuantity();
    }

    private Optional<Product> findProductWithPromotion(String productName) {
        return this.products.stream()
                .filter(product -> product.isSameName(productName))
                .filter(Product::hasPromotion)
                .findFirst();
    }

    private boolean isValidDateOfPromotion(Product product) {
        String promotionName = product.getNameOfPromotion();
        Optional<Promotion> promotionOpt = this.promotions.stream()
                .filter(promotionObj -> promotionObj.isSamePromotionName(promotionName))
                .findFirst();

        return promotionOpt.isPresent() && promotionOpt.get().isBetweenStartAndEndDate();
    }

    public List<Integer> buyPromotionProduct(String productName, int purchaseQuantity) {
        List<Integer> resultList = new ArrayList<>();

        Optional<Product> productOpt = getProductWithPromotion(productName);
        Product productWithPromotion = productOpt.get();
        int promotionBoon = getPromotionBoon(productName);

        int result;
        int lackQuantity = NO_ANY_PRODUCT;
        if (productWithPromotion.isSmallQuantityThanPromotionBoon(promotionBoon)) {
            result = handleSmallQuantityCase(productName, purchaseQuantity, productWithPromotion);
        } else {
            result = processPurchaseQuantity(productName, purchaseQuantity, productWithPromotion, promotionBoon);
        }

        resultList.add(result);
        resultList.add(lackQuantity);

        return resultList;
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

    public Optional<Product> getProductWithPromotion(String productName) {
        return findProductWithPromotion(productName);
    }

    private int getPromotionBoon(String productName) {
        Optional<Product> productOpt = getProductWithPromotion(productName);
        Product product = productOpt.get();
        String promotionName = product.getNameOfPromotion();
        Optional<Promotion> promotionOpt = this.promotions.stream()
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
        Optional<Product> productOpt = getProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
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
        int lackQuantity = handleLackOfQuantity(productName, currentQuantity, currentPurchaseQuantity);
        return result + lackQuantity;
    }

    private int handleExcessQuantity(String productName, int currentPurchaseQuantity,
                                     Product productWithPromotion, int promotionBoon, int result) {
        productWithPromotion.addQuantity(promotionBoon);
        List<Integer> promotionResult = buyPromotionProduct(productName, currentPurchaseQuantity + promotionBoon);
        result += promotionResult.get(0);
        return result;
    }

    private int handleLackOfQuantity(String productName, int currentQuantity, int currentPurchaseQuantity) {
        String answer = getValidatedAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
        return processLackOfQuantityAnswer(productName, currentQuantity, answer);
    }

    private int processLackOfQuantityAnswer(String productName, int currentQuantity, String answer) {
        if (answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            handleValidQuantityReduction(productName, currentQuantity);
            return NO_ANY_PRODUCT;
        } else if (answer.equals(AnswerConstants.ANSWER_NO.getConstants())) {
            return currentQuantity;
        }
        return NO_ANY_PRODUCT;
    }

    private void handleValidQuantityReduction(String productName, int currentQuantity) {
        Optional<Product> productOpt = getProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
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

    public Optional<Product> getProductWithoutPromotion(String productName) {
        return findProductWithoutPromotion(productName);
    }

    private Optional<Product> findProductWithoutPromotion(String productName) {
        return this.products.stream()
                .filter(product -> product.isSameName(productName))
                .filter(product -> !product.hasPromotion())
                .findFirst();
    }

    public void buyGeneralProduct(String productName, int currentQuantity) {
        Optional<Product> productOpt = getProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();
        validateSufficientQuantity(productWithoutPromotion, currentQuantity);
        productWithoutPromotion.reduceQuantity(currentQuantity);
    }

    public int getPriceOfProductPacket(String productName, int quantity) {
        Optional<Product> productOpt = getProductWithoutPromotion(productName);
        Product productWithoutPromotion = productOpt.get();

        return productWithoutPromotion.getPriceOfOnePacket(quantity);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(this.products);
    }
}