package store.enums.messages;

public enum ErrorMessage implements SystemMessage {
    INVALID_INT_NUMBER("숫자가 아닌 것이 입력 되었습니다. 다시 시도해주세요."),
    NOT_NATURAL_NUMBER("자연수가 아닌 숫자입니다. 다시 시도해주세요."),
    PROBLEM_WITH_FILE("파일을 읽는 중 문제가 발생했습니다. 파일 경로와 파일 상태를 확인한 후 다시 시도해 주세요."),

    WRONG_FORMAT_OF_ORDER("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    private static final String ERROR_LOG_LEVEL = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return ERROR_LOG_LEVEL + message;
    }
}
