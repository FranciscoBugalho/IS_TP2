package server;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import book.data.Client;
import book.data.Traveler;
import book.data.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
@Remote(IServer.class)
public class Server implements IServer {
    Logger logger = LoggerFactory.getLogger(Server.class);

    @PersistenceContext(unitName = "TripsAndUsers")
    EntityManager em;

    public Server() {
        logger.info("Created Server");
    }

    public String editPersonalInfo(String name, String email, String password) {      // Req. 6
        Client client = null;
        logger.info("Editing Personal Information of client (email): " + email);

        try {
            if (password == null) {
                Query q = em.createQuery("UPDATE Client c SET c.name = :name WHERE c.email = :email").setParameter("name", name).setParameter("email", email);
                q.executeUpdate();
            } else {
                Query q = em.createQuery("UPDATE Client c SET c.name = :name, c.password = :password WHERE c.email = :email").setParameter("name", name).setParameter("password", password).setParameter("email", email);
                q.executeUpdate();
            }
        } catch (NoResultException ex) {
            logger.warn("Error while checking email while editing personal information! (NoResultException exception) ");
        }

        logger.info("Client " + name + " successfully edited!");
        return "Client " + name + " successfully edited!";
    }

    public String deleteAccount(String email, String password) {      // Req. 7
        Traveler client = null;
        logger.info("Deleting Account of client (email): " + email);

        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email AND c.password = :password").setParameter("email", email).setParameter("password", password);
            client = (Traveler) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while checking email while registering! (NoResultException exception) ");
        }

        if (client == null) {
            logger.info("Account of client (email): " + email + " was not deleted! (Wrong Password)");
            return "Wrong Password";
        }

        Query q0 = em.createNativeQuery("DELETE FROM Client_Trip ct WHERE ct.clients_id = :id").setParameter("id", client.getId());
        q0.executeUpdate();

        Query q1 = em.createQuery("DELETE FROM Client c WHERE c.id = :id").setParameter("id", client.getId());
        q1.executeUpdate();

        logger.info("Client (email): " + email + " successfully deleted from the system!");
        return "Client (email): " + email + " successfully deleted from the system!";
    }

}
