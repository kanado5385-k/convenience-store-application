package store.view.output;

import store.domain.inventory.Product;
import store.dto.ReceiptDTO;
import store.enums.messages.IOMessage;

import java.util.List;
import java.util.Map;

public class OutputView {
    private static final String RECEIPT_HEADER = "===============W 편의점================";
    private static final String PROMOTION_HEADER = "==============증       정==============";
    private static final String RECEIPT_FOOTER = "=======================================";

    private static final String COLUMN_FORMAT = "%-15s %6s %9s%n";
    private static final String PRODUCT_LINE_FORMAT = "%-15s %3d %14s%n";
    private static final String PROMOTION_LINE_FORMAT = "%-15s %3d%n";
    private static final String TOTAL_PRICE_FORMAT = "%-15s %4d %14s%n";
    private static final String MEMBER_SHIP_DISCOUNT_FORMAT = "%-15s %18s%n";
    private static final String PROMOTION_DISCOUNT_FORMAT = "%-15s %19s%n";
    private static final String FINAL_PRICE_FORMAT = "%-15s %20s%n";
    private static final String NEGATIVE_FORMAT = "-%,d";
    private static final String POSITIVE_FORMAT = "%,d";

    private static final String COLUMN_PRODUCT_NAME = "상품명";
    private static final String COLUMN_QUANTITY = "수량";
    private static final String COLUMN_PRICE = "금액";
    private static final String TOTAL_LABEL = "총구매액";
    private static final String PROMOTION_DISCOUNT_LABEL = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    private static final String FINAL_PAYMENT_LABEL = "내실돈";

    public void printWelcomeMessage() {
        System.out.println(IOMessage.PRINT_WELCOME_MESSAGE.getMessage() + System.lineSeparator());
    }

    public void printProducts(String products) {
        System.out.println(products + System.lineSeparator());
    }

    public void printReceipt(ReceiptDTO receiptDTO) {
        System.out.println(RECEIPT_HEADER);
        printProductList(receiptDTO.getBoughtProducts());
        printPromotionList(receiptDTO.getPromotionProducts());
        printPriceSummary(receiptDTO);
    }

    private void printProductList(List<Product> products) {
        System.out.printf(COLUMN_FORMAT, COLUMN_PRODUCT_NAME, COLUMN_QUANTITY, COLUMN_PRICE);
        for (Product product : products) {
            System.out.printf(PRODUCT_LINE_FORMAT,
                product.getName(), product.getQuantity(), String.format(POSITIVE_FORMAT, product.getPrice()));
        }
        System.out.println(PROMOTION_HEADER);
    }

    private void printPromotionList(Map<String, Integer> promotionProducts) {
        if (promotionProducts.isEmpty()) {
            printNoAnyPromotion();
            return;
        }
        for (Map.Entry<String, Integer> entry : promotionProducts.entrySet()) {
            System.out.printf(PROMOTION_LINE_FORMAT, entry.getKey(), entry.getValue());
        }
        System.out.println(RECEIPT_FOOTER);
    }

    private void printNoAnyPromotion() {
        System.out.println(IOMessage.PRINT_NO_ANY_PROMOTION_PRODUCT.getMessage());
        System.out.println(RECEIPT_FOOTER);
    }

    private void printPriceSummary(ReceiptDTO receiptDTO) {
        int totalQuantity = receiptDTO.getBoughtProducts().stream().mapToInt(Product::getQuantity).sum();
        System.out.printf(TOTAL_PRICE_FORMAT, TOTAL_LABEL, totalQuantity, String.format(POSITIVE_FORMAT, receiptDTO.getGeneralPrice()));
        System.out.printf(PROMOTION_DISCOUNT_FORMAT, PROMOTION_DISCOUNT_LABEL, formatNegative(receiptDTO.getPromotionDiscount()));
        System.out.printf(MEMBER_SHIP_DISCOUNT_FORMAT, MEMBERSHIP_DISCOUNT_LABEL, formatNegative(receiptDTO.getMemberShipDiscount()));
        System.out.printf(FINAL_PRICE_FORMAT, FINAL_PAYMENT_LABEL, formatPositive(receiptDTO.getTotalPrice()));
        System.out.println(System.lineSeparator());
    }

    private String formatNegative(int amount) {
        return String.format(NEGATIVE_FORMAT, amount);
    }

    private String formatPositive(int amount) {
        return String.format(POSITIVE_FORMAT, amount);
    }
}




