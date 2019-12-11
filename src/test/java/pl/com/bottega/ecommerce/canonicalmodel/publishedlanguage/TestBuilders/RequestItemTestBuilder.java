package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.TestBuilders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public interface RequestItemTestBuilder {
    ProductData getProductData();

    void setProductData(ProductData productData);

    int getQuantity();

    void setQuantity(int quantity);

    Money getMoney();

    void setMoney(Money money);

    RequestItem build();
}
