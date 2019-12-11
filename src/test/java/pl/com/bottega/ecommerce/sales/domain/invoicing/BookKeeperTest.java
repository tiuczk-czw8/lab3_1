package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

public class BookKeeperTest {

    @Before
    public void setup() {

    }

    @Test
    public void aaaaa() {

        InvoiceFactory mockedInvoiceFactory = mock(InvoiceFactory.class);
        ClientData mockedClientData = mock(ClientData.class);
        // when(mockedInvoiceFactory.create(mockedClientData)).thenReturn("first");

        BookKeeper bookKeeper = new BookKeeper(mockedInvoiceFactory);
        // assertThat(tails, Matchers.hasSize(HELLO.length() + 1));
    }

}
