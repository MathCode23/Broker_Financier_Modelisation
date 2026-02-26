package broker.financier.model.enveloppes;

import java.time.LocalDateTime;

public class CTO extends Enveloppe {
    public CTO(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }
}
