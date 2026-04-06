package broker.financier.model.enveloppes;

import broker.financier.enums.TypeOrdre;
import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
import broker.financier.model.actifs.ETF;
import broker.financier.model.actifs.Obligation;

import java.time.LocalDateTime;

public class PEA extends Enveloppe {
    private static final double TAUX_FISCALITE_AVANT_5_ANS = 0.314;
    private static final double TAUX_FISCALITE_APRES_5_ANS = 0.186;
    private static final double FRAIS_DEPOT = 0.0;

    public PEA() {
        super();
    }

    public PEA(String numero, LocalDateTime dateOuverte, double soldeEspeces) {
        super(numero, dateOuverte, soldeEspeces, FRAIS_DEPOT);
    }

    @Override
    protected double calculerFiscalite(double plusValueBrute) {
        if (plusValueBrute <= 0.0) {
            return 0.0;
        }

        double taux = getNombreAnneesRevoluesDetention() > 5
                ? TAUX_FISCALITE_APRES_5_ANS
                : TAUX_FISCALITE_AVANT_5_ANS;
        return plusValueBrute * taux;
    }

    @Override
    protected void validerOrdre(Ordre ordre) throws TransactionFractionneeInvalideException {
        if (ordre.getType() == TypeOrdre.ACHAT) {
            double quantite = ordre.getQuantite();
            boolean estEntiere = quantite == Math.rint(quantite);
            if (!estEntiere) {
                throw new TransactionFractionneeInvalideException(
                        "Achat fractionne interdit sur PEA pour le ticker '" + ordre.getActif().getTicker() + "'."
                );
            }
        }
    }

    @Override
    public boolean estActifEligible(Actif actif) {
        return actif instanceof Action || actif instanceof ETF || actif instanceof Obligation;
    }
}
