package server;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface IAuthenticate {
    public String registerClient(String name, String email, String password);

    public List<String> authenticate(String email, String password);
}
