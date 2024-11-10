package store.domain;


import org.junit.jupiter.api.Test;

import store.domain.inventory.Inventory;
import store.domain.promotion.PromotionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class ReceiptTest {

    @Test
    void 멤버십할인_적용된_영수증_생성_테스트() {
        Product cocaWithoutPromotion = new Product("콜라", 1000, 5, null);
        Product spriteWithoutPromotion = new Product("사이다", 1500, 2, null);

        List<Product> products = List.of(cocaWithoutPromotion, spriteWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "[콜라-3],[사이다-2]";
        Order order = Order.createOrder(orderInput, inventory);

        Receipt receipt = Receipt.createReceipt(order, "Y");

        assertThat(receipt.getPromotionDiscount()).isEqualTo(0);
        assertThat(receipt.getMemberShipDiscount()).isEqualTo(1500);
        assertThat(receipt.getGeneralPrice()).isEqualTo(6000);
        assertThat(receipt.getTotalPrice()).isEqualTo(4500);
    }

    @Test
    void 멤버십할인_미적용_영수증_생성_테스트() {
        Product cocaWithoutPromotion = new Product("콜라", 1000, 5, null);
        Product spriteWithoutPromotion = new Product("사이다", 1500, 2, null);

        List<Product> products = List.of(cocaWithoutPromotion, spriteWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "[콜라-3],[사이다-2]";
        Order order = Order.createOrder(orderInput, inventory);

        Receipt receipt = Receipt.createReceipt(order, "N");

        assertThat(receipt.getPromotionDiscount()).isEqualTo(0);
        assertThat(receipt.getMemberShipDiscount()).isEqualTo(0);
        assertThat(receipt.getGeneralPrice()).isEqualTo(6000);
        assertThat(receipt.getTotalPrice()).isEqualTo(6000);
    }
}