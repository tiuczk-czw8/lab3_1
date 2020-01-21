package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface ReqItemTestBuilderImp {

    ProductData getProductData();

    void setProductData(ProductData productData);

    Money getMoney();

    void setMoney(Money money);

    int getQuantity();

    void setQuantity(int quantity);

    RequestItem build();

}
