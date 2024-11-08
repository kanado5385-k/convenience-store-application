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
        List<Product> productsWithPromotion = findProductsWithPromotion(productName);
        if (!productsWithPromotion.isEmpty()) {
            return isValidDateOfPromotion(productsWithPromotion);
        }

        return false;
    }
    
    private List<Product> findProductsWithPromotion(String productName) {
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

    public int checkQuantityOfPromotionProduct(String productName, int quantity) {
        List<Product> productsWithPromotion = findProductsWithPromotion(productName);
        Product product = productsWithPromotion.get(GET_ONLY_ONE);
        return product.gapBetweenQuantity(quantity);
    }
}