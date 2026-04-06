package broker.financier.model.achatvente;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private String id;
    private String tickerActif;
    private double prixExecution;
    private double montantTotal;
    private LocalDateTime dateExecution;

    public Transaction() {

    }

    public Transaction(String tickerActif, double prixExecution, double montantTotal, LocalDateTime dateExecution) {
        this.id = UUID.randomUUID().toString();
        this.tickerActif = tickerActif;
        this.prixExecution = prixExecution;
        this.montantTotal = montantTotal;
        this.dateExecution = dateExecution;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTickerActif() {
        return tickerActif;
    }

    public void setTickerActif(String tickerActif) {
        this.tickerActif = tickerActif;
    }

    public double getPrixExecution() {
        return prixExecution;
    }

    public void setPrixExecution(double prixExecution) {
        this.prixExecution = prixExecution;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public LocalDateTime getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(LocalDateTime dateExecution) {
        this.dateExecution = dateExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(getPrixExecution(), that.getPrixExecution()) == 0 && Double.compare(getMontantTotal(), that.getMontantTotal()) == 0 && Objects.equals(getId(), that.getId()) && Objects.equals(getTickerActif(), that.getTickerActif()) && Objects.equals(getDateExecution(), that.getDateExecution());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTickerActif(), getPrixExecution(), getMontantTotal(), getDateExecution());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", tickerActif='" + tickerActif + '\'' +
                ", prixExecution=" + prixExecution +
                ", montantTotal=" + montantTotal +
                ", dateExecution=" + dateExecution +
                '}';
    }
}
