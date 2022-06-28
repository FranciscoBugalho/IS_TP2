package book.data;

import com.github.javafaker.Faker;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateCompanyManagers {

    private static final Logger logger = LoggerFactory.getLogger(CreateCompanyManagers.class);

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripsAndUsers");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        Faker fk = new Faker();
        Random rand = new Random();
        List<CompanyManager> companyManagers = new ArrayList<>();

        // Encrypts Password
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm("SHA-1");
        passwordEncryptor.setPlainDigest(true);

        for (int i = 0; i < CompanyManagerConstants.N_COMPANY_MANAGERS; i++) {
            fk = personRegistered(fk);
            String encryptedPassword = passwordEncryptor.encryptPassword(CompanyManagerConstants.passwords[rand.nextInt(CompanyManagerConstants.passwords.length)]);
            companyManagers.add(new CompanyManager(fk.name().username(), fk.internet().emailAddress(), encryptedPassword));
        }

        et.begin();
        for (CompanyManager c : companyManagers) {
            logger.info("Registering Company Manager: " + c.getName());
            em.persist(c);
        }
        et.commit();
    }

    private static Faker personRegistered(Faker person) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripsAndUsers");
        EntityManager em = emf.createEntityManager();
        Client client;
        boolean exists = true;

        while (exists) {
            try {
                Query q = em.createQuery("FROM Client c WHERE c.email = :email").setParameter("email", person.internet().emailAddress());
                client = (Client) q.getSingleResult();
            } catch (NoResultException ex) {
                logger.warn("Error while checking email while registering a Company Manager! (NoResultException exception) ");
                break;
            }

            if (client != null) {
                logger.info("Email already registered (" + person.internet().emailAddress() + ")");
                person = new Faker();
            } else {
                exists = false;
            }
        }
        return person;
    }
}
