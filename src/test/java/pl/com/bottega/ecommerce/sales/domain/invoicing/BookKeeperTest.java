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
import static org.mockito.Mockito.*;

public class BookKeeperTest
{

    private ProductData productData = Mockito.mock(ProductData.class);
    private ProductType productType = ProductType.STANDARD;
    private InvoiceRequest createInvoiceRequest(int quantity, Money money){
        InvoiceTestBuilderImp invoiceTestBuilderImp = new InvoiceTestBuilderImp();
        invoiceTestBuilderImp.setItemsQuantity(quantity);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        return invoiceRequest;
    }

    @Test
    public void InvoiceWithOneEntryForOneListElement()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(1.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxation");

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
    }
    @Test
    public void InvokeCalculateTaxTwoTimesForTwoEntries()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);

        Money money = new Money(1.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(2, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxation");

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        verify(taxPolicy, times(2)).calculateTax(productType, money);
        assertThat(items.size(), is(2));
    }
    @Test
    public void NotInvokeCalculateTaxForRequestWithNoElements()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxation");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        verify(taxPolicy, times(0)).calculateTax(productType, money);
        assertThat(items.size(), is(0));
    }
    @Test
    public void InvokeCalculateTaxThousandTimesForRequestWithThousandElements()
    {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(1000, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxation");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();

        verify(taxPolicy, times(1000)).calculateTax(productType, money);
        assertThat(items.size(), is(1000));
    }
}