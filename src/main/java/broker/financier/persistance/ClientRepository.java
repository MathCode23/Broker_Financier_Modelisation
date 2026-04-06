package broker.financier.persistance;

import broker.financier.model.Client;

public interface ClientRepository {
    void sauvegarder(Client client);
    Client charger();
}
