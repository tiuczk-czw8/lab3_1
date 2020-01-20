package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.TestBuilders;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class ClientDataTestBuilderImpl implements ClientDataTestBuilder {
    private Id id;

    private String name;

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ClientData build() {
        return new ClientData(id, name);
    }
}