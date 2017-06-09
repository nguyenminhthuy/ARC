package DAO;
import javax.xml.soap.*;


public class EventDAO {
    
    public SOAPMessage getEventName(String us, String pwd, String identify) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/studyEventDefinition/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        envelope.addNamespaceDeclaration("bean", "http://openclinica.org/ws/beans");
        
        SoapHeaderInfo sHeader = new SoapHeaderInfo();
        sHeader.SOAPHeader_Info(envelope, us, pwd);
        
        //soap body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElement = soapBody.addChildElement("listAllRequest","v1");
        SOAPElement soapBodyElement1 = soapBodyElement.addChildElement("studyEventDefinitionListAll","v1");
        SOAPElement soapElement = soapBodyElement1.addChildElement("studyRef","bean");
        SOAPElement soapElement1 = soapElement.addChildElement("identifier","bean");
        soapElement1.addTextNode(identify);
        
        soapMessage.saveChanges();
        
        return soapMessage;        
    }
    
    public SOAPMessage getEventOID(String us, String pwd, String identify) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/studySubject/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        envelope.addNamespaceDeclaration("bean", "http://openclinica.org/ws/beans");
        
        SoapHeaderInfo sHeader = new SoapHeaderInfo();
        sHeader.SOAPHeader_Info(envelope, us, pwd);
        
        //soap body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElement = soapBody.addChildElement("listAllByStudyRequest","v1");
        SOAPElement soapElement = soapBodyElement.addChildElement("studyRef","bean");
        SOAPElement soapElement1 = soapElement.addChildElement("identifier","bean");
        soapElement1.addTextNode(identify);
        
        soapMessage.saveChanges();
        
        return soapMessage;        
    }
}
