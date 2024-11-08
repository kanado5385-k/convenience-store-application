package store.domain;

import java.util.List;
import java.util.Map;


import store.utilities.Splitter;
import store.utilities.Validator;

public class Order {
    private final Map<String, Integer> promotionProducts;
    private final List<Product> boughtProducts;

    private Order(Map<String, Integer> promotionProducts, List<Product> boughtProducts){
        this.promotionProducts = promotionProducts;
        this.boughtProducts = boughtProducts;
    }

    public static Order createOrder(String order){
        List<String> orderList = Splitter.splitStringOrder(order);
        for(String oneOrder : orderList){
            Validator.validateFormatOfOrder(oneOrder);
            List<String> productAndQuantity = Splitter.splirOneOrder(order);
        }

    }



}
