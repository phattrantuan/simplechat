// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  public void handleMessageFromClientUI(String message)
  {
	    // detect commands
	    if (message.charAt(0) == '#')
	    {
	      runCommand(message);
	    }
	    else
	    {
	      try
	      {
	        sendToServer(message);
	      }
	      catch(IOException e)
	      {
	        clientUI.display
	          ("Could not send message to server.  Terminating client.");
	        quit();
	      }
	    }
	  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
  
  private void runCommand(String message)
  {
    // a bunch of ifs

    if (message.equalsIgnoreCase("#quit"))
    {
      quit();
    }
    else if (message.equalsIgnoreCase("#logoff"))
    {
      try
      {
        closeConnection();
      }
      catch(IOException e) {}
      clientUI.display("Ban da tat ket noi su dung logoff.");
    }
    else if (message.equalsIgnoreCase("#gethost"))
    {
      clientUI.display("Current host: " + getHost());
    }
    else if (message.equalsIgnoreCase("#getport"))
    {
      clientUI.display("Current port: " + Integer.toString(getPort()));
    }
    else if (message.toLowerCase().startsWith("#setport"))
    {
      try 
      {
        int newPort = Integer.parseInt(message.substring(9));
        setPort(newPort);
        clientUI.display
          ("Port changed to " + getPort());
      }
      catch (Exception e)
      {
        System.out.println("Port cong vua ket noi la:  " + getPort());
      }
    }
    
    else if (message.equalsIgnoreCase("#login"))
    {
      try
      {
    	  openConnection();
      }
      catch(IOException e) {}
      clientUI.display("ban da dang nhap lai");
    }
    else if (message.toLowerCase().startsWith("#sethost"))
    {
      setHost(message.substring(9));
      clientUI.display
        ("Host changed to " + getHost());
    }
    
   
  }


//phuong thuc hien thi ve phia client khi sv ngat ket noi
  protected void connectionException(Exception exception)
  {
    clientUI.display
      (" Ben server tat roi ban oi");
  }
}
//End of ChatClient class