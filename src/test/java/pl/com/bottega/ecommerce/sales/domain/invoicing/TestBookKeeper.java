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
import static org.mockito.Mockito.*;

public class TestBookKeeper {


    private ProductData productData = Mockito.mock(ProductData.class);
    private ProductType productType = ProductType.STANDARD;

    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("1"), "prod");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }

    @Test
    public void shouldNotCalculateTaxForRequestWithZeroElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(0.0);
        ClientData clientData = new ClientData(new Id("1"), "prod");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "tax");

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        verify(taxPolicy, times(0)).calculateTax(productType, money);           //should invoke calculateTax zero times
        assertThat(items.size(), is(0));

    }
}
