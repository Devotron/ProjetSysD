package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author JuIngong
 */
public class MiniClient {
    private String host;
    private int port;
    private String name;

    public MiniClient(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public void connect() {
        PrintWriter out;
        try {
            Socket socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream());
            out.println("Hello I'm : " + name);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message_distant = in.readLine();
            System.out.println(message_distant);
            sendMsgAuto(out, in);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgConsole(PrintWriter out, BufferedReader in) throws IOException {
        boolean stayOn = true;
        Scanner sc = new Scanner(System.in);
        String message_distant;
        while (stayOn) {
            String str = sc.nextLine();
            int hash = str.hashCode();
            out.println(str);
            out.flush();
            message_distant = in.readLine();
            if (str == null || str.equals("exit")) {
                stayOn = false;
            } else if (hash == Integer.parseInt(message_distant)) {
                System.out.println("Validated.");
            }
        }
    }

    private void sendMsgAuto(PrintWriter out, BufferedReader in) throws IOException {
        boolean stayOn = true;
        String message_distant;
        int i = 10;
        while (stayOn && i > 0) {
            String str = "blabla";
            int hash = str.hashCode();
            out.println(str);
            out.flush();
            message_distant = in.readLine();
            if (str == null || str.equals("exit")) {
                stayOn = false;
            } else if (hash == Integer.parseInt(message_distant)) {
                i--;
                System.out.println("Validated.");
            }
        }
    }

    public static void main(String[] args) {

//        new MiniClient(args[0], Integer.parseInt(args[1]), args[2]).connect();
        new MiniClient("127.0.0.1", 8888, "test").connect();
    }
}
