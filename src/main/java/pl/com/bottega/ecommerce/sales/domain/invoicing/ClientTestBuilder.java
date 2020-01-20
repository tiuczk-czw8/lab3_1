package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;


public interface ClientTestBuilder
{
    void setId(Id id);
    void setName(String name);
    ClientData build();
}