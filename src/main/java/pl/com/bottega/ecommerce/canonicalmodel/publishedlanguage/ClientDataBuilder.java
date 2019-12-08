package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage;

public class ClientDataBuilder {
    private Id id;
    private String name;

    public ClientDataBuilder setId(Id id) {
        this.id = id;
        return this;
    }

    public ClientDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ClientData createClientData() {
        return new ClientData(id, name);
    }
}