package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

public interface InvoiceTestDirectorImpl {

    InvoiceRequestTestBuilderImpl getInvoiceRequestBuilder();

    ClientDataTestBuilderImpl getClientDataBuilder();

    RequestItemTestBuilderImpl getRequestItemBuilder();

    ProductData getProductData();

    void setProductData(ProductData productData);

    InvoiceRequest constructAndGet();

}
