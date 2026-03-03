package broker.financier.model.enveloppes;

import broker.financier.exceptions.ActifNonTrouveException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.achatvente.Transaction;
import broker.financier.model.actifs.Actif;

import java.time.LocalDateTime;
import java.util.*;

public abstract class Enveloppe {
    private String numero;
    private LocalDateTime dateOuverte;
    private double soldeEspeces;
    private double fraisDepot;

    private List<Transaction> transactions;
    private Queue<Ordre> ordresEnCours;
    private Map<String, Position> positions;

    private Enveloppe() {
        this.transactions = new ArrayList<>();
        this.ordresEnCours = new LinkedList<>();
        this.positions = new HashMap<>();
    }

    public Enveloppe(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        this();
        this.numero = numero;
        this.dateOuverte = dateOuverte;
        this.soldeEspeces = soldeEspeces;
        this.fraisDepot = fraisDepot;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getDateOuverte() {
        return dateOuverte;
    }

    public void setDateOuverte(LocalDateTime dateOuverte) {
        this.dateOuverte = dateOuverte;
    }

    public double getSoldeEspeces() {
        return soldeEspeces;
    }

    public void setSoldeEspeces(double soldeEspeces) {
        this.soldeEspeces = soldeEspeces;
    }

    public double getFraisDepot() {
        return fraisDepot;
    }

    public void setFraisDepot(double fraisDepot) {
        this.fraisDepot = fraisDepot;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Queue<Ordre> getOrdresEnCours() {
        return ordresEnCours;
    }

    public void setOrdresEnCours(Queue<Ordre> ordresEnCours) {
        this.ordresEnCours = ordresEnCours;
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Position> positions) {
        this.positions = positions;
    }

    public void ajouterTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    protected void ajouterOrdre(Ordre ordre) {
        this.ordresEnCours.add(ordre);
    }

    public void ajouterPosition(Position p) {
        if (p != null && p.getActif() != null && p.getActif().getTicker() != null) {
            positions.put(p.getActif().getTicker(), p);
        }
    }

    @Override
    public String toString() {
        return "Enveloppe{" +
                "numero='" + numero + '\'' +
                ", dateOuverte=" + dateOuverte +
                ", soldeEspeces=" + soldeEspeces +
                ", fraisDepot=" + fraisDepot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enveloppe enveloppe = (Enveloppe) o;
        return Double.compare(getSoldeEspeces(), enveloppe.getSoldeEspeces()) == 0 && Double.compare(getFraisDepot(), enveloppe.getFraisDepot()) == 0 && Objects.equals(getNumero(), enveloppe.getNumero()) && Objects.equals(getDateOuverte(), enveloppe.getDateOuverte()) && Objects.equals(getTransactions(), enveloppe.getTransactions()) && Objects.equals(getOrdresEnCours(), enveloppe.getOrdresEnCours()) && Objects.equals(getPositions(), enveloppe.getPositions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumero(), getDateOuverte(), getSoldeEspeces(), getFraisDepot(), getTransactions(), getOrdresEnCours(), getPositions());
    }

    public void passerUnOrdre(Ordre o) {}

    public double getValeurTotaleBrute() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public double getValeurTotaleNet() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean estActifEligible(Actif actif) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public double calculerFiscalite() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Actif rechercherActif(String ticker) throws ActifNonTrouveException {
        if (ticker == null) {
            throw new ActifNonTrouveException("Le ticker fourni est null.");
        }
        if (this.positions.isEmpty()) {
            throw new ActifNonTrouveException("Aucune position enregistrée dans l'enveloppe.");
        }
        Position p = this.positions.get(ticker);
        if (p == null) {
            throw new ActifNonTrouveException("Aucun actif trouvé pour le ticker : " + ticker);
        }
        return p.getActif();
    }

    public void afficherOrdresEnCours() {
        System.out.println("Ordres en cours dans l'enveloppe " + this.numero + " :");
        if (this.ordresEnCours.isEmpty()) {
            System.out.println("Aucun ordre en cours.");
            return;
        }
        for (Ordre o : this.ordresEnCours) {
            System.out.println(" - " + o);
        }
    }

    public void afficherTransactions() {
        System.out.println("Historique des transactions de l'enveloppe " + this.numero + " :");
        if (this.transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
            return;
        }
        for (Transaction t : this.transactions) {
            System.out.println(" - " + t);
        }
    }

    public void afficherPositions() {
        System.out.println("Positions détenues dans l'enveloppe  " + this.numero + " :");
        if (this.positions.isEmpty()) {
            System.out.println("Aucune position enregistrée.");
            return;
        }
        for (Map.Entry<String, Position> entry : this.positions.entrySet()) {
            String ticker = entry.getKey();
            Position p = entry.getValue();
            System.out.println(" - " + ticker + " : " + p);
        }
    }
}
