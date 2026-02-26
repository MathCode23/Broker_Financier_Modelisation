package broker.financier.model.enveloppes;

import java.time.LocalDateTime;

public class PEA extends Enveloppe {
    public PEA(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        super(numero, dateOuverte, soldeEspeces, fraisDepot);
    }
}
