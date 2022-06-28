package server;

import javax.ejb.Remote;
import java.util.Date;
import java.util.List;

@Remote
public interface ICompanyManagerEJB {
    public String createBusTrip(String email, String departurePoint, String destinationPoint, Date departureTime, int capacity, double price);

    public List<String> getDeleteTrips(String email);

    public List<String> deleteTrip(String email, String trips);

    public List<String> getTop5Travelers(String email);

    public List<String> getTripsBetweenTwoDates(Date startDate, Date endDate);

    public List<String> getTripsOnADate(Date startDate, Date endDate);

    public List<String> getTravelersOnATrip(String trip);
}
