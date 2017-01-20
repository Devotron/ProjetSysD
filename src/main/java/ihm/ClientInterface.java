package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import chord.ChordPeer;

public class ClientInterface extends JFrame {

	JPanel mainPanel;

	JTextArea chatBox;
	JScrollPane chatBoxPanel;

	
	JPanel msgPanel;
	JTextField entreeMsg;
	JButton valSaisie;
	
	private String IP;
	private int port = 0;
	private int port2 = 0;
	private String pseudo;
	
	final String defIP = "127.0.0.1";
	final String defPort = "1234";
	final String defPseudo = "Test";
	
	ChordPeer peer;
	

	public ClientInterface() {

		// - Fenetre
		setTitle("Chat - <CLIENT>");
		setSize(800, 500);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// - Panel Principal

		mainPanel = new JPanel();
		BorderLayout mLayout = new BorderLayout();
		mainPanel.setLayout(mLayout);

		// - ChatBox

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBackground(new Color(241, 241, 241));
		chatBoxPanel = new JScrollPane(chatBox);
		chatBoxPanel.setSize(800, 250);

		// Panel message
		
		msgPanel = new JPanel();
		BorderLayout msgl = new BorderLayout();
		
		
		// - TexteField

		entreeMsg = new JTextField();

		// - Validation saisie
		
		valSaisie = new JButton("ENVOYER");
		
		msgPanel.setLayout(msgl);
		msgPanel.add(entreeMsg, BorderLayout.CENTER);
		msgPanel.add(valSaisie, BorderLayout.EAST);
		
		// - Layout

		mainPanel.add(msgPanel, BorderLayout.SOUTH);
		mainPanel.add(chatBoxPanel, BorderLayout.CENTER);
		this.add(mainPanel);
		setVisible(true);
		
		
		// - Saisie des valeurs
		IP = JOptionPane.showInputDialog(mainPanel, "Saisir l'IP cible (ex: 127.0.0.1) : ", defIP);
		if (IP == null || IP.isEmpty()) forceClose();
		String tmp = JOptionPane.showInputDialog(mainPanel, "Saisir votre port", defPort);
		if (tmp == null || tmp.isEmpty() ) forceClose();
		else {
			port = Integer.parseInt(tmp);
		}
		String tmp2 = JOptionPane.showInputDialog(mainPanel, "Saisir le port cible", defPort);
		if (tmp2 == null || tmp.isEmpty() ) forceClose();
		else {
			port2 = Integer.parseInt(tmp);
		}
		pseudo = JOptionPane.showInputDialog(mainPanel, "Saisir le pseudo", defPseudo);
		if (pseudo == null || pseudo.isEmpty()) forceClose();
		
		peer = new ChordPeer(pseudo, port2, IP);
		
		System.out.println("IP : " + peer.getMe().getIp() + ", Port : " + peer.getMe().getPort() + ", Pseudo : " + peer.getMe().getPseudo() );

		try {
			Socket soc = new Socket(peer.getMe().getIp(), peer.getMe().getPort());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			//mauvaise saisie info || premier node
			e1.printStackTrace();
		}
		
		//valSaisie.setMnemonic(KeyEvent.VK_ENTER);
		
		valSaisie.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( !entreeMsg.getText().isEmpty() ) {
				
					String m = "[" + pseudo + "] : ";
					m += entreeMsg.getText() + "\n";
					
					Map message = preparerMessage(m);
					
					chatBox.append(m);
					System.out.println("Ajout saisie");
					entreeMsg.setText("");
				}
			}
		});
		
	}
	
	//Generation message � envoyer
	private Map<String, String> preparerMessage(String m) {
		 Map<String, String> msg = new HashMap<>();
	        msg.put("type", "msg");
	        msg.put("exp", "" + peer.getMe().getId());
	        msg.put("content", m);
	        
	      return msg;  
	}
	
	private void forceClose() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public static void main(String[] args) {

		ClientInterface client = new ClientInterface();
		
		
	}

}
