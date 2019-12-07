package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    private InvoiceWithOneEntryTestDirector director1;
    private InvoiceWithTwoEntriesTestDirector director2;
    private ProductType productType1;
    private ProductType productType2;
    private BookKeeper bookKeeper;

    ProductData getProductDataMock(ProductType productType) {
        ProductData productData = Mockito.mock(ProductData.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        return productData;
    }

    TaxPolicy getTaxPolicyMock(@NotNull InvoiceTestDirectorImpl director, ProductType productType) {
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        RequestItemTestBuilderImpl builder = director.getRequestItemBuilder();
        Money money = builder.getMoney();
        Tax tax = new Tax(money, "tax_test");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        return taxPolicy;
    }

    @Before
    public void setUp() {
        director1 = new InvoiceWithOneEntryTestDirector();
        productType1 = ProductType.STANDARD;
        ProductData productData = getProductDataMock(productType1);
        director1.setProductData(productData);

        director2 = new InvoiceWithTwoEntriesTestDirector();
        productType2 = ProductType.DRUG;
        ProductData productData21 = getProductDataMock(productType2);
        ProductData productData22 = getProductDataMock(productType2);
        director2.setProductData(productData21);
        director2.setProductData2(productData22);

        bookKeeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void stateTest_shouldReturnInvoiceWithOneEntry() {
        InvoiceRequest invoiceRequest = director1.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director1, productType1);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void stateTest_shouldReturnInvoiceWithTwoEntries() {
        InvoiceRequest invoiceRequest = director2.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director2, productType2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
    }

    @Test
    public void stateTest_invoiceWithTwoEntriesShouldMatchItsData() {
        InvoiceRequest invoiceRequest = director2.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director2, productType2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        ClientData clientData = invoice.getClient();
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(equalTo(2)));
        assertThat(clientData, notNullValue());
        InvoiceRequestTestBuilderImpl invoiceRequestBuilder = director2.getInvoiceRequestBuilder();
        assertThat(clientData, is(equalTo(invoiceRequestBuilder.getClientData())));
        InvoiceLine invoiceLine1 = items.get(0);
        assertThat(invoiceLine1.getProduct(), is(equalTo(director2.getProductData())));
        RequestItemTestBuilderImpl requestItemBuilder = director2.getRequestItemBuilder();
        assertThat(invoiceLine1.getQuantity(), is(equalTo(requestItemBuilder.getQuantity())));
        InvoiceLine invoiceLine2 = items.get(1);
        assertThat(invoiceLine2.getProduct(), is(equalTo(director2.getProductData2())));
        assertThat(invoiceLine2.getQuantity(), is(equalTo(requestItemBuilder.getQuantity())));
    }

    @Test
    public void behaviourTest_invoiceWithTwoEntriesShouldCalculateTaxTwice() {
        InvoiceRequest invoiceRequest = director2.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director2, productType2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice, notNullValue());
        RequestItemTestBuilderImpl builder = director2.getRequestItemBuilder();
        Money money = builder.getMoney();
        verify(taxPolicy, times(2)).calculateTax(productType2, money);
        verifyNoMoreInteractions(taxPolicy);
    }

    @Test
    public void behaviourTest_invoiceWithOneEntryShouldCalculateTaxOnce() {
        InvoiceRequest invoiceRequest = director1.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director1, productType1);
        RequestItemTestBuilderImpl itemBuilder = director1.getRequestItemBuilder();
        Money money = itemBuilder.getMoney();

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice, notNullValue());
        verify(taxPolicy, times(1)).calculateTax(productType1, money);
        verifyNoMoreInteractions(taxPolicy);
    }

    @Test
    public void behaviourTest_invoiceWithOneEntryShouldCallGetterOnceForProductData() {
        InvoiceRequest invoiceRequest = director1.constructAndGet();
        TaxPolicy taxPolicy = getTaxPolicyMock(director1, productType1);
        ProductData productData1 = director1.getProductData();

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice, notNullValue());
        verify(productData1, times(1)).getType();
        verifyNoMoreInteractions(productData1);
    }

}
