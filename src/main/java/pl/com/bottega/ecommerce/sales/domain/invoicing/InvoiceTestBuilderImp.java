package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;

public interface InvoiceTestBuilderImp {
    ClientTestBuilder getClientTestBuilderImp();

    InvoiceReqTestBuilder getInvoiceReqTestBuilderImp();

    ReqItemTestBuilder getReqItemTestBuilderImp();

    ProductData getProductData();

    void setProductData(ProductData productData);

    InvoiceRequest makeAndGet();

}
