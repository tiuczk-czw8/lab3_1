package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceRequestNoParamaterTestBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceRequestOneParameterTestBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.RequestItemTestBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private ProductData productData = Mockito.mock(ProductData.class);
    private ProductType productType = ProductType.STANDARD;
    private InvoiceRequestOneParameterTestBuilder oneParameterTestBuilder = new InvoiceRequestOneParameterTestBuilder();
    private InvoiceRequestNoParamaterTestBuilder noParamaterTestBuilder = new InvoiceRequestNoParamaterTestBuilder();

    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        oneParameterTestBuilder.setProductData(productData);
        RequestItemTestBuilder requestItemBuilder = oneParameterTestBuilder.getRequestItemBuilder();

        InvoiceRequest invoiceRequest = oneParameterTestBuilder.constructAndGet();

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(requestItemBuilder.getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, requestItemBuilder.getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void shouldReturnInvoiceWithZeroEntriesForZeroElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        noParamaterTestBuilder.setProductData(productData);
        RequestItemTestBuilder requestItemBuilder = oneParameterTestBuilder.getRequestItemBuilder();

        InvoiceRequest invoiceRequest = noParamaterTestBuilder.constructAndGet();
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(requestItemBuilder.getMoney(), "tax");
        when(taxPolicy.calculateTax(productType, requestItemBuilder.getMoney())).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(0));
    }

    @Test
    public void shouldReturnInvoiceWith100EntriesFor100Elements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientDataBuilder().setId(new Id("1")).setName("prod").createClientData();
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        for(int i = 0; i < 100; i++) {
            invoiceRequest.add(requestItem);
        }
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(100));
    }

    @Test
    public void shouldInvokeCalculateTaxTwoTimesForTwoEntries() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientDataBuilder().setId(new Id("1")).setName("prod").createClientData();
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(productType, money);           //should invoke calculateTax two times
    }

    @Test
    public void shouldNotInvokeCalculateTaxForRequestWithNoElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        ClientData clientData = new ClientDataBuilder().setId(new Id("1")).setName("prod").createClientData();
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(0)).calculateTax(productType, money);           //should invoke calculateTax zero times
    }

    @Test
    public void shouldInvokeCalculateTaxFifteenTimesForRequestWith100Elements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientDataBuilder().setId(new Id("1")).setName("prod").createClientData();
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        for (int i = 0; i < 100; i++) {
            invoiceRequest.add(requestItem);
        }
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(100)).calculateTax(productType, money);           //should invoke calculateTax 100 times

    }

}