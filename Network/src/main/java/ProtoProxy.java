import Domain.Game;
import Domain.Seller;
import protocol.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IServer {
    private String host;
    private int port;

    private IClient client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<protocol.Protocol.Response> qresponses;
    private volatile boolean finished;
    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Protocol.Response>();
    }

    public Seller login(Seller user, IClient client) throws Exception {
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(user));
        Protocol.Response response=readResponse();
        if (response.getType()==Protocol.Response.Type.Ok){
            this.client=client;
            return ProtoUtils.getSeller(response);
        }
        if (response.getType()==Protocol.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new Exception(errorText);
        }
        return null;
    }

    public void logout(Seller user, IClient client) throws Exception {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        Protocol.Response response=readResponse();
        closeConnection();
        if (response.getType()==Protocol.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
    }

    public void sellTicket(String clientName, int nrOfSeats, int idGame) throws Exception {
        sendRequest(ProtoUtils.createSellTicketRequest(clientName,nrOfSeats,idGame));
        Protocol.Response response=readResponse();
        if (response.getType()==Protocol.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
    }


    public List<Game> getGames() throws Exception {
        sendRequest(ProtoUtils.createGetGamesRequest());
        Protocol.Response response=readResponse();
        if (response.getType()==Protocol.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
        Game[] games  = ProtoUtils.getGames(response);
        List<Game> g = new ArrayList<>();
        for(Game game : games){
            g.add(game);
        }
        return g;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Protocol.Request request)throws Exception{
        try {
            System.out.println("Sending request ..."+request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Protocol.Response readResponse() throws Exception{
        Protocol.Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws Exception{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Protocol.Response updateResponse){
        switch (updateResponse.getType()){
            case Refresh:{
                Game game = ProtoUtils.getGame(updateResponse);
                try {
                    client.refresh(game);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Protocol.Response response=Protocol.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(Protocol.Response.Type type){
        switch (type){
            case Refresh:  return true;
        }
        return false;
    }
}
