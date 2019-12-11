package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    private ProductType productType;
    private ProductData productData;
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private Money money;
    private Money net;
    private Tax tax;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;

    private void prepareMocks() {
        productData = mock(ProductData.class);
        taxPolicy = Mockito.mock(TaxPolicy.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
    }

    @Before
    public void setup() {
        productType = ProductType.STANDARD;
        bookKeeper = new BookKeeper(new InvoiceFactory());
        money = new Money(2.0f);
        net = new Money(1.0f);
        tax = new Tax(money, "tax");
        clientData = new ClientData(new Id("1"), "name");
        invoiceRequest = new InvoiceRequest(clientData);
        prepareMocks();
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

    @Test
    public void behaviourTestReturnInvoiceWithTwoEnties() {
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(productType, money);
    }
}
