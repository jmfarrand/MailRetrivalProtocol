package server;

// java i/o imports
import java.io.File;
import java.io.IOException;

// javax xml imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// w3c xml imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The ModifyXML class takes in a xml file that stores the email
 * and then edits the file to change the delete attribute to yes.
 * The code used is adapted from https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
 *
 * @author 100385188 (origiannly mkyong)
 * @version 1.0
 * @since 2018-01-09
 *
 */

public class ModifyXML {
    // the file path of the xml file
    String filePath;
    // the text to set the deleted element to
    String text;
    // ===== CONSTRUCTOR =====

    /**
     * ModifyXML: Initialised the ModifyXML class with the
     * file path of the xml file to read
     * @param filePathIn The file path of the xml file to read.
     */
    public ModifyXML(String filePathIn) {
        filePath = filePathIn;
    }

    /**
     * modify: Modify's the xml document.
     */
    public void modify(String textIn) {
        //set the text element to whatever was passed in to the method
        text = textIn;
        try {
            String filepath = filePath;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node email = doc.getFirstChild();
            NodeList list = email.getChildNodes();

            // Get the deleted element by tag name directly
            Node deleted = list.item(5);
            // now set the text content of the element to yes.
            deleted.setTextContent(text);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

        } catch (IOException | ParserConfigurationException | TransformerException | SAXException e) {
            // if an error is thrown print out an error message.
            System.err.println("Error during modifying xml file: " + e.getMessage());
        }
    }
}