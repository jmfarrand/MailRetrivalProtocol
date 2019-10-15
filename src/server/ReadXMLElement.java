package server;

// imports
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
// code adapted from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

/**
 * The ReadXMLElement class reads an xml document and returns whether the
 * "deleted" element is yes or no. This is used to whether to display the xml file in the
 * retrieve function of my protocol.
 * The code is adapted from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 *
 * @author 100385188 (orignally mkyong)
 * @version 1.0
 * @since 2018-01-10
 *
 */
public class ReadXMLElement {
    // the file path
    String filePath;
    // the element to return
    String elemeneantToReturn;

    /**
     * ReadXMLElement: This initialises the class with the file path
     * of the xml file to read
     * @param filePathIn The xml file to read
     */
    public ReadXMLElement(String filePathIn){
        filePath = filePathIn;
    }

    /**
     * readElement(): Reads the deleted element from the email xml file
     * @return elemeneantToReturn: The text of the deleted attribute it is either "yes" or "no"
     */
    public String readElement(){
        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // Get the root element
            Node email = doc.getFirstChild();
            NodeList list = email.getChildNodes();

            // Get the deleted element by tag name directly
            Node deleted = list.item(5);
            // assign the value of the deleted element to be able to be returend
            elemeneantToReturn = deleted.getTextContent();

            // now return the read element.
            return elemeneantToReturn;
        } catch (Exception e) {
            // if an error is thrown print out an error message.
            System.err.println("Error during reading xml element in file file: " + e.getMessage());
        }
        return ""; //return emptey string if an error occurs
    }
}
