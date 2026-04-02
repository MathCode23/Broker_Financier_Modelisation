package broker.financier.persistance;

import broker.financier.model.actifs.Action;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonActionRepository implements ActionRepository {
    private ObjectMapper mapper = new ObjectMapper();
    private File fichier = new File("action.json");

    @Override
    public void sauvegarder(Action action) {
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.fichier, action);
    }

    @Override
    public Action charger() {
        Action action = mapper.readValue(this.fichier, Action.class);
        return action;
    }
}
