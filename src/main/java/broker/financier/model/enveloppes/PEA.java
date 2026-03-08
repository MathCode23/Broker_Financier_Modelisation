package broker.financier.model.enveloppes;

import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.actifs.Actif;

import java.time.LocalDateTime;

public class PEA extends Enveloppe {
    public PEA(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }

    @Override
    public void passerUnOrdre(Ordre o) throws TransactionFractionneeInvalideException, ActifNonEligibleException {
        Actif actif = o.getActif();
        if (!estActifEligible(actif)) {
            throw new ActifNonEligibleException("Actif non-eligible au PEA : " + actif.getTicker());
        }
        double quantite = o.getQuantite();
        if (quantite != Math.floor(quantite)) {
            throw new TransactionFractionneeInvalideException("Achat fractionné interdit pour l'enveloppe PEA : quantite = " + quantite);
        }
        this.ajouterOrdre(o);
    }

    @Override
    public boolean estActifEligible(Actif actif) {
        return true;
    }
}
