package broker.financier.model.enveloppes;

import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;

import java.time.LocalDateTime;

public class PEA extends Enveloppe {
    public PEA(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }

    @Override
    public void passerUnOrdre(Ordre o) throws TransactionFractionneeInvalideException {
        double quantite = o.getQuantite();
        if (quantite != Math.floor(quantite)) {
            throw new TransactionFractionneeInvalideException("Achat fractionné interdit pour l'enveloppe PEA : quantite = " + quantite);
        }
        this.ajouterOrdre(o);
    }
}
