package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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
import javax.swing.UIManager;

import server.ClientConnection;
import server.ServerLobby;

public class LobbyView 
{
	public static JPanel lobbyPanel;
	public static JPanel playerPanel;
	public static String gameChosen = "";
	public static JLabel playersOnlineLabel;
	public static GridLayout lobbyLayout;
	JPanel imagePanel;
	JPanel buttonPanel;
	JPanel eastPanel;
	JScrollPane scrollPanel;
	
	//Moet maybe ook naar controller
	JButton challengeOpponent;
	JButton backToLogin;
	JButton subscribe;
	
	public static JComboBox chooseGame;
	
	public LobbyView(FrameworkView fv)
	{
		lobbyPanel = new JPanel(new BorderLayout());
		lobbyPanel.setVisible(false);
		lobbyPanel.setBackground(Color.white);
		lobbyPanel.setPreferredSize(new Dimension(692,462));
		
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
		lobbyLayout = new GridLayout(10,1);
		playerPanel = new JPanel(lobbyLayout);
		playerPanel.setVisible(true);
		playerPanel.setBackground(Color.white);
		playerPanel.setMinimumSize(new Dimension(450,430));
		
		scrollPanel = new JScrollPane(playerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setPreferredSize(new Dimension(480,440));
		
		lobbyPanel.add(scrollPanel, BorderLayout.WEST);
	}
	
	public void createButtonPanel()
	{
		buttonPanel = new JPanel(new GridLayout(5,1));
		buttonPanel.setVisible(true);
		buttonPanel.setBackground(Color.white);
		buttonPanel.setPreferredSize(new Dimension(190,250));
	}
	
	//Moet nog naar FrameworkController
	public void addButtons()
	{
		challengeOpponent = new JButton("Challenge");
		challengeOpponent.setBackground(Color.black);
		challengeOpponent.setForeground(Color.white);
			
		chooseGame = new JComboBox();
		chooseGame.setBackground(Color.black);
		chooseGame.setForeground(Color.white);
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer(); 
		dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER); 
		chooseGame.setRenderer(dlcr);
		
		backToLogin = new JButton("Back");
		backToLogin.setBackground(Color.black);
		backToLogin.setForeground(Color.white);

		subscribe = new JButton("Subscribe");
		subscribe.setBackground(Color.black);
		subscribe.setForeground(Color.white);

		playersOnlineLabel = new JLabel("     0 players online");
		playersOnlineLabel.setForeground(new Color(33,179,7));
		
		buttonPanel.add(chooseGame);
		buttonPanel.add(subscribe);
		buttonPanel.add(challengeOpponent);
		buttonPanel.add(playersOnlineLabel);
		buttonPanel.add(backToLogin);
	}
	
	public void createImagePanel()
	{
		imagePanel = new JPanel();
		imagePanel.setVisible(true);
		imagePanel.setPreferredSize(new Dimension(200,200));
		imagePanel.setBackground(Color.white);
	}
	
	public void addEastPanels()
	{
		eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(210, 480));
		eastPanel.setBackground(Color.white);
		
		eastPanel.add(buttonPanel);
		eastPanel.add(imagePanel);
		
		lobbyPanel.add(eastPanel);
	}
	
	public static void updatePlayersInLobby(int i){
		if(i!=1){
			playersOnlineLabel.setText("     "+i+" players online");
		}
		else{
			playersOnlineLabel.setText("     "+i+" player online");
		}
		
		lobbyPanel.repaint();
		lobbyPanel.revalidate();
	}
	
	public void addImageToPanel()
	{
		try{
			InputStream url = this.getClass().getClassLoader().getResourceAsStream("framework/logo.PNG");
		    Image myPicture = ImageIO.read(url);
			myPicture = myPicture.getScaledInstance(210,210, myPicture. SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			imagePanel.add(picLabel);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addActionListener(JButton button, ActionListener al){
		button.addActionListener(al);
	}
	
	public void addActionListenerJComboBox(JComboBox jcb, ActionListener al){
		jcb.addActionListener(al);
	}
	
	public void setVisible(boolean visible){
		this.lobbyPanel.setVisible(visible);
	}
	
	public static void updateGameComboBox(ArrayList<String> gameList){
		if(chooseGame.getItemCount() < 1){
			for(int i = 0;i<gameList.size();i++){
				chooseGame.addItem(gameList.get(i));
			}
		}
		else{
			boolean existInList = false;
			for(int i = 0;i<gameList.size();i++){
				for(int j=0;j<chooseGame.getItemCount(); j++){
					if(chooseGame.getItemAt(j).toString().equals(gameList.get(i))){
						existInList = true;
					}
				}
				if(!existInList){
					chooseGame.addItem(gameList.get(i));
				}
			}
		}
		lobbyPanel.repaint();
		lobbyPanel.revalidate();
	}

	public void revalidate() {
		lobbyPanel.repaint();
		lobbyPanel.revalidate();
	}
}
