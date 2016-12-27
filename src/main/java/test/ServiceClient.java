package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class created on 22/11/2016
 *
 * @author JuIngong
 */
public class ServiceClient implements Runnable {

    private Socket client;

    public ServiceClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            Pattern pattern = Pattern.compile("(Hello I'm : )");
            String name = in.readLine();
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                name = name.substring(matcher.end());
                out.println("Hello " + name);
                out.flush();
            }
            System.out.println(name + " => Connect");
            boolean stayOn = true;
            int line = 0;
            while (stayOn) {
                String message_distant = in.readLine();
                if (message_distant == null || message_distant.equals("exit")) {
                    stayOn = false;
                } else {
                    System.out.println(name + " => " + ++line + " : " + message_distant);
                    out.println(message_distant.hashCode());
                    out.flush();
                }
            }
            System.out.println(name + " => Disconnect");
            client.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
