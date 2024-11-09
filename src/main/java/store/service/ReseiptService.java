package store.service;

import store.dto.ReseiptDTO;
import store.domain.Order;
import store.domain.Receipt;

public class ReseiptService {
    
    public ReseiptDTO createReseipt(Order order, Receipt receipt) {
        return new ReseiptDTO(
            order.getPromotionProducts(), 
            order.getBoughtProducts(), 
            receipt.getMemeberSipDiscount(), 
            receipt.getPromotionDiscount(), 
            receipt.getGeneralPrice(), 
            receipt.getTotalPrice()
        );
    }
}
