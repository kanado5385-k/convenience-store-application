package store.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PromotionFileReaderTest {
    private PromotionFileReader promotionFileReader;

    @Test
    void readFileAsString_파일내용읽기() {
        promotionFileReader = new PromotionFileReader();
        
        String content = promotionFileReader.readFileAsString();
        assertThat(content).contains("탄산2+1,2,1,2024-01-01,2024-12-31");
        assertThat(content).contains("MD추천상품,1,1,2024-01-01,2024-12-31");
    }
}
