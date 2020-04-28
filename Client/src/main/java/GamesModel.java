import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GamesModel extends AbstractListModel {
    private List<String> games;

    public GamesModel() {
        games=new ArrayList<String>();
    }

    public int getSize() {
        return games.size();
    }

    public Object getElementAt(int index) {
        return games.get(index);
    }

    public void friendLoggedIn(String id){
        games.add(id);
        fireContentsChanged(this,games.size()-1,games.size());
    }

    public void friendLoggedOut(String id){
        games.remove(id);
        fireContentsChanged(this,0, games.size());
    }
    int getId(){
        return 0;
    }
}
