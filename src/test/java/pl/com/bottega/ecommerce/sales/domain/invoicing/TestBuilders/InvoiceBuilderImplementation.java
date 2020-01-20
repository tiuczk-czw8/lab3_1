package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceBuilderImplementation implements InvoiceBuilder {

    private InvoiceRequestBuilderImpl invoiceRequestBuilder;
    private ClientDataBuilderImpl clientDataBuilder;
    private RequestItemBuilderImpl requestItemBuilder;
    private ProductData productData;
    private int quantity;
    private Money money;

    public InvoiceBuilderImplementation() {
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
    public InvoiceRequest build() {
        clientDataBuilder.setId(new Id("1"));
        clientDataBuilder.setName("Client");
        ClientData clientData = clientDataBuilder.build();

        invoiceRequestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.build();

        requestItemBuilder.setMoney(money);
        requestItemBuilder.setProductData(this.productData);
        requestItemBuilder.setQuantity(0);
        for (int i = 0; i < quantity; i++) {
            invoiceRequest.add(requestItemBuilder.build());
        }
        return invoiceRequest;
    }
}
