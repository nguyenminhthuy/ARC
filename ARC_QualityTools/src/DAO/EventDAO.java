/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;


public class EventDAO {
    public String hashPwdbySHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }         
        return sb.toString();
    }
    
    public SOAPMessage createSOAPRequest(String username, String password, String identify) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/studyEventDefinition/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        envelope.addNamespaceDeclaration("bean", "http://openclinica.org/ws/beans");
        
        String prefix = "wsse";
        String prefixURI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-";
        String security_uri = prefixURI + "wssecurity-secext-1.0.xsd";
        String usernametoken_uri = prefixURI + "wssecurity-utility-1.0.xsd";
        String password_uri = prefixURI + "username-token-profile-1.0#PasswordText";    
        
        //soap header
        SOAPHeader soapheader = envelope.getHeader();
        SOAPElement security = soapheader.addChildElement("Security", prefix, security_uri);
        
        SOAPElement usernameToken = security.addChildElement("UsernameToken",prefix);
        usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-27777511");
        usernameToken.addAttribute(new QName("xmlns:wsu"), usernametoken_uri);        
        
        SOAPElement username_text = usernameToken.addChildElement("Username",prefix);
        username_text.addTextNode(username);
        
        SOAPElement password_text = usernameToken.addChildElement("Password",prefix);
        password_text.addAttribute(new QName("Type"), password_uri);
        password_text.addTextNode(hashPwdbySHA1(password));
        
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
    
}
