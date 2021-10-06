// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("ben sever da ngat ket noi");
  }
  
  //Class methods ***************************************************

  
  //hien thi thong bao khi connect thanh cong
  protected void clientConnected(ConnectionToClient client) 
  {
    // hien thi tren may chu va may khach da ket noi
    String msg = "Ban da ket noi thanh cong1";
    System.out.println(msg);
    this.sendToAllClients(msg);
  }
  
  
  //hien thi thong bao khi client huy ket noi
  protected void clientException(
		    ConnectionToClient client, Throwable exception) 
		  {
		    String msg = "Co nguoi huy ket noi voi phia server";
		    System.out.println(msg);
		    this.sendToAllClients(msg);//khi co 2 client cùng connect vào sv thì khi 1 client disconnect thì client van nhan duoc thong bao
		  }
  
  //server disconnect => client same disconnect
  protected void clientDisconnected(
		    ConnectionToClient client)
		  {
		  
		    String msg =  "client has been disconnected";

		    System.out.println(msg);
		    this.sendToAllClients(msg);
		  }
  
  
  
  ChatIF serverUI;
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
    this.serverUI = serverUI;
  }
//
  public void handleMessageFromServerUI(String message)
  {
    if (message.charAt(0) == '#')
    {
      try {
		runCommand(message);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    else
    {
      // send message to clients
    	System.out.println(message);
      this.sendToAllClients("SERVER MSG> " + message);
    }
    
  }
  
  private void runCommand(String message) throws IOException
  {
    // run commands
    // a series of if statements

    if (message.equalsIgnoreCase("#quit"))
    {
      quit();
    }
    
    else if (message.toLowerCase().startsWith("#setport"))
    {
      if (getNumberOfClients() == 0 && !isListening())
      {
        // If there are no connected clients and we are not 
        // listening for new ones, assume server closed.
        // A more exact way to determine this was not obvious and
        // time was limited.
        int newPort = Integer.parseInt(message.substring(9));
        setPort(newPort);
        //error checking should be added
        System.out.println("Server port changed to " + getPort());
      }
    }else if (message.equalsIgnoreCase("#getport"))
    {
    	 System.out.println("Currently port: " + Integer.toString(getPort()));
      }
    else if (message.equalsIgnoreCase("#stop"))
    {
      stopListening();
      System.out.println("server has been stop");
    }
    else if (message.equalsIgnoreCase("#close"))
    {
    	close();
      System.out.println("server has been close");
    }
    else if (message.equalsIgnoreCase("#start"))
    {
    	if(!isListening()) {
			listen();
	  	}
      System.out.println("server has been re-start");
    }
    else if (message.equalsIgnoreCase("#getport"))
    {
    	if(!isListening()) {
			listen();
	  	}
      System.out.println("Port: "+ getPort());
    }
    else if (message.toLowerCase().startsWith("#setport"))
    {
      if (getNumberOfClients() == 0 && !isListening())
      {
       
        int newPort = Integer.parseInt(message.substring(9));
        setPort(newPort);
        //error checking should be added
        System.out.println
          ("Server port changed to " + getPort());
      }
      else
      {
    	  System.out.println
          ("The server is not closed. Port cannot be changed.");
      }
    }
  }
 
  
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e)
    {
    }
    System.exit(0);
  }
  public static void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }
  
  

  
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    //code nhạp vào server
    EchoServer sv = new EchoServer(port);
    //code nhạp vào server
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    
    //code nhạp vào server
    
   
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        display(message);
        sv.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
    //code nhạp vào server //code nhạp vào server
  }
}
//End of EchoServer class