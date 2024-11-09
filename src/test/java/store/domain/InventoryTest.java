package store.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import store.enums.messages.ErrorMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

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
    void 프로모션_상품_구매_재고_부족_일반상품에서_재고차감() {
        Product productWithPromotion = new Product("콜라", 1000, 2, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 10, "null");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        Inventory inventory = new Inventory(
            List.of(productWithPromotion, productWithoutPromotion),
            List.of(promotion)
        );

        int result = inventory.buyingPromotionPriduct("콜라", 5);
        assertThat(result).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(0);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(7);
    }

    @Test
    void 프로모션_상품_및_일반_상품_재고_부족으로_예외() {
        Product productWithPromotion = new Product("콜라", 1000, 2, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 1, "null");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        Inventory inventory = new Inventory(
            List.of(productWithPromotion, productWithoutPromotion),
            List.of(promotion)
        );

        assertThatThrownBy(() -> inventory.buyingPromotionPriduct("콜라", 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorMessage.LACK_OF_PRODUCT.getMessage());
    }

    /**
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

    
    @ParameterizedTest
    @CsvSource({
        "Orange, 15, 10, true",
        "Orange, 15, 20, false"
    })
    public void testIsEnoughQuantityOfProduct(String productName, int productQuantity, int purchaseQuantity, boolean expectedResult) {
        Product product = new Product(productName, 700, productQuantity, "null");
        Inventory inventory = new Inventory(Arrays.asList(product), Arrays.asList());

        boolean result = inventory.isEnoughQuantityOfProduct(productName, purchaseQuantity);

        assertEquals(expectedResult, result);
    }
    */

}