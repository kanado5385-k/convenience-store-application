package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class ProductFileHandlerTest {

    private ProductFileHandler productFileHandler;


    @BeforeEach
    void setUp(){    
        productFileHandler = new ProductFileHandler();
    }

    @Test
    void readFileAsString_파일내용읽기() {
        String content = productFileHandler.readFileAsString();
        assertThat(content).contains("콜라,1000,10,탄산2+1");
        assertThat(content).contains("컵라면,1700,10,null");
    }

    @Test
    void showProductsToUser_상품출력형식변환() {
        String content = productFileHandler.showProductsToUser();
        assertThat(content).contains("콜라,1000,10,탄산2+1");
        assertThat(content).contains("콜라,1000,10,");
        assertThat(content).contains("오렌지주스,1800,9,MD추천상품");
        assertThat(content).contains("탄산수,1200,5,탄산2+1");
        assertThat(content).contains("초코바,1200,5,MD추천상품");
        assertThat(content).contains("컵라면,1700,10,");
        assertThat(content).contains("컵라면,1700,1,MD추천상품");
        assertThat(content).contains("감자칩,1500,5,반짝할인");
        assertThat(content).doesNotContain("null");
    }
}