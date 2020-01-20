package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public class InvoiceRequestTestBuilder implements  InvoiceTestBuilderImp {
    private ClientData clientData;

    @Override
    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public InvoiceRequest build() {
        return new InvoiceRequest(clientData);
    }
}

