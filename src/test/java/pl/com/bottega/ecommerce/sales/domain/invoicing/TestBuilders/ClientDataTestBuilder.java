package pl.com.bottega.ecommerce.sales.domain.invoicing.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public interface ClientDataTestBuilder {
    void setId(Id id);
    void setName(String name);
    ClientData build();
}
