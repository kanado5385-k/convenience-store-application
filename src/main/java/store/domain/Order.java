package store.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import store.utilities.Parser;
import store.utilities.Splitter;
import store.utilities.Validator;

public class Order {
    private static final int INDEX_OF_BENEFIT= 0;
    private static final int INDEX_OF_REJECTED_PRODUCT = 1;
    private static final int INDEX_OF_PRODUCT_NAME = 0;
    private static final int INDEX_OF_PRODUCT_QUANTITY = 1;
    private static final int MORE_THAN_ONE_REJECTED_PRODUCT = 0;
    private static final int NO_SAME_NAME_IN_MAP = 0;
    private static final String DOES_NOT_MATTER = "null";



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

            String productName = productAndQuantity.get(INDEX_OF_PRODUCT_NAME);
            int purchaseQuantity = Parser.parseNumberToInt(productAndQuantity.get(INDEX_OF_PRODUCT_QUANTITY));
            Validator.validateQuantityNumber(purchaseQuantity);

            if (inventory.isProductWithPromotion(productName)) {
                List<Integer> resultOfBuyingPromotionProduct = inventory.buyPromotionProduct(productName, purchaseQuantity);
                int promotionalBenefits = resultOfBuyingPromotionProduct.get(INDEX_OF_BENEFIT);
                if(resultOfBuyingPromotionProduct.get(INDEX_OF_REJECTED_PRODUCT) > MORE_THAN_ONE_REJECTED_PRODUCT){
                    purchaseQuantity -= resultOfBuyingPromotionProduct.get(INDEX_OF_REJECTED_PRODUCT);
                }
                
                promotionProduct.put(productName, 
                    promotionProduct.getOrDefault(productName, NO_SAME_NAME_IN_MAP) + promotionalBenefits);
            }
            if (!inventory.isProductWithPromotion(productName)){
                inventory.buyGeneralProduct(productName, purchaseQuantity);
            }

            int priceOfOneProductPacket = inventory.getPriceOfProductPacket(productName,purchaseQuantity);
            Product boughtProduct = new Product(productName, priceOfOneProductPacket, purchaseQuantity, DOES_NOT_MATTER);
            boughtProducts.add(boughtProduct);

        }

        return new Order(promotionProduct, boughtProducts);
    }

    public Map<String, Integer> getPromotionProducts(){
        return Collections.unmodifiableMap(this.promotionProducts);
    }

    public List<Product> getBoughtProducts(){
        return Collections.unmodifiableList(this.boughtProducts);
    }

}
