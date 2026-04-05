package broker.financier.model.actifs;

import broker.financier.enums.Devise;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({ "ticker", "nom", "prixCourant", "pays", "devise" })
public abstract class Actif implements Vendable {
    private String ticker;
    private String nom;
    private double prixCourant;
    private String pays;
    private Devise devise;

    public Actif() {

    }

    public Actif(String ticker, String nom, double prixCourant, String pays, Devise devise) {
        this.ticker = ticker;
        this.nom = nom;
        this.prixCourant = prixCourant;
        this.pays = pays;
        this.devise = devise;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrixCourant(double prixCourant) {
        this.prixCourant = prixCourant;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    @Override
    public String toString() {
        return "Actif{" +
                "ticker='" + ticker + '\'' +
                ", nom='" + nom + '\'' +
                ", prixCourant=" + prixCourant +
                ", pays='" + pays + '\'' +
                ", devise=" + devise +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Actif actif = (Actif) o;
        return Double.compare(getPrixCourant(), actif.getPrixCourant()) == 0 && Objects.equals(getTicker(), actif.getTicker()) && Objects.equals(getNom(), actif.getNom()) && Objects.equals(getPays(), actif.getPays()) && getDevise() == actif.getDevise();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicker(), getNom(), getPrixCourant(), getPays(), getDevise());
    }

    public double getPrixCourant() {
        return this.prixCourant;
    }

    public void mettreAJourPrix(double nouveauPrix) {
        this.prixCourant = nouveauPrix;
    }

    @Override
    public void vendre() {
        // No-op for now.
    }
}