package store.view.input;

import camp.nextstep.edu.missionutils.Console;
import store.enums.messages.IOMessage;

public class InputView {
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    public String readOrder() {
        return readInput(IOMessage.INPUT_ORDER.getMessage());
    }

    public String readMemberShipOrNot() {
        return readInput(IOMessage.INPUT_MEMBER_SHIP.getMessage());
    }

    public String readAdditionalOrderOrNot() {
        return readInput(IOMessage.INPUT_ADDITIONAL_ORDER.getMessage());
    }

    private String readInput(String message) {
        System.out.println(String.format(message));
        String answer = Console.readLine();
        System.out.println(System.lineSeparator());

        return answer.replace(SPACE, EMPTY);
    }
}
