/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;
import javax.xml.soap.*;

public class StudySubjectDAO {
    
    //load study subject by study
    public SOAPMessage loadStudySubjectbyStudy(String us, String pwd, String identify) throws Exception {   
    
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
