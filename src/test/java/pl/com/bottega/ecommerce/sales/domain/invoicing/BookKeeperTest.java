package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
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
    private ProductType productType1;
    private BookKeeper bookKeeper;

    ProductData getProductDataMock(ProductType productType) {
        ProductData productData = Mockito.mock(ProductData.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        return productData;
    }

    TaxPolicy getTaxPolicyMock(@NotNull InvoiceTestDirectorImpl director, ProductType productType) {
        TaxPolicy taxPolicy2 = Mockito.mock(TaxPolicy.class);
        RequestItemTestBuilderImpl builder = director.getRequestItemBuilder();
        Money money2 = builder.getMoney();
        Tax tax2 = new Tax(money2, "tax_test");
        when(taxPolicy2.calculateTax(productType, money2)).thenAnswer(invocationOnMock -> tax2);
        return taxPolicy2;
    }

    @Before
    public void setUp() {
        director1 = new InvoiceWithOneEntryTestDirector();
        productType1 = ProductType.STANDARD;
        ProductData productData = getProductDataMock(productType1);
        director1.setProductData(productData);
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
        ProductData productData = Mockito.mock(ProductData.class);
        ProductType productType = ProductType.STANDARD;
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem1 = new RequestItem(productData, 0, money);
        RequestItem requestItem2 = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax_desc");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
    }

    @Test
    public void stateTest_invoiceWithTwoEntriesShouldMatchItsData() {
        ProductData productData1 = Mockito.mock(ProductData.class);
        ProductData productData2 = Mockito.mock(ProductData.class);
        ProductType productType1 = ProductType.STANDARD;
        ProductType productType2 = ProductType.DRUG;
        when(productData1.getType()).thenAnswer(invocationOnMock -> productType1);
        when(productData2.getType()).thenAnswer(invocationOnMock -> productType2);
        Money money = new Money(0.0);
        int quantity1 = 0;
        int quantity2 = 1;
        RequestItem requestItem1 = new RequestItem(productData1, quantity1, money);
        RequestItem requestItem2 = new RequestItem(productData2, quantity2, money);
        ClientData clientData1 = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData1);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax1 = new Tax(money, "tax_desc_1");
        Tax tax2 = new Tax(money, "tax_desc_2");
        when(taxPolicy.calculateTax(productType1, money)).thenAnswer(invocationOnMock -> tax1);
        when(taxPolicy.calculateTax(productType2, money)).thenAnswer(invocationOnMock -> tax2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        ClientData clientData2 = invoice.getClient();
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(equalTo(2)));
        assertThat(clientData2, notNullValue());
        assertThat(clientData2, is(equalTo(clientData1)));
        InvoiceLine invoiceLine1 = items.get(0);
        assertThat(invoiceLine1.getProduct(), is(equalTo(productData1)));
        assertThat(invoiceLine1.getQuantity(), is(equalTo(quantity1)));
        assertThat(invoiceLine1.getTax(), is(equalTo(tax1)));
        InvoiceLine invoiceLine2 = items.get(1);
        assertThat(invoiceLine2.getProduct(), is(equalTo(productData2)));
        assertThat(invoiceLine2.getQuantity(), is(equalTo(quantity2)));
        assertThat(invoiceLine2.getTax(), is(equalTo(tax2)));
    }

    @Test
    public void behaviourTest_invoiceWithTwoEntriesShouldCalculateTaxTwice() {
        ProductData productData1 = Mockito.mock(ProductData.class);
        ProductData productData2 = Mockito.mock(ProductData.class);
        ProductType productType1 = ProductType.STANDARD;
        ProductType productType2 = ProductType.DRUG;
        when(productData1.getType()).thenAnswer(invocationOnMock -> productType1);
        when(productData2.getType()).thenAnswer(invocationOnMock -> productType2);
        Money money = new Money(0.0);
        int quantity1 = 0;
        int quantity2 = 1;
        RequestItem requestItem1 = new RequestItem(productData1, quantity1, money);
        RequestItem requestItem2 = new RequestItem(productData2, quantity2, money);
        ClientData clientData1 = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData1);
        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax1 = new Tax(money, "tax_desc_1");
        Tax tax2 = new Tax(money, "tax_desc_2");
        when(taxPolicy.calculateTax(productType1, money)).thenAnswer(invocationOnMock -> tax1);
        when(taxPolicy.calculateTax(productType2, money)).thenAnswer(invocationOnMock -> tax2);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice, notNullValue());
        verify(taxPolicy, times(1)).calculateTax(productType1, money);
        verify(taxPolicy, times(1)).calculateTax(productType2, money);
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
