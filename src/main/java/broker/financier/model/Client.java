package broker.financier.model;

import broker.financier.model.enveloppes.Enveloppe;

import java.util.*;

public class Client {
    private int id;
    private String nom;
    private String prenom;

    private Set<Adresse> adresses;
    private List<Enveloppe> enveloppes;

    public Client() {

    }

    public Client(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresses = new HashSet<>();
        this.enveloppes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Set<Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(Set<Adresse> adresses) {
        this.adresses = adresses;
    }

    public List<Enveloppe> getEnveloppes() {
        return enveloppes;
    }

    public void setEnveloppes(List<Enveloppe> enveloppes) {
        this.enveloppes = enveloppes;
    }

    public void ajouterAdresse(Adresse adresse) {
        this.adresses.add(adresse);
    }

    public void ajouterEnveloppe(Enveloppe enveloppe) {
        this.enveloppes.add(enveloppe);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresses=" + adresses +
                ", enveloppes=" + enveloppes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return getId() == client.getId() && Objects.equals(getNom(), client.getNom()) && Objects.equals(getPrenom(), client.getPrenom()) && Objects.equals(getAdresses(), client.getAdresses()) && Objects.equals(getEnveloppes(), client.getEnveloppes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getPrenom(), getAdresses(), getEnveloppes());
    }

    public void afficherAdresses() {
        if (adresses.isEmpty()) {
            System.out.println("Aucune adresse enregistrée.");
            return;
        }
        for (Adresse a : adresses) {
            System.out.println(" - " + a);
        }
    }

    public void afficherEnveloppes() {
        if (enveloppes.isEmpty()) {
            System.out.println("Aucune enveloppe enregistrée.");
            return;
        }
        for (Enveloppe e : enveloppes) {
            System.out.println(" - " + e);
        }
    }
}
