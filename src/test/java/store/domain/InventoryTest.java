package store.domain;

import org.junit.jupiter.api.Test;
import store.enums.messages.ErrorMessage;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryTest {

    @Test
    void 프로모션_상품_구매_재고_충분() {
        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        Inventory inventory = new Inventory(
            List.of(productWithPromotion),
            List.of(promotion)
        );

        int result = inventory.buyingPromotionPriduct("콜라", 3);
        assertThat(result).isEqualTo(1);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(7);
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

    @Test
    void 프로모션_상품_구매_재고_충분_프로모션_재고차감() {
        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        Inventory inventory = new Inventory(
            List.of(productWithPromotion),
            List.of(promotion)
        );

        int result = inventory.buyingPromotionPriduct("콜라", 1);
        assertThat(result).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(9);
    }

    @Test
    void 프로모션_상품_구매_손님이_덜_가져올_경우() {

        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(
            List.of(productWithPromotion),
            List.of(promotion)
        );

        String simulatedInput = "N\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        int result = inventory.buyingPromotionPriduct("콜라", 2);

        assertThat(result).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(8);
    }

}