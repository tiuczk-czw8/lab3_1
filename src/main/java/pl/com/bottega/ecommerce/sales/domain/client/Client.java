package pl.com.bottega.ecommerce.sales.domain.client;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.payment.PaymentFactory;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class Client extends BaseAggregateRoot {
    private String name;


    private PaymentFactory paymentFactory;

    public ClientData generateSnapshot() {
        return new ClientDataBuilder().setId(id).setName(name).createClientData();
    }

    public boolean canAfford(Money amount) {
        return true;//TODO explore domain rules ex: credit limit
    }

}
