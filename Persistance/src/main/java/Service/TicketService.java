package Service;

import Domain.Ticket;
import Repository.CRUDRepository;

public class TicketService {
    private CRUDRepository<Integer, Ticket> repo;

    public void setRepo(CRUDRepository<Integer, Ticket> repo) {
        this.repo = repo;
    }

    public TicketService() {
    }

    public TicketService(CRUDRepository<Integer, Ticket> repo) {
        this.repo = repo;
    }
    public void add(Ticket ticket){
        repo.add(ticket);
    }
    public void delete(int id){
        repo.delete(id);
    }
    public void update(int id,Ticket ticket){
        repo.update(id,ticket);
    }
    public Ticket findOne(int id){
        return repo.findOne(id);
    }
    public Iterable<Ticket> findAll(){
        return repo.findAll();
    }
    public int getBiggestId(){
        int max = -999;
        for(Ticket t : findAll()){
            if(t.getId()>max){
                max = t.getId();
            }
        }
        return max;
    }
}
