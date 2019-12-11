package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    private ProductData productData;
    private TaxPolicy taxPolicy;
    private ProductType productType;
    private Tax tax;
    private Money money;
    private ClientData clientData;

    @Before
    public void init() {
        productData = mock(ProductData.class);
        taxPolicy = mock(TaxPolicy.class);
        productType = ProductType.FOOD;
        money = new Money(10);
        tax = new Tax(money, "50%");
        clientData = new ClientData(new Id("1"), "Client");
    }

    @Test
    public void invoiceRequestsOneItemReturnsInvoiceWithOneItem() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        RequestItem requestItem = new RequestItem(productData, 1, money);

        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> elements = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(1));
    }
}
