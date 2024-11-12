package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import store.domain.promotion.Promotion;

import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion("탄산2+1", 2, "2024-01-01", "2024-12-31");
    }

    @ParameterizedTest
    @MethodSource("providePromotionNameCases")
    void 프로모션_이름이_같은지_확인(String name, boolean expected) {
        assertThat(promotion.isSamePromotionName(name)).isEqualTo(expected);
    }

    static Stream<Arguments> providePromotionNameCases() {
        return Stream.of(
            Arguments.of("탄산2+1", true),
            Arguments.of("MD추천상품", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateRangeCases")
    void 현재_날짜가_프로모션_기간_내에_있는지_확인(String startDate, String endDate, boolean expected) {
        promotion = new Promotion("탄산2+1", 2, startDate, endDate);
        assertThat(promotion.isBetweenStartAndEndDate()).isEqualTo(expected);
    }
    
    static Stream<Arguments> provideDateRangeCases() {
        return Stream.of(
            Arguments.of("2024-01-01", "2024-12-31", true),
            Arguments.of("2023-01-01", "2023-12-31", false)
        );
    }

    @ParameterizedTest
    @MethodSource("providePromotionBoonCases")
    void 프로모션_혜택_반환_테스트(int boon, int expectedBoon) {
        promotion = new Promotion("탄산2+1", boon, "2024-01-01", "2024-12-31");
        assertThat(promotion.getPromotionBoon()).isEqualTo(expectedBoon);
    }

    static Stream<Arguments> providePromotionBoonCases() {
        return Stream.of(
            Arguments.of(2, 3),
            Arguments.of(4, 5)
        );
    }
}