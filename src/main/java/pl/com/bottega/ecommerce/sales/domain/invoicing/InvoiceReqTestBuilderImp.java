package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface InvoiceReqTestBuilderImp {

    void setProductData(ProductData productData);
    void setMoney(Money money);
    void setItemsQuantity(int quantity);
    InvoiceRequest build();

}
