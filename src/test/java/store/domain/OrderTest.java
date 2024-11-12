package store.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import store.domain.inventory.Inventory;
import store.domain.inventory.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPolicy;
import store.enums.messages.ErrorMessage;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @ParameterizedTest
    @MethodSource("provideInvalidOrderCases")
    void 잘못된_주문으로_인한_예외_발생(String orderInput, String expectedErrorMessage, List<Product> products, List<Promotion> promotions) {
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, promotions);
        Inventory inventory = new Inventory(products, promotionPolicy);

        assertThatThrownBy(() -> Order.createOrder(orderInput, inventory))
            .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> provideInvalidOrderCases() {
        return Stream.of(
            Arguments.of("[콜라-5]", ErrorMessage.LACK_OF_PRODUCT.getMessage(), 
                        List.of(new Product("콜라", 1000, 2, "탄산2+1"), 
                                 new Product("콜라", 1000, 1, null)), 
                        List.of(new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01"))),

            Arguments.of("사이다-5", null, 
                        List.of(new Product("사이다", 1500, 20, null)), 
                        List.of()),

            Arguments.of("[콜라-5]", ErrorMessage.INVALID_INPUT_PRODUCT_NAME.getMessage(), 
                        List.of(new Product("사이다", 1500, 20, null)), 
                        List.of())
        );
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