package Service;

import Domain.Game;
import Repository.CRUDRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GameService {
    private CRUDRepository<Integer, Game> repo;

    public GameService() {
    }

    public GameService(CRUDRepository<Integer, Game> repo) {
        this.repo = repo;
    }

    public void setRepo(CRUDRepository<Integer, Game> repo) {
        this.repo = repo;
    }

    public void add(Game game){
        repo.add(game);
    }
    public void delete(int id){
        repo.delete(id);
    }
    public void update(int id,Game game){
        repo.update(id,game);
    }
    public Game findOne(int id){
        return repo.findOne(id);
    }
    public Iterable<Game> findAll(){
        return repo.findAll();
    }
    public List<Game> sortDescending(){
        return StreamSupport.stream(repo.findAll().spliterator(),false)
                .sorted(Comparator.comparingInt(Game::getNrOfEmptySeats)
                        .reversed())
                .collect(Collectors.toList());
    }
}
