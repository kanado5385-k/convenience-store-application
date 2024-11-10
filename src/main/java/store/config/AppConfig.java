package store.config;

import store.controller.DomainFactory;
import store.controller.ModelFactory;
import store.controller.StoreController;
import store.controller.ViewFactory;


public class AppConfig {

    public StoreController storeController() {
        DomainFactory domainFactory = new DomainFactory();
        ModelFactory modelFactory = new ModelFactory();
        ViewFactory viewFactory = new ViewFactory();

        return new StoreController(domainFactory, modelFactory, viewFactory);
    }
}
