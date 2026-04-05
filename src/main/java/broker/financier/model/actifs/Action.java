package broker.financier.model.actifs;

import broker.financier.enums.Devise;

import java.util.Objects;

public class Action extends Actif {

    public Action() {
        super();
    }

    public Action(String ticker, String nom, double prixCourant, String pays, Devise devise) {
        super(ticker, nom, prixCourant, pays, devise);
    }

    @Override
    public String toString() {
        return "Action{"
                + "ticker='" + getTicker() + '\''
                + ", nom='" + getNom() + '\''
                + ", prixCourant=" + getPrixCourant()
                + ", pays='" + getPays() + '\''
                + ", devise=" + getDevise()
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicker(), getNom(), getPrixCourant(), getPays(), getDevise());
    }
}
