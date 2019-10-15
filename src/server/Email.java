package server;

// adapted from https://www.journaldev.com/898/read-xml-file-java-dom-parser
/**
 * Email class: class that stores all the data that is retrived from the
 * XML file generated from the SMPTClienterver application.
 * The code is adapted from https://www.journaldev.com/898/read-xml-file-java-dom-parser
 *
 * @author 100385188 (originally pankaj)
 * @version 1.0
 * @since 2018-01-03
 *
 */
public class Email {
    // initialise private local variables to store what has been retrived from the xml file.
    private String fromAddress;
    private String toAddress;
    private String from;
    private String to;
    private String date;
    private String subject;
    private String bodyText;

    /**
     * Email: Constructor that initialises the Email class
     * assinging every local variable with an emptey string value
     */
    public Email() {
        fromAddress = "";
        toAddress = "";
        from = "";
        to = "";
        date = "";
        subject = "";
        bodyText = "";
    }
    /**
     * getFromAddress: Gets the from address stored in the email document
     *
     * @return getFromAddress: The user who sent the email address
     */
    public String getFromAddress(){
        return fromAddress;
    }

    /**
     * SetFromAddress: Sets the from address of the email
     *
     * @param fromAddressIn The from email address to set to the email
     */
    public void setFromAddress(String fromAddressIn){
        fromAddress = fromAddressIn;
    }

    /**
     * getToAddress: returns the mail address of the user who recieves the email
     * @return toAddress: The address of the user who recieves the email
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * setToAddress: sets the to mail address of the email from the xml file
     * @param toAddressIn The to email address to set to the email
     */
    public void setToAddress(String toAddressIn){
        toAddress = toAddressIn;
    }

    /**
     * getFrom: Gets the from name of the user who sent the email
     * @return from: The name of the user that sent the email
     */
    public String getFrom(){
        return from;
    }

    /**
     * setFrom: Sets the from name of the user who sent the email stored as a xml file
     * @param fromIn The from name obtained from the xml file
     */
    public void setFrom(String fromIn){
        from = fromIn;
    }

    /**
     * getTo: Gets the to name of the user that recieved the email
     * @return to: The name of the user that recives the email
     */
    public String getTo(){
        return to;
    }

    /**
     * setTo: Sets the to name of the recipienet got from the xml file
     * @param toIn The name of the recipient of the email
     */
    public void setTo(String toIn){
        to = toIn;
    }

    /**
     * getDate: Get's the date and time that the email was sent
     * @return date: The date and time that the email was sent
     */
    public String getDate(){
        return date;
    }

    /**
     * setDate: Set's the date and time obtained from the xml file
     * @param dateIn The date and time that the email was sent
     */
    public void setDate(String dateIn){
        date = dateIn;
    }

    /**
     * getSubject: Gets the subject of the email address
     * @return subject: The subject of the email nessage
     */
    public String getSubject(){
        return subject;
    }

    /**
     * setSubject: Sets the subject of the email address from the xml file
     * @param subjectIn The subject of the email address that was obtained from the xml document
     */
    public void setSubject(String subjectIn){
        subject = subjectIn;
    }

    /**
     * getBodyText: Get's the body text of the email message
     * @return bodyText: A string that contains the text in the body of the email document
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * setBodyText: Set's the body text of the email message that is recieved from the xml file.
     * @param bodyTextIn The body text of the email from the xml document
     */
    public void setBodyText(String bodyTextIn) {
        bodyText = bodyTextIn;
    }
}
