import broker.financier.enums.Devise;
import broker.financier.model.Adresse;
import broker.financier.model.actifs.Action;
import broker.financier.persistance.JsonActionRepository;
import broker.financier.persistance.JsonRepository;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    @Test
    void testChargerCasNominal() throws Exception {
        JsonRepository<Adresse> repo = new JsonRepository<>("adresse.json", Adresse.class);

        // fichier source (JSON de référence)
        Path fichierSource = chargerFichierAttendu("charger_adresse_casok.json");

        // fichier cible utilisé par le repository
        Path fichierCible = Path.of("adresse.json");

        // on copie le JSON de test dans le fichier attendu par le repository
        Files.copy(fichierSource, fichierCible, StandardCopyOption.REPLACE_EXISTING);

        // objet attendu
        Adresse adresseAttendue = new Adresse("18", "Boulevard des Horizons", "Résidence Les Cèdres, Appartement 72", "13008", "France");

        // appel méthode à tester
        Adresse adresseChargee = repo.charger();

        // vérification
        assertEquals(adresseAttendue, adresseChargee);
    }

    @Test
    void testChampNullAdresse() throws Exception {
        JsonRepository<Adresse> repo = new JsonRepository<>("adresse.json", Adresse.class);

        Adresse adresse = new Adresse("20bis", "rue Dr Rémy", "Saint-Etienne", null, "France");

        // --- Sérialisation ---
        repo.sauvegarder(adresse);

        String contenu = Files.readString(Path.of("adresse.json"));

        // Vérifie que le champ null apparaît bien dans le JSON
        assertTrue(contenu.contains("\"codePostal\" : null"));

        // --- Désérialisation ---
        Adresse chargee = repo.charger();

        assertEquals(adresse, chargee);
    }


    private Path chargerFichierAttendu(String cheminRessource) throws URISyntaxException {
        URL resource = ClassLoader.getSystemResource(cheminRessource);
        assertNotNull(resource);
        return Paths.get(resource.toURI());
    }
}
