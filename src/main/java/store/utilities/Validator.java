package store.utilities;

import store.enums.messages.ErrorMessage;

public class Validator {
    public static void validateFormatOfOrder(String order) {
        validateStartsWithBracket(order);
        validateEndsWithBracket(order);
        validateContainsHyphen(order);
    }
    
    private static void validateStartsWithBracket(String order) {
        if (!order.startsWith("[")) {
            throw new IllegalArgumentException(ErrorMessage.WRONG_FORMAT_OF_ORDER.getMessage());
        }
    }
    
    private static void validateEndsWithBracket(String order) {
        if (!order.endsWith("]")) {
            throw new IllegalArgumentException(ErrorMessage.WRONG_FORMAT_OF_ORDER.getMessage());
        }
    }
    
    private static void validateContainsHyphen(String order) {
        if (!order.contains("-")) {
            throw new IllegalArgumentException(ErrorMessage.WRONG_FORMAT_OF_ORDER.getMessage());
        }
    }
}
