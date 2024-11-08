package store.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final List<Product> products;
    private final List<Promotion> promotions;

    public Inventory(List<Product> products, List<Promotion> promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public boolean isProductWithPromotion(String productName){
        List<Product> products = this.products.stream()
            .filter(Product -> Product.isSameName(productName))
            .filter(Product -> Product.hasPromotion())
            .collect(Collectors.toList());
        if (products.isEmpty()){
            return false;
        }

        return isValidDateOfPromotion(products);      
    }

    private boolean isValidDateOfPromotion(List<Product> products){
        Product product = products.get(0);
        String promotionName = product.getPromotionBoon();
        List<Promotion> promotions = this.promotions.stream()
            .filter(Promotion -> Promotion.isSamePromotionName(promotionName))
            .collect(Collectors.toList());
        Promotion promotion = promotions.get(0);

        return promotion.isBetweenStartAndEndDate();
    } 

}
