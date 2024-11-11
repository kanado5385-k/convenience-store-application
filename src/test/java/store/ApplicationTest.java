package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import store.model.ProductFileReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    private ProductFileReader productFileReader;

    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 오렌지주스 1,800원 재고 없음",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 탄산수 1,200원 재고 없음",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 일반_상품_단일_구매() {
        assertSimpleTest(() -> {
            run("[물-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈1,000");
        });
    }

    @Test
    void 혜택수량_보다_적은_프로모션_상품_구매() {
        assertSimpleTest(() -> {
            run("[콜라-1]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈1,000");
        });
    }

    @Test
    void 혜택수량_보다_하나_덜_가져올_경우_Y() {
        assertSimpleTest(() -> {
            run("[콜라-2]", "Y", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-1,000");
        });
    }
/*   
    @Test
    void 혜택수량_보다_더_가져오고_재고_상태_충분_경우() {
        assertSimpleTest(() -> {
            run("[콜라-6]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-2,000");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈4,000");
        });
    }
 */ 

    @Test
    void 혜택수량_보다_더_가져오고_재고_상태_충분_경우_Y() {
        assertSimpleTest(() -> {
            run("[콜라-10]", "Y", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-2,000");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈8,000");
        });
    }

    @Test
    void 혜택수량_보다_더_가져오고_재고_상태와_수량_같을_경우_N() {
        assertSimpleTest(() -> {
            run("[사이다-8]", "N", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-2,000");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈4,000");
        });
    }
    
    @BeforeEach
    void setUp() {
        productFileReader = new ProductFileReader();
    }

    @Test
    void readFileAsString_파일내용읽기() {
        String content = productFileReader.readFileAsString();
        assertThat(content).contains("콜라,1000,0,탄산2+1");
        assertThat(content).contains("콜라,1000,7,null");
        assertThat(content).contains("사이다,1000,2,탄산2+1");
        assertThat(content).contains("사이다,1000,7,null");
        assertThat(content).contains("물,500,5,null");
    }


    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
