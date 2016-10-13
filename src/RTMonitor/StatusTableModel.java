package RTMonitor;

import java.util.*;
import javax.swing.table.*;

public class StatusTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8568150768520790698L;
	private int rowCount;
    private String[] columnNames = {"Page", "Last Request Date", "No of Request", 
                                    "Status"};
    private Object [][] data;
    
    public StatusTableModel (ArrayList<Page> listOfPages) {
        rowCount = listOfPages.size();
        data = new Object[rowCount][getColumnCount()];
        for (int i = 0; i < rowCount; i++) {
            Page pg = (Page)listOfPages.get(i); 
            data[i][0] = pg.getName();
            data[i][1] = pg.getRequestingDateAndTime();
            data[i][2] = pg.getRequestCount();
            data[i][3] = pg.getStatusCode();
       
          }
        } 
    

    public int getRowCount() { 
        return rowCount;
    }
   
    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int row, int col) {
    	return data[row][col];
    }
	
    public String getColumnName(int col) {
	return columnNames[col];
    }
    
    public boolean isCellEditable(int row, int col) { 
	return false; 
    }
    
    public void setValueAt(Object value, int row, int col) {    
        System.out.println(value.toString());
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public StatusTableModel getModel() {
	return this;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) {
	return getValueAt(0,col).getClass();
    }

}
