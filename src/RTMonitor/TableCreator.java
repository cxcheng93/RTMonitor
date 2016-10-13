package RTMonitor;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TableCreator extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 827387034942049873L;
	private JTable table;  
    private ArrayList<Page> LOP;
    private Date from, to;
   
    public TableCreator(ArrayList<Page> listOfPages, Date fromDate, Date toDate) {
        super(new GridLayout(1,0));
        LOP = listOfPages;
        from = fromDate;
        to = toDate;
        TableSorter sorter = new TableSorter(new MyTableModel(LOP)); 
        table = new JTable(sorter);             
        sorter.setTableHeader(table.getTableHeader()); 
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(350);
        colModel.getColumn(1).setPreferredWidth(150);
        colModel.getColumn(2).setPreferredWidth(150);
        colModel.getColumn(3).setPreferredWidth(150);
        DefaultTableCellRenderer tcrCol = new DefaultTableCellRenderer();
        tcrCol.setHorizontalAlignment(SwingConstants.RIGHT);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter () {
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount()>1) {
        			int row=table.convertRowIndexToModel(table.getSelectedRow());
                    if (table.getValueAt(row, 0) != null) {
                        Iterator <Page> it = LOP.iterator();
                        while (it.hasNext()) {
                            Page pg = (Page) it.next();
                            if (pg.getName().equals(table.getValueAt(row, 0))) {
                                ChartCreator reportChart = new ChartCreator("");
                                reportChart.createChart(pg, from, to);                               
                            }
                        }
                     }
        		}
        	}
        });
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);       
        //Add the scroll pane to this panel.
        add(scrollPane);
       }         

}
