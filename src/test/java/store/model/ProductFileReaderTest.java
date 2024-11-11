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
        assertThat(content).contains("콜라,1000,10,탄산2+1");
        assertThat(content).contains("컵라면,1700,10,null");
    }
}