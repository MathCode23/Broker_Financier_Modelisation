package broker.financier.model.achatvente;

import broker.financier.model.actifs.Actif;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({"quantite", "actif", "prixAchatUnitaire"})
public class Position implements Valorisable {
    private double quantite;
    private Actif actif;
    private double prixAchatUnitaire;

    public Position() {

    }

    public Position(double quantite, Actif actif, double prixAchatUnitaire) {
        this.quantite = quantite;
        this.actif = actif;
        this.prixAchatUnitaire = prixAchatUnitaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Actif getActif() {
        return actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public double getPrixAchatUnitaire() {
        return prixAchatUnitaire;
    }

    public void setPrixAchatUnitaire(double prixAchatUnitaire) {
        this.prixAchatUnitaire = prixAchatUnitaire;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(getQuantite(), position.getQuantite()) == 0 && Double.compare(getPrixAchatUnitaire(), position.getPrixAchatUnitaire()) == 0 && Objects.equals(getActif(), position.getActif());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantite(), getActif(), getPrixAchatUnitaire());
    }

    @Override
    public String toString() {
        return "Position{" +
                "quantite=" + quantite +
                ", actif=" + actif +
                ", prixAchatUnitaire=" + prixAchatUnitaire +
                '}';
    }

    @Override
    public double getValeurCourante() {
        if (actif == null) {
            return 0.0;
        }
        double valeurCourante = this.quantite * this.actif.getPrixCourant();
        return valeurCourante;
    }

    @Override
    public double getPlusValueLatente() {
        if (actif == null || prixAchatUnitaire <= 0) {
            return 0.0;
        }
        double valeurLatente = (this.actif.getPrixCourant() - this.prixAchatUnitaire) * this.quantite;
        return valeurLatente;
    }
}