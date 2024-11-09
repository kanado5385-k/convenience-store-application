package store.enums.messages;

public enum IOMessage implements SystemMessage {
    INPUT_Y_N_TO_ONE_MORE_FREE("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    INPUT_Y_N_TO_LACK_OF_QUANTITY("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");

    private final String message;

    IOMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}