package DAO;

import BEANS.User;
import javax.xml.soap.*;

public class UserDAO {
        
    public SOAPMessage login(User u) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/study/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        
        SoapHeaderInfo sHeader = new SoapHeaderInfo();
        sHeader.SOAPHeader_Info(envelope, u.getUsername(), u.getPassword());
        
        //soap body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElement = soapBody.addChildElement("listAllRequest","v1");
        
        soapMessage.saveChanges();        
        return soapMessage;        
    }
}
