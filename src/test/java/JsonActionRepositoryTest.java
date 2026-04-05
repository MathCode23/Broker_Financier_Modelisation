import broker.financier.enums.Devise;
import broker.financier.model.actifs.Action;
import broker.financier.persistance.JsonActionRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    void testSauvegarderCasNominalRobuste() throws IOException {
        JsonActionRepository repo = new JsonActionRepository();
        Action action = new Action("AAPL", "Apple", 190.0, "USA", Devise.USD);

        Path racine = Path.of(System.getProperty("user.dir"));
        Path fichierAttendu = racine.resolve("action_test.json");

        repo.sauvegarder(action);
        Path fichierGenere = Path.of("action.json");

        assertTrue(Files.exists(fichierAttendu), "Fichier attendu manquant");
        assertTrue(Files.exists(fichierGenere), "Fichier généré manquant");
        assertEquals(-1L, Files.mismatch(fichierAttendu, fichierGenere));
    }
}
