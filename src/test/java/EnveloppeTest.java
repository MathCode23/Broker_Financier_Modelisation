import broker.financier.enums.Devise;
import broker.financier.exceptions.ActifNonTrouveException;
import broker.financier.model.achatvente.Position;
import broker.financier.model.achatvente.Transaction;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EnveloppeTest {

    @Test
    void testValeurBrute() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000, 5);
        assertTrue(e.getFraisDepot() > 0);
    }

    @Test
    void testRechercherActifCasNominal() throws ActifNonTrouveException {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000, 5);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Transaction t = new Transaction(100, 300, LocalDateTime.now());
        Position pActionAirbus = new Position(10, actionAirbus, t);
        e.ajouterPosition(pActionAirbus);
        Actif actifTrouve = e.rechercherActif("AIR");

        assertEquals(actionAirbus, actifTrouve); // Les deux actifs sont-ils égaux ?
    }

    @Test
    void testRechercherActifCasErreurTicker() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000, 5);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Transaction t = new Transaction(100, 300, LocalDateTime.now());
        Position pActionAirbus = new Position(10, actionAirbus, t);
        e.ajouterPosition(pActionAirbus);

        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif(null);}); // Ticker null
        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif("AIRBUS");}); // Ticker introuvable
    }

    @Test
    void testRechercherActifCasErreurDictionnaireVide() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000, 5);

        assertThrows(ActifNonTrouveException.class, () -> {e.rechercherActif("AIR");}); // Aucun actif enregistré
    }
}
