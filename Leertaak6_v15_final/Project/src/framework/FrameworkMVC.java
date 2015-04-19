package framework;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import game.GameController;
import game.GameView;
import server.ServerLobby;

public class FrameworkMVC 
{
	static FrameworkView fv;
	static FrameworkController fc;
	static LoginView loginv;
	static LobbyView lobbyv;
	static GameView gv;
	static GameController gc;
	public static ServerLobby sl;
	
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
	
	public static void setupGameMVC(){
		gv = new GameView(fv);
		gc = new GameController(gv);
	}
	
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
