package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

    private ProductType productType = ProductType.STANDARD;
    private ProductData productData = mock(ProductData.class);
    private TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
    private BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
    private Money money = new Money(2.0f);
    private Money net = new Money(1.0f);
    private Tax tax = new Tax(money, "tax");
    private ClientData clientData = new ClientData(new Id("1"), "name");
    private InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

    @Before
    public void setup() {
        // when(this.productData.getType()).thenReturn(productType);
        // when(taxPolicy.calculateTax(productType, net)).thenReturn(tax);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
    }

    @Test
    public void stateTestReturnInvoiceWithOneEntry() {
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

}
