package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public interface ClientDataTestBuilderImpl {

    Id getId();

    void setId(Id id);

    String getName();

    void setName(String name);

    ClientData build();

}
