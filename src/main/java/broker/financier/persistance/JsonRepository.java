package broker.financier.persistance;

import tools.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonRepository<E> implements Repository<E> {
    private ObjectMapper mapper = new ObjectMapper();
    private final File fichier;
    private final Class<E> type;

    public JsonRepository(String nomFichier, Class<E> type) {
        this.fichier = new File(nomFichier);
        this.type = type;
    }

    @Override
    public void sauvegarder(E pojo) {
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.fichier, pojo);
    }

    @Override
    public E charger() {
         return mapper.readValue(this.fichier, this.type);
    }
}
