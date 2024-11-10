package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.domain.inventory.Inventory;
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
    private static final String DOES_NOT_MATTER = null;

    private final Map<String, Integer> promotionProducts;
    private final List<Product> boughtProducts;

    private Order(Map<String, Integer> promotionProducts, List<Product> boughtProducts) {
        this.promotionProducts = promotionProducts;
        this.boughtProducts = boughtProducts;
    }

    public static Order createOrder(String order, Inventory inventory) {
        Map<String, Integer> promotionProduct = new HashMap<>();
        List<Product> boughtProducts = new ArrayList<>();
        List<String> orderList = Splitter.splitStringOrder(order);
        validateFormatOfOrder(orderList);
        for (String oneOrder : orderList) {
            processSingleOrder(oneOrder, inventory, promotionProduct, boughtProducts);
        }
        return new Order(promotionProduct, boughtProducts);
    }

    private static void validateFormatOfOrder(List<String> orderList) {
        for (String oneOrder : orderList) {
            Validator.validateFormatOfOrder(oneOrder);
        }
    }

    private static void processSingleOrder(String oneOrder, Inventory inventory, 
                                       Map<String, Integer> promotionProduct, 
                                       List<Product> boughtProducts) {
        List<String> productAndQuantity = Splitter.splitOneOrder(oneOrder);
        String productName = productAndQuantity.get(INDEX_OF_PRODUCT_NAME);
        int purchaseQuantity = parseAndValidateQuantity(productAndQuantity.get(INDEX_OF_PRODUCT_QUANTITY));
        processProductBasedOnPromotion(productName, purchaseQuantity, inventory, promotionProduct);
        addBoughtProduct(boughtProducts, inventory, productName, purchaseQuantity);
    }

    private static void processProductBasedOnPromotion(String productName, int purchaseQuantity, 
                                                   Inventory inventory, Map<String, Integer> promotionProduct) {
        if (inventory.isProductWithPromotion(productName)) {
            processPromotionProduct(productName, purchaseQuantity, inventory, promotionProduct);
            return;
        }
        inventory.buyGeneralProduct(productName, purchaseQuantity);
    }

    private static int parseAndValidateQuantity(String quantity) {
        int purchaseQuantity = Parser.parseNumberToInt(quantity);
        Validator.validateQuantityNumber(purchaseQuantity);
        return purchaseQuantity;
    }

    private static void processPromotionProduct(String productName, int purchaseQuantity, 
                                                Inventory inventory, Map<String, Integer> promotionProduct) {
        List<Integer> result = inventory.buyPromotionProduct(productName, purchaseQuantity);
        int promotionalBenefits = result.get(INDEX_OF_BENEFIT);
        int rejectedQuantity = result.get(INDEX_OF_REJECTED_PRODUCT);

        purchaseQuantity = adjustPurchaseQuantity(purchaseQuantity, rejectedQuantity);
        updatePromotionProductBenefits(productName, promotionalBenefits, promotionProduct);
    }

    private static int adjustPurchaseQuantity(int purchaseQuantity, int rejectedQuantity) {
        if (rejectedQuantity > MORE_THAN_ONE_REJECTED_PRODUCT) {
            return purchaseQuantity - rejectedQuantity;
        }
        return purchaseQuantity;
    }

    private static void updatePromotionProductBenefits(String productName, int promotionalBenefits, 
                                                    Map<String, Integer> promotionProduct) {
        promotionProduct.put(productName, 
            promotionProduct.getOrDefault(productName, NO_SAME_NAME_IN_MAP) + promotionalBenefits);
    }

    private static void addBoughtProduct(List<Product> boughtProducts, Inventory inventory, 
                                         String productName, int purchaseQuantity) {
        int priceOfOneProductPacket = inventory.getPriceOfProductPacket(productName, purchaseQuantity);
        Product boughtProduct = new Product(productName, priceOfOneProductPacket, purchaseQuantity, DOES_NOT_MATTER);
        boughtProducts.add(boughtProduct);
    }

    public Map<String, Integer> getPromotionProducts() {
        return Collections.unmodifiableMap(this.promotionProducts);
    }

    public List<Product> getBoughtProducts() {
        return Collections.unmodifiableList(this.boughtProducts);
    }
}