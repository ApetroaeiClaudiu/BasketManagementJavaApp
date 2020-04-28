package Repository;

import Domain.Seller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SellerRepository implements CRUDRepository<Integer, Seller> {
    private JDBCUtils jdbcutils;
    private static final Logger logger = LogManager.getLogger();

    public SellerRepository(Properties props) {
        logger.info("Initializing Seller Repository with properties:{}",props);
        jdbcutils = new JDBCUtils(props);
    }

    @Override
    public void add(Seller entity) {
        logger.traceEntry("Adding Seller {} ",entity);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Sellers values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUsername());
            preStmt.setString(3,entity.getPassword());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Seller with {}",integer);
        Connection con=jdbcutils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Sellers where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Seller entity) {
        logger.traceEntry("updating Seller with id {}",integer);
        Connection con = jdbcutils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE Sellers SET id=?, username=?, password=? WHERE Id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUsername());
            preStmt.setString(3,entity.getPassword());
            preStmt.setInt(4,integer);
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
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Sellers")) {
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
    public Seller findOne(Integer integer) {
        logger.traceEntry("finding Seller with id {} ",integer);
        Connection con=jdbcutils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Sellers where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Seller seller = new Seller(id,username,password);
                    logger.traceExit(seller);
                    return seller;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No Seller found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Seller> findAll() {
        Connection con=jdbcutils.getConnection();
        List<Seller> sellers = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Sellers")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Seller seller = new Seller(id,username,password);
                    sellers.add(seller);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(sellers);
        return sellers;
    }
}
