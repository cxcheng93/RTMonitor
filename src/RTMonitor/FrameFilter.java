package RTMonitor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JLabel;

public class FrameFilter extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4879896163099185856L;
	private ArrayList <Page> LOP;
    private Date from, to;
	private JPanel contentPane;
	private JTable table;
	
	public FrameFilter(){
		setSize(new Dimension(700, 400));
		setMinimumSize(new Dimension(400, 200));
		buildUI(null,null,null);
	}
	

	public FrameFilter (ArrayList<Page> l, Date f, Date t) {
		buildUI(l,f,t);
	}
	private void buildUI (final ArrayList <Page> listOfPages, Date fromDate, Date toDate) {
		LOP=listOfPages; from=fromDate; to=toDate;
		setTitle("Page Request Summary");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton StatusButton = new JButton("Page Not Found");
		StatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ArrayList<Page> filt=new ArrayList<>();
				for (Object o : listOfPages) {
					Page p=(Page) o;
					if (p.getStatusCode()==404) filt.add(p);
				}
				StatusPage sp = new StatusPage(filt);
				sp.setVisible(true);
			}
		});
		
		JButton btnNewButton = new JButton("Filter...");
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				ArrayList<Date> dateList=new ArrayList<>();
				for (Object o : listOfPages) {
					dateList.add(((Page)o).getRequestingDateAndTime());
				}
				Collections.sort(dateList);
				
				FilterDialog fd;
				if (dateList.size()>0) fd=new FilterDialog(dateList.get(0),dateList.get(dateList.size()-1));
				else fd=new FilterDialog(null,null);
				fd.setVisible(true);
				if (fd.okButtonPressed) {
				//if (!fd.filtStatement.equals("")) {
					runMonitor.filtStatement=fd.filtStatement;
					dispose();
		            VisualiseLog vl = new VisualiseLog();
		            vl.retrievePageInfo(runMonitor.filtStatement);
		            vl.createTable();
				//}
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
		});
		
		JLabel lblTimeTaken = new JLabel("");
		lblTimeTaken.setText("Time taken : "+runMonitor.timeTaken+"ms");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(StatusButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTimeTaken)
					.addPreferredGap(ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton_1)
						.addComponent(btnNewButton)
						.addComponent(StatusButton)
						.addComponent(lblTimeTaken)))
		);
		
		table = new JTable();
		table.setModel(new MyTableModel(listOfPages));
		table.getColumnModel().getColumn(0).setPreferredWidth(350);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.addMouseListener(new MouseAdapter () {
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount()>1) {
        			int row=table.convertRowIndexToModel(table.getSelectedRow());
                    if (table.getValueAt(row, 0) != null) {
                        Iterator<Page> it = LOP.iterator();
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
		scrollPane.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
	}
}
