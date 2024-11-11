package store.domain;

import org.junit.jupiter.api.Test;

import store.domain.inventory.Inventory;
import store.domain.inventory.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPolicy;
import store.enums.messages.ErrorMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @Test
    void 프로모션_상품_및_일반_상품_재고_부족으로_예외_발생() {
        Product productWithPromotion = new Product("콜라", 1000, 2, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 1, null);
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        List<Product> products = List.of(productWithPromotion, productWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of(promotion));
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "[콜라-5]";

        assertThatThrownBy(() -> Order.createOrder(orderInput, inventory))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorMessage.LACK_OF_PRODUCT.getMessage());
    }

    @Test
    void 잘못된_주문_포맷으로_인한_예외_발생() {
        Product product = new Product("사이다", 1500, 20, null);
        List<Product> products = List.of(product);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "사이다-5";

        assertThatThrownBy(() -> Order.createOrder(orderInput, inventory))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품_주문으로_인한_예외_발생() {
        Product product = new Product("사이다", 1500, 20, null);
        List<Product> products = List.of(product);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "[콜라-5]";


        assertThatThrownBy(() -> Order.createOrder(orderInput, inventory))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorMessage.INVALID_INPUT_PRODUCT_NAME.getMessage());
    }

    @Test
    void 멤버십할인이_적용되지_않은_일반_상품_정상_주문() {
        Product productWithoutPromotion = new Product("물", 500, 20, null);
        List<Product> products = List.of(productWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        String orderInput = "[물-3]";
        Order order = Order.createOrder(orderInput, inventory);

        assertThat(order.getBoughtProducts()).hasSize(1);
        assertThat(order.getBoughtProducts().get(0).getName()).isEqualTo("물");
        assertThat(order.getBoughtProducts().get(0).getQuantity()).isEqualTo(3);
        assertThat(order.getPromotionProducts()).isEmpty();
    }
}