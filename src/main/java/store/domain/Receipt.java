package store.domain;

import java.util.List;
import java.util.Map;

import store.enums.constants.AnswerConstants;
import store.utilities.Validator;


public class Receipt {
    private static final double MEMBER_SHIP_DISCOUNT_PERCENT = 0.25;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    private static final int NO_DISCOUNT = 0;
    private static final int INITIAL_VALUE = 0;

    private Integer memberShipDiscount;
    private Integer promotionDiscount;
    private Integer generalPrice;
    private Integer totalPrice;

    private Receipt(Integer memberShipDiscount, Integer promotionDiscount,
                    Integer generalPrice, Integer totalPrice) {
        this.memberShipDiscount = memberShipDiscount;
        this.promotionDiscount = promotionDiscount;
        this.generalPrice = generalPrice;
        this.totalPrice = totalPrice;
    }

    public static Receipt createReceipt(Order order, String memberShipAnswer) {
        List<Product> boughtProducts = order.getBoughtProducts();
        Map<String, Integer> promotionProducts = order.getPromotionProducts();
        Integer generalPrice = calculateGeneralPrice(boughtProducts);
        Integer promotionDiscount = calculatePromotionDiscount(boughtProducts, promotionProducts);
        Integer totalPriceBeforeDiscount = generalPrice - promotionDiscount;
        Integer memberShipDiscount = calculateMembershipDiscount(totalPriceBeforeDiscount, memberShipAnswer);
        Integer totalPrice = generalPrice - promotionDiscount - memberShipDiscount;
        return new Receipt(memberShipDiscount, promotionDiscount, generalPrice, totalPrice);
    }

    private static Integer calculateGeneralPrice(List<Product> boughtProducts) {
        Integer generalPrice = INITIAL_VALUE;
        for (Product product : boughtProducts) {
            generalPrice = product.addPriceToTotal(generalPrice);
        }
        return generalPrice;
    }

    private static Integer calculatePromotionDiscount(List<Product> boughtProducts,
                                                      Map<String, Integer> promotionProducts) {
        Integer promotionDiscount = NO_DISCOUNT;
        for (Product product : boughtProducts) {
            promotionDiscount += getProductPromotionDiscount(product, promotionProducts);
        }
        return promotionDiscount;
    }

    private static Integer getProductPromotionDiscount(Product product, Map<String, Integer> promotionProducts) {
        String productName = product.getName();
        if (!promotionProducts.containsKey(productName)) {
            return NO_DISCOUNT;
        }
        int promotionValue = promotionProducts.get(productName);
        return product.getPromotionPrice(promotionValue);
    }

    private static Integer calculateMembershipDiscount(Integer totalPriceBeforeDiscount, String memberShipAnswer) {
        if (!isMembershipApplicable(memberShipAnswer)) {
            return NO_DISCOUNT;
        }
        Integer discount = calculateRawMembershipDiscount(totalPriceBeforeDiscount);
        return capMembershipDiscount(discount);
    }

    private static boolean isMembershipApplicable(String memberShipAnswer) {
        Validator.validateAnswer(memberShipAnswer);
        return memberShipAnswer.equals(AnswerConstants.ANSWER_YES.getConstants());
    }

    private static Integer calculateRawMembershipDiscount(Integer totalPriceBeforeDiscount) {
        return (int) (totalPriceBeforeDiscount * MEMBER_SHIP_DISCOUNT_PERCENT);
    }

    private static Integer capMembershipDiscount(Integer discount) {
        if (discount > MAX_MEMBERSHIP_DISCOUNT) {
            return MAX_MEMBERSHIP_DISCOUNT;
        }
        return discount;
    }

    public Integer getMemberShipDiscount() {
        return this.memberShipDiscount;
    }

    public Integer getPromotionDiscount() {
        return this.promotionDiscount;
    }

    public Integer getGeneralPrice() {
        return this.generalPrice;
    }

    public Integer getTotalPrice() {
        return this.totalPrice;
    }
}