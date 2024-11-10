package store.controller;

import java.util.List;

import store.domain.Buying;
import store.domain.Receipt;
import store.domain.inventory.Inventory;
import store.domain.promotion.PromotionPolicy;
import store.dto.ReceiptDTO;
import store.enums.constants.AnswerConstants;
import store.model.ProductFileReader;
import store.model.ProductFileWriter;
import store.model.PromotionFileReader;
import store.service.ReceiptService;
import store.utilities.Validator;
import store.view.input.InputView;
import store.view.output.ExceptionOutputView;
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

    private DomainFactory domainFactory;
    private ModelFactory modelFactory;
    private ViewFactory viewFactory;

    public StoreController(DomainFactory domainFactory, ModelFactory modelFactory, ViewFactory viewFactory) {
        this.domainFactory = domainFactory;
        this.modelFactory = modelFactory;
        this.viewFactory = viewFactory;

        this.inputView = viewFactory.createInputView();
        this.outputView = viewFactory.createOutputView();
        this.buying = domainFactory.createBuying();
    }

    public void startBuying() {
        while (!isBuyingEnd()) {
            startOnePurchase();
            handleBuyingEnd();
        }
    }

    private void startOnePurchase() {
        outputView.printWelcomeMessage();
        List<Product> products = loadProducts();
        List<Promotion> promotions = loadPromotions();
        PromotionPolicy policy = domainFactory.createPromotionPolicy(products, promotions);
        Inventory inventory = domainFactory.createInventory(products, policy);
        Order order = createOrder(inventory);
        printReceipt(order, createReceipt(order));
        saveInventory(inventory);
    }

    private List<Product> loadProducts() {
        ProductFileReader productFileReader = modelFactory.createProductFileReader();
        outputView.printProducts(productFileReader.showProductsToUser());

        String productData = productFileReader.readFileAsString();
        ProductFactory productFactory = domainFactory.createProductFactory();
        return productFactory.createProducts(productData);
    }

    private List<Promotion> loadPromotions() {
        PromotionFileReader promotionFileReader = modelFactory.createPromotionFileReader();

        String promotionData = promotionFileReader.readFileAsString();
        PromotionFactory promotionFactory = domainFactory.createPromotionFactory();
        return promotionFactory.createPromotions(promotionData);
    }

    private Order createOrder(Inventory inventory) {
        while (true) {
            try {
                String stringOrder = inputView.readOrder();
                return domainFactory.createOrder(stringOrder, inventory);
            } catch (IllegalArgumentException e) {
                ExceptionOutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private Receipt createReceipt(Order order) {
        while (true) {
            try {
                String answerToMembership = inputView.readMemberShipOrNot();
                return domainFactory.createReceipt(order, answerToMembership);
            } catch (IllegalArgumentException e) {
                ExceptionOutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void printReceipt(Order order, Receipt receipt) {
        ReceiptDTO receiptDTO = new ReceiptService().createReseipt(order, receipt);
        outputView.printReceipt(receiptDTO);
    }

    private void saveInventory(Inventory inventory) {
        ProductFileWriter productFileWriter = modelFactory.createProductFileWriter();
        productFileWriter.writeProductsToFile(inventory.getProducts());
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
                ExceptionOutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private String validateAnswerToAdditionalOrder(String answer) {
        Validator.validateAnswer(answer);
        return answer;
    }

    private boolean isBuyingEnd() {
        return buying.isEnd();
    }
}