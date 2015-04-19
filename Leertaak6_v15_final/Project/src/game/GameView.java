package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import AI.AbstractAI;
import AI.OthelloAI;
import AI.TicTacToeAI;
import AI.TicTacToeAI;
import server.ClientConnection;
import framework.FrameworkController;
import framework.FrameworkView;
import framework.GameProtocol;
import framework.LobbyView;

public class GameView {
	
	public static JPanel gamePanel;
	public static JPanel boardPanel;
	public static JPanel controlPanel;
	public static JPanel messagePanel;
	public static JButton AISwitch;
	public static JLabel opponentLabel;
	public static JButton forfeitButton;
	public static JButton hintButton;
	public static JLabel messageLabel;
	public static ArrayList<JLabel> boardPosition = new ArrayList<JLabel>();
	public static ArrayList<MouseListener> boardListeners = new ArrayList<MouseListener>();
	public static int amountOfPositions;
	
	JPanel leftPanel;
	JPanel rightPanel;
	JPanel logoPanel;
	
	public GameView(FrameworkView fv){
		gamePanel = new JPanel(new BorderLayout());
		gamePanel.setVisible(false);
		gamePanel.setBackground(Color.white);
		gamePanel.setPreferredSize(new Dimension(690,460));
		
		addPanels();
		
		fv.add(gamePanel);
		fv.revalidate();
	}
	
	public void addPanels(){
		addLeftSidePanel();
		addRightSidePanel();
		
		addControlPanel();
		addAISwitch();
		addForfeitButton();
		addHintButton();
		
		addBoardPanel(9);
		createBoard(9,90);
		addBoardListener(boardPosition);
		
		addMessagePanel();
		
		addLogoPanel();
		addImageToPanel();
	}
	
	public static void createBoard(int positions , int fontSize){
		boardPanel.removeAll();
		boardPosition.removeAll(boardPosition);
		amountOfPositions = positions;
		for (int i =0; i<positions; i++){
			JLabel label = new JLabel(" ");
		    label.setHorizontalAlignment(label.CENTER);
			label.setFont(new Font("Serif", Font.BOLD, fontSize));
			label.setOpaque(true);
			label.setBackground(Color.green);
		    label.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
		    boardPosition.add(label);
		    boardPanel.add(label);
		}
	}
	
	
	public void addForfeitButton(){
		forfeitButton = new JButton("FORFEIT");
		forfeitButton.setFont(new Font("Serif", Font.BOLD, 14));
		forfeitButton.setBackground(Color.BLACK);
		forfeitButton.setForeground(Color.white);
		
		controlPanel.add(forfeitButton);
	}
	
	public void addHintButton(){
		hintButton = new JButton("HINT");
		hintButton.setFont(new Font("Serif", Font.BOLD, 14));
		hintButton.setBackground(Color.BLACK);
		hintButton.setForeground(Color.white);
		
		controlPanel.add(hintButton);
	}
	
	public void addAISwitch(){
		AISwitch = new JButton("TURN AI ON");
		AISwitch.setBackground(Color.BLACK);
		AISwitch.setForeground(Color.white);
		AISwitch.setFont(new Font("Serif", Font.BOLD, 14));
		controlPanel.add(AISwitch);
	}
	
	public void addRightSidePanel(){
		rightPanel = new JPanel();
		rightPanel.setVisible(true);
		rightPanel.setBackground(Color.white);
		rightPanel.setPreferredSize(new Dimension(220,440));
		
		gamePanel.add(rightPanel, BorderLayout.EAST);
	}
	
	public void addLeftSidePanel(){
		JPanel totalLeft = new JPanel();
		totalLeft.setPreferredSize(new Dimension(460, 440));
		totalLeft.setBackground(Color.white);
		
		JPanel paddingLeft = new JPanel();
		paddingLeft.setPreferredSize(new Dimension(10,440));
		paddingLeft.setBackground(Color.white);
		
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(440,440));
		rightPanel.setBackground(Color.white);
		
		
		JPanel paddingTop = new JPanel();
		paddingTop.setPreferredSize(new Dimension(410,10));
		paddingTop.setBackground(Color.white);
		
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.setVisible(true);
		leftPanel.setBackground(Color.white);
		leftPanel.setPreferredSize(new Dimension(410, 410));
		
		totalLeft.add(paddingLeft);
		totalLeft.add(rightPanel);
		
		rightPanel.add(paddingTop);
		rightPanel.add(leftPanel);
		
		gamePanel.add(totalLeft, BorderLayout.WEST);
	}
	
	public void addBoardPanel(double positions){
		positions = Math.sqrt(positions);
		
		boardPanel = new JPanel(new GridLayout((int)positions,(int)positions));
		boardPanel.setVisible(true);
		boardPanel.setBackground(Color.lightGray);
		boardPanel.setPreferredSize(new Dimension(270,270));
		
		leftPanel.add(boardPanel, BorderLayout.CENTER);
	}
	
	public void addControlPanel(){
		controlPanel = new JPanel(new GridLayout(3,1));
		controlPanel.setVisible(true);
		controlPanel.setBackground(Color.white);
		controlPanel.setPreferredSize(new Dimension(170,210));
		
		JPanel topMargin = new JPanel();
		topMargin.setBackground(Color.white);
		topMargin.setPreferredSize(new Dimension(180,15));
		
		rightPanel.add(topMargin);
		rightPanel.add(controlPanel);
	}
	
	
	public void addMessagePanel(){
		messagePanel = new JPanel();
		messagePanel.setVisible(true);
		messagePanel.setBackground(Color.white);
		messagePanel.setPreferredSize(new Dimension(320,50));
		
		leftPanel.add(messagePanel, BorderLayout.SOUTH);
		
		messageLabel = new JLabel("Initializing.. ");
		messageLabel.setFont(new Font("Serif", Font.PLAIN, 26));
		messageLabel.setHorizontalAlignment(messageLabel.CENTER);
		
		messagePanel.add(messageLabel);
	}
	
	public void addLogoPanel(){
		logoPanel = new JPanel(new GridBagLayout());
		logoPanel.setVisible(true);
		logoPanel.setBackground(Color.white);
		logoPanel.setPreferredSize(new Dimension(230,230));
		
		rightPanel.add(logoPanel);
	}
	
	public void addImageToPanel()
	{
		try{
			InputStream url = this.getClass().getClassLoader().getResourceAsStream("framework/logo.PNG");
		    Image myPicture = ImageIO.read(url);
			myPicture = myPicture.getScaledInstance(220,220, myPicture. SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			logoPanel.add(picLabel);
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(FrameworkView.window,e.toString(),"Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();
		}
	}
	
	public void addActionListenerButton(JButton btn, ActionListener al){
		btn.addActionListener(al);
	}
	
	public static void clearBoard(){
		for(int i=0; i<boardPosition.size(); i++){
			boardPosition.get(i).setText(" ");
			boardPosition.get(i).setBackground(Color.green);
			boardPosition.get(i).revalidate();
		}
		
		FrameworkController.AIModule.initBoard();
		
		refreshBoard();
		
		boardPanel.repaint();
		boardPanel.revalidate();
		
		gamePanel.repaint();
		gamePanel.revalidate();
		
		paintColorsOnBoard(boardPosition);
	}
	
	public static void addBoardListener(ArrayList<JLabel> labels){
		boardListeners.removeAll(boardListeners);
		for(int i = 0; i<labels.size();i++){
			final int c = i;
			MouseListener ma =  new MouseAdapter(){
				public void mouseClicked(MouseEvent arg0) {
					if(labels.get(c).getText().equalsIgnoreCase(" ")||labels.get(c).getText().equalsIgnoreCase("!")){
						if(ClientConnection.mayMove){
							GameProtocol.move(c);
			
							updateGameBoard(c, ClientConnection.playerChar);
							ClientConnection.mayMove = false;
						}
					}
				}
			};
			
			boardListeners.add(i, ma);
			labels.get(i).addMouseListener(boardListeners.get(i));
		}
	}
	
	public static void revalidate(){
		gamePanel.repaint();
		gamePanel.revalidate();
	}
	
	public static void changeBoardSize(int width, int height, int fontSize){
		boardPanel.setLayout(new GridLayout(width, height));
		createBoard(width*height,fontSize);
		addBoardListener(boardPosition);
		clearBoard();
		boardPanel.repaint();
		boardPanel.revalidate();
	}
	
	public static void updateGameBoard(int position, String playerChar){
		int playerInt;
		if(playerChar.equalsIgnoreCase("x")){
			playerInt = 1;
			FrameworkController.AIModule.setCurrentPlayer(2);
		}
		else{
			playerInt = 2;
			FrameworkController.AIModule.setCurrentPlayer(1);
		}
		
		int rowCount = (int) Math.sqrt((double)amountOfPositions);
		int col = position / rowCount;
		int row = position - col * rowCount;
		
		if(LobbyView.gameChosen.equalsIgnoreCase("Tic-Tac-Toe")){
			TicTacToeAI.placeOnBoard(col,row,playerInt);
		}
		if(LobbyView.gameChosen.equalsIgnoreCase("Reversi")){		
			OthelloAI.placeOnBoard(position, playerInt);
		}
		else{
			boardPosition.get(position).setText(playerChar);
		}
		
		refreshBoard();

		paintColorsOnBoard(boardPosition);
	}
	
	public static void refreshBoard(){
	
		int[][] board = FrameworkController.AIModule.getBoard();
			
		for(int i = 0;i<FrameworkController.AIModule.getHeight();i++){
			for(int j = 0;j<FrameworkController.AIModule.getWidth();j++){
				if(board[i][j] == 1){
					boardPosition.get(i*FrameworkController.AIModule.getWidth()+j).setText("X");
				}
				if(board[i][j] == 2){
					boardPosition.get(i*FrameworkController.AIModule.getWidth()+j).setText("O");
				}
			}
		}	
	}
	
	public static void paintColorsOnBoard(ArrayList<JLabel> labels){
		if(LobbyView.gameChosen.equalsIgnoreCase("Tic-Tac-Toe")){
			for(int i = 0; i<labels.size();i++){
				if(labels.get(i).getText().equalsIgnoreCase("x")){
					labels.get(i).setForeground(new Color(220,20,60));
				}
				if(labels.get(i).getText().equalsIgnoreCase("o")){
					labels.get(i).setForeground(new Color(25,25,112));
				}
			}
		}
		if(LobbyView.gameChosen.equalsIgnoreCase("Reversi")){
			for(int i = 0; i<labels.size();i++){
				if(labels.get(i).getText().equalsIgnoreCase("x")){
					labels.get(i).setForeground(Color.black);
					labels.get(i).setBackground(Color.black);
				}
				if(labels.get(i).getText().equalsIgnoreCase("o")){
					labels.get(i).setForeground(Color.white);
					labels.get(i).setBackground(Color.white);
				}
			}
		}
		
		for(int i = 0; i<labels.size();i++){
			if(labels.get(i).getText().equalsIgnoreCase("!")){
				labels.get(i).setForeground(Color.orange);
				labels.get(i).setBackground(Color.orange);
			}
		}
	}
	
	public static void placeHint(int position){
		boardPosition.get(position).setText("!");
		paintColorsOnBoard(boardPosition);
		revalidate();
	}
	
	public static void removeHint(){
		for(int i = 0;i<boardPosition.size();i++){
			if(boardPosition.get(i).getText().equals("!")){
				boardPosition.get(i).setText(" ");
			}
		}
		//boardPanel.re
	}
}
