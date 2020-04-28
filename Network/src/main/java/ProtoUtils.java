import Domain.Game;
import Domain.Seller;
import Domain.Ticket;
import Domain.TypeOfGame;
import protocol.Protocol;

import java.util.List;

public class ProtoUtils {
    public static protocol.Protocol.Request createLoginRequest(Seller seller){
        protocol.Protocol.Seller sellerDTO = protocol.Protocol.Seller.newBuilder().setId(seller.getId()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();
        protocol.Protocol.Request request= protocol.Protocol.Request.newBuilder().setType(Protocol.Request.Type.Login)
                .setSeller(sellerDTO).build();
        return request;

    }
    public static protocol.Protocol.Request createLogoutRequest(Seller seller){
        System.out.println("------------------------------------ create logout request");
        System.out.println(seller.getId() + seller.getUsername() + seller.getPassword());
        protocol.Protocol.Seller sellerDTO = protocol.Protocol.Seller.newBuilder().setId(seller.getId()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();
        protocol.Protocol.Request request= protocol.Protocol.Request.newBuilder().setType(Protocol.Request.Type.Logout)
                .setSeller(sellerDTO).build();
        return request;

    }

    public static Protocol.Request createSellTicketRequest(String client,int nr,int gameId){
        Protocol.Ticket ticketDTO = Protocol.Ticket.newBuilder().setClient(client)
                .setNr(nr)
                .setIdgame(gameId).build();
        Protocol.Request request= Protocol.Request.newBuilder()
                .setType(Protocol.Request.Type.SellTicket)
                .setTicket(ticketDTO).build();
        return request;
    }

    public static Protocol.Request createGetGamesRequest (){
        Protocol.Request request= Protocol.Request.newBuilder()
                .setType(Protocol.Request.Type.GetGames).build();
        return request;
    }


    public static Protocol.Response createOkResponse (){
        Protocol.Response response= Protocol.Response.newBuilder()
                .setType(Protocol.Response.Type.Ok).build();
        return response;
    }
    public static Protocol.Response createOkLoginResponse (Seller seller){
        protocol.Protocol.Seller sellerDTO = protocol.Protocol.Seller.newBuilder().setId(seller.getId()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();
        Protocol.Response response= Protocol.Response.newBuilder()
                .setType(Protocol.Response.Type.Ok)
                .setSeller(sellerDTO).build();
        return response;
    }

    public static Protocol.Response createErrorResponse(String text){
        Protocol.Response response=Protocol.Response.newBuilder()
                .setType(Protocol.Response.Type.Error)
                .setError(text).build();
        return response;
    }

    public static Protocol.Response createRefreshResponse(Game game){
        Protocol.Game gameDTO = Protocol.Game.newBuilder().setId(game.getId()).setHomeTeam(game.getHomeTeam()).setAwayTeam(game.getAwayTeam()).setTotalNr(game.getTotalNrOfSeats()).setNrOfEmptySeats(game.getNrOfEmptySeats()).setType(game.getType().toString()).setPrice(game.getPrice()).build();
        Protocol.Response response=Protocol.Response.newBuilder()
                .setType(Protocol.Response.Type.Refresh)
                .setGame(gameDTO).build();
        return response;
    }

    public static Protocol.Response createGetGamesResponse(List<Game> games){
        Protocol.Response.Builder response= Protocol.Response.newBuilder()
                .setType(Protocol.Response.Type.GetGames);
        for(Game game:games){
            Protocol.Game gameDTO = Protocol.Game.newBuilder().setId(game.getId()).setHomeTeam(game.getHomeTeam()).setAwayTeam(game.getAwayTeam()).setTotalNr(game.getTotalNrOfSeats()).setNrOfEmptySeats(game.getNrOfEmptySeats()).setType(game.getType().toString()).setPrice(game.getPrice()).build();
            response.addGames(gameDTO);
        }
        return response.build();
    }

    public static String getError(Protocol.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Seller getSeller(Protocol.Response response){
        Seller seller = new Seller();
        seller.setId(response.getSeller().getId());
        seller.setUsername(response.getSeller().getUsername());
        seller.setPassword(response.getSeller().getPassword());
        return seller;
    }
    public static Seller getSeller(Protocol.Request request){
        Seller seller = new Seller();
        seller.setId(request.getSeller().getId());
        seller.setUsername(request.getSeller().getUsername());
        seller.setPassword(request.getSeller().getPassword());
        System.out.println("------------------------------------ getseller request");
        System.out.println(seller.getId() + seller.getUsername() + seller.getPassword());
        return seller;
    }

    public static Ticket getTicket(Protocol.Request request){
        Ticket ticket = new Ticket(request.getTicket().getClient(),request.getTicket().getNr(),request.getTicket().getIdgame());
        return ticket;
    }

    public static Game[] getGames(Protocol.Response response){
        Game[] games = new Game[response.getGamesCount()];
        for(int i=0;i<response.getGamesCount();i++){
            Protocol.Game game =response.getGames(i);
            Game gameDTO = new Game(game.getId(),game.getHomeTeam(),game.getAwayTeam(), TypeOfGame.valueOf(game.getType()),game.getTotalNr(),game.getNrOfEmptySeats(),game.getPrice());
            games[i] = gameDTO;
        }
        return games;
    }
    public static Game getGame(Protocol.Response response){
        Game game = new Game(response.getGame().getId(),response.getGame().getHomeTeam(),response.getGame().getAwayTeam(),TypeOfGame.valueOf(response.getGame().getType()),response.getGame().getTotalNr(),response.getGame().getNrOfEmptySeats(),response.getGame().getPrice());
        return  game;

    }

}