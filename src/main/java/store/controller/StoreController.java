package store.controller;

import java.util.List;

import store.domain.Buying;
import store.domain.Inventory;
import store.domain.Receipt;
import store.dto.ReceiptDTO;
import store.enums.constants.AnswerConstants;
import store.model.ProductFileReader;
import store.model.ProductFileWriter;
import store.model.PromotionFileReader;
import store.service.ReceiptService;
import store.utilities.Validator;
import store.view.input.InputView;
import store.view.output.OutputView;
import store.domain.Order;
import store.domain.Product;
import store.domain.ProductFactory;
import store.domain.Promotion;
import store.domain.PromotionFactory;

public class StoreController {
    private Buying buying;
    private InputView inputView;
    private OutputView outputView;

    public StoreController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.buying = Buying.startBuying();
    }

    public void startBuying() {
        while (!isBuyingEnd()) {
            startOnePurchase();
            handleBuyingEnd();
        }
    }

    private void startOnePurchase() {
        outputView.printWelcomeMessage();
        Inventory inventory = loadInventory();
        Order order = createOrder(inventory);
        Receipt receipt = createReceipt(order);
        printReceipt(order, receipt);
        saveInventory(inventory);
    }

    private Inventory loadInventory() {
        ProductFileReader productFileReader = new ProductFileReader();
        PromotionFileReader promotionFileReader = new PromotionFileReader();
        outputView.printProducts(productFileReader.showProductsToUser());
        List<Product> products = new ProductFactory().createProducts(productFileReader.readFileAsString());
        List<Promotion> promotions = new PromotionFactory().createPromotions(promotionFileReader.readFileAsString());

        return new Inventory(products, promotions);
    }

    private Order createOrder(Inventory inventory) {
        while (true) {
            try {
                String stringOrder = inputView.readOrder();
                return Order.createOrder(stringOrder, inventory);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Receipt createReceipt(Order order) {
        while (true) {
            try {
                String answerToMemberShip = inputView.readMemberShipOrNot();
                return Receipt.createReceipt(order, answerToMemberShip);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printReceipt(Order order, Receipt receipt) {
        ReceiptDTO receiptDTO = new ReceiptService().createReseipt(order, receipt);
        outputView.printReceipt(receiptDTO);
    }

    private void saveInventory(Inventory inventory) {
        new ProductFileWriter().writeProductsToFile(inventory.getProducts());
    }

    private void handleBuyingEnd() {
        String answerToAdditionalOrder = readAdditionalOrderOrNot();
        if (answerToAdditionalOrder.equals(AnswerConstants.ANSWER_NO.getConstants())) {
            buying.endBuying();
        }
    }

    private String readAdditionalOrderOrNot() {
        while (true) {
            try {
                String answer = inputView.readAdditionalOrderOrNot();
                return validateAnswerToAdditionalOrder(answer);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String validateAnswerToAdditionalOrder(String answer){
        Validator.validateAnswer(answer);
        return answer;
    }

    private boolean isBuyingEnd() {
        return buying.isEnd();
    }
}