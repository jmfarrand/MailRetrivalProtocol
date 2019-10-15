package server;

// java i/o imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// xml parser imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// more xml imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The ReadXML class takes a XML file that stores the email
 * and then parses it and prints out the result.
 * The code that I used was adapted from https://www.journaldev.com/898/read-xml-file-java-dom-parser
 *
 * @author 100385188 (originally pankaj)
 * @version 1.0
 * @since 2018-01-03
 *
 */
public class ReadXML {
    String filePath;

    // ===== CONSTRUCTOR =====
    /**
     * ReadXML: Initialises the ReadXML class with the
     * file path of the xml file to read
     * @param filePathIn The file path of the xml file to read
     */
    public ReadXML(String filePathIn){
        filePath = filePathIn;
    }

    /**
     * read: Read's the xml file using the xml file path that was passed
     * in as a consructor arugument
     * @return email: The email object that was found in the xml file
     */
    public Email read() {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        // initialise emailList to be able to return the list of found emails.
        Email email = new Email();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("email");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Email> emailList = new ArrayList<Email>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                emailList.add(getEmail(nodeList.item(i)));
            }
            email = emailList.get(0);

            // return the email object to do stuff with in main server code.
            return email;
        } catch (SAXException | ParserConfigurationException | IOException e){
            System.err.println("Error in ReadXML --> " + e.getMessage());
        }
        return email;
    }

    /**
     * getEmail: Populates an email object with what is found in the xml document
     * @param node The main node of the email
     * @return email: The populated email document stored as a class Email object
     */
    private static Email getEmail(Node node) {
        try {
            // create a new blank email object to populate with the data obtained from the xml file
            Email email = new Email();
            // determine if the first element in the xml document is "email" if so then continue
            if (node.getNodeName().equals("email")) {
                // cretate a list of the child nodes
                NodeList childNodes = node.getChildNodes();
                // create a enveleope node fetching what is stored at the index 1 in the main node list
                Node enveleope = childNodes.item(1);
                // popuate the email with what is in the envnelope section
                if (enveleope.getNodeName().equals("envnelope")) {
                    Element elementEnveleope = (Element)enveleope;
                    email.setFromAddress(getTagValue("from", elementEnveleope));
                    email.setToAddress(getTagValue("to", elementEnveleope));
                }

                // now create a message node fetching what is stored in the 3rd index of the main node list
                Node message = childNodes.item(3);
                //populate the email with what is found in the message section of the xml document
                if (message.getNodeName().equals("message")) {
                    Element elementMessage = (Element)message;
                    NodeList messageChildNodes = elementMessage.getChildNodes();

                    // HEADER
                    Node header = messageChildNodes.item(1);
                    if (header.getNodeName().equals("header")) {
                        Element elementHeader = (Element)header;
                        email.setFrom(getTagValue("from", elementHeader));
                        email.setTo(getTagValue("to", elementHeader));
                        email.setDate(getTagValue("date", elementHeader));
                        email.setSubject(getTagValue("subject", elementHeader));
                    }

                    // BODY
                    Node body = messageChildNodes.item(3);
                    if (body.getNodeName().equals("body")) {
                        Element elementBody = (Element)body;
                        // get the body text straight from the xml document.
                        //each line is seperated by a carriaege return character "\r".
                        //this is replaced by a "\n" new line feed using the String.replaceAll() method below.
                        String bodyTextFromEmail = getTagValue("text", elementBody);
                        // replace all occurences of the "\r" (carriage return) character with
                        // the "\n" (new line feed) character for printing all the text out
                        String bodyText = bodyTextFromEmail.replaceAll("\r", "\n");
                        email.setBodyText(bodyText);
                    }
                }
            }
            //return the populated email
            return email;
        } catch (Exception e) {
            System.err.println("Error while getting email --> "+  e.getMessage());
        }
        // only return null if there was an error
        return null;
    }

    /**
     * getTagValue: Gets the value of the tag in the xml file
     * @param tag The tag value to look for
     * @param element The root element of the tag
     * @return node.getNodeValue(): The value of the tag as a String object.
     */
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node)nodeList.item(0);
        return node.getNodeValue();
    }
}
