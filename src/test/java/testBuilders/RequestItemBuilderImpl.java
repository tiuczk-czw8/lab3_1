package testBuilders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilderImpl implements RequestItemBuilder {
    private ProductData productData;
    private int quantity;
    private Money money;

    @Override
    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void setMoney(Money money) {
        this.money = money;
    }

    @Override
    public RequestItem build() {
        return new RequestItem(productData, quantity, money);
    }
}
