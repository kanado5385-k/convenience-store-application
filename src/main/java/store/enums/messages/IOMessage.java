package store.enums.messages;

public enum IOMessage implements SystemMessage {
    INPUT_ORDER("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    INPUT_MEMBER_SHIP("멤버십 할인을 받으시겠습니까? (Y/N)"),
    INPUT_ADDITIONAL_ORDER("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    INPUT_Y_N_TO_ONE_MORE_FREE("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    INPUT_Y_N_TO_LACK_OF_QUANTITY("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),

    PRINT_NO_ANY_PROMOTION_PRODUCT("증정 상품이 없습니다."),
    PRINT_WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n" +
        "현재 보유하고 있는 상품입니다.");

    private final String message;

    IOMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}