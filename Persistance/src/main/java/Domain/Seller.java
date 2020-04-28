package Domain;

public class Seller extends Entity<Integer> implements Comparable<Seller>{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Seller(int id, String username, String password) {
        setId(id);
        this.username = username;
        this.password = password;
    }
    public Seller() {
        this(-99);
    }

    public Seller(int id) {
        this(id,"","");
    }

    public Seller(String username, String passwd) {
        this(-99,username,passwd);
    }

    @Override
    public int compareTo(Seller o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Seller{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
