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

    private ProductData productData = mock(ProductData.class);
    private ProductType productType = ProductType.STANDARD;
    private Money money = new Money(1);
    private ClientData clientData = new ClientData(new Id("1"), "client");
    private RequestItem requestItem = new RequestItem(productData, 1, money);
    private InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
    private BookKeeper bookKeeper;
    private TaxPolicy taxPolicy = mock(TaxPolicy.class);
    private Tax tax = new Tax(money, "tax");
    ;


    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {

        when(productData.getType()).thenReturn(productType);

        bookKeeper = new BookKeeper(new InvoiceFactory());
        invoiceRequest.add(requestItem);

        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> invoiceLines = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), is(1));
    }

    @Test
    public void shouldNotCalculateTaxForRequestWithZeroElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        bookKeeper = new BookKeeper(new InvoiceFactory());


        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        verify(taxPolicy, times(0)).calculateTax(productType, money);           //should invoke calculateTax zero times
        assertThat(items.size(), is(0));

    }
}
