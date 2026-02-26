package broker.financier.model.enveloppes;

import java.time.LocalDateTime;

public class AssuranceVie extends Enveloppe {
    public AssuranceVie(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }
}
