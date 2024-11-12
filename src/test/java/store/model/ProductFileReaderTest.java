package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThat(content).contains("컵라면,1700,10,null");
        assertThat(content).contains("오렌지주스,1800,9,MD추천상품");
    }
}