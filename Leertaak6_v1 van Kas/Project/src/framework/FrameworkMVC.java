package framework;

import server.ServerLobby;
import server.ServerSetup;

public class FrameworkMVC 
{
	static FrameworkView fv;
	static LoginView loginv;
	static ServerSetup ss;
	static LobbyView lobbyv;
	public static ServerLobby sl;
	
	public static void main(String[] args)
	{
		//Setup the views
		fv = new FrameworkView(500,700,"DKSolutions");
		loginv = new LoginView(fv);
		lobbyv = new LobbyView(fv);
		
		//Setup server etc
		sl = new ServerLobby();
		ss = new ServerSetup(1234);
	}
}
