package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductFileReaderTest {

    private ProductFileReader productFileReader;

    @BeforeEach
    void setUp() {
        productFileReader = new ProductFileReader();
    }

    @Test
    void readFileAsString_파일내용읽기() {
        String content = productFileReader.readFileAsString();
        assertThat(content).contains("콜라,1000,10,탄산2+1");
        assertThat(content).contains("컵라면,1700,10,null");
    }

    @Test
    void showProductsToUser_상품출력형식변환() {
        String content = productFileReader.showProductsToUser();
        List<String> expectedLines = List.of(
            "- 콜라 1,000원 10개 탄산2+1",
            "- 콜라 1,000원 10개",
            "- 사이다 1,000원 8개 탄산2+1",
            "- 사이다 1,000원 7개",
            "- 오렌지주스 1,800원 9개 MD추천상품",
            "- 오렌지주스 1,800원 재고 없음",
            "- 탄산수 1,200원 5개 탄산2+1",
            "- 탄산수 1,200원 재고 없음",
            "- 물 500원 7개",
            "- 비타민워터 1,500원 3개",
            "- 감자칩 1,500원 5개 반짝할인",
            "- 감자칩 1,500원 5개",
            "- 초코바 1,200원 5개 MD추천상품",
            "- 초코바 1,200원 5개",
            "- 에너지바 2,000원 5개",
            "- 정식도시락 6,400원 6개",
            "- 컵라면 1,700원 1개 MD추천상품",
            "- 컵라면 1,700원 10개"
        );
        expectedLines.forEach(line -> assertThat(content).contains(line));
        assertThat(content).doesNotContain("null");
    }
}