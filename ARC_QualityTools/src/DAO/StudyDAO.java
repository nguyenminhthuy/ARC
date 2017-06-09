package DAO;
import javax.xml.soap.*;


public class StudyDAO {
 
    //load all study
    public SOAPMessage loadAllStudies(String us, String pwd) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/study/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        
        SoapHeaderInfo sHeader = new SoapHeaderInfo();
        sHeader.SOAPHeader_Info(envelope, us, pwd);
        
        //soap body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElement = soapBody.addChildElement("listAllRequest","v1");
        
        soapMessage.saveChanges();        
        return soapMessage;        
    }
}
