package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.TestBuilders;


import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemTestBuilderImpl implements RequestItemTestBuilder {
    private ProductData productData;
    private int quantity;
    private Money money;

    @Override
    public ProductData getProductData() {
        return this.productData;
    }

    @Override
    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public Money getMoney() {
        return this.money;
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