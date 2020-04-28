package Repository;

import Domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketRepository implements CRUDRepository<Integer, Ticket> {
    private JDBCUtils jdbcutils;
    private static final Logger logger = LogManager.getLogger();

    public TicketRepository(Properties props) {
        logger.info("Initializing Ticket Repository with properties:{}",props);
        jdbcutils = new JDBCUtils(props);
    }

    @Override
    public void add(Ticket entity) {
        logger.traceEntry("Adding Ticket {} ",entity);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Tickets values (?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getGameId());
            preStmt.setString(3,entity.getClientName());
            preStmt.setInt(4,entity.getNrOfSeats());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Ticket with {}",integer);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Tickets where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Ticket entity) {
        logger.traceEntry("updating Ticket with id {}",integer);
        Connection con = jdbcutils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE Tickets SET id=?, gameId=?, client=?, nrOfSeats=? WHERE id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getGameId());
            preStmt.setString(3,entity.getClientName());
            preStmt.setInt(4,entity.getNrOfSeats());
            preStmt.setInt(5,integer);
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
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Tickets")) {
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
    public Ticket findOne(Integer integer) {
        logger.traceEntry("finding Ticket with id {} ",integer);
        Connection con=jdbcutils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Tickets where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    int gameid = result.getInt("gameId");
                    String client = result.getString("client");
                    int nr = result.getInt("nrOfSeats");
                    Ticket ticket = new Ticket(id,gameid,client,nr);
                    logger.traceExit(ticket);
                    return ticket;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No Ticket found with id {}", integer);
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        Connection con=jdbcutils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Tickets")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int gameid = result.getInt("gameId");
                    String client = result.getString("client");
                    int nr = result.getInt("nrOfSeats");
                    Ticket ticket = new Ticket(id,gameid,client,nr);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(tickets);
        return tickets;
    }
}
