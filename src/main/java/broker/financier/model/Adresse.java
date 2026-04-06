package broker.financier.model;

import java.util.Objects;

public class Adresse {
    private String numeroRue;
    private String rue;
    private String complement;
    private String codePostal;
    private String pays;

    public Adresse() {

    }

    public Adresse(String numeroRue, String rue, String complement, String codePostal, String pays) {
        this.numeroRue = numeroRue;
        this.rue = rue;
        this.complement = complement;
        this.codePostal = codePostal;
        this.pays = pays;
    }

    public String getNumeroRue() {
        return numeroRue;
    }

    public void setNumeroRue(String numeroRue) {
        this.numeroRue = numeroRue;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "numeroRue='" + numeroRue + '\'' +
                ", rue='" + rue + '\'' +
                ", complement='" + complement + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return Objects.equals(getNumeroRue(), adresse.getNumeroRue()) && Objects.equals(getRue(), adresse.getRue()) && Objects.equals(getComplement(), adresse.getComplement()) && Objects.equals(getCodePostal(), adresse.getCodePostal()) && Objects.equals(getPays(), adresse.getPays());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumeroRue(), getRue(), getComplement(), getCodePostal(), getPays());
    }
}
