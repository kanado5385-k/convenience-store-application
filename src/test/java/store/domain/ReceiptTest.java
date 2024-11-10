package store.domain;


import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class ReceiptTest {
    @Test
    void 멤버십할인_적용된_영수증_생성_테스트() {
        Product cocaWithoutPromotion = new Product("콜라", 1000, 5, null);
        Product spriteWithoutPromotion = new Product("사이다", 1500, 2, null);
 
        Inventory inventory = new Inventory(
            List.of(cocaWithoutPromotion,spriteWithoutPromotion),
            List.of()
        );

        String orderInput = "[콜라-3],[사이다-2]";
        Order order = Order.createOrder(orderInput, inventory);

        Receipt receipt = Receipt.createReceipt(order, "Y");

        assertThat(receipt.getPromotionDiscount()).isEqualTo(0);
        assertThat(receipt.getMemeberSipDiscount()).isEqualTo(1800);
        assertThat(receipt.getGeneralPrice()).isEqualTo(6000);
        assertThat(receipt.getTotalPrice()).isEqualTo(4200);
    }
}
