package Service;

import Domain.Game;
import Domain.Seller;
import Repository.CRUDRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SellerService {
    private CRUDRepository<Integer, Seller> repo;

    public SellerService() {
    }

    public SellerService(CRUDRepository<Integer, Seller> repo) {
        this.repo = repo;
    }

    public void setRepo(CRUDRepository<Integer, Seller> repo) {
        this.repo = repo;
    }

    public void add(Seller seller){
        repo.add(seller);
    }
    public void delete(int id){
        repo.delete(id);
    }
    public void update(int id,Seller seller){
        repo.update(id,seller);
    }
    public Seller findOne(int id){
        return repo.findOne(id);
    }
    public List<Seller> findAll(){
        return StreamSupport.stream(repo.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
    public int findUser(String username,String password){
        int id = -99;
        for(Seller s : findAll()){
            if(s.getUsername().equals(username) && s.getPassword().equals(password)){
                id = s.getId();
                break;
            }
        }
        return id;
    }
}
