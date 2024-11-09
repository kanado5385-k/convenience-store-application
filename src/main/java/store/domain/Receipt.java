package store.domain;

import java.util.List;
import java.util.Map;

import store.enums.constants.AnswerConstants;
import store.utilities.Validator;


public class Receipt {
    private static final double MEMBER_SHIP_DISCOUNT_PERCENT= 0.3;

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

    public static Receipt createReceipt(Order order, String memberShipAnswer){
        List<Product> boughtProducts = order.getBoughtProducts();
        Map<String, Integer> promotionProducts = order.getPromotionProducts();

        Integer generalPrice = 0; //generalPrice
        for(Product oneBoughtproduct : boughtProducts){
            generalPrice = oneBoughtproduct.addPriceToTotal(generalPrice);
        }

        Integer promotionDiscount = 0; //promotionDiscount
        for(Product oneBoughtproduct : boughtProducts){
            String productName = oneBoughtproduct.getName();
            if(promotionProducts.containsKey(productName)){
                int colaValue = promotionProducts.get(productName);
                Integer onePromotionDiscount = oneBoughtproduct.getPromotionPrice(colaValue);
                promotionDiscount += onePromotionDiscount;

            }

        }

        Integer memberShipDiscount = 0;
        Integer totalPriceBeforDiscount = generalPrice - promotionDiscount;
        Validator.validateAnswer(memberShipAnswer);
        if (memberShipAnswer.equals(AnswerConstants.ANSWER_YES.getConstants())) {
            memberShipDiscount = (int) (totalPriceBeforDiscount * MEMBER_SHIP_DISCOUNT_PERCENT);
        }

        Integer totalPrice = generalPrice - promotionDiscount - memberShipDiscount;
        return new Receipt(memberShipDiscount, promotionDiscount, generalPrice, totalPrice);
    }

    public Integer getMemeberSipDiscount() {
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
