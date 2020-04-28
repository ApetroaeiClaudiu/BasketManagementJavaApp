package Domain;

public class Ticket extends Entity<Integer>{
    private int gameId;
    private String clientName;
    private int nrOfSeats;

    public int getGameId() {
        return gameId;
    }

    public String getClientName() {
        return clientName;
    }

    public int getNrOfSeats() {
        return nrOfSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "gameId=" + gameId +
                ", clientName='" + clientName + '\'' +
                ", nrOfSeats=" + nrOfSeats +
                '}';
    }

    public Ticket(int id, int gameId, String clientName, int nrOfSeats) {
        setId(id);
        this.gameId = gameId;
        this.clientName = clientName;
        this.nrOfSeats = nrOfSeats;
    }
    public Ticket(String client,int nr,int idgame){
        this(-99,idgame,client,nr);
    }
}
