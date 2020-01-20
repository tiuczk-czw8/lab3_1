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

    private InvoiceRequest createInvoiceRequest(int quantity, Money money){
        InvoiceTestBuilder invoiceTestBuilderImp = new InvoiceTestBuilder();
        invoiceTestBuilderImp.setItemsQuantity(quantity);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        return invoiceRequest;
    }

    @Test
    public void doesReturnInvoiceWithOneEntryForOneListElement() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);
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
        InvoiceRequest invoiceRequest = createInvoiceRequest(2, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxValue");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> items = invoice.getItems();
        verify(taxPolicy, times(2)).calculateTax(productType, money);
        assertThat(items.size(), is(2));
    }

    @Test
    public void doesItNotInvokeCalculationTaxForRequestWithZeroElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxValue");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> invoiceLineItems = invoice.getItems();
        verify(taxPolicy, times(0)).calculateTax(productType, money);
        assertThat(invoiceLineItems.size(), is(0));
    }

    @Test
    public void doesItInvokeCalculationTaxTwoHundredTimesForRequestWithTwoHundredElements() {
        when(productData.getType()).thenAnswer(invocationOnMock -> productType);
        Money money = new Money(0.0);
        InvoiceRequest invoiceRequest = createInvoiceRequest(200, money);
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Tax tax = new Tax(money, "taxValue");
        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> invoiceLineItems = invoice.getItems();
        verify(taxPolicy, times(200)).calculateTax(productType, money);
        assertThat(invoiceLineItems.size(), is(200));
    }

}