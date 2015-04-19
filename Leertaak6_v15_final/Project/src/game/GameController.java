package game;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JOptionPane;

import server.ClientConnection;
import AI.TicTacToeAI;
import framework.FrameworkController;
import framework.FrameworkView;
import framework.GameProtocol;
import framework.LobbyView;



public class GameController {
	GameView gv;
	public static boolean AIPlaying = false;
	public static boolean HumanPlaying = true;
	
	
	public GameController(GameView gv){
		this.gv = gv;
		
		addActionListeners();
	}
	
	public void addActionListeners(){
		gv.addActionListenerButton(gv.AISwitch,new AIListener());
		gv.addActionListenerButton(gv.hintButton,new HintListener());
		gv.addActionListenerButton(gv.forfeitButton,new ForfeitListener());
	}
	
	class AIListener implements ActionListener{	

		public void actionPerformed(ActionEvent arg0) {
			if(gv.AISwitch.getText().contains("ON")){
				AIPlaying = true;
				HumanPlaying = false;
				ClientConnection.mayMove = true;
				ClientConnection.makeAIMove();
				gv.AISwitch.setText("TURN AI OFF");
				gv.AISwitch.revalidate();
			}
			else{
				AIPlaying = false;
				HumanPlaying = true;
				gv.AISwitch.setText("TURN AI ON");
				gv.AISwitch.revalidate();
			}
			
		}
	}
	
	class ForfeitListener implements ActionListener{	

		public void actionPerformed(ActionEvent arg0) {
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to forfeit?",null, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				GameProtocol.forfeit();
			}
		}
	}
	
	class HintListener implements ActionListener{	
		public void actionPerformed(ActionEvent arg0) {
			int position = 0;
			if(LobbyView.chooseGame.getSelectedItem().toString().equalsIgnoreCase("Reversi")){
				position = FrameworkController.AIModule.getBestMove(FrameworkController.AIModule.getCurrentPlayer());
			}
			if(LobbyView.chooseGame.getSelectedItem().toString().equals("Tic-tac-toe")){
				position = 4;
				Random random=new Random();  
				while(!isEmpty(position)){
					position =random.nextInt(9);
				}
			}
			GameView.placeHint(position);
		}
	}


	private boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
}
