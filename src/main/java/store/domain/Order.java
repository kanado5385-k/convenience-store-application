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


        List<String> orderList = Splitter.splitStringOrder(order);
        for (String oneOrder : orderList) {
            Validator.validateFormatOfOrder(oneOrder);
            List<String> productAndQuantity = Splitter.splirOneOrder(order);

            String productName = productAndQuantity.get(0);
            int purchaseQuantity = Parser.parseNumberToInt(productAndQuantity.get(1));
            Validator.validateQuantityNumber(purchaseQuantity);

            if (inventory.isProductWithPromotion(productName)) {
                int promotionalBenefits = inventory.buyPromotionProduct(productName, purchaseQuantity);
            
                promotionProduct.put(productName, 
                    promotionProduct.getOrDefault(productName, 0) + promotionalBenefits);
            }
            if (!inventory.isProductWithPromotion(productName)){
                inventory.buyGeneralProduct(productName, purchaseQuantity);
            }

            int priceOfOneProductPacket = inventory.getPriceOfProductPacket(productName,purchaseQuantity);
            Product boughtProduct = new Product(productName, priceOfOneProductPacket, purchaseQuantity, "null");
            boughtProducts.add(boughtProduct);

        }

        return new Order(promotionProduct, boughtProducts);
    }

}
