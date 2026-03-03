package broker.financier.model.achatvente;

import broker.financier.model.actifs.Actif;

import java.util.Objects;

public class Position implements Valorisable {
    private double quantite;
    private Actif actif;
    private Transaction transaction;

    public Position(double quantite, Actif actif, Transaction transaction) {
        this.quantite = quantite;
        this.actif = actif;
        this.transaction = transaction;
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "Position{" +
                "quantite=" + quantite +
                ", actif=" + actif +
                ", transaction=" + transaction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(getQuantite(), position.getQuantite()) == 0 && Objects.equals(getActif(), position.getActif()) && Objects.equals(getTransaction(), position.getTransaction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantite(), getActif(), getTransaction());
    }

    @Override
    public double getValeurCourante() {
        double valeurCourante = this.quantite * this.actif.getPrixCourant();
        return valeurCourante;
    }

    @Override
    public double getPlusValeurLatente() {
        double valeurLatente = (this.actif.getPrixCourant() - this.transaction.getPrixExecution()) * this.quantite;
        return valeurLatente;
    }
}
