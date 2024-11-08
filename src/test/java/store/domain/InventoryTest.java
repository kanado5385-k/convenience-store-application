package store.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class InventoryTest {

    @Test
    public void testIsProductWithPromotion_유효한프로모션() {
        Product product = new Product("Apple", 1000, 10, "Promo1");
        Promotion promotion = new Promotion("Promo1", 10, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList(promotion));

        boolean result = inventory.isProductWithPromotion("Apple");

        assertTrue(result);
    }

    @Test
    public void testIsProductWithPromotion_만료된프로모션() {
        Product product = new Product("Banana", 500, 20, "Promo2");
        Promotion promotion = new Promotion("Promo2", 15, "2020-01-01", "2021-01-01");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList(promotion));

        boolean result = inventory.isProductWithPromotion("Banana");

        assertFalse(result);
    }

    @Test
    public void testIsProductWithPromotion_프로모션없음() {
        Product product = new Product("Orange", 700, 15, "null");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList());

        boolean result = inventory.isProductWithPromotion("Orange");

        assertFalse(result);
    }

    @Test
    public void testCheckQuantityOfPromotionProduct_구매가능한수량() {
        Product product = new Product("Apple", 1000, 10, "Promo1");
        Promotion promotion = new Promotion("Promo1", 10, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList(promotion));

        int gap = inventory.checkQuantityOfPromotionProduct("Apple", 5);

        assertEquals(5, gap);
    }

    @Test
    public void testReduceQuantityOfPromotionProduct() {
        Product product = new Product("Apple", 1000, 10, "Promo1");
        Promotion promotion = new Promotion("Promo1", 10, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList(promotion));

        inventory.reduceQuantityOfPromotionProduct("Apple", 3);

        assertEquals(9, product.getQuantity());
    }

    @Test
    public void testReduceQuantityOfProductWithoutPromotion() {
        Product product = new Product("Orange", 700, 20, "null");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList());

        inventory.reduceQuantityOfProductWithoutPromotion("Orange", 5);

        assertEquals(19, product.getQuantity());
    }

}