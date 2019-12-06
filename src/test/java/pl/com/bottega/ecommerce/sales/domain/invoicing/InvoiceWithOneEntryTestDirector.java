package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceWithOneEntryTestDirector {

    private InvoiceRequestTestBuilderImpl invoiceRequestBuilder;
    private ClientDataTestBuilderImpl clientDataBuilder;
    private RequestItemTestBuilderImpl requestItemBuilder;
    private ProductData productData;

    public InvoiceWithOneEntryTestDirector() {
        invoiceRequestBuilder = new InvoiceRequestTestBuilder();
        clientDataBuilder = new ClientDataTestBuilder();
        requestItemBuilder = new RequestItemTestBuilder();
    }

    public InvoiceRequestTestBuilderImpl getInvoiceRequestBuilder() {
        return invoiceRequestBuilder;
    }

    public ClientDataTestBuilderImpl getClientDataBuilder() {
        return clientDataBuilder;
    }

    public RequestItemTestBuilderImpl getRequestItemBuilder() {
        return requestItemBuilder;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public InvoiceRequest constructAndGet() {
        clientDataBuilder.setId(new Id("001"));
        clientDataBuilder.setName("product001");
        ClientData clientData = clientDataBuilder.build();
        invoiceRequestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.build();
        requestItemBuilder.setProductData(productData);
        requestItemBuilder.setMoney(new Money(111.0));
        requestItemBuilder.setQuantity(1);
        RequestItem requestItem = requestItemBuilder.build();
        invoiceRequest.add(requestItem);
        return invoiceRequest;
    }

}
