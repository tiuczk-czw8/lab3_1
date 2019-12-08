package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceTestBuilderImp;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    ProductData productData;
    ProductType productType;
    TaxPolicy taxPolicy;

    @Before
    public void Initialize() {
        productData = Mockito.mock(ProductData.class);
        productType = ProductType.STANDARD;
        taxPolicy = Mockito.mock(TaxPolicy.class);
    }

    private InvoiceRequest createInvoiceRequest(int quantity, Money money){
        InvoiceTestBuilderImp invoiceTestBuilderImp = new InvoiceTestBuilderImp();
        invoiceTestBuilderImp.setItemsQuantity(quantity);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        return invoiceRequest;
    }
    @Test
    public void shouldReturnInvoiceWithOneEntryForSingleElement()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(1.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();


        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));

    }

    @Test
    public void shouldCalculateTaxTwiceForInvoiceWithTwoEntries()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(1.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(2, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(2)).calculateTax(productType,money);
    }

    @Test
    public void shouldNotCalculateTaxForInvoiceWithNoEntries()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);


        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(0)).calculateTax(productType,money);
    }

    @Test
    public void shouldCalculateTax500TimesForInvoiceWith500Entries()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(500, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(500)).calculateTax(productType,money);
    }

    @Test
    public void shouldReturnInvoiceWith500EntriesFor500Elements()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("1"), "product");

        InvoiceRequest invoiceRequest = createInvoiceRequest(500, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();


        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(500));

    }
    @Test
    public void shouldReturnInvoiceWithNoEntriesFor0Elements()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();


        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(0));

    }

}