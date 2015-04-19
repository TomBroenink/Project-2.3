package framework;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import game.GameController;
import game.GameView;
import server.ServerLobby;

/**
 * The Class FrameworkMVC.
 */
public class FrameworkMVC 
{
	
	/** The fv. */
	static FrameworkView fv;
	
	/** The fc. */
	static FrameworkController fc;
	
	/** The loginv. */
	static LoginView loginv;
	
	/** The lobbyv. */
	static LobbyView lobbyv;
	
	/** The gv. */
	static GameView gv;
	
	/** The gc. */
	static GameController gc;
	
	/** The sl. */
	public static ServerLobby sl;
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{
		setLookAndFeel("Nimbus");
		//Setup the views
		//Frame
		fv = new FrameworkView(500,700,"DKSolutions");
		
		//Views
		loginv = new LoginView(fv);
		lobbyv = new LobbyView(fv);
		
		gv = new GameView(fv);
		
		//Framework/lobby ciontroller
		fc = new FrameworkController(lobbyv);
		gc = new GameController(gv);
		
		//Setup server etc
		sl = new ServerLobby();
	}
	
	/**
	 * Setup game mvc.
	 */
	public static void setupGameMVC(){
		gv = new GameView(fv);
		gc = new GameController(gv);
	}
	
	/**
	 * Sets the look and feel.
	 *
	 * @param lookAndFeel the new look and feel
	 */
	public static void setLookAndFeel(String lookAndFeel){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if (lookAndFeel.equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		    
		    
		} catch (Exception e) {
		    // If Nimbus is not available, fall back to cross-platform
		    try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } 
		    catch (Exception ex) {}
		}
	}
}
