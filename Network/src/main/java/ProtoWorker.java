import Domain.Game;
import Domain.Seller;
import Domain.Ticket;
import protocol.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class ProtoWorker implements Runnable, IClient {
    private IServer server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;
    public ProtoWorker(IServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                System.out.println("Waiting requests ...");
                protocol.Protocol.Request request = protocol.Protocol.Request.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                protocol.Protocol.Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    public void refresh(Game game ) throws IOException {
        try {
            sendResponse(ProtoUtils.createRefreshResponse(game));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private protocol.Protocol.Response handleRequest(protocol.Protocol.Request request){
        protocol.Protocol.Response response=null;
        switch (request.getType()){
            case Login:{
                System.out.println("Login request ...");
                Seller seller = ProtoUtils.getSeller(request);
                try {
                    Seller aux ;
                    aux = server.login(seller, this);
                    return ProtoUtils.createOkLoginResponse(aux);
                } catch (Exception e) {
                    connected=false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case Logout:{
                System.out.println("Logout request");
                Seller seller = ProtoUtils.getSeller(request);
                try {
                    server.logout(seller, this);
                    connected=false;
                    return ProtoUtils.createOkResponse();

                } catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case SellTicket:{
                Ticket ticket = ProtoUtils.getTicket(request);
                try {
                    server.sellTicket(ticket.getClientName(),ticket.getNrOfSeats(),ticket.getGameId());
                    return ProtoUtils.createOkResponse();
                } catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GetGames:{
                try {
                    List<Game> games = server.getGames();
                    return ProtoUtils.createGetGamesResponse(games);
                } catch (Exception e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }

    private void sendResponse(Protocol.Response response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        output.flush();
    }
}
