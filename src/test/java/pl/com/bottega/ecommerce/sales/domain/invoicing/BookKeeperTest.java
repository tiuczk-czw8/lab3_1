package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.List;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import static org.junit.Assert.*;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


public class BookKeeperTest {

	@Test
	public void requestOneInvoiceWithOnePosition() {
		ProductData productData = Mockito.mock(ProductData.class);
		ProductType productType = ProductType.DRUG;
		when(productData.getType()).thenAnswer(invocationOnMock -> {
			return productType;
		});
		 Money money = new Money(0.0);
		 RequestItem requestItem = new RequestItem(productData, 0, money);
		 ClientData clientData = new ClientData(new Id("001"), "prod001");
		 InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		 InvoiceRequest.add(requestItem);
		 BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		 TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		 Tax tax = new Tax(money, "tax_desc");
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
	public void CheckInvoiceWithTreePositionsMatchWithData() {
		ProductData productData1 = Mockito.mock(ProductData.class);
		ProductData productData2 = Mockito.mock(ProductData.class);
		ProductData productData3 = Mockito.mock(ProductData.class);
		ProductType productType1 = ProductType.FOOD;
		ProductType productType2 = ProductType.STANDARD;
		ProductType productType3 = ProductType.DRUG;
		when(productData1.getType()).thenAnswer(invocationOnMock -> {
			return productType1;
		});
		when(productData2.getType()).thenAnswer(invocationOnMock -> {
			return productType2;
		});
		when(productData3.getType()).thenAnswer(invocationOnMock -> {
			return productType3;
		});
		Money money1 = new Money(0.0);
		Money money2 = new Money(1.0);
		Money money3 = new Money(2.0);
		int Quantity1 = 1;
		int Quantity2 = 4;
		int Quantity3 = 6;
		RequestItem requestItem1 = new RequestItem(productData1, Quantity1, money1);
		RequestItem requestItem2 = new RequestItem(productData2, Quantity2, money2);
		RequestItem requestItem3 = new RequestItem(productData3, Quantity3, money3);
		ClientData clientData = new ClientData(new Id("001"), "prod001");
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		InvoiceRequest.add(requestItem1);
		InvoiceRequest.add(requestItem2);
		InvoiceRequest.add(requestItem3);
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax1 = new Tax(money1, "tax_desc1");
		Tax tax2 = new Tax(money2, "tax_desc2");
		Tax tax3 = new Tax(money3, "tax_desc3");
		when(taxPolicy.calculateTax(productType1,money1)).thenAnswer(invocationOnMock -> {
			return tax1;
		});
		when(taxPolicy.calculateTax(productType2,money2)).thenAnswer(invocationOnMock -> {
			return tax2;
		});
		when(taxPolicy.calculateTax(productType3,money3)).thenAnswer(invocationOnMock -> {
			return tax3;
		});

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		assertThat(invoice, notNullValue());
		assertThat(items, notNullValue());
		assertThat(items.size(),is(3));
		assertThat(invoice.getClient(), is(equalTo(clientData)));
		InvoiceLine invoiceLine1 = items.get(0);
		InvoiceLine invoiceLine2 = items.get(1);
		InvoiceLine invoiceLine3 = items.get(2);
		assertThat(invoiceLine1.getTax(), is(equalTo(tax1)));
		assertThat(invoiceLine2.getTax(), is(equalTo(tax2)));
		assertThat(invoiceLine3.getTax(), is(equalTo(tax3)));
		assertThat(invoiceLine1.getQuantity(), is(equalTo(Quantity1)));
		assertThat(invoiceLine2.getQuantity(), is(equalTo(Quantity2)));
		assertThat(invoiceLine3.getQuantity(), is(equalTo(Quantity3)));
		assertThat(invoiceLine1.getProduct(), is(equalTo(productData1)));
		assertThat(invoiceLine2.getProduct(), is(equalTo(productData2)));
		assertThat(invoiceLine3.getProduct(), is(equalTo(productData3)));

	}

	@Test
	public void requestInvoiceWithTwoPositionCalculateTaxTwice() {
		ProductData productData1 = Mockito.mock(ProductData.class);
		ProductData productData2 = Mockito.mock(ProductData.class);
		ProductType productType1 = ProductType.STANDARD;
		ProductType productType2 = ProductType.DRUG;
		when(productData1.getType()).thenAnswer(invocationOnMock -> {
			return productType1;
		});
		when(productData2.getType()).thenAnswer(invocationOnMock -> {
			return productType2;
		});
		Money money1 = new Money(0.0);
		Money money2 = new Money(3.0);
		RequestItem requestItem1 = new RequestItem(productData1, 1, money1);
		RequestItem requestItem2 = new RequestItem(productData2, 3, money2);
		ClientData clientData = new ClientData(new Id("001"), "prod001");
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		InvoiceRequest.add(requestItem1);
		InvoiceRequest.add(requestItem2);
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		Tax tax1 = new Tax(money1, "tax_desc_1");
		Tax tax2 = new Tax(money2, "tax_desc_2");
		when(taxPolicy.calculateTax(productType1,money1)).thenAnswer(invocationOnMock -> {
			return tax1;
		});
		when(taxPolicy.calculateTax(productType2,money2)).thenAnswer(invocationOnMock -> {
			return tax2;
		});

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		List<InvoiceLine> items = invoice.getItems();

		assertThat(invoice, notNullValue());
		assertThat(items.size(),is(2));
		InvoiceLine invoiceLine1 = items.get(0);
		InvoiceLine invoiceLine2 = items.get(1);
		assertThat(invoiceLine1.getTax(), is(equalTo(tax1)));
		assertThat(invoiceLine2.getTax(), is(equalTo(tax2)));
		verify(taxPolicy, times(1)).calculateTax(productType1,money1);
		verify(taxPolicy, times(1)).calculateTax(productType2,money2);
		verify(taxPolicy, times(2));
	}

}
//izolacja od taxpolicy