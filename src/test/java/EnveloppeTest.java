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

import java.time.LocalDateTime;

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

    private static Position creerPosition(Actif actif, double quantite) {
        Position position = new Position(quantite, actif, actif.getPrixCourant());
        position.setActif(actif);
        return position;
    }
}
