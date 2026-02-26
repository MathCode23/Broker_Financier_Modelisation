package broker.financier.model.achatvente;

import broker.financier.model.actifs.Actif;

import java.util.Objects;

public class Position implements Valorisable {
    private double quantite;
    private Actif actif;

    public Position(double quantite, Actif actif) {
        this.quantite = quantite;
        this.actif = actif;
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

    @Override
    public String toString() {
        return "Position{" +
                "quantite=" + quantite +
                ", actif=" + actif +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(getQuantite(), position.getQuantite()) == 0 && Objects.equals(getActif(), position.getActif());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantite(), getActif());
    }

    @Override
    public double getValeurCourante() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public double getPlusValueLatente() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
