package broker.financier.model.enveloppes;

import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.actifs.Actif;

import java.time.LocalDateTime;

public class CTO extends Enveloppe {
    public CTO(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }

    @Override
    public void passerUnOrdre(Ordre o) throws ActifNonEligibleException{
        Actif actif = o.getActif();
        if (!estActifEligible(actif)) {
            throw new ActifNonEligibleException("Actif non-eligible au CTO : " + actif.getTicker());
        }
        this.ajouterOrdre(o);
    }

    @Override
    public boolean estActifEligible(Actif actif) {
        return true;
    }
}
