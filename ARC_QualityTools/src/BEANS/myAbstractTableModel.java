package BEANS;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class myAbstractTableModel extends AbstractTableModel {

    private final String[] header;
    private final ArrayList<Object[]> data;

    public myAbstractTableModel(String[] header, ArrayList<Object[]> data) {
        this.header = header;
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data.get(row)[column];
    }
   
    @Override
    public void setValueAt(Object value, int row, int column) {
        //data.get(row)[column] = value;
        fireTableCellUpdated(row, column);
    }
    
}
