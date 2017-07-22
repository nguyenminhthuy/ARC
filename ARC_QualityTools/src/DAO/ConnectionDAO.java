package DAO;

import java.io.IOException;
import java.net.*;


public class ConnectionDAO {
    public String urlPart(URL url) {
        return url.getProtocol() + "://" + url.getAuthority();
    }
    
    public boolean internet_Connection(String link) throws IOException {
        boolean chk = false;
        try {
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == 200) {
                chk = true;
            }
        } catch (IOException ex) {
            chk = false;
        }
        return chk;
    }
}
