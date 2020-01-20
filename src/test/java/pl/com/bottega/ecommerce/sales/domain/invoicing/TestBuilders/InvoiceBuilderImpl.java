package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceBuilderImpl implements InvoiceBuilder {
    InvoiceRequestBuilderImpl invoiceRequestBuilder;
    ClientDataBuilderImpl clientDataBuilder;
    RequestItemBuilderImpl requestItemBuilder;
    ProductData productData;
    int quantity;
    Money money;

    public InvoiceBuilderImpl() {
        invoiceRequestBuilder = new InvoiceRequestBuilderImpl();
        clientDataBuilder = new ClientDataBuilderImpl();
        requestItemBuilder = new RequestItemBuilderImpl();
    }

    @Override
    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    @Override
    public void setMoney(Money money) {
        this.money = money;
    }

    @Override
    public void setItemsQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public InvoiceRequest setInvoiceRequest() {
        clientDataBuilder.setId(new Id("1"));
        clientDataBuilder.setName("Client");
        ClientData clientData = clientDataBuilder.setClientData();

        invoiceRequestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.setInvoiceRequest();

        requestItemBuilder.setMoney(money);
        requestItemBuilder.setProductData(this.productData);
        requestItemBuilder.setQuantity(0);
        for (int i = 0; i < quantity; i++) {
            invoiceRequest.add(requestItemBuilder.setRequestItem());
        }
        return invoiceRequest;
    }
}
