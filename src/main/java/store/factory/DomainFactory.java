package store.factory;

import java.util.List;

import store.domain.Buying;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.inventory.Inventory;
import store.domain.promotion.PromotionPolicy;

public class DomainFactory {

    public Buying createBuying() {
        return Buying.startBuying();
    }

    public ProductFactory createProductFactory() {
        return new ProductFactory();
    }

    public PromotionFactory createPromotionFactory() {
        return new PromotionFactory();
    }

    public PromotionPolicy createPromotionPolicy(List<Product> products, List<Promotion> promotions) {
        return new PromotionPolicy(products, promotions);
    }

    public Inventory createInventory(List<Product> products, PromotionPolicy policy) {
        return new Inventory(products, policy);
    }

    public Order createOrder(String stringOrder, Inventory inventory) {
        return Order.createOrder(stringOrder, inventory);
    }

    public Receipt createReceipt(Order order, String answerToMembership) {
        return Receipt.createReceipt(order, answerToMembership);
    }
}
