package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.TestBuilders;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public interface ClientDataTestBuilder {
    Id getId();

    void setId(Id id);

    String getName();

    void setName(String name);

    ClientData build();
}
