package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public interface InvoiceRequestTestBuilderImpl {

    ClientData getClientData();

    void setClientData(ClientData clientData);

    InvoiceRequest build();

}
