package store.enums.constants;

public enum AnswerConstants implements CommonConstants {
    ANSWER_YES("Y"),
    ANSWER_NO("N");

    private final String constants;

    AnswerConstants(String constants) {
        this.constants = constants;
    }

    @Override
    public String getConstants() {
        return constants;
    }
}
