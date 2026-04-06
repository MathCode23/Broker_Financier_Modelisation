import broker.financier.BrokerDataFactory;
import broker.financier.enums.Devise;
import broker.financier.enums.TypeDistribution;
import broker.financier.enums.TypeOrdre;
import broker.financier.enums.TypeReplication;
import broker.financier.model.Adresse;
import broker.financier.model.Client;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.achatvente.Transaction;
import broker.financier.model.actifs.Action;
import broker.financier.model.actifs.ETF;
import broker.financier.model.actifs.Obligation;
import broker.financier.model.enveloppes.CTO;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;
import broker.financier.persistance.JsonActionRepository;
import broker.financier.persistance.JsonClientRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static broker.financier.BrokerDataFactory.alimenterPositionsClient;
import static broker.financier.BrokerDataFactory.creerPosition;
import static org.junit.jupiter.api.Assertions.*;

public class JsonClientRepositoryTest {
    @Test
    public void testSauvegarderCasNominalNaif() {
        // ============== Création du client ==============
        Client client = new Client(42, "Durand", "Alice");
        // Adresse
        Adresse adresse = new Adresse("12", "Avenue des Lumières", "Bâtiment B", "69000", "France");
        client.ajouterAdresse(adresse);
        // Actifs
        Action action = new Action("MSFT", "Microsoft", 320.0, "USA", Devise.USD);
        Obligation obligation = new Obligation("FR10Y", "Obligation France 10 ans", 98.5, "France", Devise.EUR, 3.2);
        ETF etf = new ETF("WORLD", "Amundi MSCI World", 450.0, "France", Devise.EUR, 0.12, 5000.0, TypeDistribution.CAPITALISANT, TypeReplication.SYNTHETIQUE);
        // Positions
        Position posAction = new Position(10, action, 300.0);
        Position posObligation = new Position(5, obligation, 450.0);
        Position posETF = new Position(2, etf,900.0);
        // Transactions
        Transaction t1 = new Transaction("MSFT", 320.0, 3200.0, LocalDateTime.now());
        Transaction t2 = new Transaction("WORLD", -450.0, -900.0, LocalDateTime.now());
        // Enveloppe
        CTO cto = new CTO("CTO-001", LocalDateTime.now(), 0.0);
        cto.ajouterPosition(posAction);
        cto.ajouterPosition(posObligation);
        cto.ajouterPosition(posETF);
        cto.ajouterTransaction(t1);
        cto.ajouterTransaction(t2);
        client.ajouterEnveloppe(cto);

        JsonClientRepository repo = new JsonClientRepository();
        repo.sauvegarder(client);

        try {
            String contenu = Files.readString(Path.of("client.json"));

            assertTrue(contenu.contains("\"numeroRue\" : \"12\""));
            assertTrue(contenu.contains("\"rue\" : \"Avenue des Lumières\""));
            assertTrue(contenu.contains("\"codePostal\" : \"69000\""));
            assertTrue(contenu.contains("\"pays\" : \"France\""));

            // MSFT
            assertTrue(contenu.contains("\"ticker\" : \"MSFT\""));
            assertTrue(contenu.contains("\"nom\" : \"Microsoft\""));
            assertTrue(contenu.contains("\"prixCourant\" : 320.0"));
            assertTrue(contenu.contains("\"devise\" : \"USD\""));

            // FR10Y
            assertTrue(contenu.contains("\"ticker\" : \"FR10Y\""));
            assertTrue(contenu.contains("\"nom\" : \"Obligation France 10 ans\""));
            assertTrue(contenu.contains("\"tauxInteret\" : 3.2"));

            // WORLD
            assertTrue(contenu.contains("\"ticker\" : \"WORLD\""));
            assertTrue(contenu.contains("\"nom\" : \"Amundi MSCI World\""));
            assertTrue(contenu.contains("\"pourcentageFrais\" : 5000.0")); // ✔ correspond à TON JSON
            assertTrue(contenu.contains("\"tailleFondsM\" : 0.12"));        // ✔ correspond à TON JSON

            // Quantités
            assertTrue(contenu.contains("\"quantite\" : 10.0")); // MSFT
            assertTrue(contenu.contains("\"quantite\" : 2.0"));  // WORLD
            assertTrue(contenu.contains("\"quantite\" : 5.0"));  // FR10Y

            // Transactions
            assertTrue(contenu.contains("\"tickerActif\" : \"MSFT\""));
            assertTrue(contenu.contains("\"montantTotal\" : 3200.0"));

            assertTrue(contenu.contains("\"tickerActif\" : \"WORLD\""));
            assertTrue(contenu.contains("\"montantTotal\" : -900.0"));

            // Enveloppe
            assertTrue(contenu.contains("\"type\" : \"CTO\""));
            assertTrue(contenu.contains("\"numero\" : \"CTO-001\""));

            // Client
            assertTrue(contenu.contains("\"id\" : 42"));
            assertTrue(contenu.contains("\"nom\" : \"Durand\""));
            assertTrue(contenu.contains("\"prenom\" : \"Alice\""));
        }
        catch (IOException e) {
            fail("Erreur inattendue lors de la lecture du fichier JSON : " + e.getMessage());
        }
    }

    @Test
    void testChargerCasNominalNaif() {
        String json = """
                {
                  "id" : 42,
                  "nom" : "Durand",
                  "prenom" : "Alice",
                  "adresses" : [ {
                    "numeroRue" : "12",
                    "rue" : "Avenue des Lumières",
                    "complement" : "Bâtiment B",
                    "codePostal" : "69000",
                    "pays" : "France"
                  } ],
                  "enveloppes" : [ {
                    "type" : "CTO",
                    "numero" : "CTO-001",
                    "dateOuverte" : "2026-04-06T10:00:00",
                    "fraisDepot" : 0.0,
                    "soldeEspeces" : 5000.0,
                    "positions" : {
                      "MSFT" : {
                        "actif" : {
                          "type" : "ACTION",
                          "ticker" : "MSFT",
                          "nom" : "Microsoft",
                          "prixCourant" : 320.0,
                          "pays" : "USA",
                          "devise" : "USD"
                        },
                        "quantite" : 10.0,
                        "prixAchatUnitaire" : 300.0,
                        "plusValueLatente" : 200.0,
                        "valeurCourante" : 3200.0
                      },
                      "FR10Y" : {
                        "actif" : {
                          "type" : "OBLIGATION",
                          "ticker" : "FR10Y",
                          "nom" : "Obligation France 10 ans",
                          "prixCourant" : 98.5,
                          "pays" : "France",
                          "devise" : "EUR",
                          "tauxInteret" : 3.2
                        },
                        "quantite" : 5.0,
                        "prixAchatUnitaire" : 450.0,
                        "plusValueLatente" : -50.0,
                        "valeurCourante" : 492.5
                      },
                      "WORLD" : {
                        "actif" : {
                          "type" : "ETF",
                          "ticker" : "WORLD",
                          "nom" : "Amundi MSCI World",
                          "prixCourant" : 450.0,
                          "pays" : "France",
                          "devise" : "EUR",
                          "pourcentageFrais" : 0.12,
                          "tailleFondsM" : 5000.0,
                          "typeDistribution" : "CAPITALISANT",
                          "typeReplication" : "SYNTHETIQUE"
                        },
                        "quantite" : 2.0,
                        "prixAchatUnitaire" : 900.0,
                        "plusValueLatente" : 0.0,
                        "valeurCourante" : 900.0
                      }
                    },
                    "transactions" : [ {
                      "tickerActif" : "MSFT",
                      "prixExecution" : 320.0,
                      "montantTotal" : 3200.0,
                      "dateExecution" : "2026-04-06T10:00:00"
                    }, {
                      "tickerActif" : "WORLD",
                      "prixExecution" : -450.0,
                      "montantTotal" : -900.0,
                      "dateExecution" : "2026-04-06T10:00:00"
                    } ]
                  } ]
                }
                
                """;

        try {
            Files.writeString(Path.of("client.json"), json);
        } catch (IOException e) {
            fail("Erreur inattendue lors de l'écriture du fichier JSON : " + e.getMessage());
        }

        JsonClientRepository repo = new JsonClientRepository();
        Client client = repo.charger();

        // Vérification des champs principaux
        assertEquals(42, client.getId());
        assertEquals("Durand", client.getNom());
        assertEquals("Alice", client.getPrenom());

        // Vérification adresse
        Adresse adr = client.getAdresses().iterator().next();
        assertEquals("12", adr.getNumeroRue());
        assertEquals("Avenue des Lumières", adr.getRue());
        assertEquals("Bâtiment B", adr.getComplement());
        assertEquals("69000", adr.getCodePostal());
        assertEquals("France", adr.getPays());

        // Vérification enveloppe
        Enveloppe env = client.getEnveloppes().get(0);
        assertTrue(env instanceof CTO);
        assertEquals("CTO-001", env.getNumero());

        // Vérification positions
        CTO cto = (CTO) env;
        assertTrue(cto.getPositions().containsKey("MSFT"));
        assertTrue(cto.getPositions().containsKey("FR10Y"));
        assertTrue(cto.getPositions().containsKey("WORLD"));

        // Vérification transactions
        assertEquals(2, cto.getTransactions().size());
        assertEquals("MSFT", cto.getTransactions().get(0).getTickerActif());
        assertEquals("WORLD", cto.getTransactions().get(1).getTickerActif());
    }

    private Client makeDummyClient() {
        Enveloppe pea = new PEA("PEA-AL-001", LocalDateTime.now().minusYears(2), 18000.0);
        Enveloppe cto = new CTO("CTO-AL-002", LocalDateTime.now().minusYears(1), 12000.0);

        Position pAapl = creerPosition(new Action("AAPL", "Apple Inc.", 192.30, "USA", Devise.USD), 15);
        Position pCw8 = creerPosition(new ETF(
                "CW8", "Amundi MSCI World", 510.20, "France", Devise.EUR,
                5500.0,0.38, TypeDistribution.CAPITALISANT, TypeReplication.SYNTHETIQUE), 20);
        Position pOat = creerPosition(new Obligation("FRTR10Y", "OAT France 10 ans", 101.40, "France", Devise.EUR, 2.9), 80);

        pea.ajouterPosition(pCw8);
        pea.ajouterPosition(pOat);
        cto.ajouterPosition(pAapl);

        // Un exemple : Ajoute un ordre d'achat sur l'ETF CW8 dans l'enveloppe PEA passé il y a 2 jours.
        pea.getOrdresEnCours().add(new Ordre(508.0, Devise.EUR, 3, TypeOrdre.ACHAT,
                LocalDateTime.now().minusDays(2), pCw8.getActif()));

        // d'autres exemples
        pea.getOrdresEnCours().add(new Ordre(102.0, Devise.EUR, 10, TypeOrdre.VENTE,
                LocalDateTime.now().minusDays(1), pOat.getActif()));
        cto.getOrdresEnCours().add(new Ordre(190.0, Devise.USD, 5, TypeOrdre.ACHAT,
                LocalDateTime.now().minusHours(18), pAapl.getActif()));

        pea.ajouterTransaction(new Transaction("PEA-001",505.0, 1515.0, LocalDateTime.now().minusMonths(1)));
        cto.ajouterTransaction(new Transaction("CTO-002",188.0, 940.0, LocalDateTime.now().minusWeeks(2)));

        Client client = new Client(1, "Martin", "Alice");
        client.ajouterAdresse(new Adresse("12", "Rue de la Paix", "Appartement 4B", "75002", "France"));
        client.ajouterAdresse(new Adresse("22", "Avenue de l'Opera", "Bureau 3", "75001", "France"));
        client.ajouterEnveloppe(pea);
        client.ajouterEnveloppe(cto);
        alimenterPositionsClient(client);
        return client;
    }

    private Path chargerFichierAttendu(String cheminRessource) throws URISyntaxException {
        URL resource = ClassLoader.getSystemResource(cheminRessource);
        assertNotNull(resource);
        return Paths.get(resource.toURI());
    }
}
