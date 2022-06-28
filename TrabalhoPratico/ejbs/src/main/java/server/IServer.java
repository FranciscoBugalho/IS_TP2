package server;

import javax.ejb.Remote;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Remote
public interface IServer {
    public String editPersonalInfo(String name, String email, String password);

    public String deleteAccount(String email, String password);
}
