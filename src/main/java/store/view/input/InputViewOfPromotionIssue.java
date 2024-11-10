package store.view.input;

import camp.nextstep.edu.missionutils.Console;
import store.enums.messages.IOMessage;

public class InputViewOfPromotionIssue {
    public static String readAnswerToOneMoreProduct(String productName) {
        System.out.println(String.format(IOMessage.INPUT_Y_N_TO_ONE_MORE_FREE.getMessage(), productName));
        String answer = Console.readLine();
        System.out.println(System.lineSeparator());

        return answer.replace(" ", "");
    }

    public static String readAnswerToLackOfQuantity(String productName, int purchaseQuantity) {
        System.out.println(String.format(IOMessage.INPUT_Y_N_TO_LACK_OF_QUANTITY.getMessage(), productName, purchaseQuantity));
        String answer = Console.readLine();
        System.out.println(System.lineSeparator());

        return answer.replace(" ", "");
    }
}
