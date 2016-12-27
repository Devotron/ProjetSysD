package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JuIngong
 */
public class ServerMulti {

    private ServerSocket serverSocket;

    public ServerMulti(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }


    public void launch() {
        boolean waitClient = true;
        try {
            System.out.println("Server launch " + serverSocket.getLocalPort());
            List<Thread> threads = new ArrayList<>();
            while (waitClient) {
                Socket socket = serverSocket.accept();
                threads.add(new Thread(new ServiceClient(socket)));
                threads.get(threads.size() - 1).start();

            }
            System.out.println("Server close");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
//            int port = Integer.parseInt(args[0]);
            int port = 8888;
            ServerMulti serverJouet = new ServerMulti(8888);
            serverJouet.launch();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
