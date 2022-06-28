package server;

import book.data.Client;
import book.data.Traveler;
import book.data.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Remote(ITravelerEJB.class)
public class TravelerEJB implements ITravelerEJB {
    Logger logger = LoggerFactory.getLogger(TravelerEJB.class);

    @PersistenceContext(unitName = "TripsAndUsers")
    EntityManager em;

    public TravelerEJB() {
        logger.info("Created Server");
    }

    public String chargeWallet(String email, double money) {      // Req. 9
        logger.info("Charging wallet of client (email): " + email + " with " + money + "€");

        try {
            Query q = em.createQuery("UPDATE Traveler t SET t.wallet = t.wallet + :money WHERE t.email = :email").setParameter("money", money).setParameter("email", email);
            q.executeUpdate();
        } catch (NoResultException ex) {
            logger.warn("Error while checking email while editing personal information! (NoResultException exception) ");
        }

        logger.info("Client (email) " + email + " successfully charged with " + money + "€");
        return "Client (email) " + email + " successfully charged with " + money + "€";
    }

    public List<String> getAllDestinations(int whatToOrder, String orderBy) {
        logger.info("Getting all destination points!");
        List<Trip> trips = new ArrayList<>();
        List<String> tripsInfo = new ArrayList<>();

        try {
            Query q = switch (whatToOrder) {
                case 0 -> em.createQuery("FROM Trip t WHERE t.departureTime >= NOW() ");
                case 1 -> em.createQuery("FROM Trip t WHERE t.departureTime >= NOW() ORDER BY t.destination " + orderBy);
                case 2 -> em.createQuery("FROM Trip t WHERE t.departureTime >= NOW() ORDER BY t.departurePoint " + orderBy);
                case 3 -> em.createQuery("FROM Trip t WHERE t.departureTime >= NOW() ORDER BY t.departureTime " + orderBy);
                case 4 -> em.createQuery("FROM Trip t WHERE t.departureTime >= NOW() ORDER BY t.price " + orderBy);
                default -> null;
            };
            trips = q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while checking trips! (NoResultException exception) ");
            return new ArrayList<>();
        }
        return getTripsWithCapacity(tripsInfo, trips, false);
    }

    public List<String> purchaseTicket(String email, double wallet, String trips) {     // Req. 10
        logger.info("Traveler " + email + " will purchase the trip: " + trips);

        List<String> result = new ArrayList<>();
        List<String> info1 = List.of(trips.split(" > "));
        String departurePoint = info1.get(0);
        String aux = info1.get(info1.size() - 1);
        List<String> info2 = List.of(aux.split(" "));
        String destinationPoint = info2.get(0);
        String time = info2.get(1).substring(1) + " " + info2.get(2).substring(0, 5);
        Timestamp departureTime = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parsedDate = dateFormat.parse(time);
            departureTime = new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            logger.warn("Error parsing the time (" + time + ")! (ParseException exception) ");
        }

        int clientId = -1;
        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            Client c = (Client) q.getSingleResult();
            clientId = c.getId();
            Traveler cTraveler = (Traveler) c;
            wallet = cTraveler.getWallet();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the client id (" + email + ")! (NoResultException exception) ");
            result.add(String.valueOf(wallet));
            result.add("Can't buy this ticket now, try again later!");
            return result;
        }
        Trip trip = null;
        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departurePoint = :departurePoint AND t.destination = :destination AND t.departureTime = :departureTime")
                    .setParameter("departurePoint", departurePoint)
                    .setParameter("destination", destinationPoint)
                    .setParameter("departureTime", departureTime);
            trip = (Trip) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip (" + trips + ")! (NoResultException exception) ");
            result.add(String.valueOf(wallet));
            result.add("Can't buy this ticket now, try again later!");
            return result;
        }

        if (clientId == -1 || trip == null) {
            logger.info("Client Id is -1 or trip is null!");
            result.add(String.valueOf(wallet));
            result.add("Can't buy this ticket now, try again later!");
            return result;
        }

        double money = wallet - trip.getPrice();
        if (money < 0) {
            result.add(String.valueOf(wallet));
            logger.info("Client (email) " + email + " doesn't have enough money to buy the trip " + trip.getId());
            result.add("Charge your wallet to purchase this ticket!");
            return result;
        }

        logger.info("Adding trip to the user (" + email + ")" + "[Client ID: " + clientId + "] [Trip ID: " + trip.getId() + "]");
        Query q = em.createNativeQuery("INSERT INTO client_trip (trips_id, clients_id) VALUES (:tripId, :clientId)")
                .setParameter("tripId", trip.getId())
                .setParameter("clientId", clientId);
        q.executeUpdate();

        logger.info("Removing money (" + trip.getPrice() + " -> wallet = " + money + ") from user (" + email + ")");
        Query q1 = em.createQuery("UPDATE Traveler c SET c.wallet = :wallet WHERE c.email = :email").setParameter("wallet", money).setParameter("email", email);
        q1.executeUpdate();

        result.add(String.valueOf(money));
        result.add("Ticket bought!");
        return result;
    }

    public List<String> getTripsSortedByDate(Timestamp startDateTime, Timestamp endDateTime) {
        logger.info("Searching by trips order by date (" + startDateTime.toString() + " - " + endDateTime.toString() + ")");
        List<Trip> trips = new ArrayList<>();
        List<String> tripsInfo = new ArrayList<>();

        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departureTime BETWEEN :departureTimeStart AND :departureTimeEnd")
                    .setParameter("departureTimeStart", startDateTime)
                    .setParameter("departureTimeEnd", endDateTime);
            trips = (List<Trip>) q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip between dates (" + startDateTime.toString() + " - " + endDateTime.toString() + ") ! (NoResultException exception) ");
            return tripsInfo;
        }

        return getTripsWithCapacity(tripsInfo, trips, true);
    }

    public List<String> getMyTrips(String email) {
        logger.info("Getting all user (" + email + ") trips.");
        List<String> tripInfo = new ArrayList<>();

        int clientId = -1;
        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            Client c = (Client) q.getSingleResult();
            clientId = c.getId();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the client id (" + email + ")! (NoResultException exception) ");
            tripInfo.add("Can't find your trips! Try again later!");
            return tripInfo;
        }

        List<Integer> tripsId = new ArrayList<>();
        try {
            Query q = em.createNativeQuery("SELECT trips_id FROM Client_Trip WHERE clients_id = :clientId").setParameter("clientId", clientId);
            tripsId = q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting client (" + email + ") trips! (NoResultException exception) ");
            tripInfo.add("You never bought a ticket!");
            return tripInfo;
        }

        List<Trip> trips = new ArrayList<>();
        for (Integer i : tripsId) {
            Query q = em.createQuery("FROM Trip t WHERE t.id = :tripId").setParameter("tripId", i);
            Trip tripAux = (Trip) q.getSingleResult();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String strDate = dateFormat.format(tripAux.getDepartureTime());
            tripInfo.add(tripAux.getDeparturePoint() + " > " + tripAux.getDestination() + " (" + strDate + ") " + tripAux.getPrice() + "\t&#8364;");
        }

        return tripInfo;
    }

    private List<String> getTripsWithCapacity(List<String> tripsInfo, List<Trip> trips, boolean showCapacity) {
        int capacity = 0;
        if (trips != null) {
            for (Trip t : trips) {
                try {
                    Query q = em.createNativeQuery("SELECT COUNT(clients_id) FROM Client_Trip WHERE trips_id = :tripId").setParameter("tripId", t.getId());
                    BigInteger o = (BigInteger) q.getSingleResult();
                    capacity = t.getCapacity() - o.intValue();
                } catch (NoResultException ex) {
                    logger.warn("Error while getting the trips and the travelers for trip (" + t.getDeparturePoint() + " - " + t.getDestination() + ")! (NoResultException exception) ");
                    capacity = 0;
                }

                if (capacity > 0) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String strDate = dateFormat.format(t.getDepartureTime());
                    if (!showCapacity)
                        tripsInfo.add(t.getDeparturePoint() + " > " + t.getDestination() + " (" + strDate + ") " + t.getPrice() + "\t&#8364;");
                    else
                        tripsInfo.add(t.getDeparturePoint() + " > " + t.getDestination() + " (" + strDate + ") " + t.getPrice() + "\t&#8364; - " + capacity + " Available Sits");
                }
            }
        }
        return tripsInfo;
    }

    public List<String> getRefundTrips(String email) {
        logger.info("Getting user (" + email + ") refund trips.");
        List<String> tripInfo = new ArrayList<>();

        int clientId = -1;
        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            Client c = (Client) q.getSingleResult();
            clientId = c.getId();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the client id (" + email + ")! (NoResultException exception) ");
            tripInfo.add("Can't find your trips! Try again later!");
            return tripInfo;
        }

        List<Integer> tripsId = new ArrayList<>();
        try {
            Query q = em.createNativeQuery(
                            "SELECT trips_id FROM Client_Trip  ct, Trip t WHERE ct.clients_id = :clientId AND ct.trips_id = t.id AND t.departuretime > NOW()")
                    .setParameter("clientId", clientId);
            tripsId = q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting client (" + email + ") trips! (NoResultException exception) ");
            tripInfo.add("You don't have any future trips!");
            return tripInfo;
        }

        List<Trip> trips = new ArrayList<>();
        for (Integer i : tripsId) {
            Query q = em.createQuery("FROM Trip t WHERE t.id = :tripId").setParameter("tripId", i);
            Trip tripAux = (Trip) q.getSingleResult();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strDate = dateFormat.format(tripAux.getDepartureTime());
            tripInfo.add(tripAux.getDeparturePoint() + " > " + tripAux.getDestination() + " (" + strDate + ") " + tripAux.getPrice() + "\t&#8364;");
        }

        return tripInfo;
    }

    public List<String> refundTicket(String email, String trips) {     // Req. 11
        logger.info("Traveler " + email + " will refund the trip: " + trips);

        List<String> result = new ArrayList<>();
        List<String> info1 = List.of(trips.split(" > "));
        String departurePoint = info1.get(0);
        String aux = info1.get(info1.size() - 1);
        List<String> info2 = List.of(aux.split(" "));
        String destinationPoint = info2.get(0);
        String time = info2.get(1).substring(1) + " " + info2.get(2).substring(0, 5);
        Timestamp departureTime = null;
        double wallet = 0.0;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parsedDate = dateFormat.parse(time);
            departureTime = new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) { //this generic but you can control another types of exception
            logger.warn("Error parsing the time (" + time + ")! (ParseException exception) ");
        }

        int clientId = -1;
        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            Client c = (Client) q.getSingleResult();
            clientId = c.getId();
            Traveler cTraveler = (Traveler) c;
            wallet = cTraveler.getWallet();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the client id (" + email + ")! (NoResultException exception) ");
            result.add(String.valueOf(wallet));
            result.add("Can't refund this ticket now, try again later!");
            return result;
        }
        Trip trip = null;
        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departurePoint = :departurePoint AND t.destination = :destination AND t.departureTime = :departureTime")
                    .setParameter("departurePoint", departurePoint)
                    .setParameter("destination", destinationPoint)
                    .setParameter("departureTime", departureTime);
            trip = (Trip) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip (" + trips + ")! (NoResultException exception) ");
            result.add(String.valueOf(wallet));
            result.add("Can't refund this ticket now, try again later!");
            return result;
        }

        if (clientId == -1 || trip == null) {
            logger.info("Client Id is -1 or trip is null!");
            result.add(String.valueOf(wallet));
            result.add("Can't refund this ticket now, try again later!");
            return result;
        }

        double money = wallet + trip.getPrice();

        logger.info("Removing user (" + email + ") from trip: " + "[Client ID: " + clientId + "] [Trip ID: " + trip.getId() + "]");
        Query q = em.createNativeQuery("DELETE FROM client_trip ct WHERE ct.clients_id = :idClient AND ct.trips_id = :idTrip")
                .setParameter("idClient", clientId)
                .setParameter("idTrip", trip.getId());
        q.executeUpdate();

        logger.info("Adding money (" + trip.getPrice() + " -> wallet = " + money + ") from user (" + email + ")");
        Query q1 = em.createQuery("UPDATE Traveler c SET c.wallet = :wallet WHERE c.email = :email").setParameter("wallet", money).setParameter("email", email);
        q1.executeUpdate();

        result.add(String.valueOf(money));
        result.add("Ticket refunded!");
        return result;
    }
}
