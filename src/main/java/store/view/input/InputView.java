package store.view.input;

import camp.nextstep.edu.missionutils.Console;
import store.enums.messages.IOMessage;

public class InputView {
    public String readOrder() {
        System.out.println(String.format(IOMessage.INPUT_ORDER.getMessage()));
        String answer = Console.readLine();

        return answer.replace(" ", "");
    }

    public String readMemberShipOrNot() {
        System.out.println(String.format(IOMessage.INPUT_MEMBER_SHIP.getMessage()));
        String answer = Console.readLine();

        return answer.replace(" ", "");
    }

    public String readAdditionalOrderOrNot() {
        System.out.println(String.format(IOMessage.INPUT_ADDITIONAL_ORDER.getMessage()));
        String answer = Console.readLine();

        return answer.replace(" ", "");
    }
}
