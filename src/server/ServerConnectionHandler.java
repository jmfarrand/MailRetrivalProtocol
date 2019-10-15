package server;

import java.io.*;
import java.net.*;

/**
 * The ServerConnectionHandler class handles a new incoming client connection
 * for the mail retrival procol. The user is first prompted to enter in a user name
 * and then a password. If the user entered in a correct user name & password
 * then the user can start the main protocol.<br>
 * This class implements runnable to be able to run this in a Thread
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 * 
 */

public class ServerConnectionHandler implements Runnable {
	Socket clientSocket = null;
    public static String strInput;
    public static String[] strInputArray;
    State state = State.NC;
    /**
     * ServerConnectionHandler: Initialises the ServerConnectionHandler class taking
     * the socket to communicate over as a paramater
     * 
     * @param inSoc The socket to communicate over using the class
     * @author 100385188
     * @version 1.0
     * @since 2017-12-08
     */
	public ServerConnectionHandler (Socket inSoc) {
		clientSocket = inSoc;
	}
	/**
	 * run: the code that is called when the instance of the ServerConnectionHandler class calls the method run(). 
	 */
	public void run() {
		try {
			// print out message that confirms that client is conencted
			System.out.println("Client connected");
			// create data input and output streams
			DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
			String userName = "";
			Boolean running = false;
			Boolean password = false;
			// set the user's state to authentication
			state = State.AU;
			dataOut.writeUTF("You are now in the authentication (AU) stage. You need to enter in the username and password in order to log on.");
			// USER NAME
			Boolean enteringUserName = true;
			while (enteringUserName == true) {
				dataOut.writeUTF("Enter in the user name");
				userName = dataIn.readUTF();
				if (userName.equals("test1")) {
					dataOut.writeUTF("100: User name success");
					password = true;
					enteringUserName = false;
				} else if (userName.equals("test2")) {
					dataOut.writeUTF("100: User name success");
					password = true;
				} else {
					dataOut.writeUTF("110: User name rejected");
					password = false;
					running = false;
				}
			}
            // PASSWORD
			Boolean enteringPassword = true;
			while (enteringPassword == true) {
				if (password){
					dataOut.writeUTF("Enter in the password");
					String passwordInput = dataIn.readUTF();
					if (userName.equals("test1") && passwordInput.equals("test1password")){
						dataOut.writeUTF("120: Password success");
						running = true;
						enteringPassword = false;
					} else if (userName.equals("test2") && passwordInput.equals("test2password")) {
						dataOut.writeUTF("120: Password success");
						running = true;
					} else {
						dataOut.writeUTF("130: Password rejected");
						running = false;
					}
				}
			}

            // only print out these messages if the user has successfully authenticated.
            if (running) {
            	// send message to client telling them that they have authenticated
				dataOut.writeUTF("You have successfully authenticatieted. You are now in the CE (Connection established) staged and can now use the server to retrive or delete your mail");
				state = State.CE;
				// send message to client that service is ready
				dataOut.writeUTF("220: Service Ready");
			}
			// the command the user enteres
			String command = "";
			// the address (the second paramater) the user enteres
			String emailChoice = "";
			Boolean mainCommand = false;

			// MAIN RUNNING LOOP!!!
			while(running) {
				if (dataIn.available() > 0) {
					//read the user input from the socket connection and
					//assing it to the strInput variable
					strInput = dataIn.readUTF();
					// determine if the input string contains a space.
					// if so then split the string to get the command the user requested
					// if not send a prompt for the user to enter in a domain name
					if (strInput.contains(" ")) {
						strInputArray = strInput.split(" ");
						if (strInputArray.length == 2) {
							// move what is stored in the splitted input array into local variables
							command = strInputArray[0];
							emailChoice = strInputArray[1];
							mainCommand = true;
						} else {
							dataOut.writeUTF("500: Syntax error. command unrecognized. ");
							dataOut.flush();
						}
					} else if (strInput.toUpperCase().equals("NOOP")) {
						//NOOP - FEATURE2
						dataOut.writeUTF("250: OK");
						mainCommand = false;
						dataOut.flush();
					} else if (strInput.toUpperCase().equals("QUIT")) {
						//QUIT
						dataOut.writeUTF("221: Service quitting...");
						mainCommand = false;
						running = false;
						dataOut.flush();
					} else {
						dataOut.writeUTF("500: Syntax error. command unrecognized.");
						dataOut.flush();
					}
					if (mainCommand == true) {
						if (!command.toUpperCase().equals("QUIT") && strInputArray.length == 2) {
							// ******************************************************
							// *													*
							// *					MAIN CODE						*
							// *													*
							// ******************************************************
							if (command.toUpperCase().equals("RETRIVE") && state == State.CE) {
								// RETRIVE
								if (emailChoice.toUpperCase().equals("MAIL1") || emailChoice.toUpperCase().equals("ALL")) {
									//MAIL1
									// initialise the class for reading the xml element to determine if it should be read or not
									ReadXMLElement document1Deleted = new ReadXMLElement(userName.toLowerCase()  + "mail1.xml");
									// assign the deleted text to a local variable.
									String document1DeletedString = document1Deleted.readElement();
									// determine if the first email is deleted or not
									// if it is then dont print the contents
									if (document1DeletedString.equals("no")) {
										// print out email contents
										ReadXML document1 = new ReadXML(userName.toLowerCase()  + "mail1.xml");
										Email email1 = document1.read();
										dataOut.writeUTF("Printing out the first email address.");
										dataOut.writeUTF("Email from address: " + email1.getFromAddress());
										dataOut.writeUTF("Email to address: " + email1.getToAddress());
										dataOut.writeUTF("Email from: " + email1.getFrom());
										dataOut.writeUTF("Email to: " + email1.getTo());
										dataOut.writeUTF("Email date: " + email1.getDate());
										dataOut.writeUTF("Email subject: " + email1.getSubject());
										dataOut.writeUTF("Email body text: " + email1.getBodyText());
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has been deleted so send message to user to tell them that the message has been delteted.
										dataOut.writeUTF("400: User has deleted the email.");
										dataOut.flush();
									}
								}
								if (emailChoice.toUpperCase().equals("MAIL2") || emailChoice.toUpperCase().equals("ALL")) {
									// MAIL2
									// initialise the class for reading the xml element to determine if it should be read or not
									ReadXMLElement document2Deleted = new ReadXMLElement(userName.toLowerCase()  + "mail2.xml");
									// assign the deleted text to a local variable.
									String document2DeletedString = document2Deleted.readElement();
									// determine if the second email is deleted or not
									// if it is then dont print the contents
									if (document2DeletedString.equals("no")) {
										// print out email contents
										ReadXML document2 = new ReadXML(userName.toLowerCase() + "mail2.xml");
										Email email2 = document2.read();
										dataOut.writeUTF("Printing out the second email address.");
										dataOut.writeUTF("Email from address: " + email2.getFromAddress());
										dataOut.writeUTF("Email to address: " + email2.getToAddress());
										dataOut.writeUTF("Email from: " + email2.getFrom());
										dataOut.writeUTF("Email to: " + email2.getTo());
										dataOut.writeUTF("Email date: " + email2.getDate());
										dataOut.writeUTF("Email subject: " + email2.getSubject());
										dataOut.writeUTF("Email body text: " + email2.getBodyText());
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has been deleted so send message to user to tell them that the message has been delteted.
										dataOut.writeUTF("400: User has deleted the email.");
										dataOut.flush();
									}
								} else {
									dataOut.writeUTF("410: The email you wanted to view doesn't exist.");
									dataOut.flush();
								}
							} else if (command.toUpperCase().equals("DELETE") && state == State.CE) {
								// DELETE
								if (emailChoice.toUpperCase().equals("MAIL1") || emailChoice.toUpperCase().equals("ALL")) {
									// deleting the first email
									// initialise the class for reading the xml element to determine if it has already been deleted or not
									ReadXMLElement document1Deleted = new ReadXMLElement(userName.toLowerCase()  + "mail1.xml");
									// assign the deleted text to a local variable.
									String document1DeletedString = document1Deleted.readElement();
									// determine if the first email is deleted or not
									// if it is then dont delete it as it already has been
									if (document1DeletedString.equals("no")) {
										// set the email to deleted
										ModifyXML document1 = new ModifyXML(userName.toLowerCase() + "mail1.xml");
										document1.modify("yes");
										dataOut.writeUTF("420: Email has been deleted.");
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has been deleted so send message to user to tell them that the message has been delteted.
										dataOut.writeUTF("430: The email has already been deleted.");
										dataOut.flush();
									}
								}
								if (emailChoice.toUpperCase().equals("MAIL2") || emailChoice.toUpperCase().equals("ALL")) {
									// deleting the second email
									// initialise the class for reading the xml element to determine if it has already been deleted or not
									ReadXMLElement document2Deleted = new ReadXMLElement(userName.toLowerCase()  + "mail2.xml");
									// assign the deleted text to a local variable.
									String document2DeletedString = document2Deleted.readElement();
									// determine if the first email is deleted or not
									// if it is then dont delete it as it already has been
									if (document2DeletedString.equals("no")) {
										// set the email to deleted
										ModifyXML document2 = new ModifyXML(userName.toLowerCase() + "mail2.xml");
										document2.modify("yes");
										dataOut.writeUTF("420: Email has been deleted.");
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has been deleted so send message to user to tell them that the message has been delteted.
										dataOut.writeUTF("430: The email has already been deleted.");
										dataOut.flush();
									}
								} else {
									dataOut.writeUTF("440: The email you want to delete doesn't exist");
									dataOut.flush();
								}
							} else if (command.toUpperCase().equals("UNDELETE") && state == State.CE) {
								// UNDELETE - FEATURE1
								if (emailChoice.toUpperCase().equals("MAIL1") || emailChoice.toUpperCase().equals("ALL")) {
									// un-deleting the first email
									// deleting the second email
									// initialise the class for reading the xml element to determine if it has already been un-deleted or not
									ReadXMLElement document1UnDeleted = new ReadXMLElement(userName.toLowerCase()  + "mail1.xml");
									// assign the deleted text to a local variable.
									String document1UnDeletedString = document1UnDeleted.readElement();
									// determine if the first email is deleted or not
									// if it is then dont print the contents
									if (document1UnDeletedString.equals("yes")) {
										// set the email to un-deleted
										ModifyXML document1 = new ModifyXML(userName.toLowerCase() + "mail1.xml");
										document1.modify("no");
										dataOut.writeUTF("450: Email has been un-deleted.");
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has already been un-deleted so send message to user to tell them that the message has already been un-delteted.
										dataOut.writeUTF("460: The email has already been un-deleted.");
										dataOut.flush();
									}
								}
								if (emailChoice.toUpperCase().equals("MAIL2") || emailChoice.toUpperCase().equals("ALL")) {
									// un-deleting the second email
									// deleting the second email
									// initialise the class for reading the xml element to determine if it has already been un-deleted or not
									ReadXMLElement document2UnDeleted = new ReadXMLElement(userName.toLowerCase()  + "mail2.xml");
									// assign the deleted text to a local variable.
									String document2UnDeletedString = document2UnDeleted.readElement();
									// determine if the first email is deleted or not
									// if it is then dont print the contents
									if (document2UnDeletedString.equals("yes")) {
										// set the email to un-deleted
										ModifyXML document2 = new ModifyXML(userName.toLowerCase() + "mail2.xml");
										document2.modify("no");
										dataOut.writeUTF("450: Email has been un-deleted.");
										dataOut.writeUTF("250: OK");
										dataOut.flush();
									} else {
										// the email has already been un-deleted so send message to user to tell them that the message has already been un-delteted.
										dataOut.writeUTF("460: The email has already been un-deleted.");
										dataOut.flush();
									}
								} else {
									dataOut.writeUTF("470: The email you want to un-delete doesn't exist");
									dataOut.flush();
								}
							} else {
								// the user didnt enter in a valid command so send message informing
								// them that the command they entered was incorrect
								dataOut.writeUTF("500: Syntax error. command unrecognized.");
								dataOut.flush();
							}
						} else if (command.toUpperCase().equals("QUIT")) {
							// QUIT is handled above when message is sent to the client
						} else {
							// do nothing
						}
						// clear the array so that the main code isnt called upon again when the user enteres in the next command
						strInputArray = new String[0];
					}
				}
			}
			// end of main running loop
		}
		catch (Exception e) {
			//Exception thrown (except) when something went wrong, pushing message to the console
			System.err.println("Error in ServerHandler--> " + e.getMessage());
		}
	}
}
