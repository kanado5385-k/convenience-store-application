package store.domain;

public class Product {
    private static final String NOT_HAVE_PROMOTION = "null";

    private final String name;
    private final Integer price;
    private final String nameOfPromotion;
    private Integer quantity;

    public Product(String name, Integer price, Integer quantity, String nameOfPromotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.nameOfPromotion = nameOfPromotion;  
    }

    public boolean isSameName(String name) {
        return this.name == name;
    }

    public boolean hasPromotion() {
        return !NOT_HAVE_PROMOTION.equals(this.nameOfPromotion);
    }

    public boolean isEnoughQuantity(Integer purchaseQuantity) {
        return this.quantity >= purchaseQuantity;
    }

    public int gapBetweenQuantity(int purchaseQuantity) {
        return quantity - purchaseQuantity;
    }

    public void reduceQuantity() {
        this.quantity --;
    }

    public void addQuantity(int quantity) {
        this.quantity =+ quantity;
    }

    public String getName() {
        return this.name;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public String getNameOfPromotion() {
        return this.nameOfPromotion;
    }

    public Integer addPriceToTotal(Integer totalPrice) {
        return totalPrice += this.price;
    }

    public Integer getPromotionPrice(Integer quantityOfPromotionProduct){
        Integer price = this.price/this.quantity;
        return quantityOfPromotionProduct * price;
    }
}
