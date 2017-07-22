package GUI;

// <editor-fold desc="Library">
import BEANS.*;
import DAO.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.xml.soap.*;
import java.net.*;
// </editor-fold>

public class frmLogin extends javax.swing.JFrame {

    // <editor-fold desc="Declare varibale.">
    private final String studyWSDL = "/OpenClinica-ws/ws/study/v1/studyWsdl.wsdl";
    User us;
    UserDAO usDAO;
    // </editor-fold>

    // <editor-fold desc="frmStatusCheck()"> 
    public frmLogin() {
        initComponents();
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnStatusCheck = new javax.swing.JButton();
        btnLateCheck = new javax.swing.JButton();
        txtURL = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quality Tools - Advanced Research Center INC");
        setName("frmLogin"); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Advanced Research Center, INC");

        jLabel1.setText("URL");

        jLabel3.setText("Username");

        jLabel4.setText("Password");

        btnStatusCheck.setText("Status Check");
        btnStatusCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatusCheckActionPerformed(evt);
            }
        });

        btnLateCheck.setText("Late Check");
        btnLateCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLateCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnStatusCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLateCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(txtURL)
                    .addComponent(txtPassword))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStatusCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLateCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean checkLogin() throws SOAPException, Exception {
        boolean flag = false;
        String pwd = String.valueOf(txtPassword.getPassword());
        usDAO = new UserDAO();
        us = new User();

        ConnectionDAO con = new ConnectionDAO();
        String url_Part = con.urlPart(new URL(txtURL.getText()));
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        String wsdlURL = url_Part + studyWSDL;
        SOAPMessage soapMessage;

        try {
            us.setUsername(txtUsername.getText());
            us.setPassword(pwd);
            us.setBaseURL(url_Part);

            soapMessage = soapConnection.call(usDAO.login(us), wsdlURL);
            SOAPBody soapBody = soapMessage.getSOAPBody();

            if (soapBody != null) {
                flag = !soapBody.hasFault();
            }
            soapConnection.close();
        } catch (MalformedURLException ex) {
        }
        return flag;
    }

    private boolean isEmptyInput() {
        String pwd = String.valueOf(txtPassword.getPassword());
        return txtURL.getText().length() == 0 || txtUsername.getText().length() == 0 || pwd.length() == 0;
    }

    // <editor-fold desc="Status Check Action">  
    private void btnStatusCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatusCheckActionPerformed
        try {
            us = new User();
            if (isEmptyInput()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Message", JOptionPane.ERROR_MESSAGE);
            } else {//cannot validate url, if url is invalid, it shows in "catch"
                if (checkLogin()) {
                    this.setVisible(false);
                    frmStatusCheck frmStatus = new frmStatusCheck(us);
                    frmStatus.pack();
                    frmStatus.setLocationRelativeTo(null);
                    ImageIcon img = new ImageIcon(frmStatusCheck.class.getResource("/image/logo.jpg"));
                    frmStatus.setIconImage(img.getImage());
                    frmStatus.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "One of your fields is invalid. Please check again.",
                            "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Login failed. Please check your URL",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login failed. Please check your URL",
                    "Message", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnStatusCheckActionPerformed
    // </editor-fold>

    // <editor-fold desc="Late Check Action">   
    private void btnLateCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLateCheckActionPerformed
        JOptionPane.showMessageDialog(this, "This function hasn't  finished yet!",
                "Message", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnLateCheckActionPerformed
    // </editor-fold>

    // <editor-fold desc="Main">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            frmLogin frmLogin = new frmLogin();
            frmLogin.pack();
            frmLogin.setLocationRelativeTo(null);
            ImageIcon img = new ImageIcon(frmLogin.class.getResource("/image/logo.jpg"));
            frmLogin.setIconImage(img.getImage());
            frmLogin.setVisible(true);
        });
    }
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLateCheck;
    private javax.swing.JButton btnStatusCheck;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtURL;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
