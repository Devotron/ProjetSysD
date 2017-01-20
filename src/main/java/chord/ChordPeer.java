package chord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by JuIngong on 19/01/2017.
 */
public class ChordPeer {

    private Peer me;

    private Peer pred;

    private Peer succ;

    private Socket pSock;

    private Socket sSock;

    private Thread pThread;

    private boolean running = true;

    private TreeMap<Integer, ChordPeer> fingerTable;

    private Map<String, List<Peer>> chatRooms;

    private String myChat;

    public ChordPeer(String pseudo, int port, String ip) {
        this.me = new Peer(pseudo, port, ip);
        new Thread(() -> startClientServer(port)).start();
        this.pred = null;
        chatRooms = new HashMap<>();
        this.succ = null;
    }

    public static void main(String[] args) {
        test2();
    }

    static void test0() {
        ChordPeer c = new ChordPeer("test0", 1234, "127.0.0.1");

    }

    static void test1() {
        ChordPeer c1 = new ChordPeer("test1", 1224, "127.0.0.1");
        try {
            c1.joinChord(new Socket("127.0.0.1", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }
        c1.joinChatRoom("test");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String> test = new HashMap<>();
        test.put("type", "msg");
        test.put("exp", "" + c1.getMe().getId());
        test.put("content", "blabla");
        c1.forwardMessage(test);

    }

    static void test3() {
        ChordPeer c2 = new ChordPeer("test2", 1264, "127.0.0.1");
        try {
            c2.joinChord(new Socket("127.0.0.1", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> test = new HashMap<>();
        test.put("type", "msg");
        test.put("exp", "" + c2.getMe().getId());
        test.put("content", "blabla");

        c2.forwardMessage(test);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c2.joinChatRoom("test");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c2.sendToChatRoom("Blablou");
        c2.leaveChatRoom();
    }

    static void test2() {
        ChordPeer c2 = new ChordPeer("test2", 1294, "127.0.0.1");
        try {
            c2.joinChord(new Socket("127.0.0.1", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> test = new HashMap<>();
        test.put("type", "msg");
        test.put("exp", "" + c2.getMe().getId());
        test.put("content", "blabla");

        c2.forwardMessage(test);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c2.joinChatRoom("test");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c2.sendToChatRoom("Blablou");
        c2.leaveChatRoom();
    }

    private void startClientServer(int portNum) {
        try {
            ServerSocket server = new ServerSocket(portNum);
            while (running) {
                if (pred != null && succ != null) {
                }
                Socket connection = server.accept();
                Map<String, String> msg = receivMsg(connection);
                if ("join".equals(msg.get("type"))) {
                    String p = msg.get("pseudo");
                    int i = Integer.parseInt(msg.get("port"));
                    String s = msg.get("ip");
                    msg.clear();
                    if (pThread != null) {
                        pThread.interrupt();
                    }
                    if (pSock != null) {
                        pSock.close();
                    }
                    if (pred != null) {
                        msg.put("type", "pred");
                        msg.put("ip", pred.getIp());
                        msg.put("port", Integer.toString(pred.getPort()));
                        msg.put("pseudo", pred.getPseudo());

                    } else {
                        msg.put("type", "pred");
                        msg.put("ip", me.getIp());
                        msg.put("port", Integer.toString(me.getPort()));
                        msg.put("pseudo", me.getPseudo());
                        succ = new Peer(p, i, s);
                        sSock = connection;
                    }
                    sendMsg(msg, connection);
                    pred = new Peer(p, i, s);
                    pSock = connection;
                    pThread = new Thread(new ChatIn());
                    pThread.start();
                }
                if ("find".equals(msg.get("type"))) {
                    String m = findMainChord(Integer.parseInt(msg.get("key")));
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.println(m);
                    out.flush();
                    connection.close();
                }
                if ("succ".equals(msg.get("type"))) {
                    succ = new Peer(msg.get("pseudo"), Integer.parseInt(msg.get("port")), msg.get("ip"));
                    sSock = connection;
                }
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Peer>> getChatRoomsList() {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "salon");
        msg.put("goal", "info");
        sendMsg(msg, sSock);
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, List<Peer>>>() {
        }.getType();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(sSock.getInputStream()));
            String s = in.readLine();
            return gson.fromJson(s, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinChatRoom(String name) {
        Map<String, String> msg = new HashMap<>();
        if (chatRooms.get(name) != null) {
            msg.put("type", "salon");
            msg.put("goal", "join");
            msg.put("name", name);
            msg.put("ip", me.getIp());
            msg.put("port", Integer.toString(me.getPort()));
            msg.put("pseudo", me.getPseudo());
        } else {
            msg.put("type", "salon");
            msg.put("goal", "create");
            msg.put("name", name);
            msg.put("ip", me.getIp());
            msg.put("port", Integer.toString(me.getPort()));
            msg.put("pseudo", me.getPseudo());
            List<Peer> chat = new ArrayList<>();
            chat.add(me);
            chatRooms.put(name, chat);
        }
        myChat = name;
        forwardMessage(msg);
    }

    public void sendToChatRoom(String s) {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "salon");
        msg.put("name", myChat);
        msg.put("goal", "msg");
        msg.put("exp", Integer.toString(me.getId()));
        msg.put("content", s);
        forwardMessage(msg);
    }

    public void leaveChatRoom() {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "salon");
        msg.put("name", myChat);
        msg.put("goal", "leave");
        msg.put("exp", Integer.toString(me.getId()));
        forwardMessage(msg);
        chatRooms.get(myChat).remove(me);
        myChat = "";
    }


    public void joinChord(Socket socket) {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "find");
        msg.put("key", Integer.toString(me.getId()));
        sendMsg(msg, socket);
        msg.clear();
        msg = receivMsg(socket);
        if ("found".equals(msg.get("type"))) {
            succ = new Peer(msg.get("pseudo"), Integer.parseInt(msg.get("port")), msg.get("ip"));
            try {
                sSock = new Socket(succ.getIp(), succ.getPort());
                joinMainChord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error joining.");
        }
    }

    public void joinMainChord() {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "join");
        msg.put("ip", me.getIp());
        msg.put("port", Integer.toString(me.getPort()));
        msg.put("pseudo", me.getPseudo());
        sendMsg(msg, sSock);
        msg.clear();
        msg = receivMsg(sSock);
        if ("pred".equals(msg.get("type"))) {
            pred = new Peer(msg.get("pseudo"), Integer.parseInt(msg.get("port")), msg.get("ip"));
            try {
                pSock = new Socket(pred.getIp(), pred.getPort());
                chatRooms = getChatRoomsList();
                msg.clear();
                msg.put("type", "succ");
                msg.put("ip", me.getIp());
                msg.put("port", Integer.toString(me.getPort()));
                msg.put("pseudo", me.getPseudo());
                sendMsg(msg, pSock);
                pThread = new Thread(new ChatIn());
                pThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error joining.");
        }

    }

    public void leaveMainChord() {
        pThread.interrupt();
        try {
            pSock.close();

            Map<String, String> msg = new HashMap<>();
            msg.put("type", "leave");
            msg.put("ipPred", pred.getIp());
            msg.put("portPred", Integer.toString(pred.getPort()));
            msg.put("pseudoPred", pred.getPseudo());
            sendMsg(msg, sSock);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findMainChord(int key) {
        Gson gson = new Gson();
        Map<String, String> msg = new HashMap<>();

        if ((pred == null) || (succ == null) || (((key > pred.getId()) || ((pred.getId() > me.getId()) && (key < pred.getId()))) && (key <= me.getId()))) {
            msg.put("type", "found");
            msg.put("ip", me.getIp());
            msg.put("port", Integer.toString(me.getPort()));
            msg.put("pseudo", me.getPseudo());
            return gson.toJson(msg);
        } else if (key > me.getId() && (me.getId() > succ.getId() || key < succ.getId())) {
            msg.put("type", "found");
            msg.put("ip", succ.getIp());
            msg.put("port", Integer.toString(succ.getPort()));
            msg.put("pseudo", succ.getPseudo());
            return gson.toJson(msg);
        } else {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(sSock.getInputStream()));
                PrintWriter out = new PrintWriter(sSock.getOutputStream());
                msg.put("type", "find");
                msg.put("key", Integer.toString(key));
                out.println(gson.toJson(msg));
                out.flush();
                String st = in.readLine();
                return st;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, String> receivMsg(Socket con) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String s = in.readLine();
            return gson.fromJson(s, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMsg(Map<String, String> msg, Socket con) {
        Gson gson = new Gson();
        try {
            PrintWriter out = new PrintWriter(con.getOutputStream());
            out.println(gson.toJson(msg));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forwardMessage(Map<String, String> msg) {
        sendMsg(msg, sSock);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Peer getMe() {
        return me;
    }

    public Peer getPred() {
        return pred;
    }

    public Peer getSucc() {
        return succ;
    }

    public class ChatIn implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                Map<String, String> msg = receivMsg(pSock);
                if (msg != null) {
                    if ("msg".equals(msg.get("type"))) {
                        if (!Integer.toString(succ.getId()).equals(msg.get("exp"))) {
                            System.out.println(msg.get("content"));
                            forwardMessage(msg);
                        } else {
                            System.out.println(msg.get("content"));
                        }
                    }
                    if ("salon".equals(msg.get("type"))) {
                        if ("info".equals(msg.get("goal"))) {
                            Gson gson = new Gson();
                            try {
                                PrintWriter out = new PrintWriter(pSock.getOutputStream());
                                out.println(gson.toJson(chatRooms));
                                out.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if ("leave".equals(msg.get("goal"))) {
                            Peer t = null;
                            for (Peer p : chatRooms.get(msg.get("name"))) {
                                if (p.getId() == Integer.parseInt(msg.get("exp"))) {
                                    t = p;
                                }
                            }
                            chatRooms.get(msg.get("name")).remove(t);
                            if (succ.getId() != Integer.parseInt(msg.get("exp"))) {
                                forwardMessage(msg);
                            }
                        } else if ("join".equals(msg.get("goal"))) {
                            Peer p = new Peer(msg.get("pseudo"), Integer.parseInt(msg.get("port")), msg.get("ip"));
                            chatRooms.get(msg.get("name")).add(p);
                            if (succ.getId() != p.getId()) {
                                forwardMessage(msg);
                            }
                        } else if ("create".equals(msg.get("goal"))) {
                            Peer p = new Peer(msg.get("pseudo"), Integer.parseInt(msg.get("port")), msg.get("ip"));
                            List<Peer> chat = new ArrayList<>();
                            chat.add(p);
                            chatRooms.put(msg.get("name"), chat);
                            if (succ.getId() != p.getId()) {
                                forwardMessage(msg);
                            }
                        } else if ("msg".equals(msg.get("goal"))) {
                            if (msg.get("name").equals(myChat)) {
                                if (!Integer.toString(succ.getId()).equals(msg.get("exp"))) {
                                    System.out.println(msg.get("content"));
                                    forwardMessage(msg);
                                } else {
                                    System.out.println(msg.get("content"));
                                }
                            } else {
                                if (!Integer.toString(succ.getId()).equals(msg.get("exp"))) {
                                    forwardMessage(msg);
                                }
                            }
                        }
                    }
                    if ("leave".equals(msg.get("type")) && msg.get("pseudoPred") != null) {
                        if (me.getPseudo().equals(msg.get("pseudoPred"))) {
                            succ = null;
                            pred = null;
                            try {
                                sSock.close();
                                pSock.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pThread.interrupt();
                        } else {
                            try {
                                pred = new Peer(msg.get("pseudoPred"), Integer.parseInt(msg.get("portPred")), msg.get("ipPred"));
                                pSock = new Socket(msg.get("ipPred"), Integer.parseInt(msg.get("portPred")));
                                msg.clear();
                                msg.put("type", "succ");
                                msg.put("ip", me.getIp());
                                msg.put("port", Integer.toString(me.getPort()));
                                msg.put("pseudo", me.getPseudo());
                                sendMsg(msg, pSock);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if ("find".equals(msg.get("type"))) {
                        String m = findMainChord(Integer.parseInt(msg.get("key")));
                        PrintWriter out = null;
                        try {
                            out = new PrintWriter(pSock.getOutputStream());
                            out.println(m);
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }
}
