package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;

public interface InvoiceRequestTestBuilder {
    ClientData getClientData();
    void setClientData(ClientData clientData);
    InvoiceRequest build();
}
