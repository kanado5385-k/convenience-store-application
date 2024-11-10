package store.controller;

import store.view.input.InputView;
import store.view.output.ExceptionOutputView;
import store.view.output.OutputView;

public class ViewFactory {

    public InputView createInputView() {
        return new InputView();
    }

    public OutputView createOutputView() {
        return new OutputView();
    }
}
