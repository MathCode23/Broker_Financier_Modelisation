import broker.financier.enums.Devise;
import broker.financier.enums.TypeOrdre;
import broker.financier.exceptions.ActifNonTrouveException;
import broker.financier.exceptions.SoldeEspeceNonSuffisantException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
import broker.financier.model.enveloppes.CTO;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnveloppeTest {

    @Test
    void testValeurBrute() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        assertTrue(e.getFraisDepot() >= 0);
    }

    @Test
    void testRechercherActifCasNominal() throws ActifNonTrouveException {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Position pActionAirbus = new Position(10, actionAirbus, actionAirbus.getPrixCourant());
        e.ajouterPosition(pActionAirbus);
        Actif actifTrouve = e.rechercherActif("AIR");

        assertEquals(actionAirbus, actifTrouve); // Les deux actifs sont-ils égaux ?
    }

    @Test
    void testRechercherActifCasErreurTicker() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Position pActionAirbus = new Position(10, actionAirbus, actionAirbus.getPrixCourant());
        e.ajouterPosition(pActionAirbus);

        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif(null);}); // Ticker null
        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif("AIRBUS");}); // Ticker introuvable
    }

    @Test
    void testRechercherActifCasErreurDictionnaireVide() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);

        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif("AIR");}); // Aucun actif enregistré
    }

    @Test
    public void totalValeurBrute() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Position pActionAirbus = new Position(10, actionAirbus, actionAirbus.getPrixCourant());
        e.ajouterPosition(pActionAirbus);
        double valeurTotaleBrute = e.getValeurTotaleBrute();

        assertEquals(1000 + 10 * 145.20, valeurTotaleBrute);
    }

    @Test
    void getValeurTotaleBrute_calculeCorrectement() {
        Enveloppe enveloppe = new CTO("CTO-T-003", LocalDateTime.now(), 1_000.0);
        Action actif1 = new Action("AIR", "Airbus", 100.0, "France", Devise.EUR);
        Action actif2 = new Action("ORA", "Orange", 50.0, "France", Devise.EUR);

        enveloppe.ajouterPosition(creerPosition(actif1, 2.0)); // 200
        enveloppe.ajouterPosition(creerPosition(actif2, 5.0)); // 250

        double valeurTotaleBrute = enveloppe.getValeurTotaleBrute();

        assertEquals(1_450.0, valeurTotaleBrute, 0.0001);
    }

    @Test
    void passerUnOrdre_achatAvecSoldeEspeceInsuffisant_declencheException() {
        Enveloppe enveloppe = new CTO("CTO-T-004", LocalDateTime.now(), 100.0);
        Action actif = new Action("MC", "LVMH", 700.0, "France", Devise.EUR);
        Ordre ordreAchatTropCher = new Ordre(
                60.0,
                Devise.EUR,
                2.0,
                TypeOrdre.ACHAT,
                LocalDateTime.now(),
                actif
        );

        try {
            enveloppe.passerUnOrdre(ordreAchatTropCher);
            fail("Une SoldeEspeceNotEnoughException devait etre declenchee.");
        } catch (SoldeEspeceNonSuffisantException e) {
            // Comportement attendu
        } catch (TransactionFractionneeInvalideException e) {
            fail("Exception inattendue: " + e.getMessage());
        }
    }

    @Test
    void afficherPositionsParPays_afficheUniquementLesPositionsDuPaysDansLOrdreDInsertion() {
        Enveloppe enveloppe = new CTO("CTO-T-005", LocalDateTime.now(), 1_000.0);
        Position airbus = creerPosition(new Action("AIR", "Airbus", 100.0, "France", Devise.EUR), 2.0);
        Position apple = creerPosition(new Action("AAPL", "Apple", 190.0, "USA", Devise.USD), 3.0);
        Position orange = creerPosition(new Action("ORA", "Orange", 50.0, "France", Devise.EUR), 5.0);
        enveloppe.ajouterPosition(airbus);
        enveloppe.ajouterPosition(apple);
        enveloppe.ajouterPosition(orange);

        String sortie = capturerSortieStandard(() -> enveloppe.afficherPositionsParPays("France"));

        assertEquals(
                List.of(airbus.toString(), orange.toString()),
                lignes(sortie)
        );
    }

    @Test
    void afficherPositionsParPays_quandAucunePositionNeCorrespond_neRienAffiche() {
        Enveloppe enveloppe = new CTO("CTO-T-006", LocalDateTime.now(), 1_000.0);
        enveloppe.ajouterPosition(creerPosition(new Action("AAPL", "Apple", 190.0, "USA", Devise.USD), 3.0));
        enveloppe.ajouterPosition(creerPosition(new Action("SAP", "SAP", 240.0, "Allemagne", Devise.EUR), 2.0));

        String sortie = capturerSortieStandard(() -> enveloppe.afficherPositionsParPays("France"));

        assertEquals(List.of(), lignes(sortie));
    }

    @Test
    void afficherPositionsParPays_quandEnveloppeEstVide_neRienAffiche() {
        Enveloppe enveloppe = new CTO("CTO-T-007", LocalDateTime.now(), 1_000.0);

        String sortie = capturerSortieStandard(() -> enveloppe.afficherPositionsParPays("France"));

        assertEquals("", sortie);
    }

    @Test
    void afficherPositionsParPays_avecPaysNull_neRienAffiche() {
        Enveloppe enveloppe = new CTO("CTO-T-008", LocalDateTime.now(), 1_000.0);
        enveloppe.ajouterPosition(creerPosition(new Action("AIR", "Airbus", 100.0, "France", Devise.EUR), 2.0));

        String sortie = capturerSortieStandard(() -> enveloppe.afficherPositionsParPays(null));

        assertEquals(List.of(), lignes(sortie));
    }

    @Test
    void afficherPositionsParPays_comparaisonExacteRespecteLaCasse() {
        Enveloppe enveloppe = new CTO("CTO-T-009", LocalDateTime.now(), 1_000.0);
        enveloppe.ajouterPosition(creerPosition(new Action("AIR", "Airbus", 100.0, "France", Devise.EUR), 2.0));

        String sortie = capturerSortieStandard(() -> enveloppe.afficherPositionsParPays("france"));

        assertEquals(List.of(), lignes(sortie));
    }

    private static Position creerPosition(Actif actif, double quantite) {
        Position position = new Position(quantite, actif, actif.getPrixCourant());
        position.setActif(actif);
        return position;
    }

    private String capturerSortieStandard(Runnable action) {
        // On conserve la vraie sortie standard pour la restaurer apres le test.
        PrintStream sortieOriginale = System.out;

        // Ce tampon memoire remplace temporairement la console.
        ByteArrayOutputStream tampon = new ByteArrayOutputStream();

        try {
            // Tout ce qui sera ecrit sur System.out ira dans le tampon.
            System.setOut(new PrintStream(tampon, true, StandardCharsets.UTF_8));

            // On execute la methode a tester.
            action.run();
        } finally {
            // On restaure System.out meme si le test echoue.
            System.setOut(sortieOriginale);
        }

        // On retourne tout ce qui a ete affiche, sous forme de texte.
        return tampon.toString(StandardCharsets.UTF_8);
    }

    private List<String> lignes(String sortie) {
        return sortie.lines().toList();
    }
}
