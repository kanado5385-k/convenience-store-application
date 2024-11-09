package store.view.input;

import camp.nextstep.edu.missionutils.Console;
import store.enums.messages.IOMessage;

public class InputViewOfPromotionIssue {
    public static String readAnswerToOneMoreProduct(String productName) {
        System.out.println(String.format(IOMessage.INPUT_Y_N_TO_ONE_MORE_FREE.getMessage(), productName));
        String answer = Console.readLine();

        return answer.replace(" ", "");
    }
}
