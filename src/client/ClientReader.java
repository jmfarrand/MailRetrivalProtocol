package client;

import java.io.*;
import java.net.*;

/**
 * The ClientReader class handles reading messages that are sent from the server.
 * It runs in a loop checking that the server has sent a message.<br>
 * This class implements runnable to be able to run this in a Thread
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 */

//this thread is for the client reader
public class ClientReader implements Runnable {
	Socket clientSocket = null;
	static Boolean running = null;
	String data = null;
	
	/**
	 * ClientReader: Creates a new instance of the ClientReader class
	 * Passing in the socket to communicate over and a Boolean to decide if the program
	 * Is 
	 * @param inSoc The socket to communicate over
	 * @param runningIn The running Boolean so both threads can quit at the same time
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	public ClientReader (Socket inSoc, Boolean runningIn) {
		clientSocket = inSoc;
		running = runningIn;
	}
	
	/**
	 * run: This is the code that is runned when the thread is run
	 * 
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	
	public void run() {
		try {
			DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
			while (running) {
				//Catch the incoming data in a data stream, read a line and output it to the console
				data = dataIn.readUTF();
				//Print out message
	            System.out.println("Message from server --> " + data);
			}
			//close the stream once we are done with it
			dataIn.close();
		}
		catch (SocketException se) {
			// again I'm not sure how to get the QUIT command to work so it just prints out service quitting to the client.
			// the server does send over the quitting message but again, I apologize for not figuring out the proper way to do it. :(
			System.out.println("Message from server --> 221: Service quitting...");
			// printing the actuall error to err console so that the user knows whats gone wring in case of a legitamate SocketException error.
			System.err.println("Socket error: " + se.getMessage());
		}
		catch (Exception e) {
            //Exception thrown (e) when something went wrong, pushing message to the console
            System.err.println("Error in ClientReader--> " + e.getMessage());
        }
	}
}
