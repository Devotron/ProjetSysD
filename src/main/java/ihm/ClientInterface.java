package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

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
	private String pseudo;
	
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
		IP = JOptionPane.showInputDialog(mainPanel, "Saisir l'IP");
		if (IP == null || IP.isEmpty()) forceClose();
		String tmp = JOptionPane.showInputDialog(mainPanel, "Saisir n° port");
		if (tmp == null || tmp.isEmpty() ) forceClose();
		else {
			port = Integer.parseInt(tmp);
		}
		pseudo = JOptionPane.showInputDialog(mainPanel, "Saisir le pseudo");
		if (pseudo == null || pseudo.isEmpty()) forceClose();
		
		peer = new ChordPeer(pseudo, port, IP);
		
		System.out.println(peer.getMe().getIp() + ", " + peer.getMe().getPort() + ", " + peer.getMe().getPseudo() );

		//valSaisie.setMnemonic(KeyEvent.VK_ENTER);
		
		valSaisie.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( !entreeMsg.getText().isEmpty() ) {
				
					String m = "[" + pseudo + "] : ";
					m += entreeMsg.getText() + "\n";
					
					chatBox.append(m);
					System.out.println("Ajout saisie");
					entreeMsg.setText("");
				}
			}
		});
		
	}
	
	private void forceClose() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public static void main(String[] args) {

		ClientInterface client = new ClientInterface();
		
		
	}

}
