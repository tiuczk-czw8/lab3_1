package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

public interface InvoiceRequestTest {
    InvoiceRequestTestBuilderImpl getInvoiceRequestBuilder();

    ClientDataTestBuilderImpl getClientDataBuilder();

    RequestItemTestBuilderImpl getRequestItemBuilder();

    ProductData getProductData();

    void setProductData(ProductData productData);

    InvoiceRequest constructAndGet();
}
