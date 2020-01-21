package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ReqItemTestBuilder implements ReqItemTestBuilderImp {

    private ProductData productData;
    private Money money;
    private int quantity;


    @Override
    public ProductData getProductData(){
        return productData;
    }

    @Override
    public void setProductData(ProductData productData){
        this.productData = productData;
    }

    @Override
    public int getQuantity(){
        return quantity;
    }

    @Override
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public Money getMoney(){
        return money;
    }

    @Override
    public void setMoney(Money money){
        this.money = money;
    }

    @Override
    public RequestItem build(){
        return new RequestItem(productData,quantity,money);
    }

}
