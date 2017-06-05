/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.net.URL;
import java.security.*;
import javax.xml.namespace.*;
import javax.xml.soap.*;


public class UserDAO {
    
    public String urlPart(URL url) {
        return url.getProtocol() + "://" + url.getAuthority();
    }
    
    public String hashPwdbySHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }         
        return sb.toString();
    }
    
    //login
    public SOAPMessage createSOAPRequest(String username, String password) throws Exception {   
    
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        String studyURL = "http://openclinica.org/ws/study/v1";
        
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", studyURL);
        
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
        
        soapMessage.saveChanges();        
        return soapMessage;        
    }
}
