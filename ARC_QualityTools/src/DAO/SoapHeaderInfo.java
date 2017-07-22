package DAO;
import java.security.*;
import javax.xml.namespace.QName;
import javax.xml.soap.*;

public class SoapHeaderInfo {
    
    private final String prefix = "wsse";
    private final String prefixURI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-";
    private final String security_uri = prefixURI + "wssecurity-secext-1.0.xsd";
    private final String usernametoken_uri = prefixURI + "wssecurity-utility-1.0.xsd";
    private final String password_uri = prefixURI + "username-token-profile-1.0#PasswordText";    
    
    public String hashPwdbySHA1(String password) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }         
        return sb.toString();
    }
    
    public void SOAPHeader_Info(SOAPEnvelope envelope, String us, String pwd) 
            throws SOAPException, NoSuchAlgorithmException{
        SOAPHeader soapheader = envelope.getHeader();
        SOAPElement security = soapheader.addChildElement("Security", prefix, security_uri);        
        SOAPElement usernameToken = security.addChildElement("UsernameToken",prefix);
        usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-27777511");
        usernameToken.addAttribute(new QName("xmlns:wsu"), usernametoken_uri);
        SOAPElement username = usernameToken.addChildElement("Username",prefix);
        username.addTextNode(us);
        SOAPElement password = usernameToken.addChildElement("Password",prefix);
        password.addAttribute(new QName("Type"), password_uri);        
        password.addTextNode(hashPwdbySHA1(pwd));
    }
    
}
