import java.io.IOException;
import java.util.Properties;

public class Client {
    private static int defaultChatPort=55560;
    private static String defaultServer="localhost";

    public static void main(String[] args) {

        Properties clientProps=new Properties();
        try {
            clientProps.load(Client.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);

        IServer server=new ProtoProxy(serverIP, serverPort);
        ClientCtrl ctrl=new ClientCtrl(server);


        LoginWindow logWin=new LoginWindow("Chat XYZ", ctrl);
        logWin.setSize(200,200);
        logWin.setLocation(150,150);
        logWin.setVisible(true);

    }
}
