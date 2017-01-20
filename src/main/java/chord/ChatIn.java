package chord;

import java.net.Socket;

/**
 * Created on 19/01/2017.
 *
 * @author JuIngong
 */
public class ChatIn implements Runnable {

    private Socket socket;

    public ChatIn(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
