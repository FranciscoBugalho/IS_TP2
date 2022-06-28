package book.data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Traveler extends Client implements Serializable {

    private double wallet;

    public Traveler() {
    }

    public Traveler(String name, String email, String password) {
        super(name, email, password, false);
        this.wallet = 0.0;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "Traveler{" +
                "wallet=" + wallet +
                '}';
    }
}