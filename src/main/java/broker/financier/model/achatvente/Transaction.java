package broker.financier.model.achatvente;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private double prixExecution;
    private double montantTotal;
    private LocalDateTime dateExecution;

    public Transaction(double prixExecution, double montantTotal, LocalDateTime dateExecution) {
        this.prixExecution = prixExecution;
        this.montantTotal = montantTotal;
        this.dateExecution = dateExecution;
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
    public String toString() {
        return "Transaction{" +
                "prixExecution=" + prixExecution +
                ", montantTotal=" + montantTotal +
                ", dateExecution=" + dateExecution +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(getPrixExecution(), that.getPrixExecution()) == 0 && Double.compare(getMontantTotal(), that.getMontantTotal()) == 0 && Objects.equals(getDateExecution(), that.getDateExecution());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrixExecution(), getMontantTotal(), getDateExecution());
    }
}
