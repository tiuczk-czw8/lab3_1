package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    @Test
    public void stateTest_shouldReturnInvoiceWithOneEntry() {
        ProductData productData = Mockito.mock(ProductData.class);
        ProductType productType = ProductType.STANDARD;
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax_desc");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void stateTest_invoiceWithOneEntryShouldShouldMatchClientData() {
        ProductData productData = Mockito.mock(ProductData.class);
        ProductType productType = ProductType.STANDARD;
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax_desc");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        ClientData resultClientData = resultInvoice.getClient();

        assertThat(resultInvoice, notNullValue());
        assertThat(resultClientData, notNullValue());
        assertThat(resultClientData, is(equalTo(clientData)));
    }

    @Test
    public void stateTest_EntryDataShouldMatchForInvoiceWithOneEntry() {
        ProductData productData = Mockito.mock(ProductData.class);
        ProductType productType = ProductType.STANDARD;
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        int quantity = 0;
        RequestItem requestItem = new RequestItem(productData, quantity, money);
        ClientData clientData = new ClientData(new Id("001"), "prod001");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax_desc");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        ClientData resultClientData = resultInvoice.getClient();
        List<InvoiceLine> resultItems = resultInvoice.getItems();

        assertThat(resultInvoice, notNullValue());
        assertThat(resultClientData, notNullValue());
        assertThat(resultItems, notNullValue());
        assertThat(resultClientData, is(equalTo(clientData)));
        assertThat(resultItems.size(), is(equalTo(1)));
        InvoiceLine invoiceLine = resultItems.get(0);
        assertThat(invoiceLine.getProduct(), is(equalTo(productData)));
        assertThat(invoiceLine.getQuantity(), is(equalTo(quantity)));
        assertThat(invoiceLine.getTax(), is(equalTo(tax)));
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

}
