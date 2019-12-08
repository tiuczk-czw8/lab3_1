package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private ProductData productData;
    private ProductType productType;
    private InvoiceRequesWithParametersTestBuilder invoiceRequesWithParametersTestBuilder;

    @Before
    public void setUp() {
        productData = Mockito.mock(ProductData.class);
        invoiceRequesWithParametersTestBuilder = new InvoiceRequesWithParametersTestBuilder();
        productType = ProductType.STANDARD;
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        invoiceRequesWithParametersTestBuilder.setProductData(productData);
    }

    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetOneParameter();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void shouldReturnInvoiceWithZeroEntriesForZeroElements() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetZeroParameter();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(0));
    }

    @Test
    public void shouldReturnInvoiceWith100EntriesFor100Elements() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetOneHundredParameter();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(100));
    }

    @Test
    public void shouldInvokeCalculateTaxTwoTimesForTwoEntries() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetTwoParameter();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney());           //should invoke calculateTax two times
    }

    @Test
    public void shouldNotInvokeCalculateTaxForRequestWithNoElements() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetZeroParameter();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(0)).calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney());           //should invoke calculateTax zero times
    }

    @Test
    public void shouldInvokeCalculateTaxOneHundredTimesForRequestWithOneHundredElements() {
        InvoiceRequest invoiceRequest = invoiceRequesWithParametersTestBuilder.constructAndGetOneHundredParameter();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(100)).calculateTax(productType, invoiceRequesWithParametersTestBuilder.getRequestItemBuilder().getMoney());           //should invoke calculateTax 100 times

    }

}