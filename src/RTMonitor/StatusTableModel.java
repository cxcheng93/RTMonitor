package RTMonitor;

import java.util.*;
import java.text.*;
import javax.swing.table.*;

public class StatusTableModel extends AbstractTableModel {
	private int rowCount;
    private String[] columnNames = {"Page", "No of Request", "Request Date", 
                                    "Status"};
    private Object [][] data;
    
    public StatusTableModel (ArrayList listOfPages) {
        rowCount = listOfPages.size();
        data = new Object[rowCount][getColumnCount()];
        for (int i = 0; i < rowCount; i++) {
            Page pg = (Page)listOfPages.get(i); 
            data[i][0] = pg.getName();
            data[i][1] = pg.getRequestCount();
            data[i][2] = pg.getRequestingDateAndTime();
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
    
    public Class getColumnClass(int col) {
	return getValueAt(0,col).getClass();
    }

}
