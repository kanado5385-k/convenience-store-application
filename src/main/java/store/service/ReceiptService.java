package store.service;

import store.dto.ReceiptDTO;
import store.domain.Order;
import store.domain.Receipt;

public class ReceiptService {
    
    public ReceiptDTO createReseipt(Order order, Receipt receipt) {
        return new ReceiptDTO(
            order.getPromotionProducts(), 
            order.getBoughtProducts(), 
            receipt.getMemberShipDiscount(), 
            receipt.getPromotionDiscount(), 
            receipt.getGeneralPrice(), 
            receipt.getTotalPrice()
        );
    }
}
