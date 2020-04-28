import Domain.Game;
import Domain.Seller;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ClientCtrl implements IClient {
    private GamesModel list;
    private IServer server;
    private Seller seller;

    public ClientCtrl(IServer server) {
        this.server = server;
        seller = null;
    }

    public GamesModel getGamesModel(){return list;}

    public void logout() {
        try {
            System.out.println("------------------------------------------- trimit seller de logout");
            System.out.println(seller.getId() + seller.getUsername() + seller.getPassword());
            server.logout(seller, this);
            seller = null;
        } catch (Exception e) {
            System.out.println("Logout error "+e);
        }
    }

    public void login(String username, String pass) throws Exception {
        Seller sel = new Seller(username,pass);
        System.out.println("trimit seller" + username + pass);
        seller = server.login(sel,this);
        System.out.println(seller.getId() + seller.getUsername() + seller.getPassword());
    }

    public void sellTicket(String clientName,int nrOfSeats,int idGame) throws Exception{
        server.sellTicket(clientName,nrOfSeats,idGame);
    }

    public List<Game> getGames() throws Exception {
        return server.getGames();
    }

    @Override
    public void refresh(Game game) throws IOException {

    }
}
