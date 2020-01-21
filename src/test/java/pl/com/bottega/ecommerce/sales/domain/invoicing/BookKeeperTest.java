package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


public class BookKeeperTest {

	private ProductData productData = Mockito.mock(ProductData.class);
	private ProductType productType = ProductType.STANDARD;
	private InvoiceRequest createInvoiceRequest(int quantity, Money money){
		InvoiceReqTestBuilder invoiceTestBuilder = new InvoiceReqTestBuilder();
		invoiceTestBuilder.setItemsQuantity(quantity);
		invoiceTestBuilder.setProductData(productData);
		invoiceTestBuilder.setMoney(money);
		InvoiceRequest invoiceRequest = invoiceTestBuilder.build();
		return invoiceRequest;
	}

	@Test
	public void requestOneInvoiceWithOnePosition() {
		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});

		Money money = new Money(1.0);
		InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax = new Tax(money, "taxation");
		when(taxPolicy.calculateTax(productType,money)).thenAnswer(invocationOnMock -> {
		 	return tax;
		 });

		 Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		 List<InvoiceLine> items = invoice.getItems();

		 assertThat(invoice, notNullValue());
		 assertThat(items, notNullValue());
		 assertThat(items.size(),is(1));
	}

	@Test
	public void checkRequestInvoiceWithOnePositionWithClientData() {
		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});
		Money money = new Money(2.0);
		InvoiceRequest invoiceRequest = createInvoiceRequest(1, money);

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax = new Tax(money, "tax");
		when(taxPolicy.calculateTax(productType,money)).thenAnswer(invocationOnMock -> {
			return tax;
		});

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		assertThat(invoice, notNullValue());
		assertThat(items, notNullValue());
		assertThat(items.size(),is(1));
	}

	@Test
	public void CheckInvoiceWith100PositionsCalculateTax100times() {
		when(productData.getType()).thenAnswer(invocationOnMock -> productType);
		Money money = new Money(0.0);
		InvoiceRequest invoiceRequest = createInvoiceRequest(100, money);
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax = new Tax(money, "taxation");
		when(taxPolicy.calculateTax(productType, money)).thenAnswer(invocationOnMock -> tax);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		verify(taxPolicy, times(100)).calculateTax(productType, money);
		assertThat(items.size(), is(100));
	}


	@Test
	public void requestInvoiceWithTwoPositionCalculateTaxTwice() {

		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});

		Money money = new Money(3.0);

		InvoiceRequest invoiceRequest = createInvoiceRequest(2, money);

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax = new Tax(money, "tax1");

		when(taxPolicy.calculateTax(productType,money)).thenAnswer(invocationOnMock -> {
			return tax;
		});


		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		verify(taxPolicy, times(1)).calculateTax(productType,money);
		verify(taxPolicy, times(2));
	}

	@Test
	public void InvoiceWithTwoPositionReturnOneProductType() {

		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});
		Money money1 = new Money(1.0);
		InvoiceRequest invoiceRequest = createInvoiceRequest(2, money1);
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax1 = new Tax(money1, "tax1");
		when(taxPolicy.calculateTax(productType,money1)).thenAnswer(invocationOnMock -> {
			return tax1;
		});

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(invoice, notNullValue());
		verify(productData, times(1)).getType();
		verify(productData,times(1));
	}

	@Test
	public void InvoiceWithDifferentProductType() {
		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});
		ProductData productData2 = Mockito.mock(ProductData.class);
		ProductType productType2 = ProductType.STANDARD;
		when(productData2.getType()).thenAnswer(invocationOnMock -> {
			return productType2;
		});
		ProductData productData3 = Mockito.mock(ProductData.class);
		ProductType productType3 = ProductType.FOOD;
		when(productData3.getType()).thenAnswer(invocationOnMock -> {
			return productType3;
		});

		Money money1 = new Money(1.0);
		Money money2 = new Money(3.0);
		Money money3 = new Money(4.0);

		InvoiceRequest invoiceRequest1 = createInvoiceRequest(1000, money1);
		InvoiceRequest invoiceRequest2 = createInvoiceRequest(1000, money2);
		InvoiceRequest invoiceRequest3 = createInvoiceRequest(1000, money3);



		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax1 = new Tax(money1, "tax1");
		Tax tax2 = new Tax(money2, "tax2");
		Tax tax3 = new Tax(money3, "tax3");
		when(taxPolicy.calculateTax(productType,money1)).thenAnswer(invocationOnMock -> {
			return tax1;
		});
		when(taxPolicy.calculateTax(productType2,money2)).thenAnswer(invocationOnMock -> {
			return tax2;
		});
		when(taxPolicy.calculateTax(productType3,money3)).thenAnswer(invocationOnMock -> {
			return tax3;
		});

		Invoice invoice = bookKeeper.issuance(invoiceRequest1, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		assertThat(invoice, notNullValue());
		assertThat(items.size(), is(3));
		verify(productData, times(1)).getType();
		verify(productData2, times(1)).getType();
		verify(productData3, times(1)).getType();
		verify(productData, times(1));
		verify(productData2, times(1));
		verify(productData3, times(1));

	}



}
