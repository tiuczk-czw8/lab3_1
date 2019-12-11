package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    ProductData productData;
    ProductType productType;
    TaxPolicy taxPolicy;
    Money money;
    Tax tax;

    @Before
    public void tearsUp() {
        productData = Mockito.mock(ProductData.class);
        productType = ProductType.DRUG;
        taxPolicy = Mockito.mock(TaxPolicy.class);
        money = new Money(1, Money.DEFAULT_CURRENCY);
        tax = new Tax(money, "TAX");
    }

    @Test
    public void forSignleElementShouldReturnInvoiceWithOneElement() {
        when(productData.getType()).thenReturn(productType);

        Id id = new Id("42");
        ClientData client = new ClientData(id, "product");
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);

        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(1));
    }

}
