package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.Matches;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;


public class BookKeeperTest{




    @Test
    public  void returnOnePositionInOneInvoice(){

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData= new ClientData(new Id("1"), "client");
        InvoiceRequest request = new InvoiceRequest(clientData);

        ProductData productData = mock(ProductData.class);
        ProductType productType = ProductType.STANDARD;
        when(productData.getType()).thenReturn(productType);

        Money money=new Money(1);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        request.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);

        Invoice invoice = bookKeeper.issuance( request,taxPolicy);
        List<InvoiceLine> invoiceLines = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), Matchers.equalTo(1));
    }
}
