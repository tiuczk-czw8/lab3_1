package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.runner.Request;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {

    private ProductData productData = null;
    private int quanity = 0;
    private Money totalCost = new Money(1.0);

    public RequestItemBuilder() {
    }

    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder withQuanity(int quanity) {
        this.quanity = quanity;
        return this;
    }

    public RequestItemBuilder withTotalCost(Money totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public RequestItem build() {
        return new RequestItem(productData, quanity, totalCost);
    }

}