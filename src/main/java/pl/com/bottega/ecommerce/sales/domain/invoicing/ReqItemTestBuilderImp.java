package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ReqItemTestBuilderImp implements ReqItemTestBuilder
{
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