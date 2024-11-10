package store.domain.promotion;

import store.utilities.Validator;
import store.view.input.InputViewOfPromotionIssue;

public class UserInteractionHandler {
    public String getValidatedAnswerForOneMoreProduct(String productName) {
        while (true) {
            try {
                String answer = InputViewOfPromotionIssue.readAnswerToOneMoreProduct(productName);
                Validator.validateAnswer(answer);
                return answer;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getValidatedAnswerToLackOfQuantity(String productName, int currentPurchaseQuantity) {
        while (true) {
            try {
                String answer = InputViewOfPromotionIssue.readAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
                Validator.validateAnswer(answer);
                return answer;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
