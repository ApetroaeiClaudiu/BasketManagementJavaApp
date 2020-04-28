import Domain.Game;
import Domain.Seller;
import Domain.Ticket;
import Service.GameService;
import Service.SellerService;
import Service.TicketService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements IServer {

    private GameService gameService;
    private TicketService ticketService;
    private SellerService sellerService;
    private Map<Integer, IClient> loggedClients;

    public ServerImpl(GameService gameService, TicketService ticketService, SellerService sellerService) {
        this.gameService = gameService;
        this.ticketService = ticketService;
        this.sellerService = sellerService;
        loggedClients = new ConcurrentHashMap<>();
    }


    public synchronized Seller login(Seller seller, IClient client) throws Exception {
        Seller s = null;
        for (Seller sel : sellerService.findAll()) {
            if (sel.getUsername().equals(seller.getUsername()) && sel.getPassword().equals(seller.getPassword())) {
                s = new Seller(sel.getId(),sel.getUsername(),sel.getPassword());
            }
        }
        if (s != null) {
            if (loggedClients.get(s.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(s.getId(), client);
        } else
            throw new Exception("Authentication failed.");
        return s;
    }


    private final int defaultThreadsNo = 5;

    public List<Game> getGames() {
        List<Game> games = gameService.sortDescending();
        return games;
    }

    public synchronized void logout(Seller seller, IClient client) throws Exception {
        System.out.println("------------------------------Deleting client with id : ");
        System.out.println(seller.getId());
        IClient localClient = loggedClients.remove(seller.getId());
        if (localClient == null)
            throw new Exception("User " + seller.getId() + " is not logged in.");
    }

    public void sellTicket(String client, int nr, int idgame) throws Exception {
        int id = ticketService.getBiggestId() + 1;
        Ticket ticket = new Ticket(id, idgame, client, nr);
        ticketService.add(ticket);
        Game game = gameService.findOne(idgame);
        game.nrOfEmptySeats = game.nrOfEmptySeats - nr;
        gameService.update(idgame, game);
        notifyAllRefresh(game);
    }

    private void notifyAllRefresh(Game game) {
        List<Seller> sellers = sellerService.findAll();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Seller us : sellers) {
            IClient client = loggedClients.get(us.getId());
            if (client != null)
                executor.execute(() -> {
                    try {
                        client.refresh(game);
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                });
        }
        executor.shutdown();
    }
}