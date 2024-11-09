package store.domain;

import java.util.List;
import java.util.stream.Collectors;

import store.enums.messages.ErrorMessage;

public class Inventory {
    private static final int GET_ONLY_ONE = 0;
    private static final int NO_ANY_PRODUCT = 0;
    private static final int NO_ANY_PROMOTION_BOON = 0;

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

    public int checkGapBetweenQuantityAndBoon(String productName, int purchaseQuantity) { //수정
        Product product = getProductWithPromotion(productName);
        String promotionName = product.getNameOfPromotion();
        List<Promotion> promotions = this.promotions.stream()
            .filter(Promotion -> Promotion.isSamePromotionName(promotionName))
            .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);
        return promotion.gapBetweenQuantityAndBoon(purchaseQuantity);
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


    
    public int buyingPromotionPriduct(String productName, int purchaseQuantity) {
        Product productWithPromotion = getProductWithPromotion(productName);
        if(productWithPromotion.isSmallQuantityThanPromotionBoon(purchaseQuantity)) {
            int currentQuantityOfProduct = productWithPromotion.reduceQuantityOfPromotionProduct(purchaseQuantity);
            if(currentQuantityOfProduct < NO_ANY_PRODUCT) {
                Product productWithoutPromotion = getProductWithoutPromotion(productName);
                if(productWithoutPromotion.isNotEnoughQuantityToBuy(Math.abs(currentQuantityOfProduct))) {
                    throw new IllegalArgumentException(ErrorMessage.LACK_OF_PRODUCT.getMessage());
                }
                productWithoutPromotion.reduceQuantity(Math.abs(currentQuantityOfProduct));
                return NO_ANY_PROMOTION_BOON;
            }
        }return 1; //for test

    }
}