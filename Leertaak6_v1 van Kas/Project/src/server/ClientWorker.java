package server;

import java.io.*;
import java.net.*;

import framework.FrameworkMVC;

//Thread die het mogelijk maakt om met meerdere clients met de server te verbinden
public class ClientWorker implements Runnable 
{
	public Socket client;
	public ServerSocket server;

	 //Constructor
	 public ClientWorker(Socket client, ServerSocket server) {
	    this.client = client;
	    this.server = server;
	 }

	 public void run()
	 {
		 String line;
		 BufferedReader in = null;
		 PrintWriter out = null;
		
		 try
		 {
		  	in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		  	out = new PrintWriter(client.getOutputStream(), true);
		  	out.println("Welcome");
		  	out.println("Type \"getconnections\" for a list of connected sockets");
		  	out.println("Type \"getplayers\" for a list of connected players");
		  	out.println("Type \"isconnected\" to test if you're still connected");
		  	out.println("Type \"killserver\" to close the server");
		 } 
		 catch (IOException e) 
		 {
		 	System.out.println("in or out failed");
		 	System.exit(-1);
		 }
		 
		 while(true)
		 {
			  try
			  {		  
			  	  line = in.readLine();
			  	  if(line!=null){
				  	  //for testing purpose
				  	 if(line.contains("getconnections")){
				  		  out.println(FrameworkMVC.sl.getLobby());
				  	  }
				  	 
				     if(line.contains("getplayers")){
				  		  out.println(FrameworkMVC.sl.getPlayers());
				  	  }
				  	  
				  	  if(line.contains("isconnected")){
				  		  out.println("Client is still connected to server");
				  	  }
				  	  if(line.contains("killserver")){
				  		  out.println("Server is being closed");
				  		  server.close();
				  	  }
			  	  }
			  }
			  catch (IOException e) 
			  {
			  	  System.out.println("Read failed");
			  	  System.exit(-1);
			  }
		  }
	 }
}