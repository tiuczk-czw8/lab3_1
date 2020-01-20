package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceTestBuilderImp implements InvoiceTestBuilder {

    InvoiceRequestTestBuilderImp invoiceRequestTestBuilder;
    ClientDataTestBuilderImp clientDataTestBuilder;
    RequestItemBuilderImp requestItemBuilder;
    ProductData productData;
    int quantity;
    Money money;

    public InvoiceTestBuilderImp() {
        invoiceRequestTestBuilder = new InvoiceRequestTestBuilderImp();
        clientDataTestBuilder = new ClientDataTestBuilderImp();
        requestItemBuilder = new RequestItemBuilderImp();
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
        clientDataTestBuilder.setId(new Id("1"));
        clientDataTestBuilder.setName("name");
        ClientData clientData = clientDataTestBuilder.build();

        invoiceRequestTestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestTestBuilder.build();

        requestItemBuilder.setMoney(money);
        requestItemBuilder.setProductData(this.productData);
        requestItemBuilder.setQuantity(0);
        for (int i = 0; i < quantity; i++) {
            invoiceRequest.add(requestItemBuilder.build());
        }
        return invoiceRequest;
    }

}
