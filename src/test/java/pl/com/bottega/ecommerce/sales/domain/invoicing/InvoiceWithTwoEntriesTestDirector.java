package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceWithTwoEntriesTestDirector implements InvoiceTestDirectorImpl {

    private InvoiceRequestTestBuilderImpl invoiceRequestBuilder;
    private ClientDataTestBuilderImpl clientDataBuilder;
    private RequestItemTestBuilderImpl requestItemBuilder;
    private ProductData productData1;
    private ProductData productData2;

    public InvoiceWithTwoEntriesTestDirector() {
        invoiceRequestBuilder = new InvoiceRequestTestBuilder();
        clientDataBuilder = new ClientDataTestBuilder();
        requestItemBuilder = new RequestItemTestBuilder();
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
        return productData1;
    }

    @Override
    public void setProductData(ProductData productData) {
        this.productData1 = productData;
    }

    public ProductData getProductData2() {
        return productData2;
    }

    public void setProductData2(ProductData productData2) {
        this.productData2 = productData2;
    }

    @Override
    public InvoiceRequest constructAndGet() {
        clientDataBuilder.setId(new Id("002"));
        clientDataBuilder.setName("product002");
        ClientData clientData = clientDataBuilder.build();

        invoiceRequestBuilder.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.build();

        requestItemBuilder.setProductData(productData1);
        requestItemBuilder.setMoney(new Money(12.3));
        requestItemBuilder.setQuantity(2);
        RequestItem requestItem1 = requestItemBuilder.build();
        requestItemBuilder.setProductData(productData2);
        RequestItem requestItem2 = requestItemBuilder.build();
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);

        return invoiceRequest;
    }

}
