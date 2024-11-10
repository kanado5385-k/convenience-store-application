package store.domain;

import org.junit.jupiter.api.Test;

import store.domain.inventory.Inventory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    void 일반_상품_두개_주문_생성_테스트() {
        Product productWithoutPromotion = new Product("사이다", 1500, 20, "null");
        Product productWithPromotion = new Product("콜라", 1000, 10, "null");
        Inventory inventory = new Inventory(List.of(productWithoutPromotion,productWithPromotion), List.of());
    
        String orderInput = "[콜라-3],[사이다-5]";
    
        Order order = Order.createOrder(orderInput, inventory);
    
        assertThat(order.getPromotionProducts()).isEmpty();
        assertThat(order.getBoughtProducts()).hasSize(2);
        assertThat(order.getBoughtProducts().get(0).getName()).isEqualTo("콜라");
        assertThat(order.getBoughtProducts().get(1).getName()).isEqualTo("사이다");
        assertThat(order.getBoughtProducts().get(0).getQuantity()).isEqualTo(3);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(15);
    }

    @Test
    void 일반_상품_하나_주문_생성_테스트() {
        Product productWithoutPromotion = new Product("사이다", 1500, 20, "null");
        Inventory inventory = new Inventory(List.of(productWithoutPromotion), List.of());

        String orderInput = "[사이다-5]";

        Order order = Order.createOrder(orderInput, inventory);

        assertThat(order.getPromotionProducts()).isEmpty();
        assertThat(order.getBoughtProducts()).hasSize(1);
        assertThat(order.getBoughtProducts().get(0).getName()).isEqualTo("사이다");
        assertThat(order.getBoughtProducts().get(0).getQuantity()).isEqualTo(5);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(15);
    }



    
}
