package Domain;

public class Game extends Entity<Integer>{
    private String homeTeam;
    private String awayTeam;
    private TypeOfGame type;
    private int totalNrOfSeats;
    public int nrOfEmptySeats;
    private float price;

    public String getAvailable(){
        if(nrOfEmptySeats==0) {
            return "SOLD OUT";
        }
        else{
            return "AVAILABLE";
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", type=" + type +
                ", totalNrOfSeats=" + totalNrOfSeats +
                ", nrOfEmptySeats=" + nrOfEmptySeats +
                ", price=" + price +
                '}';
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public TypeOfGame getType() {
        return type;
    }

    public int getTotalNrOfSeats() {
        return totalNrOfSeats;
    }

    public int getNrOfEmptySeats() {
        return nrOfEmptySeats;
    }

    public float getPrice() {
        return price;
    }


    public Game(int id, String homeTeam,String awayTeam, TypeOfGame type, int totalNrOfSeats, int nrOfEmptySeats, float price) {
        setId(id);
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.type = type;
        this.totalNrOfSeats = totalNrOfSeats;
        this.nrOfEmptySeats = nrOfEmptySeats;
        this.price = price;
    }
}
