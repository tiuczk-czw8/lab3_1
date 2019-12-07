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


public class BookKeeperTest {

	@Test
	public void requestOneInvoiceWithOnePosition() {
		 ProductData productData = Mockito.mock(ProductData.class);
		 ProductType productType = ProductType.STANDARD;
		 Money money = new Money(0.0);
		 RequestItem requestItem = new RequestItem(productData, 0, money);
		 ClientData clientData = new ClientData(new Id("001"), "prod001");
		 InvoiceRequest inoviceRequest = new InvoiceRequest(clientData);
		 InvoiceRequest.add(requestItem);
		 BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		 TaxPolicy  taxPolicy = Mockito.mock(TaxPolicy.class);
		 Tax tax = new Tax(money, "tax_desc");

		 Invoice invoice = bookKeeper.issuance(inoviceRequest, taxPolicy);
		 List<InvoiceLine> items = invoice.getItems();

		 assertThat(invoice, notNullValue());
		 assertThat(items, notNullValue());
		 assertThat(items.size(),is(1));
	}

}
//izolacja od taxpolicy