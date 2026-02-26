package broker.financier.model.achatvente;

import broker.financier.enums.Devise;
import broker.financier.enums.TypeOrdre;
import broker.financier.model.actifs.Actif;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ordre {
    private double prixMinMax;
    private Devise devise;
    private double quantite;
    private TypeOrdre type;
    private LocalDateTime date;
    private Actif actif;

    public Ordre(double prixMinMax, Devise devise, double quantite, TypeOrdre type, LocalDateTime date, Actif actif) {
        this.prixMinMax = prixMinMax;
        this.devise = devise;
        this.quantite = quantite;
        this.type = type;
        this.date = date;
        this.actif = actif;
    }

    public double getPrixMinMax() {
        return prixMinMax;
    }

    public void setPrixMinMax(double prixMinMax) {
        this.prixMinMax = prixMinMax;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public TypeOrdre getType() {
        return type;
    }

    public void setType(TypeOrdre type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Actif getActif() {
        return actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Ordre{" +
                "prixMinMax=" + prixMinMax +
                ", devise=" + devise +
                ", quantite=" + quantite +
                ", type=" + type +
                ", date=" + date +
                ", actif=" + actif +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ordre ordre = (Ordre) o;
        return Double.compare(getPrixMinMax(), ordre.getPrixMinMax()) == 0 && Double.compare(getQuantite(), ordre.getQuantite()) == 0 && getDevise() == ordre.getDevise() && getType() == ordre.getType() && Objects.equals(getDate(), ordre.getDate()) && Objects.equals(getActif(), ordre.getActif());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrixMinMax(), getDevise(), getQuantite(), getType(), getDate(), getActif());
    }
}
