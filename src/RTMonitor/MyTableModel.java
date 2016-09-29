package RTMonitor;

import java.util.*;
import java.text.*;
import javax.swing.table.*;

public class MyTableModel extends AbstractTableModel {
	private int rowCount;
    private String[] columnNames = {"Page", "No of Request", "Avg Response Time", 
                                    "Avg Responsiveness"};
    private Object [][] data;
    
    public MyTableModel (ArrayList listOfPages) {
        rowCount = listOfPages.size();
        data = new Object[rowCount][getColumnCount()];
        for (int i = 0; i < rowCount; i++) {
            Page pg = (Page)listOfPages.get(i); 
            data[i][0] = pg.getName();
            data[i][1] = new Integer(pg.getRequestCount());
            data[i][2] = new Double(pg.getAverageResponseTime());
            data[i][3] = new Double(pg.getAverageResponsiveness());
       
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

    public MyTableModel getModel() {
	return this;
    }
    
    public Class getColumnClass(int col) {
	return getValueAt(0,col).getClass();
    }

}
