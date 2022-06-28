package book.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private String password;

    private boolean isCompanyManager;

    public Client() {
    }

    public Client(String name, String email, String password, boolean isCompanyManager) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isCompanyManager = isCompanyManager;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCompanyManager() {
        return isCompanyManager;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isCompanyManager=" + isCompanyManager +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return id == client.id && isCompanyManager == client.isCompanyManager && Objects.equals(name, client.name) && Objects.equals(email, client.email) && Objects.equals(password, client.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, isCompanyManager);
    }
}