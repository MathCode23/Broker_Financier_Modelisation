package broker.financier.model.enveloppes;

import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;

import java.time.LocalDateTime;

public class AssuranceVie extends Enveloppe {
    public AssuranceVie(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }
    @Override
    public void passerUnOrdre(Ordre o) {
        this.ajouterOrdre(o);
    }
}
