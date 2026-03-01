package broker.financier;

import broker.financier.enums.Devise;
import broker.financier.enums.TypeDistribution;
import broker.financier.enums.TypeOrdre;
import broker.financier.enums.TypeReplication;
import broker.financier.model.Adresse;
import broker.financier.model.Client;
import broker.financier.model.achatvente.Ordre;
import broker.financier.model.achatvente.Position;
import broker.financier.model.achatvente.Transaction;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.Action;
import broker.financier.model.actifs.ETF;
import broker.financier.model.actifs.Obligation;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    static void main() {
        //Tests basiques
        
        Adresse adresse1Client1 = new Adresse("5", "Rue des Fleurs", "Appartement 333", "42000", "France");

        Client client1 = new Client(1, "JULLIEN", "Mathieu");

        Actif actionAirbus = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        Actif etfCac40 = new ETF("CAC40", "Lyxor CAC40", 32.50, "France", Devise.EUR, 15000, 32, TypeDistribution.CAPITALISANT, TypeReplication.PHYSIQUE);
        Actif obligationEtat = new Obligation("OAT10", "Obligation État FR", 100.0, "France", Devise.EUR, 10);

        Position pActionAirbus = new Position(10, actionAirbus);
        Position pEtfCac40 = new Position(20, etfCac40);
        Position pObligationEtat = new Position(30, obligationEtat);

        Enveloppe eclient1 = new PEA("123", LocalDateTime.now(), 1000, 5);

        Ordre o1 = new Ordre(100, Devise.EUR, 10, TypeOrdre.ACHAT, LocalDateTime.now(), actionAirbus);
        Ordre o2 = new Ordre(100, Devise.EUR, 10, TypeOrdre.VENTE, LocalDateTime.now(), etfCac40);

        Transaction t1 = new Transaction(100, 300, LocalDateTime.now());
        Transaction t2 = new Transaction(200, 400, LocalDateTime.now());

        System.out.println("=== Adresse ===");
        System.out.println(adresse1Client1);

        System.out.println("\n=== Client ===");
        System.out.println(client1);

        System.out.println("\n=== Actifs ===");
        System.out.println(actionAirbus);
        System.out.println(etfCac40);
        System.out.println(obligationEtat);

        System.out.println("\n=== Enveloppes ===");
        System.out.println(eclient1);

        System.out.println("\n=== Tests equals() ===");
        Actif actionAirbus2 = new Action("AIR", "Airbus", 145.20, "France", Devise.EUR);
        System.out.println("actionAirbus.equals(actionAirbus2) : " + actionAirbus.equals(actionAirbus2));
        System.out.println("\n=== Tests instanceof ===");
        System.out.println("actionAirbus instanceof Actif : " + (actionAirbus instanceof Actif));
        System.out.println("etfCac40 instanceof ETF : " + (etfCac40 instanceof ETF));
        System.out.println("obligationEtat instanceof Action : " + (obligationEtat instanceof Action));

        System.out.println("\n=== Test afficherAdresses() ===");
        client1.ajouterAdresse(adresse1Client1);
        client1.afficherAdresses();

        System.out.println("\n=== Test afficherEnveloppes() ===");
        client1.ajouterEnveloppe(eclient1);
        client1.afficherEnveloppes();

        System.out.println("\n=== Test rechercherActif(String ticker) ===");
        eclient1.ajouterPosition(pActionAirbus);
        eclient1.ajouterPosition(pEtfCac40);
        eclient1.ajouterPosition(pObligationEtat);
        System.out.println("Recherche Airbus : " + eclient1.rechercherActif("AIR"));
        System.out.println("Recherche CAC40 : " + eclient1.rechercherActif("CAC40"));
        System.out.println("Recherche Etat : " + eclient1.rechercherActif("OAT10"));

        System.out.println("\n=== Test afficherPositions() ===");
        eclient1.afficherPositions();

        System.out.println("\n=== Test afficherOrdresEnCours() ===");
        eclient1.ajouterOrdre(o1);
        eclient1.ajouterOrdre(o2);
        eclient1.afficherOrdresEnCours();

        System.out.println("\n=== Test afficherTransactions() ===");
        eclient1.ajouterTransaction(t1);
        eclient1.ajouterTransaction(t2);
        eclient1.afficherTransactions();


        // Tests complets
        /*
        List<Client> clients = BrokerDataFactory.creerClientsFictifs();

        for (Client client : clients) {
            afficherClient(client);
        }
        */
    }

    private static void afficherClient(Client client) {
        System.out.println("==============================================");
        System.out.println("Client #" + client.getId() + " - " + client.getPrenom() + " " + client.getNom());
        System.out.println("Adresses:");
        for (Adresse adresse : client.getAdresses()) {
            System.out.println("  - " + adresse);
        }

        System.out.println("Enveloppes:");
        for (Enveloppe enveloppe : client.getEnveloppes()) {
            System.out.println("  * " + enveloppe.getClass().getSimpleName() + " (" + enveloppe.getNumero() + ")");

            System.out.println("    Positions:");
            for (Position position : enveloppe.getPositions().values()) {
                System.out.println("      - " + position);
            }

            System.out.println("    Ordres en cours:");
            for (Ordre ordre : enveloppe.getOrdresEnCours()) {
                System.out.println("      - " + ordre);
            }
        }
    }
}
