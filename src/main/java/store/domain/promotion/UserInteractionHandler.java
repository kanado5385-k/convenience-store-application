package store.domain.promotion;

import store.utilities.Validator;
import store.view.input.InputViewOfPromotionIssue;
import store.view.output.ExceptionOutputView;

public class UserInteractionHandler {

    private String readAndValidateAnswerForOneMoreProduct(String productName) {
        String answer = InputViewOfPromotionIssue.readAnswerToOneMoreProduct(productName);
        Validator.validateAnswer(answer);
        return answer;
    }
    
    public String getValidatedAnswerForOneMoreProduct(String productName) {
        while (true) {
            try {
                return readAndValidateAnswerForOneMoreProduct(productName);
            } catch (IllegalArgumentException e) {
                ExceptionOutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private String readAndValidateAnswerToLackOfQuantity(String productName, int currentPurchaseQuantity) {
        String answer = InputViewOfPromotionIssue.readAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
        Validator.validateAnswer(answer);
        return answer;
    }
    
    public String getValidatedAnswerToLackOfQuantity(String productName, int currentPurchaseQuantity) {
        while (true) {
            try {
                return readAndValidateAnswerToLackOfQuantity(productName, currentPurchaseQuantity);
            } catch (IllegalArgumentException e) {
                ExceptionOutputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
