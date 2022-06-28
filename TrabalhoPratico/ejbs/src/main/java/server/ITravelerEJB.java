package server;

import java.sql.Timestamp;
import java.util.List;

public interface ITravelerEJB {
    public String chargeWallet(String email, double money);

    public List<String> getAllDestinations(int whatToOrder, String orderBy);

    public List<String> purchaseTicket(String email, double wallet, String trips);

    public List<String> getTripsSortedByDate(Timestamp startDateTime, Timestamp endDateTime);

    public List<String> getMyTrips(String email);

    public List<String> getRefundTrips(String email);

    public List<String> refundTicket(String email, String trips);
}
