package book.data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class CompanyManager extends Client implements Serializable {

    public CompanyManager() {
    }

    public CompanyManager(String name, String email, String password) {
        super(name, email, password, true);
    }

}
