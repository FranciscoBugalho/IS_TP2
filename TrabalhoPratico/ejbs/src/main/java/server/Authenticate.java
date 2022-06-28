package server;

import book.data.Client;
import book.data.Traveler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Remote(IAuthenticate.class)
public class Authenticate implements IAuthenticate {
    Logger logger = LoggerFactory.getLogger(Authenticate.class);

    @PersistenceContext(unitName = "TripsAndUsers")
    EntityManager em;

    public Authenticate() {
        logger.info("Created Authenticate");
    }

    public String registerClient(String name, String email, String password) {      // Req. 1
        Client client = null;
        logger.info("Registering client: " + name);
        try {

            Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", email);
            client = (Client) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while checking email while registering! (NoResultException exception) ");
        }

        if (client != null) {
            logger.info("That email is already registered!");
            return "That email is already registered!";
        }

        Traveler traveler = new Traveler(name, email, password);
        em.persist(traveler);
        logger.info("Client " + name + " successfully registered!");
        return "Client " + name + " successfully registered!";
    }

    public List<String> authenticate(String email, String password) {     // Req. 4
        List<String> result = new ArrayList<>();
        Client client = null;
        logger.info("Trying to authenticate: " + email);

        try {
            Query q = em.createQuery("FROM Client c WHERE c.email = :email AND c.password = :password").setParameter("email", email).setParameter("password", password);
            client = (Client) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.warn("Error while checking email/password while authenticating! (NoResultException exception) ");
        }

        if (client == null) {
            logger.info("Login Failed. Wrong credentials! For email: " + email);
            result.add("Error");
            result.add("Login Failed. Wrong credentials!");
        } else if (client.isCompanyManager()) {
            logger.info("Company Manager " + client.getName() + " has just authenticated!");
            result.add("CompanyManager");
            result.add(client.getName());
        } else {
            logger.info("Traveler " + client.getName() + " has just authenticated!");
            result.add("Traveler");
            result.add(client.getName());
            Traveler aux = (Traveler) client;
            result.add(String.valueOf(aux.getWallet()));
        }

        return result;
    }
}
