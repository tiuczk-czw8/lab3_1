import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import testBuilders.InvoiceTestBuilderImpl;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTests {

    private ProductData productData;
    private ProductType productType;
    private TaxPolicy taxPolicy;
    private Money money;
    private Tax tax;

    @Before
    public void initialize() {
        productData = Mockito.mock(ProductData.class);
        productType = ProductType.STANDARD;
        taxPolicy = Mockito.mock(TaxPolicy.class);
        money = new Money(5.0);
        tax = new Tax(money, "taxes");
    }

    @Test
    public void invoiceWithOneEntryForOneListElement() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        InvoiceTestBuilderImpl invoiceTestBuilderImp = new InvoiceTestBuilderImpl();
        invoiceTestBuilderImp.setItemsQuantity(1);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void calculateTaxTwiceForInvoiceWithTwoEntries() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        InvoiceTestBuilderImpl invoiceTestBuilderImp = new InvoiceTestBuilderImpl();
        invoiceTestBuilderImp.setItemsQuantity(2);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        verify(taxPolicy, times(2)).calculateTax(productType, money);

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
    }

    @Test
    public void calculateTaxForInvoiceWith100Entries() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        InvoiceTestBuilderImpl invoiceTestBuilderImp = new InvoiceTestBuilderImpl();
        invoiceTestBuilderImp.setItemsQuantity(100);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        verify(taxPolicy, times(100)).calculateTax(productType, money);

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(100));
    }

    @Test
    public void calculateTaxForInvoiceWithNoEntries() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        InvoiceTestBuilderImpl invoiceTestBuilderImp = new InvoiceTestBuilderImpl();
        invoiceTestBuilderImp.setItemsQuantity(0);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        verify(taxPolicy, times(0)).calculateTax(productType, money);

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(0));
    }

}
