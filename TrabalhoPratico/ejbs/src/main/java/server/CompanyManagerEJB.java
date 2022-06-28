package server;

import book.data.Client;
import book.data.Traveler;
import book.data.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Remote(ICompanyManagerEJB.class)
public class CompanyManagerEJB implements ICompanyManagerEJB {
    Logger logger = LoggerFactory.getLogger(CompanyManagerEJB.class);

    @PersistenceContext(unitName = "TripsAndUsers")
    EntityManager em;

    public CompanyManagerEJB() {
        logger.info("Created Company Manager EJB");
    }

    public String createBusTrip(String email, String departurePoint, String destinationPoint, Date departureTime, int capacity, double price) {
        if (!isCompanyManager(email)) {
            logger.info("Traveler (" + email + ") trying to use Company Manager functionalities");
            return "You can't do this actions!!";
        }

        Trip trip = new Trip(departureTime, departurePoint, destinationPoint, capacity, price);
        em.persist(trip);
        logger.info("Trip (" + departurePoint + " - " + destinationPoint + ") successfully saved!");

        return "Trip successfully created!";
    }

    public List<String> getDeleteTrips(String email) {
        logger.info("Getting Company Manager (email) " + email + " trips for deletion.");
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

        List<Trip> trips = new ArrayList<>();
        Query q = em.createQuery("FROM Trip t WHERE t.departureTime > NOW()");
        trips = q.getResultList();

        for (Trip i : trips) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strDate = dateFormat.format(i.getDepartureTime());
            tripInfo.add(i.getDeparturePoint() + " > " + i.getDestination() + " (" + strDate + ") " + i.getPrice() + "\t&#8364;");
        }

        return tripInfo;
    }

    public List<String> deleteTrip(String email, String trips) {     // Req. 14
        logger.info("Company Manager " + email + " will delete the trip: " + trips);

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

        Trip trip = null;
        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departurePoint = :departurePoint AND t.destination = :destination AND t.departureTime = :departureTime")
                    .setParameter("departurePoint", departurePoint)
                    .setParameter("destination", destinationPoint)
                    .setParameter("departureTime", departureTime);
            trip = (Trip) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip (" + trips + ")! (NoResultException exception) ");
            result.add("Couldn't refund this trip now, try again later!");
            return result;
        }

        if (trip == null) {
            logger.warn("Trip is null!");
            result.add("Couldn't refund this trip now, try again later!");
            return result;
        }

        try {
            logger.info("Refunding (" + trip.getPrice() + " euros) all travelers!");
            for (Traveler traveler : trip.getTravelers()) {
                Query q1 = em.createQuery("UPDATE Traveler t SET t.wallet = t.wallet + :tripPrice WHERE t.id = :travelerId")
                        .setParameter("tripPrice", trip.getPrice())
                        .setParameter("travelerId", traveler.getId());
                q1.executeUpdate();
                sendEmail(traveler.getEmail(), "ptjfIntSys@gmail.com",
                        "Refund of trip " + trip.getDestination() + " > " + trip.getDeparturePoint(),
                        "Dear customer " + traveler.getName() + "\nYour trip " + trip.getDestination() + " > " + trip.getDeparturePoint() +
                                " has been canceled. " + trip.getPrice() + "€ have been credited to your wallet.\n" +
                                "\n\nRegards,\nISassignment2"
                );
            }
        } catch (Exception ex) {
            logger.warn("Error while refunding all client's wallet! (NoResultException exception) ");
            result.add("Couldn't refund this trip now, try again later!");
            return result;
        }

        logger.info("Removing users from trip: " + "[Trip ID: " + trip.getId() + "]");
        Query q = em.createNativeQuery("DELETE FROM client_trip ct WHERE ct.trips_id = :idTrip")
                .setParameter("idTrip", trip.getId());
        q.executeUpdate();

        logger.info("Removing trip from list of available trips: " + "[Trip ID: " + trip.getId() + "]");
        Query q2 = em.createQuery("DELETE FROM Trip t WHERE t.id = :idTrip")
                .setParameter("idTrip", trip.getId());
        q2.executeUpdate();

        result.add("Trip deleted and all tickets have been refunded!");
        return result;
    }

    public List<String> getTop5Travelers(String email) {
        logger.info("Getting Company Manager (email) " + email + " top 5 travelers.");
        List<String> clientInfo = new ArrayList<>();
        List<Integer> clients = new ArrayList<>();
        List<Integer> clientsCount = new ArrayList<>();

        try {
            Query q = em.createNativeQuery("SELECT c.id FROM client_trip ct, trip t, client c WHERE t.departuretime < NOW() AND ct.clients_id = c.id AND t.id = ct.trips_id GROUP BY c.id ORDER BY COUNT(*) DESC");
            clients = q.getResultList();
            q = em.createNativeQuery("SELECT COUNT(*) FROM client_trip ct, trip t, client c WHERE t.departuretime < NOW() AND ct.clients_id = c.id AND t.id = ct.trips_id GROUP BY c.id ORDER BY COUNT(*) DESC");
            clientsCount = q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting top 5 travelers! (NoResultException exception) ");
            clientInfo.add("Error while getting top 5 travelers! Try again later!");
            return clientInfo;
        }

        Traveler aux;
        for (int i = 0; i < clients.size(); i++) {
            if (i == 5)
                break;
            Query q1 = em.createQuery("FROM Traveler t WHERE t.id = :idTraveler")
                    .setParameter("idTraveler", clients.get(i));
            aux = (Traveler) q1.getSingleResult();
            clientInfo.add((i + 1) + "º < Name: " + aux.getName() + " | Email: " + aux.getEmail() + " | Number of Trips: " + clientsCount.get(i) + " >");
        }

        return clientInfo;
    }

    public List<String> getTripsBetweenTwoDates(Date startDate, Date endDate) {
        logger.info("Company Manager searching all trips between two dates (" + startDate.toString() + " - " + endDate.toString() + ")");
        List<String> tripInfo = new ArrayList<>();
        List<Trip> trips = new ArrayList<>();

        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departureTime BETWEEN :departureTimeStart AND :departureTimeEnd ORDER BY t.departureTime ASC")
                    .setParameter("departureTimeStart", startDate)
                    .setParameter("departureTimeEnd", endDate);
            trips = (List<Trip>) q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip between dates (" + startDate.toString() + " - " + endDate.toString() + ") ! (NoResultException exception) ");
            return tripInfo;
        }

        for (Trip trip : trips) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strDate = dateFormat.format(trip.getDepartureTime());
            tripInfo.add(trip.getDeparturePoint() + " > " + trip.getDestination() + " (" + strDate + ")");
        }

        return tripInfo;
    }

    public List<String> getTripsOnADate(Date startDate, Date endDate) {
        logger.info("Company Manager searching all trips on a date (" + startDate + ")");
        List<String> tripInfo = new ArrayList<>();
        List<Trip> trips = new ArrayList<>();

        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departureTime BETWEEN :departureTimeStart AND :departureTimeEnd ORDER BY t.departureTime ASC")
                    .setParameter("departureTimeStart", startDate)
                    .setParameter("departureTimeEnd", endDate);
            trips = (List<Trip>) q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip on a date (" + startDate.toString() + ") ! (NoResultException exception) ");
            return tripInfo;
        }

        for (Trip trip : trips) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strDate = dateFormat.format(trip.getDepartureTime());
            tripInfo.add(trip.getDeparturePoint() + " > " + trip.getDestination() + " (" + strDate + ")");
        }

        return tripInfo;
    }

    public List<String> getTravelersOnATrip(String trip) {
        logger.info("Company Manager searching travelers on a trip (" + trip + ")");

        List<String> travelers = new ArrayList<>();
        List<String> info1 = List.of(trip.split(" > "));
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

        Trip tripData = null;
        try {
            Query q = em.createQuery("FROM Trip t WHERE t.departurePoint = :departurePoint AND t.destination = :destination AND t.departureTime = :departureTime")
                    .setParameter("departurePoint", departurePoint)
                    .setParameter("destination", destinationPoint)
                    .setParameter("departureTime", departureTime);
            tripData = (Trip) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while getting the trip (" + trip + ")! (NoResultException exception) ");
            return travelers;
        }

        List<Integer> clientIds = null;
        try {
            Query q = em.createNativeQuery("SELECT DISTINCT clients_id FROM Client_Trip WHERE trips_id = :tripId").setParameter("tripId", tripData.getId());
            clientIds = q.getResultList();
        } catch (NoResultException ex) {
            logger.warn("Error while getting client (trips_id -> " + tripData.getId() + ") trips! (NoResultException exception) ");
            return travelers;
        }

        for (Integer i : clientIds) {
            Query q = em.createQuery("SELECT name FROM Client c WHERE c.id = :clientId").setParameter("clientId", i);
            String name = (String) q.getSingleResult();
            travelers.add(name);
        }

        return travelers;
    }

    private boolean isCompanyManager(String email) {
        Client client = null;
        logger.info("Checking " + email);

        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            client = (Client) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while checking email while verifying Company Manager! (NoResultException exception) ");
        }

        return client != null;
    }

    @Resource(mappedName = "java:jboss/mail/TrabalhoPratico")
    private Session mailSession;

    @Asynchronous // To don't block the session until mail is sent
    public void sendEmail(String to, String from, String subject, String content) {

        logger.info("Email sent " + from + " to " + to + " : " + subject);
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            logger.info("Email sent");
        } catch (MessagingException e) {
            logger.error("Error sending email : " + e.getMessage());
        }
    }
}
