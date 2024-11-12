package store.domain;

import store.enums.BuyingStatus;

public class Buying {
    private BuyingStatus status;

    private Buying(BuyingStatus status) {
        this.status = status;
    }

    public static Buying startBuying() {
        return new Buying(BuyingStatus.BUYING);
    }

    public void endBuying() {
        this.status = BuyingStatus.FINISH;
    }

    public boolean isEnd() {
        return this.status == BuyingStatus.FINISH;
    }
}
