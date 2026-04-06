package broker.financier.model.enveloppes;

import broker.financier.enums.TypeOrdre;
import broker.financier.exceptions.ActifNonEligibleException;
import broker.financier.exceptions.ActifNonTrouveException;
import broker.financier.exceptions.SoldeEspeceNonSuffisantException;
import broker.financier.exceptions.TransactionFractionneeInvalideException;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.achatvente.Transaction;
import broker.financier.model.actifs.Actif;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PEA.class, name = "PEA"),
        @JsonSubTypes.Type(value = CTO.class, name = "CTO"),
        @JsonSubTypes.Type(value = AssuranceVie.class, name = "AV")
})

@JsonPropertyOrder({"numero", "dateOuverte", "fraisDepot", "transactions", "ordresEnCours", "positions"})
public abstract class Enveloppe {
    private String numero;
    private LocalDateTime dateOuverte;
    private double soldeEspeces;
    private double fraisDepot;

    private List<Transaction> transactions;
    private Queue<Ordre> ordresEnCours;
    private Map<String, Position> positions;

    public Enveloppe() {

    }

    public Enveloppe(String numero, LocalDateTime dateOuverte, double soldeEspeces, double fraisDepot) {
        this.numero = numero;
        this.dateOuverte = dateOuverte;
        this.soldeEspeces = soldeEspeces;
        this.fraisDepot = fraisDepot;
        this.transactions = new ArrayList<>();
        this.ordresEnCours = new LinkedList<>();
        this.positions = new HashMap<>();
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

    public void ajouterPosition(Position position) {
        if (position != null && position.getActif() != null && position.getActif().getTicker() != null) {
            if (!estActifEligible(position.getActif())) {
                throw new ActifNonEligibleException(
                        "L'actif '" + position.getActif().getTicker() + "' n'est pas eligible pour cette enveloppe."
                );
            }
            positions.put(position.getActif().getTicker(), position);
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

    public void passerUnOrdre(Ordre ordre)
            throws TransactionFractionneeInvalideException, SoldeEspeceNonSuffisantException {
        // 1) Validation des préconditions.
        if (ordre == null) {
            throw new IllegalArgumentException("L'ordre ne peut pas etre null.");
        }
        if (ordre.getActif() == null || ordre.getActif().getTicker() == null || ordre.getActif().getTicker().isBlank()) {
            throw new IllegalArgumentException("L'ordre doit contenir un actif avec un ticker valide.");
        }
        if (ordre.getType() == null) {
            throw new IllegalArgumentException("Le type d'ordre est obligatoire.");
        }
        if (ordre.getQuantite() <= 0.0) {
            throw new IllegalArgumentException("La quantite d'ordre doit etre strictement positive.");
        }
        if (ordre.getPrixMinMax() <= 0.0) {
            throw new IllegalArgumentException("Le prix d'execution doit etre strictement positif.");
        }

        // 2) Vérification des espèces disponibles pour un achat.
        // Les espèces disponibles = solde espèces - espèces déjà engagées par les autres ordres d'achat en cours.
        if (ordre.getType() == TypeOrdre.ACHAT) {
            double especesDejaEngagees = calculerEspecesDejaEngagees(ordre);
            double especesDisponibles = soldeEspeces - especesDejaEngagees;
            double montantOrdre = ordre.getPrixMinMax() * ordre.getQuantite();
            if (montantOrdre > especesDisponibles) {
                throw new SoldeEspeceNonSuffisantException(
                        "Solde especes insuffisant. Disponible=" + especesDisponibles + ", requis=" + montantOrdre + "."
                );
            }
        }

        // 3) Application des règles spécifiques de l'enveloppe (ex: achat fractionné PEA).
        validerOrdre(ordre);

        // 4) Exécution de l'ordre: création/mise à jour position, mouvement espèces, traçabilité transaction.
        String ticker = ordre.getActif().getTicker();
        double quantite = ordre.getQuantite();
        double montant = ordre.getPrixMinMax() * quantite;
        Position positionExistante = positions.get(ticker);

        if (ordre.getType() == TypeOrdre.ACHAT) {
            // 4a) Achat: on augmente la position et on débite le solde espèces.
            if (positionExistante == null) {
                positionExistante = new Position(quantite, null, ordre.getPrixMinMax());
                positionExistante.setActif(ordre.getActif());
                positions.put(ticker, positionExistante);
            } else {
                double ancienneQuantite = positionExistante.getQuantite();
                double nouvelleQuantite = ancienneQuantite + quantite;

                // Maintient un prix d'achat moyen pour alimenter les calculs de plus-value latente.
                if (nouvelleQuantite > 0.0) {
                    double ancienPrixAchat = positionExistante.getPrixAchatUnitaire();
                    double nouveauPrixAchat = ((ancienPrixAchat * ancienneQuantite) + (ordre.getPrixMinMax() * quantite))
                            / nouvelleQuantite;
                    positionExistante.setPrixAchatUnitaire(nouveauPrixAchat);
                }
                positionExistante.setQuantite(nouvelleQuantite);
                positionExistante.setActif(ordre.getActif());
            }
            soldeEspeces -= montant;
            ajouterTransaction(new Transaction(ticker, ordre.getPrixMinMax(), montant, LocalDateTime.now()));
        } else {
            // 4b) Vente: on diminue la position et on crédite le solde espèces.
            if (positionExistante == null || positionExistante.getQuantite() < quantite) {
                throw new IllegalStateException("Quantite insuffisante pour vendre l'actif '" + ticker + "'.");
            }
            double nouvelleQuantite = positionExistante.getQuantite() - quantite;
            if (nouvelleQuantite == 0.0) {
                positions.remove(ticker);
            } else {
                positionExistante.setQuantite(nouvelleQuantite);
            }
            soldeEspeces += montant;
            ajouterTransaction(new Transaction(ticker, -ordre.getPrixMinMax(), -montant, LocalDateTime.now()));
        }

        // 5) L'ordre est exécuté, il est retiré des ordres en cours.
        ordresEnCours.remove(ordre);
    }

    private static void checkOrdreArguments(Ordre ordre) {
        if (ordre == null) {
            throw new IllegalArgumentException("L'ordre ne peut pas etre null.");
        }
        if (ordre.getActif() == null || ordre.getActif().getTicker() == null || ordre.getActif().getTicker().isBlank()) {
            throw new IllegalArgumentException("L'ordre doit contenir un actif avec un ticker valide.");
        }
        if (ordre.getType() == null) {
            throw new IllegalArgumentException("Le type d'ordre est obligatoire.");
        }
        if (ordre.getQuantite() <= 0.0) {
            throw new IllegalArgumentException("La quantite d'ordre doit etre strictement positive.");
        }
        if (ordre.getPrixMinMax() <= 0.0) {
            throw new IllegalArgumentException("Le prix d'execution doit etre strictement positif.");
        }
    }

    protected void validerOrdre(Ordre ordre) throws TransactionFractionneeInvalideException {
        // Hook surchargeable par les enveloppes qui appliquent des contraintes particulières.
        // On illustre ici la limite d'une classe asbtraite par rapport à une interface : rien n'oblige
        // une enveloppe (CTO, PEA, ...) à surcharger cette méthode et donc on peut arriver à coder
        // une nouvelle enveloppe en oubliant de traiter ce comportement (certes on peut aussi coder une
        // en faisant exprès de ne pas surcharger cette méthode si l'enveloppe n'a pas de restriction.
        // Une alerte cognitive à mettre en place : Comportement => interface !
    }

    private double calculerEspecesDejaEngagees(Ordre ordreAExclure) {
        double especesDejaEngagees = 0.0;
        for (Ordre ordreEnAttente : ordresEnCours) {
            boolean ordreAchat = ordreEnAttente.getType() == TypeOrdre.ACHAT;
            boolean ordreDifferent = ordreAExclure == null || !ordreEnAttente.equals(ordreAExclure);
            if (ordreAchat && ordreDifferent) {
                especesDejaEngagees += ordreEnAttente.getQuantite() * ordreEnAttente.getPrixMinMax();
            }
        }
        return especesDejaEngagees;
    }

    public double getValeurTotaleBrute() {
        double valeurTotaleBrute = this.soldeEspeces;
        for (Position p : positions.values()) {
            valeurTotaleBrute += p.getValeurCourante();
        }
        return valeurTotaleBrute;
    }

    public double getValeurTotaleNette() {
        double plusValueBruteTotale = getPlusValueLatenteBruteTotale();
        if (plusValueBruteTotale <= 0.0) {
            return plusValueBruteTotale;
        }
        double fiscalite = calculerFiscalite(plusValueBruteTotale);
        return plusValueBruteTotale - fiscalite;
    }

    public boolean estActifEligible(Actif actif) {
        return actif != null;
    }

    protected abstract double calculerFiscalite(double plusValueBrute);

    protected long getNombreAnneesRevoluesDetention() {
        if (dateOuverte == null) {
            return 0L;
        }
        long annees = ChronoUnit.YEARS.between(dateOuverte.toLocalDate(), LocalDateTime.now().toLocalDate());
        return Math.max(0L, annees);
    }

    private double getPlusValueLatenteBruteTotale() {
        if (!transactions.isEmpty()) {
            Map<String, Double> prixAchatParTicker = new HashMap<>();
            Map<String, Double> prixVenteParTicker = new HashMap<>();
            Set<String> tickersActifsVendus = new HashSet<>();
            double montantTotalInvesti = 0.0;

            // 1) On indexe les transactions par ticker pour détecter les paires achat/vente.
            // Une transaction sans ticker est conservée dans le montant investi "générique".
            for (Transaction transaction : transactions) {
                String ticker = transaction.getTickerActif();
                if (ticker == null || ticker.isBlank()) {
                    montantTotalInvesti += transaction.getMontantTotal();
                } else {
                    double prixExecution = transaction.getPrixExecution();
                    if (prixExecution > 0.0) {
                        prixAchatParTicker.put(ticker, prixExecution);
                    } else if (prixExecution < 0.0) {
                        prixVenteParTicker.put(ticker, prixExecution);
                    }
                }
            }

            // 2) On reconstruit le montant investi à partir des seules lignes non revendues.
            // Si achat+vente existent pour un ticker, l'actif est considéré soldé.
            for (Transaction transaction : transactions) {
                String ticker = transaction.getTickerActif();
                if (ticker != null && !ticker.isBlank()) {
                    boolean actifVendu =
                            prixAchatParTicker.containsKey(ticker)
                                    && prixVenteParTicker.containsKey(ticker);
                    if (actifVendu) {
                        tickersActifsVendus.add(ticker);
                    } else {
                        montantTotalInvesti += transaction.getMontantTotal();
                    }
                }
            }

            // 3) Valeur courante des positions encore détenues (on retire les actifs vendus).
            double valeurCouranteTotale = 0.0;
            for (Position position : positions.values()) {
                boolean actifDefini = position.getActif() != null;
                if (actifDefini) {
                    String ticker = position.getActif().getTicker();
                    if (!tickersActifsVendus.contains(ticker)) {
                        valeurCouranteTotale += position.getValeurCourante();
                    }
                }
            }

            // 4) Pour les actifs vendus: PV/MV = prix de vente - prix d'achat.
            // Le prix de vente est stocké négatif dans le modèle, d'où la valeur absolue.
            double plusValueActifsVendus = 0.0;
            for (String ticker : tickersActifsVendus) {
                double prixAchat = prixAchatParTicker.get(ticker);
                double prixVente = prixVenteParTicker.get(ticker);
                plusValueActifsVendus += Math.abs(prixVente) - prixAchat;
            }

            // 5) PV/MV brute totale = PV/MV du portefeuille encore ouvert + PV/MV des lignes soldées.
            return (valeurCouranteTotale - montantTotalInvesti) + plusValueActifsVendus;
        }

        // Fallback quand aucune transaction n'est disponible: on somme les PV latentes des positions.
        double plusValueLatenteTotale = 0.0;
        for (Position position : positions.values()) {
            plusValueLatenteTotale += position.getPlusValueLatente();
        }
        return plusValueLatenteTotale;
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

    public void afficherPositionsLambda() {
        System.out.println("Positions détenues dans l'enveloppe  " + this.numero + " :");
        if (this.positions.isEmpty()) {
            System.out.println("Aucune position enregistrée.");
            return;
        }
        this.positions.forEach((ticker, p) -> System.out.println(" - " + ticker + " : " + p));
    }

    public double getValeurTotaleBruteLambda() {
        double valeurCourante = this.soldeEspeces;
        double sumValeurCourante = this.positions.values()
                .stream()
                .mapToDouble(Position::getValeurCourante)
                .sum();
        valeurCourante += sumValeurCourante;
        return valeurCourante;
    }

    public void afficherPositionsImportantes() {
        double valeurBrute = this.getValeurTotaleBruteLambda();
        double seuil = 0.05 * valeurBrute;
        System.out.println("Positions importantes de l'enveloppe " + this.numero + " :");
        this.positions.values()
                .stream()
                .filter(p -> p.getValeurCourante() > seuil)
                .forEach(p -> System.out.println(" - " + p));
    }

    public List<Position> getPositionsImportantes() {
        double valeurBrute = this.getValeurTotaleBruteLambda();
        double seuil = 0.05 * valeurBrute;
        List<Position> positionsImportantes = this.positions.values()
                .stream()
                .filter(p -> p.getValeurCourante() > seuil)
                .collect(Collectors.toList());
        return positionsImportantes;
    }

    public void afficherPositionsParPays(String pays) {
        positions.values()
                .stream()
                .filter(p -> p.getActif().getPays().equals(pays))
                .forEach(System.out::println);
    }

    public List<Position> lesPositionsParPays(String pays) {
        List<Position> resultats = this.positions.values()
                .stream()
                .filter(p -> p.getActif().getPays().equals(pays))
                .collect(Collectors.toList());
        return resultats;
    }

    private void afficherBlocAnalyse(String titre, List<Position> positions) {
        if (positions.size() > 0) {
            System.out.println("\n" + titre + " :");
            positions.forEach(System.out::println);
        }
    }

    public void afficherAnalysePortefeuille() {
        System.out.println("Analyse du portefeuille n°" + this.numero);
        System.out.println("Valeur totale brute : " + this.getValeurTotaleBruteLambda());
        System.out.println("Nombres de positions : " + this.positions.size());
        System.out.println("Plus/Moins value latente : " + this.getPlusValueLatenteBruteTotale());

        afficherBlocAnalyse("Positions importantes", getPositionsImportantes());
        afficherBlocAnalyse("Positions françaises", lesPositionsParPays("France"));
        afficherBlocAnalyse("Positions américaines", lesPositionsParPays("USA"));
    }
}
