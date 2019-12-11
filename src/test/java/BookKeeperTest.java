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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookKeeperTest {
    private ProductData productData;
    private ProductType productType = ProductType.STANDARD;
    private TaxPolicy taxPolicy;

    @Before
    public void mockClass() {
        productData = mock(ProductData.class);
        taxPolicy = mock(TaxPolicy.class);
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
    }

    @Test
    public void shouldReturnInvoiceWithOneEntryForOneListElement() {
        Money money = new Money(0.0);
        RequestItem requestItem = new RequestItem(productData, 0, money);
        ClientData clientData = new ClientData(new Id("1"), "prod");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        Tax tax = new Tax(money, "tax");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> itemList = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(itemList, notNullValue());
        assertThat(itemList.size(), is(1));
    }
}
