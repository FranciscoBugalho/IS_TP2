package server;

import book.data.CompanyManager;
import book.data.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DailySummaryEJB {

    @PersistenceContext(unitName = "TripsAndUsers")
    EntityManager em;
    Logger logger = LoggerFactory.getLogger(DailySummaryEJB.class);

    public DailySummaryEJB() {
    }

    // @Schedule(minute = "*", hour = "*/24") | Para efeitos de teste, utilizamos um Schedule de 5 minutos na linha em baixo, em vez de 24horas
    @Schedule(minute = "*/5", hour = "*")
    public void atSchedule() throws InterruptedException {
        Thread.sleep(10000);
        logger.info("Sending daily summary to all Company Managers.");

        List<CompanyManager> cmList = new ArrayList<>();
        Query q = em.createQuery("FROM CompanyManager cm");
        cmList = q.getResultList();

        // Does statistics
        double total = 0;
        StringBuilder summary = new StringBuilder();
        List<Trip> tripList = new ArrayList<>();
        try {
            Query q1 = em.createQuery("FROM Trip t WHERE DATE(t.departureTime) = CURRENT_DATE");
            tripList = q1.getResultList();
        } catch (NoResultException e) {
            logger.info("No trips were made today!");
            for (CompanyManager i : cmList) {
                sendEmail(i.getEmail(), "ptjfIntSys@gmail.com",
                        "Daily Summary",
                        "Dear Company Manager, there were no trips today.\n\nRegards,\nISassignment2"
                );
            }
            return;
        }

        for (Trip t : tripList) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strDate = dateFormat.format(t.getDepartureTime());
            total += t.getPrice() * t.getTravelers().size();
            summary.append("< " + t.getDeparturePoint() + " --> " + t.getDestination() + " (" + strDate + ") " +
                    t.getPrice() + "€ | Travelers: " + t.getTravelers().size() +
                    " | Total Profit: " + t.getPrice() * t.getTravelers().size() + "€ >\n");
        }

        // Sends mail
        for (CompanyManager i : cmList) {
            sendEmail(i.getEmail(), "ptjfIntSys@gmail.com",
                    "Daily Summary",
                    "Dear Company Manager " + i.getName() + "\nHere is today's summary:\n " +
                            summary + "\n Today's total profit: " + total + "€" +
                            "\n\nRegards,\nISassignment2"
            );
        }

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
