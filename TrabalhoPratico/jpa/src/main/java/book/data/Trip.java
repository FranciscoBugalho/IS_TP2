package book.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Date departureTime;

    private String departurePoint;

    private String destination;

    private int capacity;

    private double price;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = DataConstants.THIRD_TABLE_NAME,
            joinColumns = @JoinColumn(name = DataConstants.COLUMN_NAME_TRIP),
            inverseJoinColumns = @JoinColumn(name = DataConstants.COLUMN_NAME_CLIENT))
    private List<Traveler> travelers;

    public Trip() {
        this.travelers = new ArrayList<>();
    }

    public Trip(Date departureTime, String departurePoint, String destination, int capacity, double price) {
        this.departureTime = departureTime;
        this.departurePoint = departurePoint;
        this.destination = destination;
        this.capacity = capacity;
        this.price = price;
        this.travelers = new ArrayList<>();
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public int getId() {
        return id;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                ", departurePoint='" + departurePoint + '\'' +
                ", destination='" + destination + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", clients=" + travelers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return id == trip.id && capacity == trip.capacity && Double.compare(trip.price, price) == 0 && Objects.equals(departureTime, trip.departureTime) && Objects.equals(departurePoint, trip.departurePoint) && Objects.equals(destination, trip.destination) && Objects.equals(travelers, trip.travelers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureTime, departurePoint, destination, capacity, price, travelers);
    }
}