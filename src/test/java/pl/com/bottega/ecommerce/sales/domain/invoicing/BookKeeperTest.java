package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;

public class BookKeeperTest {

    private ProductData productData = Mockito.mock(ProductData.class);
    private ProductType productType = ProductType.STANDARD;

    @Test
    public void doesReturnInvoiceWithOneEntryForOneListElement() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("0"), "p");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxValue");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> invoiceLineItems = invoice.getItems();
        assertThat(invoice, notNullValue());
        assertThat(invoiceLineItems, notNullValue());
        assertThat(invoiceLineItems.size(), is(1));
    }

    @Test
    public void doesItInvokeCalculationTaxTwiceForRequestWithTwoElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("0"), "p");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxValue");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        verify(taxPolicy, times(2)).calculateTax(productType, money);
        assertThat(items.size(), is(2));
    }

   

}