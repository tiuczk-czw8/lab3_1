package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;

public class InvoiceRequestTestBuilderImpl implements InvoiceRequestTestBuilder {
    private ClientData clientData;

    @Override
    public ClientData getClientData() {
        return clientData;
    }

    @Override
    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public InvoiceRequest build() {
        return new InvoiceRequest(clientData);
    }

}