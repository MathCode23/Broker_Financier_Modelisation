package broker.financier.persistance;

import broker.financier.model.actifs.Action;

public class ActionRepositoryImpl extends JsonRepository<Action> {
    public ActionRepositoryImpl() {
        super("action.json", Action.class);
    }
}
