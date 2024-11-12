package store.config;

import store.controller.StoreController;
import store.factory.DomainFactory;
import store.factory.ModelFactory;
import store.factory.ViewFactory;


public class AppConfig {

    public StoreController storeController() {
        DomainFactory domainFactory = new DomainFactory();
        ModelFactory modelFactory = new ModelFactory();
        ViewFactory viewFactory = new ViewFactory();

        return new StoreController(domainFactory, modelFactory, viewFactory);
    }
}
