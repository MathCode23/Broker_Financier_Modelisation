import broker.financier.enums.Devise;
import broker.financier.enums.TypeOrdre;
import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
import broker.financier.model.actifs.Obligation;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PEATest {
    @Test
    public void testAchatFractionne() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Ordre o = new Ordre(100, Devise.EUR, 1.5, TypeOrdre.ACHAT, LocalDateTime.now(), actionAirbus);

        assertThrows(TransactionFractionneeInvalideException.class, () -> {e.passerUnOrdre(o);});
    }
    /*
    @Test
    public void testActifNonEligible() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Ordre o = new Ordre(100, Devise.EUR, 1.5, TypeOrdre.ACHAT, LocalDateTime.now(), actionAirbus);

        assertThrows(ActifNonEligibleException.class, () -> {e.passerUnOrdre(o);});;
    }

    @Test
    void ajouterActifNonEligibleDansPea_declencheException() {
        Enveloppe pea = new PEA("PEA-T-001", LocalDateTime.now(), 2_000.0);
        Actif obligations = new Obligation("FRTR10Y", "OAT 10Y", 100.0, "France", Devise.EUR, 2.5);
        Position positionNonEligible = creerPosition(obligations, 10.0);

        try {
            pea.ajouterPosition(positionNonEligible);
            fail("Une ActifNonEligibleException devait etre declenchee.");
        } catch (ActifNonEligibleException e) {
            // Comportement attendu
        }
    }

    private static Position creerPosition(Actif actif, double quantite) {
        Position position = new Position(quantite, actif, actif.getPrixCourant());
        position.setActif(actif);
        return position;
    }
     */
}
