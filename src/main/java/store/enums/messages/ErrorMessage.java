package store.enums.messages;

public enum ErrorMessage implements SystemMessage {
    PROBLEM_WITH_FILE("파일을 읽는 중 문제가 발생했습니다. 파일 경로와 파일 상태를 확인한 후 다시 시도해 주세요.");

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
