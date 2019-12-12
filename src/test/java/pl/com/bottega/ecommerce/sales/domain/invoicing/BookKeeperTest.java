package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceTestBuilderImplementation;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    ProductData productData;
    ProductType productType;
    TaxPolicy taxPolicy;
    Money money;
    Tax tax;

    private InvoiceRequest createInvoiceRequest(int quantity, Money money){
        InvoiceTestBuilderImplementation invoiceTestBuilderImp = new InvoiceTestBuilderImplementation();
        invoiceTestBuilderImp.setItemsQuantity(quantity);
        invoiceTestBuilderImp.setProductData(productData);
        invoiceTestBuilderImp.setMoney(money);
        InvoiceRequest invoiceRequest = invoiceTestBuilderImp.build();
        return invoiceRequest;
    }

    @Before
    public void tearsUp() {
        productData = Mockito.mock(ProductData.class);
        productType = ProductType.DRUG;
        taxPolicy = Mockito.mock(TaxPolicy.class);
        money = new Money(1, Money.DEFAULT_CURRENCY);
        tax = new Tax(money, "TAX");
    }

    @Test
    public void forSignleElementShouldReturnInvoiceWithOneElement() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(1));
    }

    @Test
    public void shouldReturnInvoiceWithZeroElement() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(0));
    }

    @Test
    public void shouldReturnInvoiceWithMultipleElements() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(20, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(20));
    }

    @Test
    public void shouldCallTaxMethodTwoTimes() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(2, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(2)).calculateTax(productType,money);
    }

    @Test
    public void shouldCallTaxMethodZeroTimes() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(0, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(0)).calculateTax(productType,money);
    }

    @Test
    public void shouldCallTaxMethodMultipleTimes() {
        when(productData.getType()).thenReturn(productType);

        InvoiceRequest invoiceRequest = createInvoiceRequest(20, money);

        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

        when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy,times(20)).calculateTax(productType,money);
    }
}
