package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceRequestOneParameterTestBuilder implements InvoiceRequestTest {
    private InvoiceRequestTestBuilderImpl invoiceRequestBuilder;
    private ClientDataTestBuilderImpl clientDataBuilder;
    private RequestItemTestBuilderImpl requestItemBuilder;
    private ProductData productData;

    public InvoiceRequestOneParameterTestBuilder() {
        invoiceRequestBuilder = new InvoiceRequestTestBuilderImpl();
        clientDataBuilder = new ClientDataTestBuilderImpl();
        requestItemBuilder = new RequestItemTestBuilderImpl();
    }

    @Override
    public InvoiceRequestTestBuilderImpl getInvoiceRequestBuilder() {
        return invoiceRequestBuilder;
    }

    @Override
    public ClientDataTestBuilderImpl getClientDataBuilder() {
        return clientDataBuilder;
    }

    @Override
    public RequestItemTestBuilderImpl getRequestItemBuilder() {
        return requestItemBuilder;
    }

    @Override
    public ProductData getProductData() {
        return productData;
    }

    @Override
    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    @Override
    public InvoiceRequest constructAndGet() {
        clientDataBuilder.setId(new Id("1"));
        clientDataBuilder.setName("prod");
        ClientData clientData = clientDataBuilder.build();
        invoiceRequestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.build();
        requestItemBuilder.setProductData(productData);
        requestItemBuilder.setMoney(new Money(0.0));
        requestItemBuilder.setQuantity(0);
        RequestItem requestItem = requestItemBuilder.build();
        invoiceRequest.add(requestItem);
        return invoiceRequest;
    }
}
