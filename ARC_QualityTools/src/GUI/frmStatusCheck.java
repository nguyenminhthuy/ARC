package GUI;

// <editor-fold desc="Library">
import BEANS.*;
import DAO.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.xml.soap.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
// </editor-fold>

public class frmStatusCheck extends javax.swing.JFrame {

    // <editor-fold desc="Declare varibale.">
    private final String studyWSDL = "/OpenClinica-ws/ws/study/v1/studyWsdl.wsdl";
    private final String studySubjectWSDL = "/OpenClinica-ws/ws/studySubject/v1/studySubjectWsdl.wsdl";
    private final String eventWSDL = "/OpenClinica-ws/ws/studyEventDefinition/v1/studyEventDefinitionWsdl.wsdl";
    public static List<String> arr_eventOID = new ArrayList<>();

    public static String username;
    public static String password;
    public static String url;
    // </editor-fold>

    // <editor-fold desc="frmStatusCheck()">    
    public frmStatusCheck() throws Exception {
        initComponents();
    }
    // </editor-fold>   

    // <editor-fold desc="frmStatusCheck(User)">    
    public frmStatusCheck(User us) throws Exception {
        initComponents();

        cbProtocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Select one"}));
        cbPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Select one"}));
        cbEvent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Select one"}));

        lbUsername.setText(us.getUsername());
        lbMessage.setVisible(false);

        username = us.getUsername();
        password = us.getPassword();
        url = us.getBaseURL();

        loadAllStudies();
    }
    // </editor-fold>   

    // <editor-fold desc="Load All Study">
    private void loadAllStudies() throws Exception {
        try {
            StudyDAO studyDAO = new StudyDAO();
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();

            String studyURL = url + studyWSDL;

            SOAPMessage soapMessage = soapConnection.call(studyDAO.loadAllStudies(username, password), studyURL);

            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("study");
            if (nList != null) {
                int nList_Lenght = nList.getLength();
                if (nList_Lenght != 0) {
                    for (int temp = 0; temp < nList_Lenght; temp++) {
                        Node nNode = (Node) nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            cbProtocol.addItem(eElement.getElementsByTagName("identifier").item(0).getTextContent());
                            cbProtocol.setName(eElement.getElementsByTagName("oid").item(0).getTextContent());
                        }
                    }
                } else {
                    cbProtocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No data to display"}));
                }
            }
            soapConnection.close();
        } catch (UnsupportedOperationException | SOAPException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Load Study Subject by Study">
    private void loadStudySubjectbyStudy(String studyName) throws Exception {
        try {
            StudySubjectDAO studySubjectDAO = new StudySubjectDAO();

            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();

            String studySubjectURL = url + studySubjectWSDL;

            SOAPMessage soapMessage = soapConnection.call(
                    studySubjectDAO.loadStudySubjectbyStudy(username, password, studyName), studySubjectURL);

            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("ns2:studySubject");

            if (nList != null) {
                int nList_Lenght = nList.getLength();

                if (nList_Lenght != 0) {
                    for (int temp = 0; temp < nList_Lenght; temp++) {
                        Node nNode = (Node) nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            String label = eElement.getElementsByTagName("ns2:label").item(0).getTextContent();
                            cbPatient.addItem(label);
                        }
                    }
                } else {
                    cbPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No data to display"}));
                    cbEvent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No data to display"}));
                }
            }
            soapConnection.close();
        } catch (UnsupportedOperationException | SOAPException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Get Event OID by Study Subject">
    private void getEventOIDbyStudySubject(String studyName, String studySubject) throws SOAPException, Exception {
        try {
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();

            String studySubjectURL = url + studySubjectWSDL;
            EventDAO eventDAO = new EventDAO();

            SOAPMessage soapMessage = soapConnection.call(eventDAO.getEventOID(username, password, studyName), studySubjectURL);

            arr_eventOID.clear();

            NodeList nList = soapMessage.getSOAPBody().getElementsByTagName("ns2:studySubject");
            if (nList != null) {

                int nList_Lenght = nList.getLength();
                for (int i = 0; i < nList_Lenght; i++) {
                    Node nNode = (Node) nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String nLabel = eElement.getElementsByTagName("ns2:label").item(0).getTextContent();

                        if (nLabel.equals(studySubject)) {
                            NodeList elementList = eElement.getElementsByTagName("ns2:event");
                            int elementList_Lenght = elementList.getLength();
                            for (int j = 0; j < elementList_Lenght; j++) {
                                Node nNode1 = (Node) elementList.item(j);
                                if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element1 = (Element) nNode1;
                                    String oid = element1.getElementsByTagName("ns2:eventDefinitionOID").item(0).getTextContent();
                                    arr_eventOID.add(oid);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            soapConnection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Get Event Name by OID">
    private myAbstractTableModel matm;

    private void getEventNamebyOID(String studyName) throws SOAPException, Exception {
        try {
            int arr_size = arr_eventOID.size();
            if (arr_size != 0) {
                SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection soapConnection = soapConenctionFactory.createConnection();

                //-----------------------------------------
                String[] header;
                header = new String[]{"Event OID", "Event Name"};
                ArrayList<Object[]> data = new ArrayList<>();
                ((DefaultTableCellRenderer) tbResult.getTableHeader().getDefaultRenderer())
                        .setHorizontalAlignment(JLabel.CENTER);
                //-----------------------------------------               

                EventDAO eventDAO = new EventDAO();
                String eventURL = url + eventWSDL;

                SOAPMessage soapMessage1 = soapConnection.call(eventDAO.getStudyEvent(username, password, studyName), eventURL);

                NodeList nList1 = soapMessage1.getSOAPBody().getElementsByTagName("studyEventDefinition");
                if (nList1 != null) {
                    int lenght = nList1.getLength();
                    for (int i = 0; i < lenght; i++) {
                        Node nNode1 = (Node) nList1.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement1 = (Element) nNode1;

                            String oid = eElement1.getElementsByTagName("oid").item(0).getTextContent();
                            String name = eElement1.getElementsByTagName("name").item(0).getTextContent();

                            for (int x = 0; x < arr_size; x++) {
                                if (arr_eventOID.get(x).equals(oid)) {
                                    cbEvent.addItem(name);

                                    //-----------------------------------------
                                    data.add(new Object[]{oid, name});
                                    matm = new myAbstractTableModel(header, data);
                                    tbResult.setModel(matm);
                                    //-----------------------------------------  
                                }
                            }
                        }
                    }
                }
                soapConnection.close();
            } else {
                cbEvent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No data to display"}));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Get Event OID by Name">
    public String str;

    private String getEventOIDbyName(String studyName, String eventName) throws SOAPException, Exception {
        try {
            SOAPConnectionFactory soapConenctionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConenctionFactory.createConnection();

            EventDAO eventDAO = new EventDAO();
            String eventURL = url + eventWSDL;

            SOAPMessage soapMessage = soapConnection.call(eventDAO.getStudyEvent(username, password, studyName), eventURL);

            NodeList nList1 = soapMessage.getSOAPBody().getElementsByTagName("studyEventDefinition");
            if (nList1 != null) {
                int lenght = nList1.getLength();
                for (int i = 0; i < lenght; i++) {
                    Node nNode1 = (Node) nList1.item(i);
                    if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement1 = (Element) nNode1;
                        String oid = eElement1.getElementsByTagName("oid").item(0).getTextContent();
                        String name = eElement1.getElementsByTagName("name").item(0).getTextContent();
                        if (eventName.equals(name)) {
                            str = oid;
                        }
                    }
                }
            }
            soapConnection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        return str;
    }
    // </editor-fold>

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
        cbProtocol = new javax.swing.JComboBox<>();
        cbStatus = new javax.swing.JComboBox<>();
        lbUsername = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbPatient = new javax.swing.JComboBox<>();
        cbEvent = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResult = new javax.swing.JTable();
        lbMessage = new javax.swing.JLabel();

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

        cbProtocol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbProtocolItemStateChanged(evt);
            }
        });

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one", "Not Started", "Scheduled", "Data entry started", "Completed", "Stopped", "Skipped", "Signed", "Locked", "Invalid" }));

        lbUsername.setText("Username");

        jLabel6.setText("Account:");

        cbPatient.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbPatientItemStateChanged(evt);
            }
        });

        jScrollPane1.setViewportView(tbResult);

        lbMessage.setText("Message");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbProtocol, javax.swing.GroupLayout.Alignment.LEADING, 0, 209, Short.MAX_VALUE)
                            .addComponent(cbPatient, javax.swing.GroupLayout.Alignment.LEADING, 0, 209, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(lbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbMessage)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
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
                                    .addComponent(cbEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10)
                .addComponent(lbMessage)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbUsername)
                    .addComponent(jLabel6))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold desc="Cancel Action">
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.setVisible(false);
        frmLogin frmLogin = new frmLogin();
        frmLogin.pack();
        frmLogin.setLocationRelativeTo(null);
        ImageIcon img = new ImageIcon(frmLogin.class.getResource("/image/logo.jpg"));
        frmLogin.setIconImage(img.getImage());
        frmLogin.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    // </editor-fold>

    // <editor-fold desc="Search Action">
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String eventName = cbEvent.getSelectedItem().toString();
        String studyName = cbProtocol.getSelectedItem().toString();
        try {
            String s = getEventOIDbyName(studyName, eventName);
            JOptionPane.showMessageDialog(this, s, "Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed
    // </editor-fold>

    // <editor-fold desc="Combobox Protocol">
    private void cbProtocolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbProtocolItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                cbPatient.removeAllItems(); //remove all item before reselect 
                cbEvent.removeAllItems();
                String studyName = cbProtocol.getSelectedItem().toString();
                loadStudySubjectbyStudy(studyName);
            } catch (Exception ex) {
                Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cbProtocolItemStateChanged
    // </editor-fold>

    // <editor-fold desc="Combobox Patient">
    private void cbPatientItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPatientItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            cbEvent.removeAllItems();
            String studyName = cbProtocol.getSelectedItem().toString();
            String studySubject = cbPatient.getSelectedItem().toString();
            try {
                getEventOIDbyStudySubject(studyName, studySubject);
                getEventNamebyOID(studyName);
            } catch (Exception ex) {
                Logger.getLogger(frmStatusCheck.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cbPatientItemStateChanged
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmStatusCheck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

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
    // </editor-fold>

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
    private javax.swing.JLabel lbMessage;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JTable tbResult;
    // End of variables declaration//GEN-END:variables
}
