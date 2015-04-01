package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import server.ClientConnection;
import server.ServerLobby;

public class LobbyView 
{
	public static JPanel lobbyPanel;
	public static JPanel playerPanel;
	String gameChosen;
	JPanel imagePanel;
	JPanel buttonPanel;
	JPanel eastPanel;
	JScrollPane scrollPanel;
	
	//Moet maybe ook naar controller
	JButton challengeOpponent;
	JButton backToLogin;
	JComboBox chooseGame;
	
	public LobbyView(FrameworkView fv)
	{
		lobbyPanel = new JPanel(new BorderLayout());
		lobbyPanel.setVisible(false);
		lobbyPanel.setBackground(Color.white);
		lobbyPanel.setPreferredSize(new Dimension(680,450));
		
		addScrollPanel();
		createButtonPanel();
		addButtons();
		createImagePanel();
		addImageToPanel();
		addEastPanels();
		
		fv.add(lobbyPanel);
		fv.revalidate();
	}
	
	public void addScrollPanel()
	{
		playerPanel = new JPanel(new GridLayout(10,1));
		playerPanel.setVisible(true);
		playerPanel.setBackground(Color.white);
		
		scrollPanel = new JScrollPane(playerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setPreferredSize(new Dimension(480,440));
		
		lobbyPanel.add(scrollPanel, BorderLayout.WEST);
	}
	
	public void createButtonPanel()
	{
		buttonPanel = new JPanel(new GridLayout(3,1));
		buttonPanel.setVisible(true);
		buttonPanel.setBackground(Color.white);
		buttonPanel.setPreferredSize(new Dimension(190,250));
	}
	
	//Moet nog naar FrameworkController
	public void addButtons()
	{
		challengeOpponent = new JButton("CHALLENGE");
		challengeOpponent.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		challengeOpponent.setBackground(Color.black);
		challengeOpponent.setForeground(Color.white);
		challengeOpponent.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to challenge "+ServerLobby.selectedOpponentUsername+"?",null, JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					System.out.println("Challeging...");
				} 
			}	
		});
			
		chooseGame = new JComboBox();
		chooseGame.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		chooseGame.setBackground(Color.black);
		chooseGame.setForeground(Color.white);
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer(); 
		dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER); 
		chooseGame.setRenderer(dlcr);
		chooseGame.addItem("TICTACTOE");
		chooseGame.addItem("OTHELLO");
		chooseGame.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				gameChosen = chooseGame.getSelectedItem().toString();
				System.out.println(gameChosen);
			}
		});
		
		backToLogin = new JButton("BACK");
		backToLogin.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		backToLogin.setBackground(Color.black);
		backToLogin.setForeground(Color.white);
		backToLogin.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				LoginView.loginPanel.setVisible(true);
				lobbyPanel.setVisible(false);
				try {
					ClientConnection.socket.close();
					
					//reset all lobby values
					ServerLobby.playersInLobby = new JCheckBox[10];
					ServerLobby.userNames.removeAll(ServerLobby.userNames);
					ServerLobby.Lobby.removeAll(ServerLobby.Lobby);
					
					playerPanel.removeAll();
					playerPanel.repaint();
					playerPanel.revalidate();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}	
		});
		

		buttonPanel.add(challengeOpponent);
		buttonPanel.add(chooseGame);
		buttonPanel.add(backToLogin);
	}
	
	public void createImagePanel()
	{
		imagePanel = new JPanel();
		imagePanel.setVisible(true);
		imagePanel.setPreferredSize(new Dimension(190,180));
		imagePanel.setBackground(Color.white);
	}
	
	public void addEastPanels()
	{
		eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(190, 480));
		eastPanel.setBackground(Color.white);
		
		eastPanel.add(buttonPanel);
		eastPanel.add(imagePanel);
		
		lobbyPanel.add(eastPanel, BorderLayout.EAST);
	}
	
	public void addImageToPanel()
	{
		try{
			String path = LobbyView.class.getResource("logo.png").getPath();
			Image myPicture = ImageIO.read(new File(path));
			myPicture = myPicture.getScaledInstance(190,190, myPicture. SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			imagePanel.add(picLabel);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
