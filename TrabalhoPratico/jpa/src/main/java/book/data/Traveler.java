package book.data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Traveler extends Client implements Serializable {

    private double wallet;

    @ManyToMany(mappedBy = "travelers")
    private List<Trip> trips;

    public Traveler() {
        this.trips = new ArrayList<>();
    }

    public Traveler(String name, String email, String password) {
        super(name, email, password, false);
        this.wallet = 0.0;
        this.trips = new ArrayList<>();
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "Traveler{" +
                "wallet=" + wallet +
                ", trips=" + trips +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Traveler)) return false;
        Traveler traveler = (Traveler) o;
        return Double.compare(traveler.wallet, wallet) == 0 && Objects.equals(trips, traveler.trips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wallet, trips);
    }
}
