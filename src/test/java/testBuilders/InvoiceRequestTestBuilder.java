package testBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;

public interface InvoiceRequestTestBuilder {
    void setClientData(ClientData clientData);
    InvoiceRequest build();
}
