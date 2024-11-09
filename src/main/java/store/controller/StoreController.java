package store.controller;

import java.util.List;

import store.domain.Buying;
import store.domain.Inventory;
import store.domain.Receipt;
import store.dto.ReseiptDTO;
import store.enums.constants.AnswerConstants;
import store.model.ProductFileReader;
import store.model.ProductFileWriter;
import store.model.PromotionFileReader;
import store.service.ReseiptService;
import store.utilities.Validator;
import store.view.input.InputView;
import store.view.output.OutputView;
import store.domain.Order;
import store.domain.Product;
import store.domain.ProductFactory;
import store.domain.Promotion;
import store.domain.PromotionFactory;

public class StoreController {
    private  Buying buying;
    private  Inventory inventory;
    private  Order order;
    private  Receipt receipt;
    private  ProductFileReader productFileReader;
    private  ProductFileWriter productFileWriter;
    private  PromotionFileReader promotionFileReader;
    private  InputView inputView;
    private  OutputView outputView;
    private  ProductFactory productFactory;
    private  PromotionFactory promotionFactory;
    private  ReseiptService reseiptService;


    public StoreController(){
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.buying = Buying.startBuying();
    }

    public void startBuying() {
        while (!isBuyingEnd()) {
            startOnePurchase();
            endBuying();
        }
    }

    private void startOnePurchase() {
        outputView.printWellcomeMessage();

        this.productFileReader = new ProductFileReader();
        this.promotionFileReader = new PromotionFileReader();
        outputView.printProdocts(productFileReader.showProductsToUser());

        this.productFactory = new ProductFactory();
        this.promotionFactory = new PromotionFactory();
        List<Product> products = productFactory.createProducts(productFileReader.readFileAsString());
        List<Promotion> promotions = promotionFactory.createPromotions(promotionFileReader.readFileAsString());
        this.inventory =  new Inventory(products, promotions);

        while (true) {
            try {
                String stringOrder = inputView.readOrder();
                this.order = Order.createOrder(stringOrder, inventory);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }


        this.reseiptService = new ReseiptService();
        ReseiptDTO reseiptDTO = this.reseiptService.createReseipt(this.order, this.receipt);
        outputView.printReseipt(reseiptDTO);

        this.productFileWriter = new ProductFileWriter();
        this.productFileWriter.writeProductsToFile(this.inventory.getProducts());
    }

    private void endBuying() {
        String answerToAdditionalOrder = "";
        while (true) {
            try {
                answerToAdditionalOrder = inputView.readAdditionalOrderOrNot();
                Validator.validateAnswer(answerToAdditionalOrder);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (answerToAdditionalOrder.equals(AnswerConstants.ANSWER_NO.getConstants())){
            buying.endBuying();
        }
    }

    private boolean isBuyingEnd() {
        return buying.isEnd();
    }
}