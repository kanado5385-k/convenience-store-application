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

        List<Integer> result = inventory.buyPromotionProduct("콜라", 3);
        assertThat(result.get(0)).isEqualTo(1);
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

        List<Integer> result = inventory.buyPromotionProduct("콜라", 5);
        assertThat(result.get(0)).isEqualTo(0);
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

        assertThatThrownBy(() -> inventory.buyPromotionProduct("콜라", 5))
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
        
        List<Integer> result = inventory.buyPromotionProduct("콜라", 1);
        assertThat(result.get(0)).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(9);
    }

    @Test
    void 프로모션_상품_구매_손님이_덜_가져올_경우_시용자_N() {

        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(
            List.of(productWithPromotion),
            List.of(promotion)
        );

        String simulatedInput = "N\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        List<Integer> result = inventory.buyPromotionProduct("콜라", 2);

        assertThat(result.get(0)).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(8);
    }

    @Test
    void 프로모션_상품_구매_손님이_덜_가져올_경우_시용자_Y() {
        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(
            List.of(productWithPromotion),
            List.of(promotion)
        );

        String simulatedInput = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        List<Integer> result = inventory.buyPromotionProduct("콜라", 2);

        assertThat(result.get(0)).isEqualTo(1);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(7);
    }

    @Test
    void 프로모션_상품_수량이_혜택수량_보다_많고_재고_상태가_충분하지_않을때_사용자_Y() {

        Product productWithPromotion = new Product("콜라", 1000, 7, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 10, "null");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(
            List.of(productWithPromotion, productWithoutPromotion),
            List.of(promotion)
        );

        String simulatedInput = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        List<Integer> result = inventory.buyPromotionProduct("콜라", 10);

        assertThat(result.get(0)).isEqualTo(2);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(0);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(7);
    }

    @Test
    void 프로모션_상품_수량이_혜택수량_보다_많고_재고_상태가_충분할때_사용자_Y() {

        Product productWithPromotion = new Product("콜라", 1000, 20, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 10, "null");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        Inventory inventory = new Inventory(
            List.of(productWithPromotion, productWithoutPromotion),
            List.of(promotion)
        );

        String simulatedInput = "Y\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        List<Integer> result = inventory.buyPromotionProduct("콜라", 10);

        assertThat(result.get(0)).isEqualTo(3);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(10);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(10);
    }

}