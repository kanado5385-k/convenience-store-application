package store.domain.inventory;

public class Product {
    private static final int NO_ANY_PRODUCT = 0;

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
        return this.name.equals(name);
    }

    public boolean hasPromotion() {
        return this.nameOfPromotion != null && !this.nameOfPromotion.isEmpty();
    }

    public boolean isEnoughQuantity() {
        return this.quantity > NO_ANY_PRODUCT;
    }

    public boolean isNotEnoughQuantityToBuy(int quantity) {
        return this.quantity < quantity;
    }

    public int firstReduceQuantityThanCheck(int purchaseQuantity) {
        int currentQuantity = this.quantity - purchaseQuantity;
        if(currentQuantity < NO_ANY_PRODUCT) {
            this.quantity = NO_ANY_PRODUCT;
            return currentQuantity;
        }
        this.quantity = currentQuantity;

        return currentQuantity;
    }

    public void reduceQuantity(int purchaseQuantity) {
        this.quantity -= purchaseQuantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
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

    public Integer getPriceOfOnePacket(int quantity) {
        return price * quantity;
    }

    public Integer getPromotionPrice(Integer quantityOfPromotionProducts) {
        Integer price = this.price/this.quantity;
        return quantityOfPromotionProducts * price;
    }

    public boolean isSmallQuantityThanPromotionBoon(int promotionBoon) {
        return quantity < promotionBoon;
    }

    public Integer addPriceToTotal(int totalPrice) {
        return price + totalPrice;
    }
}
