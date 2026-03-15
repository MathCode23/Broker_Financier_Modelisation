package broker.financier.model.enveloppes;

import java.time.LocalDateTime;

public class CTO extends Enveloppe {
    private static final double TAUX_FISCALITE = 0.314;
    private static final double FRAIS_DEPOT = 0.0;

    public CTO(String numero, LocalDateTime dateOuverte, double soldeEspeces) {
        super(numero, dateOuverte, soldeEspeces, FRAIS_DEPOT);
    }

    @Override
    protected double calculerFiscalite(double plusValueBrute) {
        if (plusValueBrute <= 0.0) {
            return 0.0;
        }
        return plusValueBrute * TAUX_FISCALITE;
    }
}
