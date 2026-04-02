package broker.financier.persistance;

import broker.financier.model.actifs.Action;

public interface ActionRepository {
    void sauvegarder(Action action);
    Action charger();
}
