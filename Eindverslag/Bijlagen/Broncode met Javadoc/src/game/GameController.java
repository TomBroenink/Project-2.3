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



/**
 * The Class GameController.
 */
public class GameController {
	
	/** The gv. */
	GameView gv;
	
	/** The AI playing. */
	public static boolean AIPlaying = false;
	
	/** The Human playing. */
	public static boolean HumanPlaying = true;
	
	
	/**
	 * Instantiates a new game controller.
	 *
	 * @param gv the gv
	 */
	public GameController(GameView gv){
		this.gv = gv;
		
		addActionListeners();
	}
	
	/**
	 * Adds the action listeners.
	 */
	public void addActionListeners(){
		gv.addActionListenerButton(gv.AISwitch,new AIListener());
		gv.addActionListenerButton(gv.hintButton,new HintListener());
		gv.addActionListenerButton(gv.forfeitButton,new ForfeitListener());
	}
	
	/**
	 * The listener interface for receiving AI events.
	 * The class that is interested in processing a AI
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addAIListener<code> method. When
	 * the AI event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see AIEvent
	 */
	class AIListener implements ActionListener{	

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
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
	
	/**
	 * The listener interface for receiving forfeit events.
	 * The class that is interested in processing a forfeit
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addForfeitListener<code> method. When
	 * the forfeit event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ForfeitEvent
	 */
	class ForfeitListener implements ActionListener{	

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to forfeit?",null, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				GameProtocol.forfeit();
			}
		}
	}
	
	/**
	 * The listener interface for receiving hint events.
	 * The class that is interested in processing a hint
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addHintListener<code> method. When
	 * the hint event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see HintEvent
	 */
	class HintListener implements ActionListener{	
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
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


	/**
	 * Checks if is empty.
	 *
	 * @param position the position
	 * @return true, if is empty
	 */
	private boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
}
