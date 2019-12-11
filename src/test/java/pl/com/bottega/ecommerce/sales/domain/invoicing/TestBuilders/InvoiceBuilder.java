package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface InvoiceBuilder {

    void setProductData(ProductData productData);

    void setMoney(Money money);

    void setItemsQuantity(int quantity);

    InvoiceRequest build();
}
