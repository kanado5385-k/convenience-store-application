package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    private Promotion promotion;
    private Promotion createPromotion(String name, int boon, String startDate, String endDate) {
        return new Promotion(name, boon, startDate, endDate);
    }

    private void assertPromotionName(String name, boolean expected) {
        assertThat(promotion.isSamePromotionName(name)).isEqualTo(expected);
    }

    @BeforeEach
    void setUp() {
        promotion = createPromotion("탄산2+1", 2, "2024-01-01", "2024-12-31");
    }

    @Test
    void 프로모션_이름이_같은지_확인() {
        assertPromotionName("탄산2+1", true);
        assertPromotionName("MD추천상품", false);
    }

    @Test
    void 현재_날짜가_프로모션_기간_내에_있는지_확인() {
        assertThat(promotion.isBetweenStartAndEndDate()).isTrue();
    }

    @Test
    void 현재_날짜가_프로모션_기간_외에_있는지_확인() {
        promotion = createPromotion("탄산2+1", 2, "2023-01-01", "2023-12-31");
        assertThat(promotion.isBetweenStartAndEndDate()).isFalse();
    }

    @Test
    void 프로모션_혜택_반환_테스트() {
        assertThat(promotion.getPromotionBoon()).isEqualTo(2);
    }
}