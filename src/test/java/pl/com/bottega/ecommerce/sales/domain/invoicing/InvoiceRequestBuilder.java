package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRequestBuilder {

    private ClientData clientData = null;
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder() {
    }

    public InvoiceRequestBuilder withClientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public InvoiceRequestBuilder addRequestItem(RequestItem requestItem) {
        items.add(requestItem);
        return this;
    }

    public InvoiceRequest build() {
        InvoiceRequest inv = new InvoiceRequest(clientData);
        for (RequestItem r : items) {
            inv.add(r);
        }
        return inv;
    }
}
