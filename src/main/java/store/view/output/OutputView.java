package store.view.output;

import store.dto.ReseiptDTO;
import store.enums.messages.IOMessage;
import store.domain.Product;

import java.util.List;
import java.util.Map;

public class OutputView {
    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String PROMOTION_HEADER = "==============증\t정==================";
    private static final String RECEIPT_FOOTER = "====================================";

    private static final String COLUMN_FORMAT = "%-40s %10s %15s%n";
    private static final String PRODUCT_LINE_FORMAT = "%-40s %10d %,15d%n";
    private static final String PROMOTION_LINE_FORMAT = "%-40s %10d%n";
    private static final String TOTAL_PRICE_FORMAT = "%-40s %10d %,15d%n";
    private static final String PROMOTION_DISCOUNT_FORMAT = "%-40s %27s%n";
    private static final String MEMBER_SHIP_DISCOUNT_FORMAT = "%-38s %27s%n";
    private static final String FINAL_PRICE_FORMAT = "%-42s %27s%n";
    private static final String NEGATIVE_FORMAT = "-%,d";
    private static final String POSITIVE_FORMAT = "%,d";

    private static final String COLUMN_PRODUCT_NAME = "상품명";
    private static final String COLUMN_QUANTITY = "수량";
    private static final String COLUMN_PRICE = "금액";
    private static final String TOTAL_LABEL = "총구매액";
    private static final String PROMOTION_DISCOUNT_LABEL = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_LABEL = "멤버십할인";
    private static final String FINAL_PAYMENT_LABEL = "내실돈";

    public void printWellcomeMessage() {
        System.out.println(IOMessage.PRINT_WELLCOME_MESSAGE.getMessage());
    }

    public void printProdocts(String products) {
        System.out.println(products);
    }

    public void printReseipt(ReseiptDTO reseiptDTO) {
        System.out.println(RECEIPT_HEADER);
        printProductList(reseiptDTO.getBoughtProducts());
        printPromotionList(reseiptDTO.getPromotionProducts());
        printPriceSummary(reseiptDTO);
    }

    private void printProductList(List<Product> products) {
        System.out.printf(COLUMN_FORMAT, COLUMN_PRODUCT_NAME, COLUMN_QUANTITY, COLUMN_PRICE);
        for (Product product : products) {
            System.out.printf(PRODUCT_LINE_FORMAT,
                product.getName(), product.getQuantity(), product.getPrice());
        }
        System.out.println(PROMOTION_HEADER);
    }

    private void printPromotionList(Map<String, Integer> promotionProducts) {
        for (Map.Entry<String, Integer> entry : promotionProducts.entrySet()) {
            System.out.printf(PROMOTION_LINE_FORMAT, entry.getKey(), entry.getValue());
        }
        System.out.println(RECEIPT_FOOTER);
    }

    private void printPriceSummary(ReseiptDTO reseiptDTO) {
        System.out.printf(TOTAL_PRICE_FORMAT, TOTAL_LABEL, reseiptDTO.getBoughtProducts().size(), reseiptDTO.getGeneralPrice());
        System.out.printf(PROMOTION_DISCOUNT_FORMAT, PROMOTION_DISCOUNT_LABEL, formatNegative(reseiptDTO.getPromotionDiscount()));
        System.out.printf(MEMBER_SHIP_DISCOUNT_FORMAT, MEMBERSHIP_DISCOUNT_LABEL, formatNegative(reseiptDTO.getMemberShipDiscount()));
        System.out.printf(FINAL_PRICE_FORMAT, FINAL_PAYMENT_LABEL, formatPositive(reseiptDTO.getTotalPrice()));
    }

    private String formatNegative(int amount) {
        return String.format(NEGATIVE_FORMAT, amount);
    }

    private String formatPositive(int amount) {
        return String.format(POSITIVE_FORMAT, amount);
    }
}