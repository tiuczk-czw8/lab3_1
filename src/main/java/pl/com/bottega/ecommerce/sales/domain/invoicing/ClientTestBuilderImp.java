package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public interface ClientTestBuilderImp {

    void setName(String name);

    String getName();

    void setId(Id id);

    Id getId();

    ClientData build();
}
