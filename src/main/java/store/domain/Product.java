package store.domain;

public class Product {
    public static final int NOT_HAVE_PROMOTION = 0;

    private String name;
    private Integer price;
    private Integer quantity;
    private Integer promotionBoon;

    public Product(String name, Integer price, Integer quantity, Integer promotionBoon){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionBoon = promotionBoon;  
    }

    public boolean isSameName(String name){
        return this.name == name;
    }

    public boolean isHavePromotion(){
        return this.promotionBoon != NOT_HAVE_PROMOTION; 
    }

    public boolean isEnoughQuantity(Integer purchaseQuantity){
        return this.quantity >= purchaseQuantity;
    }

    public void reduceQuantity(){
        this.quantity --;
    }

    public String getName(){
        return this.name;
    }

    public Integer getPrice(){
        return this.price;
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public Integer getPromotionBoon(){
        return this.promotionBoon;
    }
}
