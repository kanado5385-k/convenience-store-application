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
    private static final int INDEX_OF_PRODUCT_NAME = 0;
    private static final int INDEX_OF_PRODUCT_QUANTITY = 1;
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
        int changedQuantity = 0;
        changedQuantity = processProductBasedOnPromotion(productName, purchaseQuantity, inventory, promotionProduct);
        if (changedQuantity > 0) {
            purchaseQuantity = changedQuantity;
        }
        addBoughtProduct(boughtProducts, inventory, productName, purchaseQuantity);
    }

    private static int processProductBasedOnPromotion(String productName, int purchaseQuantity, 
                                               Inventory inventory, Map<String, Integer> promotionProduct) {
        if (inventory.isProductWithPromotion(productName)) {
            return processPromotionProduct(productName, purchaseQuantity, inventory, promotionProduct);
        }
        inventory.buyGeneralProduct(productName, purchaseQuantity);
        return 0;
    }

    private static int parseAndValidateQuantity(String quantity) {
        int purchaseQuantity = Parser.parseNumberToInt(quantity);
        Validator.validateQuantityNumber(purchaseQuantity);
        return purchaseQuantity;
    }

    private static int processPromotionProduct(String productName, int purchaseQuantity, 
                                            Inventory inventory, Map<String, Integer> promotionProduct) {
        Map<String, Integer> result = inventory.buyPromotionProduct(productName, purchaseQuantity);
        int promotionalBenefits = result.get("benefit");
        int rejectedQuantity = result.get("adjusted");
        int addedQuantity = result.get("added");
        purchaseQuantity = adjustPurchaseQuantity(purchaseQuantity, rejectedQuantity, addedQuantity);
        updatePromotionProductBenefits(productName, promotionalBenefits, promotionProduct);
        return purchaseQuantity;
    }

    private static int adjustPurchaseQuantity(int purchaseQuantity, int rejectedQuantity, int addedQuantity) {
            return purchaseQuantity - rejectedQuantity + addedQuantity;
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