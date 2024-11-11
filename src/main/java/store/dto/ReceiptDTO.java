package store.dto;

import java.util.List;
import java.util.Map;

import store.domain.inventory.Product;

public class ReceiptDTO {
    private final Map<String, Integer> promotionProducts; 
    private final List<Product> boughtProducts;           
    private final Integer memberShipDiscount;             
    private final Integer promotionDiscount;              
    private final Integer generalPrice;                  
    private final Integer totalPrice;                     

    public ReceiptDTO(Map<String, Integer> promotionProducts, List<Product> boughtProducts,
        Integer memberShipDiscount, Integer promotionDiscount,
        Integer generalPrice, Integer totalPrice) {
        this.promotionProducts = promotionProducts;
        this.boughtProducts = boughtProducts;
        this.memberShipDiscount = memberShipDiscount;
        this.promotionDiscount = promotionDiscount;
        this.generalPrice = generalPrice;
        this.totalPrice = totalPrice;
    }

    public Map<String, Integer> getPromotionProducts() {
        return promotionProducts;
    }

    public List<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public Integer getMemberShipDiscount() {
        return memberShipDiscount;
    }

    public Integer getPromotionDiscount() {
        return promotionDiscount;
    }

    public Integer getGeneralPrice() {
        return generalPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

}