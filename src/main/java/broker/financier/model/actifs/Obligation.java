package broker.financier.model.actifs;

import broker.financier.enums.Devise;

import java.util.Objects;

public class Obligation extends Actif {
    private double tauxInteret;

    public Obligation(String ticker, String nom, double prixCourant, String pays, Devise devise, double tauxInteret) {
        super(ticker, nom, prixCourant, pays, devise);
        this.tauxInteret = tauxInteret;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    @Override
    public String toString() {
        return "Obligation{" +
                "tauxInteret=" + tauxInteret +
                '}' + " " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Obligation that = (Obligation) o;
        return Double.compare(getTauxInteret(), that.getTauxInteret()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTauxInteret());
    }
}