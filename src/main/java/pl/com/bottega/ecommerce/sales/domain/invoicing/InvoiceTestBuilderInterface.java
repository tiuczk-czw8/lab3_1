package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface InvoiceTestBuilderInterface {
    void setProductData(ProductData productData);
    void setMoney(Money money);
    void setItemsQuantity(int quantity);
    InvoiceRequest build();
}