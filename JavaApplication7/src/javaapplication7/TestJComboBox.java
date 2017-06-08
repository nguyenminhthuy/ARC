
package javaapplication7;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static java.lang.System.*;

public class TestJComboBox extends javax.swing.JFrame {

    public TestJComboBox() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    private static final Color FOREGROUND_FOR_LABEL = new Color( 0x0000b0 );

    /**
     * Debugging harness for a Frame
     *
     * @param args command line arguments are ignored.
     */
    public static void main( String args[] )
        {
        SwingUtilities.invokeLater(() -> {
            final JFrame jFrame = new JFrame();
            final Container contentPane = jFrame.getContentPane();
            contentPane.setLayout( new FlowLayout() );
            final JComboBox<String> flavour = new JComboBox<>( new String[] {
                "strawberry", "chocolate", "vanilla" } );
            // ensure all three choices will be displayed without scrolling.
            flavour.setMaximumRowCount( 3 );
            flavour.setForeground( FOREGROUND_FOR_LABEL );
            flavour.setBackground( Color.WHITE );
            flavour.setFont( new Font( "Dialog", Font.BOLD, 15 ) );
            // turn off the write-in feature
            flavour.setEditable( false );
            // setting the selection
            flavour.setSelectedIndex( 0 );
            // alternatively, by value.
            // Compares against defined items with. .equals, not ==.
            // For custom objects will want a custom equals method.
            // Selected items work best with enums.
            flavour.setSelectedItem( "chocolate" );
            // No multiple selections permitted, ever, even though this is called a combo box.
            // so there is nothing we need to do to prevent them.
            final ItemListener theListener = (ItemEvent e) -> {
                final int selectedIndex = flavour.getSelectedIndex();
                // even though JComboBox is generic, you still need the (String) cast, a legacy quirk.
                final String choice = ( String ) flavour.getSelectedItem();
                out.println( selectedIndex + " " + choice + " " + e.toString() );
            } /**
             * Called whenever the value of the selection changes. Will
             * be called twice for every change.
             * @param e the event that characterizes the change.
             */ ;
            // add components
            flavour.addItemListener( theListener );
            contentPane.add( flavour );
            // build frame
            jFrame.setSize( 100, 100 );
            jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            jFrame.validate();
            jFrame.setVisible( true );
            // experiment with JComboBox.setSelectedIndex
            // This generates TWO itemStateChanged events.
            // One with item=old value stateChange=DESELECTED
            // and one with item=new value and stateChange=SELECTED
            flavour.setSelectedIndex( 2 );
            // suppress itemChangedEvents
            flavour.removeItemListener( theListener );
            // generates no itemChangedEvents
            flavour.setSelectedIndex( 1 );
            // turn itemChangedEvents back ot
            flavour.addItemListener( theListener );
            // generates two itemChangedEvents, usual per usual
            flavour.setSelectedIndex( 0 );
        });
        } // end main

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
