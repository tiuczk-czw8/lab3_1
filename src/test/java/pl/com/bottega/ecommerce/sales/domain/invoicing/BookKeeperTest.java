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

import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceBuilderImpl;
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
        prepareMocks();
    }

    @Test
    public void stateTestReturnInvoiceWithNoEntries() {
        invoiceRequest = buildInvoiceRequest(0);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceState(invoice, 0);
    }

    @Test
    public void stateTestReturnInvoiceWithOneEntry() {
        invoiceRequest = buildInvoiceRequest(1);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceState(invoice, 1);
    }

    @Test
    public void stateTestReturnInvoiceWith10Entries() {
        invoiceRequest = buildInvoiceRequest(10);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceState(invoice, 10);
    }

    @Test
    public void behaviourTestReturnInvoiceWithNoEnties() {
        invoiceRequest = buildInvoiceRequest(0);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceBehaviour(invoice, 0);
    }

    @Test
    public void behaviourTestReturnInvoiceWithTwoEnties() {
        invoiceRequest = buildInvoiceRequest(2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceBehaviour(invoice, 2);
    }

    @Test
    public void behaviourTestReturnInvoiceWith10Enties() {
        invoiceRequest = buildInvoiceRequest(10);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        testIssuanceBehaviour(invoice, 10);
    }

    private void testIssuanceState(Invoice invoice, int numberOfItems) {
        List<InvoiceLine> items = invoice.getItems();
        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(numberOfItems));
    }

    private void testIssuanceBehaviour(Invoice invoice, int numberOfCalls) {
        verify(taxPolicy, times(numberOfCalls)).calculateTax(productType, money);
    }

    private InvoiceRequest buildInvoiceRequest(int itemsQuantity) {
        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(itemsQuantity);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        return invoiceBuilderImpl.build();
    }
}
