import Domain.Game;
import Domain.Seller;

import java.util.List;

public interface IServer {
    Seller login(Seller seller, IClient client) throws Exception;
    void logout(Seller seller, IClient client) throws Exception;
    List<Game> getGames() throws Exception;
    void sellTicket(String clientName, int nrOfSeats, int idGame) throws Exception;
}
