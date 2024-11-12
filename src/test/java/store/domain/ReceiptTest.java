package store.domain;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import store.domain.inventory.Inventory;
import store.domain.inventory.Product;
import store.domain.promotion.PromotionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

class ReceiptTest {

    @ParameterizedTest
    @MethodSource("provideReceiptTestCases")
    void 영수증_생성_테스트(String orderInput, String membership, int expectedPromotionDiscount, int expectedMembershipDiscount, int expectedGeneralPrice, int expectedTotalPrice) {
        Product cocaWithoutPromotion = new Product("콜라", 1000, 5, null);
        Product spriteWithoutPromotion = new Product("사이다", 1500, 2, null);

        List<Product> products = List.of(cocaWithoutPromotion, spriteWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of());
        Inventory inventory = new Inventory(products, promotionPolicy);

        Order order = Order.createOrder(orderInput, inventory);
        Receipt receipt = Receipt.createReceipt(order, membership);

        assertThat(receipt.getPromotionDiscount()).isEqualTo(expectedPromotionDiscount);
        assertThat(receipt.getMemberShipDiscount()).isEqualTo(expectedMembershipDiscount);
        assertThat(receipt.getGeneralPrice()).isEqualTo(expectedGeneralPrice);
        assertThat(receipt.getTotalPrice()).isEqualTo(expectedTotalPrice);
    }

    static Stream<Arguments> provideReceiptTestCases() {
        return Stream.of(
            Arguments.of("[콜라-3],[사이다-2]", "Y", 0, 1000, 6000, 5000),
            Arguments.of("[콜라-3],[사이다-2]", "N", 0, 0, 6000, 6000)
        );
    }
}