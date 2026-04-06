import broker.financier.model.Adresse;
import broker.financier.persistance.JsonRepository;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JsonAdresseRepositoryTest {
    @Test
    void testSauvegarderCasNominal() throws Exception {
        JsonRepository<Adresse> repo = new JsonRepository<>("adresse.json", Adresse.class);
        Adresse adresse = new Adresse("5", "Rue des Fleurs", "Appartement 333", "42000", "France");

        Path fichierAttendu = chargerFichierAttendu("sauvegarder_adresse_casok.json");

        repo.sauvegarder(adresse);
        Path fichierGenere = Path.of("adresse.json");

        assertTrue(Files.exists(fichierAttendu), "Fichier attendu manquant");
        assertTrue(Files.exists(fichierGenere), "Fichier généré manquant");
        assertEquals(-1L, Files.mismatch(fichierAttendu, fichierGenere));
    }

    private Path chargerFichierAttendu(String cheminRessource) throws URISyntaxException {
        URL resource = ClassLoader.getSystemResource(cheminRessource);
        assertNotNull(resource);
        return Paths.get(resource.toURI());
    }
}
