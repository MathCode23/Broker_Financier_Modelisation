package broker.financier.model.actifs;

import broker.financier.enums.Devise;
import broker.financier.enums.TypeDistribution;
import broker.financier.enums.TypeReplication;

import java.util.Objects;

public class ETF extends Actif {
    private double tailleFondsM;
    private double pourcentageFrais;
    private TypeDistribution typeDistribution;
    private TypeReplication typeReplication;

    public ETF(String ticker, String nom, double prixCourant, String pays, Devise devise, double tailleFondsM, double pourcentageFrais, TypeDistribution typeDistribution, TypeReplication typeReplication) {
        super(ticker, nom, prixCourant, pays, devise);
        this.tailleFondsM = tailleFondsM;
        this.pourcentageFrais = pourcentageFrais;
        this.typeDistribution = typeDistribution;
        this.typeReplication = typeReplication;
    }

    public double getTailleFondsM() {
        return tailleFondsM;
    }

    public void setTailleFondsM(double tailleFondsM) {
        this.tailleFondsM = tailleFondsM;
    }

    public double getPourcentageFrais() {
        return pourcentageFrais;
    }

    public void setPourcentageFrais(double pourcentageFrais) {
        this.pourcentageFrais = pourcentageFrais;
    }

    public TypeDistribution getTypeDistribution() {
        return typeDistribution;
    }

    public void setTypeDistribution(TypeDistribution typeDistribution) {
        this.typeDistribution = typeDistribution;
    }

    public TypeReplication getTypeReplication() {
        return typeReplication;
    }

    public void setTypeReplication(TypeReplication typeReplication) {
        this.typeReplication = typeReplication;
    }

    @Override
    public String toString() {
        return "ETF{" +
                "tailleFondsM=" + tailleFondsM +
                ", pourcentageFrais=" + pourcentageFrais +
                ", typeDistribution=" + typeDistribution +
                ", typeReplication=" + typeReplication +
                '}' + " " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ETF etf = (ETF) o;
        return Double.compare(getTailleFondsM(), etf.getTailleFondsM()) == 0 && Double.compare(getPourcentageFrais(), etf.getPourcentageFrais()) == 0 && getTypeDistribution() == etf.getTypeDistribution() && getTypeReplication() == etf.getTypeReplication();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTailleFondsM(), getPourcentageFrais(), getTypeDistribution(), getTypeReplication());
    }
}