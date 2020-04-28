import Domain.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.List;

public class MainWindow extends JFrame {
    private JTable gamesList;
    private JTextField client;
    private JTextField nrOfSeats;
    private JButton sellButton;
    private ClientCtrl ctrl;

    public MainWindow(String title, ClientCtrl ctrl) throws Exception {
        super(title);
        this.ctrl=ctrl;
        JPanel panel=new JPanel(new BorderLayout());

        panel.add(createSellTicket(), BorderLayout.SOUTH);
        panel.add(createFriends(), BorderLayout.CENTER);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                close();
            }
        });
    }

    private void close(){
        ctrl.logout();
    }

    private JPanel createFriends() throws Exception {
        JPanel res=new JPanel(new GridLayout(1,1));
        String[] columns = {"Id","Home","Away","Type","NrOfSeats","NrOfEmptySeats","Price","Availability"};
        List<Game> games = ctrl.getGames();
        int size = games.size();
        String[][] datas = new String[size][8];
        for(int i = 0 ;i < size;i++){
            datas[i][0] = games.get(i).getId().toString();
            datas[i][1] = games.get(i).getHomeTeam();
            datas[i][2] = games.get(i).getAwayTeam();
            datas[i][3] = games.get(i).getType().toString();
            datas[i][4] = Integer.toString(games.get(i).getTotalNrOfSeats());
            datas[i][5] = Integer.toString(games.get(i).getNrOfEmptySeats());
            datas[i][6] = Float.toString(games.get(i).getPrice());
            if(games.get(i).getNrOfEmptySeats() <= 0){
                datas[i][7] = "NOT AVAILABLE";
            }
            else{
                datas[i][7] = "AVAILABLE";
            }
        }
        gamesList = new JTable(datas,columns);
        JScrollPane scroll=new JScrollPane(gamesList);
        res.add(scroll);
        res.setBorder(BorderFactory.createTitledBorder("Games"));
        return res;
    }

    private JPanel createSellTicket(){
        JPanel res=new JPanel(new GridLayout(3,1));
        JPanel line1=new JPanel();
        line1.add(new JLabel("Client name "));
        line1.add(client = new JTextField(30));
        res.add(line1);
        JPanel line2=new JPanel();
        line2.add(new JLabel("Nr of seats "));
        line2.add(nrOfSeats = new JTextField(30));
        res.add(line2);
        JPanel line3=new JPanel();
        line3.add(sellButton=new JButton("Sell ticket"));
        sellButton.addActionListener(new ButListener());
        res.add(line3);
        return res;
    }


    private class ButListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            System.out.println("Send button pressed");
            int indexFriend = gamesList.getSelectedRow();
            if (indexFriend>=0) {
                int idgame = Integer.parseInt(gamesList.getModel().getValueAt(indexFriend, 0).toString());
                String clientA = client.getText();
                int nrOfSeatsA = Integer.parseInt(nrOfSeats.getText());
                if ("".equals(clientA.trim()) || "".equals(nrOfSeatsA)) {
                    JOptionPane.showMessageDialog(MainWindow.this, "You must enter the inputs", "Send error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    ctrl.sellTicket(clientA,nrOfSeatsA,idgame);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Error "+e1, "Sell error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }else{
                JOptionPane.showMessageDialog(MainWindow.this, "You must select a game ", "Send error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
}
