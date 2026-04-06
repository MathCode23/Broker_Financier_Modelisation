import broker.financier.enums.Devise;
import broker.financier.model.actifs.Action;
import broker.financier.persistance.JsonActionRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

public class JsonActionRepositoryTest {
    @Test
    void testSauvegarderCasNominalNaif() {
        JsonActionRepository repo = new JsonActionRepository();
        Action action = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);

        repo.sauvegarder(action);

        try {
            String contenu = Files.readString(Path.of("action.json"));

            assertTrue(contenu.contains("\"ticker\" : \"AIR\""));
            assertTrue(contenu.contains("\"nom\" : \"Airbus\""));
            assertTrue(contenu.contains("\"prixCourant\" : 145.2"));
        }
        catch (IOException e) {
            fail("Erreur inattendue lors de la lecture du fichier JSON : " + e.getMessage());
        }
    }

    @Test
    void testSauvegarderCasNominalRobuste() throws Exception {
        JsonActionRepository repo = new JsonActionRepository();
        Action action = new Action("AAPL", "Apple", 190.0, "USA", Devise.USD);

        Path fichierAttendu = chargerFichierAttendu("sauvegarder_action_casok.json");

        repo.sauvegarder(action);
        Path fichierGenere = Path.of("action.json");

        assertTrue(Files.exists(fichierAttendu), "Fichier attendu manquant");
        assertTrue(Files.exists(fichierGenere), "Fichier généré manquant");
        assertEquals(-1L, Files.mismatch(fichierAttendu, fichierGenere));
    }

    @Test
    void testChargerCasNominalNaif() {
        String json = """
        {
          "type" : "ACTION",
          "ticker": "AIR",
          "nom": "Airbus",
          "prixCourant": 145.20,
          "pays": "France",
          "devise": "EUR"
        }
        """;

        try {
            Files.writeString(Path.of("action.json"), json);
        }
        catch (IOException e) {
            fail("Erreur inattendue lors de l'écriture dans le fichier JSON : " + e.getMessage());
        }

        JsonActionRepository repo = new JsonActionRepository();
        Action action = repo.charger();

        assertEquals("AIR", action.getTicker());
        assertEquals("Airbus", action.getNom());
        assertEquals(145.20, action.getPrixCourant());
        assertEquals("France", action.getPays());
        assertEquals(Devise.EUR, action.getDevise());
    }

    @Test
    void testChargerCasNominalRobuste() throws Exception {
        JsonActionRepository repository = new JsonActionRepository();

        // fichier source (JSON de référence)
        Path fichierSource = chargerFichierAttendu("charger_action_casok.json");

        // fichier cible utilisé par le repository
        Path fichierCible = Path.of("action.json");

        // on copie le JSON de test dans le fichier attendu par le repository
        Files.copy(fichierSource, fichierCible, StandardCopyOption.REPLACE_EXISTING);

        // objet attendu
        Action actionAttendue = new Action("AAPL", "Apple", 190.0, "USA", Devise.USD);

        // appel méthode à tester
        Action actionChargee = repository.charger();

        // vérification
        assertEquals(actionAttendue, actionChargee);
    }

    private Path chargerFichierAttendu(String cheminRessource) throws URISyntaxException {
        URL resource = ClassLoader.getSystemResource(cheminRessource);
        assertNotNull(resource);
        return Paths.get(resource.toURI());
    }
}
