package store.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private static final int GET_ONLY_ONE = 0;

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

    private boolean isEnoughQuantityOfPromotionProduct(List<Product> products){
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

    public int checkGapBetweenQuantityAndBoon(String productName, int purchaseQuantity){ //수정
        Product product = getProductWithPromotion(productName);
        String promotionName = product.getNameOfPromotion();
        List<Promotion> promotions = this.promotions.stream()
            .filter(Promotion -> Promotion.isSamePromotionName(promotionName))
            .collect(Collectors.toList());
        Promotion promotion = promotions.get(GET_ONLY_ONE);
        return promotion.gapBetweenQuantityAndBoon(purchaseQuantity);
    }

    public int checkQuantityOfPromotionProduct(String productName, int purchaseQuantity) { // 수정
        Product product = getProductWithPromotion(productName);
        return product.gapBetweenQuantity(purchaseQuantity);
    }

    public void reduceQuantityOfPromotionProduct(String productName, int purchaseQuantity) {
        Product product = getProductWithPromotion(productName);
        product.reduceQuantity();
    }

    private Product getProductWithPromotion(String productName){
        List<Product> productsWithPromotion = findProductWithPromotion(productName);
        return productsWithPromotion.get(GET_ONLY_ONE);
    }

    private List<Product> findProductWithoutPromotion(String productName) {
        return this.products.stream()
            .filter(product -> product.isSameName(productName))
            .filter(product -> !product.hasPromotion())
            .collect(Collectors.toList());
    }

    public boolean isEnoughQuantityOfProduct(String productName, int purchaseQuantity) {
        List<Product> productsWithPromotion = findProductWithoutPromotion(productName);
        Product product = productsWithPromotion.get(GET_ONLY_ONE);

        return product.isEnoughQuantity(purchaseQuantity);
    }

    public void reduceQuantityOfProductWithoutPromotion(String productName, int purchaseQuantity) {
        List<Product> productsWithPromotion = findProductWithoutPromotion(productName);
        Product product = productsWithPromotion.get(GET_ONLY_ONE);
        product.reduceQuantity();
    }
    
}