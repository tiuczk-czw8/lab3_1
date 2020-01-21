package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceReqTestBuilder implements InvoiceReqTestBuilderImp{

    InvoiceRequestTestBuilder invoiceRequestTestBuilder;
    ClientTestBuilder clientDataTestBuilder;
    ReqItemTestBuilderImp requestItemBuilder;
    ProductData productData;
    int quantity;
    Money money;

    public InvoiceReqTestBuilder() {
        invoiceRequestTestBuilder = new InvoiceRequestTestBuilder();
        clientDataTestBuilder = new ClientTestBuilder();
        requestItemBuilder = new ReqItemTestBuilder();
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
        clientDataTestBuilder.setId(new Id("42"));
        clientDataTestBuilder.setName("product");
        ClientData clientData = clientDataTestBuilder.build();
        invoiceRequestTestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestTestBuilder.build();
        requestItemBuilder.setMoney(money);
        requestItemBuilder.setProductData(this.productData);
        requestItemBuilder.setQuantity(1);
        for (int i = 0; i < quantity; i++) {
            invoiceRequest.add(requestItemBuilder.build());
        }
        return invoiceRequest;
    }
}