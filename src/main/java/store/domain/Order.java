package store.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import store.utilities.Parser;
import store.utilities.Splitter;
import store.utilities.Validator;

public class Order {
    private final Map<String, Integer> promotionProducts;
    private final List<Product> boughtProducts;

    private Order(Map<String, Integer> promotionProducts, List<Product> boughtProducts){
        this.promotionProducts = promotionProducts;
        this.boughtProducts = boughtProducts;
    }

    public static Order createOrder(String order, Inventory inventory) {
        Map<String, Integer> promotionProduct = new HashMap<>();
        List<Product> boughtProducts = new LinkedList<>();

        List<String> forCheckDublicatProduct = new ArrayList<>();

        List<String> orderList = Splitter.splitStringOrder(order);
        for(String oneOrder : orderList) {
            Validator.validateFormatOfOrder(oneOrder);
            List<String> productAndQuantity = Splitter.splirOneOrder(order);

            String product = productAndQuantity.get(0);
            int quantity = Parser.parseNumberToInt(productAndQuantity.get(1));
            Validator.validateQuantityNumber(quantity);

            if(forCheckDublicatProduct.contains(product)) {
                for(Product product1 : boughtProducts){
                    if(product1.isSameName(product)) {
                        product1.addQuantity(quantity);
                    }
                }
            }

            if(inventory.isProductWithPromotion(product)){
                int quantityOfPromotionProduct = inventory.checkQuantityOfPromotionProduct(product, quantity);
                int gapBetweenQuantityAndBoon = inventory.checkGapBetweenQuantityAndBoon(product, quantity);
                if(quantityOfPromotionProduct < 0){ // n만큼 정가로 해도 되는지 물어보기
                    return;
                }
                if(gapBetweenQuantityAndBoon > 0) 
            }


        }

    }



}
