package store.factory;

import store.domain.promotion.Promotion;
import store.utilities.Parser;
import store.utilities.Splitter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PromotionFactory {
    private static final String HEADER_LINE = "name,buy,get,start_date,end_date";
    private static final int EXPECTED_FIELD_COUNT = 5;

    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    public List<Promotion> createPromotions(String promotionInformation) {
        return Splitter.splitStringFileLines(promotionInformation).stream()
            .filter(line -> !isSkippablePromotionLine(line))
            .map(this::parsePromotion)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private boolean isSkippablePromotionLine(String line) {
        return line.trim().isEmpty() || line.startsWith(HEADER_LINE);
    }

    private Promotion parsePromotion(String line) {
        List<String> fields = Splitter.splitStringLine(line);
        if (fields.size() != EXPECTED_FIELD_COUNT) {
            return null;
        }
        Integer get = Parser.parseNumberToInt(fields.get(BUY_INDEX));
        return new Promotion(fields.get(NAME_INDEX), get, fields.get(START_DATE_INDEX), fields.get(END_DATE_INDEX));
    }
}