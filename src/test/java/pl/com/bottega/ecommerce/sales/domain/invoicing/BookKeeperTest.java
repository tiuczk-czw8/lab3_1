package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.Matches;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders.InvoiceBuilderImpl;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.List;


public class BookKeeperTest{

    ProductData productData = mock(ProductData.class);
    ProductType productType = ProductType.STANDARD;
    Money money  = new Money(1);
    TaxPolicy taxPolicy = mock(TaxPolicy.class);
    BookKeeper bookKeeper;
    InvoiceRequest request;
    Tax tax= new Tax(money, "tax");;
    Invoice invoice;
    List<InvoiceLine> invoiceLines;


    @Test
    public  void returnOnePositionForOneItemInInvoice(){
        bookKeeper = new BookKeeper(new InvoiceFactory());
        when(productData.getType()).thenReturn(productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(1);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        request = invoiceBuilderImpl.build();

        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);
        invoice = bookKeeper.issuance( request,taxPolicy);
        invoiceLines = invoice.getItems();

        assertThat(invoice, notNullValue());
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), Matchers.equalTo(1));
    }

    @Test
    public  void returnTwoPositionForTwoItemInInvoiceTaxCalculatedTwice(){

        bookKeeper = new BookKeeper(new InvoiceFactory());
        when(productData.getType()).thenReturn(productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(2);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        request = invoiceBuilderImpl.build();

        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);
        invoice = bookKeeper.issuance( request,taxPolicy);
        invoiceLines = invoice.getItems();

        verify(taxPolicy, times(2)).calculateTax(productType, money);
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), Matchers.equalTo(2));
    }

    @Test
    public  void returnZeroPositionForZeroItemInInvoiceTaxCalculatedZeroTimes(){

        bookKeeper = new BookKeeper(new InvoiceFactory());
        when(productData.getType()).thenReturn(productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(0);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        request = invoiceBuilderImpl.build();

        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);
        invoice = bookKeeper.issuance( request,taxPolicy);
        invoiceLines = invoice.getItems();

        verify(taxPolicy, times(0)).calculateTax(productType, money);
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), Matchers.equalTo(0));
    }
    @Test
    public  void returnTenPositionsForTenItemInInvoiceTaxCalculatedTenTimes(){

        bookKeeper = new BookKeeper(new InvoiceFactory());
        when(productData.getType()).thenReturn(productType);

        InvoiceBuilderImpl invoiceBuilderImpl = new InvoiceBuilderImpl();
        invoiceBuilderImpl.setItemsQuantity(10);
        invoiceBuilderImpl.setProductData(productData);
        invoiceBuilderImpl.setMoney(money);
        request = invoiceBuilderImpl.build();

        when(taxPolicy.calculateTax(productType, money)).thenReturn(tax);
        invoice = bookKeeper.issuance( request,taxPolicy);
        invoiceLines = invoice.getItems();

        verify(taxPolicy, times(10)).calculateTax(productType, money);
        assertThat(invoiceLines, notNullValue());
        assertThat(invoiceLines.size(), Matchers.equalTo(10));
    }
}
