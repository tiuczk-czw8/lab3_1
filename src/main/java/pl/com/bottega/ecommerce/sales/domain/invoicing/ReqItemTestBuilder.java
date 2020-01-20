package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface ReqItemTestBuilder {

    void setProductData(ProductData productData);
    void setQuantity(int quantity);
    void setMoney(Money money);
    RequestItem build();
}
