import broker.financier.enums.Devise;
import broker.financier.enums.TypeOrdre;
import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
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

    @Test
    public void testActifNonEligible() {
        Enveloppe e = new PEA("123", LocalDateTime.now(), 1000);
        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Ordre o = new Ordre(100, Devise.EUR, 1.5, TypeOrdre.ACHAT, LocalDateTime.now(), actionAirbus);

        assertThrows(ActifNonEligibleException.class, () -> {e.passerUnOrdre(o);});;
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
}
