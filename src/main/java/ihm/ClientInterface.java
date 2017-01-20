package ihm;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ClientInterface extends JFrame {

	JPanel mainPanel;

	JTextArea chatBox;
	JScrollPane chatBoxPanel;

	
	JPanel msgPanel;
	JTextField entreeMsg;
	JButton valSaisie;

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

	}

	public static void main(String[] args) {

		ClientInterface client = new ClientInterface();

	}

}
