import Domain.Seller;
import Service.GameService;
import Service.SellerService;
import Service.TicketService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;


public class Server {
    private static int defaultPort=55560;
    public static void main(String[] args) {


        Properties serverProps=new Properties();
        try {
            serverProps.load(Server.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("server.xml");
        GameService gameService = context.getBean(GameService.class);
        TicketService ticketService = context.getBean(TicketService.class);
        SellerService sellerService = context.getBean(SellerService.class);
        IServer serverImpl =new ServerImpl(gameService,ticketService,sellerService);
        for(Seller s : sellerService.findAll()){
            System.out.println(s);
        }
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ProtobuffConcurrentServer(chatServerPort, serverImpl);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }



    }
}
