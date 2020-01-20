package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceBuilderImpl;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private ProductData productData;
    private TaxPolicy taxPolicy;
    private ProductType productType;
    private Tax tax;
    private Money money;

    @Before
    public void init() {
        productData = mock(ProductData.class);
        taxPolicy = mock(TaxPolicy.class);
        productType = ProductType.FOOD;
        money = new Money(10);
        tax = new Tax(money, "23%");
    }

    @Test
    public void invoiceRequestsOneItemReturnsInvoiceWithOneItem() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(1);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceBuilderImpl.setInvoiceRequest();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> elements = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(1));
    }

    @Test
    public void calculateTaxForTwoElementsInInvoice() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(2);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceBuilderImpl.setInvoiceRequest();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> elements = invoice.getItems();

        verify(taxPolicy, times(2)).calculateTax(productType, money);
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(2));


    }

    @Test
    public void noElementsInInvoiceShloudNotCalculateTax() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(0);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceBuilderImpl.setInvoiceRequest();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> elements = invoice.getItems();

        verify(taxPolicy, times(0)).calculateTax(productType, money);
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(0));
    }

    @Test
    public void shouldCalculateTaxFor1000ElementsInInvoice() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(1000);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceBuilderImpl.setInvoiceRequest();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> elements = invoice.getItems();

        verify(taxPolicy, times(1000)).calculateTax(productType, money);
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(1000));
    }
}
