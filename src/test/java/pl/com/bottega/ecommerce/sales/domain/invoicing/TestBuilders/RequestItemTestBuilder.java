package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface RequestItemTestBuilder {
    void setProductData(ProductData productData);
    void setQuantity(int quantity);
    void setMoney(Money money);
    RequestItem build();
}
