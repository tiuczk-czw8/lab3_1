package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public interface InvoiceTestBuilderImp {
        void setClientData(ClientData clientData);
        InvoiceRequest build();
    }
