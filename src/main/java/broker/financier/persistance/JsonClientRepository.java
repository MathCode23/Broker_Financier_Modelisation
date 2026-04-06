package broker.financier.persistance;

import broker.financier.model.Client;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonClientRepository implements ClientRepository {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File fichier = new File("client.json");

    @Override
    public void sauvegarder(Client client) {
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.fichier, client);
    }

    @Override
    public Client charger() {
        return mapper.readValue(this.fichier, Client.class);
    }
}
