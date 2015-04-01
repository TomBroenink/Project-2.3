package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSetup 
{
	public ServerSetup(int portNumber)
	{
		System.out.println("Server started on port : "+portNumber);
		Setup(portNumber);
	}
	
	public static void Setup(int portNumber)
	{
		ServerSocket server = null;

		try
		{
		    server = new ServerSocket(portNumber); 
		} 
		catch (IOException e)
		{
			System.out.println("Could not listen on port "+portNumber);
			System.out.println("Server is already online");
		}
	
		while(true)
		{
			ClientWorker w;
			try
			{
				if(server!=null){
					w = new ClientWorker(server.accept(), server);
					Thread t = new Thread(w);
					t.start();
				}
			} 
			catch (IOException e) 
			{
				System.out.println("Accept failed");
				System.exit(-1);   
			}
		}	
	}
}
