package GUI;

import DAO.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import javax.swing.*;
import javax.xml.soap.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class frmStatusCheck extends javax.swing.JFrame {

    private final String studyWSDL = "/OpenClinica-ws/ws/study/v1/studyWsdl.wsdl";
    private final String studySubjectWSDL = "/OpenClinica-ws/ws/studySubject/v1/studySubjectWsdl.wsdl";
    private final String eventWSDL = "/OpenClinica-ws/ws/studyEventDefinition/v1/studyEventDefinitionWsdl.wsdl";
    public static List<String> arr_eventOID = new ArrayList<>();   
    
    
    public frmStatusCheck() throws Exception{
        initComponents();  
    }
    
    //received data from login form
    public frmStatusCheck(String para_us, String para_pwd, String para_wsdl) throws Exception {
        initComponents();   
        
        cbProtocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));
        cbPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));
        cbEvent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));
        
        lbUsername.setText(para_us);
        lbPassword.setText(para_pwd);
        lbWSDL.setText(para_wsdl);
        
        lbPassword.setVisible(false);
        lbWSDL.setVisible(false);
        
        loadAllStudies();
    }
    
    private void loadAllStudies() throws Exception{        
        try {
            StudyDAO studyDAO = new StudyDAO();
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();
            
            String studyURL;
            studyURL = lbWSDL.getText() + studyWSDL;
            String us = lbUsername.getText();
            String pwd = lbPassword.getText();
            
            SOAPMessage soapMessage = soapConnection.call(studyDAO.createSOAPRequest(us, pwd), studyURL);
            
            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("study");
            if(nList != null){
                if(nList.getLength() < 1){
                    cbProtocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No data to display" }));
                } 
                else {
                    for(int temp = 0; temp < nList.getLength(); temp ++){
                        Node nNode = (Node) nList.item(temp);
                        if(nNode.getNodeType() == Node.ELEMENT_NODE){
                            Element eElement = (Element) nNode;                        
                            cbProtocol.addItem(eElement.getElementsByTagName("identifier").item(0).getTextContent());
                            cbProtocol.setName(eElement.getElementsByTagName("oid").item(0).getTextContent());
                        }
                    } 
                } 
            } 
            soapConnection.close();            
        } 
        catch (UnsupportedOperationException | SOAPException e) {            
        }
    }
           
    private void loadStudySubjectbyStudy(String studyName) throws Exception{        
        try {
            StudySubjectDAO studySubjectDAO = new StudySubjectDAO();
            
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();
            
            String studySubjectURL;
            studySubjectURL = lbWSDL.getText() + studySubjectWSDL;
            String us = lbUsername.getText();
            String pwd = lbPassword.getText();
            
            SOAPMessage soapMessage = soapConnection.call(
                    studySubjectDAO.createSOAPRequest(us, pwd, studyName), studySubjectURL);
            
            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("ns2:studySubject");
            
            if(nList != null){
                if(nList.getLength() < 1){
                    cbPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No data to display" }));
                } 
                else {
                    for(int temp = 0; temp < nList.getLength(); temp ++){
                        Node nNode = (Node) nList.item(temp);
                        if(nNode.getNodeType() == Node.ELEMENT_NODE){
                            Element eElement = (Element) nNode;                        
                            cbPatient.addItem(eElement.getElementsByTagName("ns2:label").item(0).getTextContent());
                        }
                    } 
                }
            }              
            soapConnection.close();            
        } 
        catch (UnsupportedOperationException | SOAPException e) {            
        }
    }
        
    @SuppressWarnings("empty-statement")
    private void loadEventbyStudySubject(String studySubject, String studyName) throws Exception{        
        try {
            StudySubjectDAO studySubjectDAO = new StudySubjectDAO();
            
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();
            
            String eventOIDURL;
            eventOIDURL = lbWSDL.getText() + studySubjectWSDL;
            String us = lbUsername.getText();
            String pwd = lbPassword.getText();
            
            //load event (oid) by study subject
            SOAPMessage soapMessage = soapConnection.call(
                    studySubjectDAO.createSOAPRequest(us, pwd, studyName), eventOIDURL);
            
            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("ns2:studySubject");
            
            if(nList != null){   
                for(int i = 0; i < nList.getLength(); i++){                    
                    Node nNode = (Node) nList.item(i);                      
                    if(nNode.getNodeType()==Node.ELEMENT_NODE){  
                        Element eElement = (Element) nNode;                        
                        String nLabel  = eElement.getElementsByTagName("ns2:label").item(0).getTextContent();                        
                        if(nLabel.equals(studySubject)){
                            NodeList nList1 = eElement.getElementsByTagName("ns2:event");                            
                            if(nList1.getLength() < 1){
                                cbEvent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No data to display" }));
                            } 
                            else {
                                for(int j = 0; j < nList1.getLength(); j++){
                                    Node nNode1 = (Node) nList1.item(j);
                                    if(nNode1.getNodeType() == Node.ELEMENT_NODE){
                                        Element element1 = (Element) nNode1;
                                        arr_eventOID.add(element1.getElementsByTagName("ns2:eventDefinitionOID").item(0).getTextContent());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }      
            }
            
            //get event name from study
            String eventNameURL;
            eventNameURL = lbWSDL.getText() + eventWSDL;
            EventDAO eventDAO = new EventDAO();
            
            SOAPMessage soapMessage1 = soapConnection.call(eventDAO.createSOAPRequest(us, pwd, studyName), eventNameURL);
            
            NodeList nList_event = soapMessage1.getSOAPBody().getElementsByTagName("studyEventDefinition");
            if(nList_event != null){
                for(int i = 0; i < nList_event.getLength(); i++){
                    Node nNode_event = (Node) nList_event.item(i);
                    if(nNode_event.getNodeType()==Node.ELEMENT_NODE){
                        Element eElement_event = (Element) nNode_event;
                        String oid = eElement_event.getElementsByTagName("oid").item(0).getTextContent();                            
                        for(int k = 0;k < arr_eventOID.size();k++){
                            if(arr_eventOID.get(k).equals(oid)){
                                cbEvent.addItem(eElement_event.getElementsByTagName("name").item(0).getTextContent());
                            }
                        }  
                    }
                }
            } 
            soapConnection.close();            
        } 
        catch (UnsupportedOperationException | SOAPException e) {         
            //show something
        }
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lbWSDL = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResult = new javax.swing.JTable();
        cbProtocol = new javax.swing.JComboBox<>();
        cbStatus = new javax.swing.JComboBox<>();
        lbUsername = new javax.swing.JLabel();
        lbPassword = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbPatient = new javax.swing.JComboBox<>();
        cbEvent = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quality Toold - Advanced Research Center INC");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setText("Advanced Research Center, INC");

        jLabel1.setText("Protocol");

        jLabel3.setText("Status");

        jLabel4.setText("Patient");

        jLabel5.setText("Visit");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lbWSDL.setText("WSDL");

        tbResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbResult);

        cbProtocol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbProtocolItemStateChanged(evt);
            }
        });

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "Not Started", "Scheduled", "Data entry started", "Completed", "Stopped", "Skipped", "Signed", "Locked", "Invalid" }));

        lbUsername.setText("Username");

        lbPassword.setText("Password");

        jLabel6.setText("Account:");

        cbPatient.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbPatientItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbPatient, 0, 209, Short.MAX_VALUE)
                            .addComponent(cbProtocol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(78, 78, 78)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbStatus, 0, 230, Short.MAX_VALUE)
                            .addComponent(cbEvent, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbWSDL, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(cbProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(cbPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(cbEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(55, 55, 55)
                        .addComponent(lbWSDL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lbUsername)
                            .addComponent(lbPassword))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        frmLogin frmLogin = new frmLogin();
        frmLogin.pack();
        frmLogin.setLocationRelativeTo(null);
        ImageIcon img = new ImageIcon(frmLogin.class.getResource("/image/logo.jpg"));
        frmLogin.setIconImage(img.getImage());
        frmLogin.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

          
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        
        String indexProtocol = Integer.toString(cbProtocol.getSelectedIndex());
        String indexPatient = Integer.toString(cbPatient.getSelectedIndex());
        String indexEvent = Integer.toString(cbEvent.getSelectedIndex());
        String indexStatus = Integer.toString(cbStatus.getSelectedIndex());
        
        String nameStudy = cbProtocol.getName();
        
        JOptionPane.showMessageDialog(this,nameStudy , "Message", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cbProtocolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbProtocolItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if(this.cbProtocol.getSelectedIndex() < 0){
                //not thing, check when click Search
            } 
            else {
                try {
                    cbPatient.removeAllItems(); //remove all item before reselect 
                    String studyName = cbProtocol.getSelectedItem().toString();
                    loadStudySubjectbyStudy(studyName);
                } catch (Exception ex) {
                    Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
    }//GEN-LAST:event_cbProtocolItemStateChanged

    private void cbPatientItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPatientItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if(this.cbPatient.getSelectedIndex() < 0){
                //not thing, check when click Search
            } 
            else {
                try {
                    cbEvent.removeAllItems(); //remove all item before reselect 
                    String studyName = cbProtocol.getSelectedItem().toString();
                    String studySubject = cbPatient.getSelectedItem().toString();
                    loadEventbyStudySubject(studySubject, studyName);
                } catch (Exception ex) {
                    Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
                }
            }          
       }
    }//GEN-LAST:event_cbPatientItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */              
        java.awt.EventQueue.invokeLater(() -> {
            frmStatusCheck frmStatus = null;
            try {
                frmStatus = new frmStatusCheck();
            } catch (Exception ex) {
                Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
            }
            frmStatus.pack();
            frmStatus.setLocationRelativeTo(null);
            ImageIcon img = new ImageIcon(frmStatusCheck.class.getResource("/image/logo.jpg"));
            frmStatus.setIconImage(img.getImage());
            frmStatus.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cbEvent;
    private javax.swing.JComboBox<String> cbPatient;
    private javax.swing.JComboBox<String> cbProtocol;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JLabel lbWSDL;
    private javax.swing.JTable tbResult;
    // End of variables declaration//GEN-END:variables
}
