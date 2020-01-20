import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import testBuilders.InvoiceTestBuilder;
import testBuilders.InvoiceTestBuilderImpl;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {
    private ProductData productData;
    private ProductType productType = ProductType.STANDARD;
    private TaxPolicy taxPolicy;
    private Money money;
    private InvoiceRequest invoiceRequest;
    private BookKeeper bookKeeper;
    private Tax tax;
    private InvoiceTestBuilder invoiceTestBuilder;

    @Before
    public void mockClass() {
        productData = mock(ProductData.class);
        taxPolicy = mock(TaxPolicy.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        invoiceTestBuilder = new InvoiceTestBuilderImpl();
        money = new Money(0.0);
        bookKeeper = new BookKeeper(new InvoiceFactory());
        tax = new Tax(money, "tax");
    }


    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        setInvoiceTestBuilder(1);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> itemList = invoice.getItems();
        assertThat(invoice, notNullValue());
        assertThat(itemList, notNullValue());
        assertThat(itemList.size(), is(1));
    }

    @Test
    public void shouldReturnInvoiceWithTwoEntryForTwoListElement() {
        setInvoiceTestBuilder(2);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> itemList = invoice.getItems();
        assertThat(invoice, notNullValue());
        assertThat(itemList, notNullValue());
        assertThat(itemList.size(), is(2));
    }

    @Test
    public void shouldNotInvokeCalculateTaxForRequestWithNoElements() {
        setInvoiceTestBuilder(0);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        assertThat(items.size(), is(0));
        verify(taxPolicy, never()).calculateTax(productType, money);
    }

    @Test
    public void shouldInvokeCalculateTaxTwentyTimesForRequestWithTwentyElements() {
        setInvoiceTestBuilder(20);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        assertThat(items.size(), is(20));
        verify(taxPolicy, times(20)).calculateTax(productType, money);                                              //should be list with 100 elements
    }

    private void setInvoiceTestBuilder(int nofItems) {
        invoiceTestBuilder.setItemsQuantity(nofItems);
        invoiceTestBuilder.setProductData(productData);
        invoiceTestBuilder.setMoney(money);
        invoiceRequest = invoiceTestBuilder.build();
    }
}
