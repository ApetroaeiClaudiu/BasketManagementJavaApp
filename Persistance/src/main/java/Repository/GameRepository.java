package Repository;

import Domain.Game;
import Domain.TypeOfGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameRepository implements CRUDRepository<Integer, Game> {
    private JDBCUtils jdbcutils;
    private static final Logger logger = LogManager.getLogger();

    public GameRepository(Properties props) {
        logger.info("Initializing Game Repository with properties:{}",props);
        jdbcutils = new JDBCUtils(props);
    }

    @Override
    public void add(Game entity) {
        logger.traceEntry("Adding Game {} ",entity);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Games values (?,?,?,?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getHomeTeam());
            preStmt.setString(3,entity.getAwayTeam());
            preStmt.setString(4,entity.getType().toString());
            preStmt.setInt(5,entity.getTotalNrOfSeats());
            preStmt.setInt(6,entity.getNrOfEmptySeats());
            preStmt.setFloat(7,entity.getPrice());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Game with {}",integer);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Games where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Game entity) {
        logger.traceEntry("updating Game with id {}",integer);
        Connection con = jdbcutils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE Games SET id=?, homeTeam=?, awayTeam=?, type=?, nrOfSeats=?,emptySeats=?,price=? WHERE id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getHomeTeam());
            preStmt.setString(3,entity.getAwayTeam());
            preStmt.setString(4,entity.getType().toString());
            preStmt.setInt(5,entity.getTotalNrOfSeats());
            preStmt.setInt(6,entity.getNrOfEmptySeats());
            preStmt.setFloat(7,entity.getPrice());
            preStmt.setInt(8,integer);
            int result = preStmt.executeUpdate();
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB"+ex);
        }
        logger.traceExit();
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Games")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public Game findOne(Integer integer) {
        logger.traceEntry("finding Game with id {} ",integer);
        Connection con=jdbcutils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Games where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    TypeOfGame type = TypeOfGame.valueOf(result.getString("type"));
                    int nrOfSeats = result.getInt("nrOfSeats");
                    int emptySeats = result.getInt("emptySeats");
                    float price = result.getFloat("price");
                    Game game = new Game(id,homeTeam,awayTeam,type,nrOfSeats,emptySeats,price);
                    logger.traceExit(game);
                    return game;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No Game found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Game> findAll() {
        Connection con=jdbcutils.getConnection();
        List<Game> games = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Games")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    TypeOfGame type = TypeOfGame.valueOf(result.getString("type"));
                    int nrOfSeats = result.getInt("nrOfSeats");
                    int emptySeats = result.getInt("emptySeats");
                    float price = result.getFloat("price");
                    Game game = new Game(id,homeTeam,awayTeam,type,nrOfSeats,emptySeats,price);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(games);
        return games;
    }
}
