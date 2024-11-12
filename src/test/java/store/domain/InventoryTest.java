package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import store.domain.inventory.Inventory;
import store.domain.inventory.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPolicy;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryTest {
    private PromotionPolicy promotionPolicy;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        Product productWithPromotion = new Product("콜라", 1000, 10, "탄산2+1");
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");
        promotionPolicy = new PromotionPolicy(List.of(productWithPromotion), List.of(promotion));
        inventory = new Inventory(List.of(productWithPromotion), promotionPolicy);
    }

    @ParameterizedTest
    @MethodSource("providePromotionPurchaseCases")
    void 프로모션_상품_구매_테스트(String productName, int purchaseQuantity, int expectedBenefit, int remainingQuantity) {
        Map<String, Integer> result = inventory.buyPromotionProduct(productName, purchaseQuantity);

        assertThat(result.get("benefit")).isEqualTo(expectedBenefit);
    }

    static Stream<Arguments> providePromotionPurchaseCases() {
        return Stream.of(
            Arguments.of("콜라", 3, 1, 7),
            Arguments.of("콜라", 1, 0, 9)
        );
    }

    @Test
    void 프로모션_상품_구매_재고_부족_일반상품에서_재고차감() {
        Product productWithPromotion = new Product("콜라", 1000, 2, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 10, null);
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        List<Product> products = List.of(productWithPromotion, productWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of(promotion));
        Inventory inventory = new Inventory(products, promotionPolicy);

        Map<String, Integer> result = inventory.buyPromotionProduct("콜라", 5);

        assertThat(result.get("benefit")).isEqualTo(0);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(0);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(7);
    }

    @Test
    void 프로모션_상품_및_일반_상품_재고_부족으로_예외() {
        Product productWithPromotion = new Product("콜라", 1000, 2, "탄산2+1");
        Product productWithoutPromotion = new Product("콜라", 1000, 1, null);
        Promotion promotion = new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01");

        List<Product> products = List.of(productWithPromotion, productWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, List.of(promotion));
        Inventory inventory = new Inventory(products, promotionPolicy);

        assertThatThrownBy(() -> inventory.buyPromotionProduct("콜라", 5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 프로모션_상품_구매_손님이_덜_가져올_경우_사용자_N() {
        String simulatedInput = "N\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Map<String, Integer> result = inventory.buyPromotionProduct("콜라", 2);

        assertThat(result.get("benefit")).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("provideSufficientPromotionInventoryCases")
    void 프로모션_상품_수량이_혜택수량_보다_많고_재고_상태가_충분할_때(String productName, int purchaseQuantity, int expectedBenefit,
        int remainingWithPromo, int remainingWithoutPromo) {
        Product productWithPromotion = new Product(productName, 1000, 20, "탄산2+1");
        Product productWithoutPromotion = new Product(productName, 1000, 10, null);

        List<Product> products = List.of(productWithPromotion, productWithoutPromotion);
        PromotionPolicy promotionPolicy = new PromotionPolicy(products, 
            List.of(new Promotion("탄산2+1", 2, "2023-01-01", "2025-01-01")));
        Inventory inventory = new Inventory(products, promotionPolicy);

        Map<String, Integer> result = inventory.buyPromotionProduct(productName, purchaseQuantity);

        assertThat(result.get("benefit")).isEqualTo(expectedBenefit);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(remainingWithPromo);
        assertThat(productWithoutPromotion.getQuantity()).isEqualTo(remainingWithoutPromo);
    }

    static Stream<Arguments> provideSufficientPromotionInventoryCases() {
        return Stream.of(
            Arguments.of("콜라", 10, 3, 8, 10)
        );
    }
}