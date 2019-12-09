package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceRequestTestBuilder implements  InvoiceTestBuilderImp {

    private InvoiceReqTestBuilder invoiceReqTestBuilderImp;
    private ClientTestBuilder clientTestBuilderImp;
    private ReqItemTestBuilder reqItemTestBuilderImp;
    private ProductData productData;

    public InvoiceRequestTestBuilder() {
        invoiceReqTestBuilderImp = new InvoiceReqTestBuilder();
        clientTestBuilderImp = new ClientTestBuilder();
        reqItemTestBuilderImp = new ReqItemTestBuilder();
    }

    @Override
    public InvoiceReqTestBuilder getInvoiceReqTestBuilderImp(){
        return invoiceReqTestBuilderImp;
    }

    @Override
    public ClientTestBuilder getClientTestBuilderImp(){
        return clientTestBuilderImp;
    }

    @Override
    public ReqItemTestBuilder getReqItemTestBuilderImp(){
        return reqItemTestBuilderImp;
    }

    @Override
    public ProductData getProductData(){
        return productData;
    }

    @Override
    public void setProductData(ProductData productData){
        this.productData = productData;
    }

    @Override
    public InvoiceRequest makeAndGet() {
        clientTestBuilderImp.setId(new Id("001"));
        clientTestBuilderImp.setName("product001");
        ClientData clientData = clientTestBuilderImp.build();
        invoiceReqTestBuilderImp.setClientData(clientData);
        InvoiceRequest invoiceRequest = invoiceReqTestBuilderImp.build();
        reqItemTestBuilderImp.setMoney(new Money(0.0));
        reqItemTestBuilderImp.setQuantity(1);
        reqItemTestBuilderImp.setProductData(productData);
        RequestItem requestItem = reqItemTestBuilderImp.build();
        InvoiceRequest.add(requestItem);
        return invoiceRequest;

    }
}
