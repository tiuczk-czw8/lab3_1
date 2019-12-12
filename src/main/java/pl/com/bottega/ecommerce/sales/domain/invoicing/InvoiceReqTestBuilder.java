package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public interface InvoiceReqTestBuilder
{
    void setClientData(ClientData clientData);
    InvoiceRequest build();
}