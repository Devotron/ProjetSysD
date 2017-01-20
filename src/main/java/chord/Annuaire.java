package chord;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic on 20/01/2017.
 */
public class Annuaire {

    private ServerSocket server;
    private List<Peer> users;

    public Annuaire(int port){
        try {
            this.server = new ServerSocket(port);
            this.users = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return server;
    }

    public void launch() {
        boolean waitClient = true;
        try {
            System.out.println("Server launch " + server.getLocalPort());
            while (waitClient) {
                Socket socket = server.accept();
            }
            System.out.println("Server close");
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Peer> getUsers(){
        return this.users;
    }

    public synchronized void addUser(Peer p){
        this.users.add(p);
    }

    public static void main(String[] args) {
        Annuaire annuaire = new Annuaire(1000);
        annuaire.launch();
    }
}
