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
import broker.financier.model.actifs.Action;
import broker.financier.model.actifs.Actif;
import broker.financier.model.actifs.ETF;
import broker.financier.model.actifs.Obligation;
import broker.financier.model.enveloppes.AssuranceVie;
import broker.financier.model.enveloppes.CTO;
import broker.financier.model.enveloppes.Enveloppe;
import broker.financier.model.enveloppes.PEA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BrokerDataFactory {
    private BrokerDataFactory() {
    }

    public static List<Client> creerClientsFictifs() {
        List<Client> clients = new ArrayList<>();

        clients.add(creerClientAlice());
        clients.add(creerClientBruno());
        clients.add(creerClientChloe());

        return clients;
    }

    private static Client creerClientAlice() {
        Enveloppe pea = new PEA("PEA-AL-001", LocalDateTime.now().minusYears(2), 18000.0);
        Enveloppe cto = new CTO("CTO-AL-002", LocalDateTime.now().minusYears(1), 12000.0);

        Position pAapl = creerPosition(new Action("AAPL", "Apple Inc.", 192.30, "USA", Devise.USD), 15);
        Position pCw8 = creerPosition(new ETF(
                "CW8", "Amundi MSCI World", 510.20, "France", Devise.EUR,
                5500.0,0.38, TypeDistribution.CAPITALISANT, TypeReplication.SYNTHETIQUE), 20);
        Position pOat = creerPosition(new Obligation("FRTR10Y", "OAT France 10 ans", 101.40, "France", Devise.EUR, 2.9), 80);

        pea.ajouterPosition(pCw8);
        pea.ajouterPosition(pOat);
        cto.ajouterPosition(pAapl);

        // Un exemple : Ajoute un ordre d'achat sur l'ETF CW8 dans l'enveloppe PEA passé il y a 2 jours.
        pea.getOrdresEnCours().add(new Ordre(508.0, Devise.EUR, 3, TypeOrdre.ACHAT,
                LocalDateTime.now().minusDays(2), pCw8.getActif()));

        // d'autres exemples
        pea.getOrdresEnCours().add(new Ordre(102.0, Devise.EUR, 10, TypeOrdre.VENTE,
                LocalDateTime.now().minusDays(1), pOat.getActif()));
        cto.getOrdresEnCours().add(new Ordre(190.0, Devise.USD, 5, TypeOrdre.ACHAT,
                LocalDateTime.now().minusHours(18), pAapl.getActif()));

        pea.ajouterTransaction(new Transaction("PEA-001",505.0, 1515.0, LocalDateTime.now().minusMonths(1)));
        cto.ajouterTransaction(new Transaction("CTO-002",188.0, 940.0, LocalDateTime.now().minusWeeks(2)));

        Client client = new Client(1, "Martin", "Alice");
        client.ajouterAdresse(new Adresse("12", "Rue de la Paix", "Appartement 4B", "75002", "France"));
        client.ajouterAdresse(new Adresse("22", "Avenue de l'Opera", "Bureau 3", "75001", "France"));
        client.ajouterEnveloppe(pea);
        client.ajouterEnveloppe(cto);
        alimenterPositionsClient(client);
        return client;
    }

    private static Client creerClientBruno() {
        Enveloppe av = new AssuranceVie("AV-BR-001", LocalDateTime.now().minusYears(5), 45000.0);
        Enveloppe cto = new CTO("CTO-BR-002", LocalDateTime.now().minusYears(3), 22000.0);
        Enveloppe pea = new PEA("PEA-BR-003", LocalDateTime.now().minusYears(2), 14000.0);

        Position pMsft = creerPosition(new Action("MSFT", "Microsoft Corp.", 415.80, "USA", Devise.USD), 12);
        Position pSpy = creerPosition(new ETF(
                "SPY", "SPDR S&P 500 ETF", 562.10, "USA", Devise.USD,
                540000.0, 0.09, TypeDistribution.DISTRIBUANT, TypeReplication.PHYSIQUE), 9);
        Position pUs10y = creerPosition(new Obligation("UST10Y", "US Treasury 10Y", 99.20, "USA", Devise.USD, 4.0), 120);

        av.ajouterPosition(pUs10y);
        cto.ajouterPosition(pMsft);
        pea.ajouterPosition(pSpy);

        av.getOrdresEnCours().add(new Ordre(99.0, Devise.USD, 20, TypeOrdre.ACHAT,
                LocalDateTime.now().minusDays(3), pUs10y.getActif()));
        cto.getOrdresEnCours().add(new Ordre(410.0, Devise.USD, 4, TypeOrdre.VENTE,
                LocalDateTime.now().minusDays(1), pMsft.getActif()));
        pea.getOrdresEnCours().add(new Ordre(560.0, Devise.USD, 2, TypeOrdre.ACHAT,
                LocalDateTime.now().minusHours(20), pSpy.getActif()));

        av.ajouterTransaction(new Transaction("AV-001",98.5, 1970.0, LocalDateTime.now().minusMonths(3)));
        cto.ajouterTransaction(new Transaction("CTO-002",405.0, 1620.0, LocalDateTime.now().minusMonths(2)));
        pea.ajouterTransaction(new Transaction("PEA-003",555.0, 1110.0, LocalDateTime.now().minusWeeks(1)));

        Client client = new Client(2, "Durand", "Bruno");
        client.ajouterAdresse(new Adresse("7", "Rue du Bac", "Maison", "69003", "France"));
        client.ajouterAdresse(new Adresse("105", "Cours Lafayette", "Tour B", "69006", "France"));
        client.ajouterEnveloppe(av);
        client.ajouterEnveloppe(cto);
        client.ajouterEnveloppe(pea);
        alimenterPositionsClient(client);
        return client;
    }

    private static Client creerClientChloe() {
        Enveloppe cto = new CTO("CTO-CH-001", LocalDateTime.now().minusYears(4), 26000.0);
        Enveloppe av = new AssuranceVie("AV-CH-002", LocalDateTime.now().minusYears(2), 30000.0);

        Position pNvo = creerPosition(new Action("NVO", "Novo Nordisk", 119.40, "Danemark", Devise.USD), 18);
        Position pEunl = creerPosition(new ETF(
                "EUNL", "iShares Core MSCI World", 96.70, "Irlande", Devise.EUR,
                82000.0, 0.20, TypeDistribution.CAPITALISANT, TypeReplication.PHYSIQUE), 140);
        Position pBund = creerPosition(new Obligation("DE10Y", "Bund Allemagne 10Y", 97.80, "Allemagne", Devise.EUR, 2.3), 60);

        cto.ajouterPosition(pNvo);
        cto.ajouterPosition(pEunl);
        av.ajouterPosition(pBund);

        cto.getOrdresEnCours().add(new Ordre(118.0, Devise.USD, 6, TypeOrdre.ACHAT,
                LocalDateTime.now().minusDays(4), pNvo.getActif()));
        cto.getOrdresEnCours().add(new Ordre(95.0, Devise.EUR, 20, TypeOrdre.ACHAT,
                LocalDateTime.now().minusDays(2), pEunl.getActif()));
        av.getOrdresEnCours().add(new Ordre(98.0, Devise.EUR, 12, TypeOrdre.VENTE,
                LocalDateTime.now().minusHours(30), pBund.getActif()));

        cto.ajouterTransaction(new Transaction("CTO-001",117.5, 705.0, LocalDateTime.now().minusMonths(4)));
        cto.ajouterTransaction(new Transaction("CTO-002",94.8, 1896.0, LocalDateTime.now().minusWeeks(3)));
        av.ajouterTransaction(new Transaction("AV-003",98.1, 1177.2, LocalDateTime.now().minusDays(10)));

        Client client = new Client(3, "Petit", "Chloe");
        client.ajouterAdresse(new Adresse("3", "Rue des Lilas", "Appartement 12", "33000", "France"));
        client.ajouterAdresse(new Adresse("88", "Quai des Chartrons", "Bureau 5", "33300", "France"));
        client.ajouterEnveloppe(cto);
        client.ajouterEnveloppe(av);
        alimenterPositionsClient(client);
        return client;
    }

    public static Position creerPosition(Actif actif, double quantite) {
        Position position = new Position(quantite, actif, actif.getPrixCourant());
        position.setActif(actif);
        return position;
    }

    public static void alimenterPositionsClient(Client client) {
        for (Enveloppe enveloppe : client.getEnveloppes()) {
            for (Position position : enveloppe.getPositions().values()) {
                enveloppe.ajouterPosition(position);
            }
        }
    }
}
