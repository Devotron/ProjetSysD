package chord;

import java.io.Serializable;

/**
 * Created on 19/01/2017.
 *
 * @author JuIngong
 */
public class Peer implements Serializable{
    private String pseudo;
    private int port;
    private String ip;
    private int id;

    public Peer(String pseudo, int port, String ip) {
        this.pseudo = pseudo;
        this.port = port;
        this.ip = ip;
        this.id = 17 * 31 + port;
    }

    public String getPseudo() {

        return pseudo;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }
}
