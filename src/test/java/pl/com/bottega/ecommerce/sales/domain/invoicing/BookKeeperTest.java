
package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BookKeeperTest {

    @Test
    public void OneInvoiceOnePosition() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        RequestItem requestItem = new RequestItem(productData, 1, new Money(1));
        invoiceRequest.add(requestItem);
        Invoice returnInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertTrue(returnInvoice.getItems().size() == 1);
    }

    @Test
    public void TwoInvoicesCalculateTax() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        RequestItem requestItem = new RequestItem(productData, 1, new Money(1));
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        Invoice returnInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertTrue(calculateTaxCount == 2);
    }

    @Test
    public void InvoiceWithoutItems() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        Invoice returnInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertTrue(returnInvoice.getItems().isEmpty());
    }

    @Test
    public void InvoiceWithoutItemsCalculateTax() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        Invoice returnInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertTrue((calculateTaxCount == 0));
    }

    private TaxPolicy taxPolicy = new TaxPolicy() {
        @Override
        public Tax calculateTax(ProductType productType, Money net) {
            calculateTaxCount++;
            return new Tax(new Money(1), "");
        }
    };

    private int calculateTaxCount = 0;
    private BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
    @Mock private ClientData clientData;
    @Mock private ProductData productData;

}