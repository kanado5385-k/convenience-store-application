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
            .filter(Promotion -> Promotion.isSamePromotionName(promotionName))
            .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);

        return promotion.isBetweenStartAndEndDate();
    } 

    private int getPromotionBoon(String productName) {
        Product product = getProductWithPromotion(productName);
        String promotionName = product.getNameOfPromotion();
        List<Promotion> promotions = this.promotions.stream()
            .filter(Promotion -> Promotion.isSamePromotionName(promotionName))
            .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);

        return promotion.getPromotionBoon();
    }

    private List<Product> findProductWithoutPromotion(String productName) {
        return this.products.stream()
            .filter(product -> product.isSameName(productName))
            .filter(product -> !product.hasPromotion())
            .collect(Collectors.toList());
    }

    private Product getProductWithPromotion(String productName) {
        List<Product> productsWithPromotion = findProductWithPromotion(productName);
        return productsWithPromotion.get(GET_ONLY_ONE);
    }

    public Product getProductWithoutPromotion(String productName) {
        List<Product> productsWithoutPromotion = findProductWithoutPromotion(productName);
        return productsWithoutPromotion.get(GET_ONLY_ONE);
    }


    
    public int buyingPromotionProduct(String productName, int purchaseQuantity) {
        Product productWithPromotion = getProductWithPromotion(productName);
        int promotionBoon = getPromotionBoon(productName);
        if(productWithPromotion.isSmallQuantityThanPromotionBoon(promotionBoon)) {
            int currentQuantityOfProduct = productWithPromotion.firstReduceQuantityThanCheck(purchaseQuantity);
            if(currentQuantityOfProduct < NO_ANY_PRODUCT) {
                Product productWithoutPromotion = getProductWithoutPromotion(productName);
                if(productWithoutPromotion.isNotEnoughQuantityToBuy(Math.abs(currentQuantityOfProduct))) {
                    throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
                }
                productWithoutPromotion.reduceQuantity(Math.abs(currentQuantityOfProduct));

                return NO_ANY_PROMOTION_BOON;
            }
        }
        if(purchaseQuantity == promotionBoon) {
            productWithPromotion.reduceQuantity(purchaseQuantity);

            return ONE_PROMOTION_BOON;
        }
        if(purchaseQuantity < (promotionBoon - GET_ONE_FREE)) {
            productWithPromotion.reduceQuantity(purchaseQuantity);

            return NO_ANY_PROMOTION_BOON;
        }
        if(purchaseQuantity == (promotionBoon - GET_ONE_FREE)) {
            boolean isValid = false;
            String answer = "";
            while (!isValid) {
                try {
                    answer = InputViewOfPromotionIssue.readAnswerToOneMoreProduct(productName);
                    Validator.validateAnswer(answer);
                    isValid = true;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(answer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
                productWithPromotion.reduceQuantity(purchaseQuantity + ONE_PROMOTION_BOON);
                return ONE_PROMOTION_BOON;
            }
            productWithPromotion.reduceQuantity(purchaseQuantity);
            return NO_ANY_PROMOTION_BOON;
        }
        if(purchaseQuantity > promotionBoon){
            int currentPurchaseQuantity = purchaseQuantity;
            int result = 0;
            while (true) {
                int currentQuantityOfProduct = productWithPromotion.firstReduceQuantityThanCheck(promotionBoon);
                currentPurchaseQuantity =- promotionBoon;
                if (currentQuantityOfProduct < 0){
                    boolean isValid = false;
                    String answer = "";
                    while (!isValid) {
                        try {
                            answer = InputViewOfPromotionIssue.readAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
                            Validator.validateAnswer(answer);
                            isValid = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if(answer.equals(AnswerConstants.ANSWER_YES.getConstants())){
                        Product productWithoutPromotion = getProductWithoutPromotion(productName);
                        if(productWithoutPromotion.isNotEnoughQuantityToBuy(currentPurchaseQuantity)) {
                            throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
                        }
                        productWithoutPromotion.reduceQuantity(currentPurchaseQuantity);
                    }
                    break;
                }
                result =+ 1;
            }
            return result;
        }
        return 1;
    }

}