package broker.financier.model.enveloppes;

import java.time.LocalDateTime;

public class AssuranceVie extends Enveloppe {
    private static final double TAUX_FISCALITE = 0.314;
    private static final double ABATTEMENT_ANNUEL = 4600.0;
    private static final double FRAIS_DEPOT = 0.01;

    public AssuranceVie(String numero, LocalDateTime dateOuverte, double soldeEspeces) {
        super(numero, dateOuverte, soldeEspeces, FRAIS_DEPOT);
    }

    @Override
    protected double calculerFiscalite(double plusValueBrute) {
        if (plusValueBrute <= 0.0) {
            return 0.0;
        }
        double abattementTotal = ABATTEMENT_ANNUEL * getNombreAnneesRevoluesDetention();
        double baseTaxable = Math.max(0.0, plusValueBrute - abattementTotal);
        return baseTaxable * TAUX_FISCALITE;
    }
}
