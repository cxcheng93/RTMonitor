package RTMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTable;

public class StatusPage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6991796670455055697L;
	private JTable table;

	public StatusPage(ArrayList<Page> listOfPages) {
		setTitle("Page Not Found");
		setBounds(100, 100, 700, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setHorizontalAlignment(SwingConstants.RIGHT);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				dispose();
			}
		});

		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
					.addGap(0))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(359, Short.MAX_VALUE)
					.addComponent(cancelButton)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cancelButton)
					.addContainerGap())
		);
		
		table = new JTable();
		table.setModel(new StatusTableModel(listOfPages));
		table.getColumnModel().getColumn(0).setPreferredWidth(350);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
	}
}
