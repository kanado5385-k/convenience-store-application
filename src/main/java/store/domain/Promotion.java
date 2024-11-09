package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import camp.nextstep.edu.missionutils.DateTimes;
import store.utilities.Parser;


public class Promotion {
    private static final int GET_ONE_FREE = 1;

    private final String nameOfPromotion;
    private final Integer promotionBoon;
    private final String startDate;
    private final String endDate;
    
    public Promotion(String nameOfPromotion, Integer promotionBoon, String startDate, String endDate) {
        this.nameOfPromotion = nameOfPromotion;
        this.promotionBoon = promotionBoon;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isSamePromotionName(String nameOfPromotion) {
        return this.nameOfPromotion.equals(nameOfPromotion);
    }

    public boolean isBetweenStartAndEndDate() {
        LocalDateTime now = DateTimes.now();
        LocalDate todayDate = now.toLocalDate();
        LocalDate start = Parser.parseToDateFormat(startDate);
        LocalDate end = Parser.parseToDateFormat(endDate);

        return (todayDate.isEqual(start) || todayDate.isAfter(start)) && 
               (todayDate.isEqual(end) || todayDate.isBefore(end));
    }

    public Integer getPromotionBoon() {
        return this.promotionBoon + GET_ONE_FREE;
    }

    public int gapBetweenQuantityAndBoon(int purchaseQuantity) {
        return promotionBoon - purchaseQuantity;
    }
}
