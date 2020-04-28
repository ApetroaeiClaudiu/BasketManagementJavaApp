import java.net.Socket;


public class ProtobuffConcurrentServer extends AbsConcurrentServer {
    private IServer chatServer;
    public ProtobuffConcurrentServer(int port, IServer chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Protobuff Concurrent Server Starting");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker=new ProtoWorker(chatServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
}