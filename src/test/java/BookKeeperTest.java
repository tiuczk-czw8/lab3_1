import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {
    private ProductData productData;
    private ProductType productType = ProductType.STANDARD;
    private TaxPolicy taxPolicy;
    private Money money;
    private RequestItem requestItem;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;
    private BookKeeper bookKeeper;
    private Tax tax;

    @Before
    public void mockClass() {
        productData = mock(ProductData.class);
        taxPolicy = mock(TaxPolicy.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
    }

    private void initData() {
        money = new Money(0.0);
        requestItem = new RequestItem(productData, 0, money);
        clientData = new ClientData(new Id("1"), "prod");
        invoiceRequest = new InvoiceRequest(clientData);
        bookKeeper = new BookKeeper(new InvoiceFactory());
        tax = new Tax(money, "tax");
    }

    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        initData();
        invoiceRequest.add(requestItem);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> itemList = invoice.getItems();
        assertThat(invoice, notNullValue());
        assertThat(itemList, notNullValue());
        assertThat(itemList.size(), is(1));
    }

    @Test
    public void shouldInvokeCalculateTaxTwoTimesForTwoEntries() {
        initData();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        assertThat(items.size(), is(2));
        verify(taxPolicy, times(2)).calculateTax(productType, money);           //should invoke calculateTax two times
    }
}
