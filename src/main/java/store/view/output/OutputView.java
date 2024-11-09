package store.view.output;

import store.dto.ReseiptDTO;
import store.domain.Product;

import java.util.List;
import java.util.Map;

public class OutputView {

    public void printReseipt(ReseiptDTO reseiptDTO) {
        System.out.println("==============W 편의점================");
        printProductList(reseiptDTO.getBoughtProducts());
        printPromotionList(reseiptDTO.getPromotionProducts());
        printPriceSummary(reseiptDTO);
    }

    private void printProductList(List<Product> products) {
        System.out.printf("%-40s %10s %15s%n", "상품명", "수량", "금액");
        for (Product product : products) {
            System.out.printf("%-40s %10d %,15d%n",
                product.getName(), product.getQuantity(), product.getPrice());
        }
        System.out.println("==============증\t정==================");
    }

    private void printPromotionList(Map<String, Integer> promotionProducts) {
        for (Map.Entry<String, Integer> entry : promotionProducts.entrySet()) {
            System.out.printf("%-40s %10d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("====================================");
    }

    private void printPriceSummary(ReseiptDTO reseiptDTO) {
        System.out.printf("%-40s %10d %,15d%n", "총구매액", reseiptDTO.getBoughtProducts().size(), reseiptDTO.getGeneralPrice());
        System.out.printf("%-40s %27s%n", "행사할인", formatNegative(reseiptDTO.getPromotionDiscount()));
        System.out.printf("%-38s %27s%n", "멤버십할인", formatNegative(reseiptDTO.getMemberShipDiscount()));
        System.out.printf("%-42s %27s%n", "내실돈", formatPositive(reseiptDTO.getTotalPrice()));
    }

    private String formatNegative(int amount) {
        return String.format("-%,d", amount);
    }

    private String formatPositive(int amount) {
        return String.format("%,d", amount);
    }
}