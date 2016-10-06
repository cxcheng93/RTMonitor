package RTMonitor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class FilterDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField NoOfRequestTextField;
	private JTextField AvgRespTimeTextField;
	private JTextField AvgResponsivenessTextField;
	private JTextField StatusTextField;
	public String symbolStrings;
	public String filtStatement="";
	private JComboBox AvgResponseTimeComboBox;
	private JComboBox NOofReqComboBox;
	private JComboBox AvgResponsivenessComboBox;
	private JComboBox StatusComboBox;

	public FilterDialog() {
		setModal(true);
		setBounds(100, 100, 450, 300);
		setTitle("Filter Dialog");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblNewLabel = new JLabel("Number of Request");
		JLabel lblNewLabel_1 = new JLabel("Avg Response Time");
		JLabel lblNewLabel_2 = new JLabel("Average Responsiveness");
		JLabel lblNewLabel_3 = new JLabel("Status");
		NOofReqComboBox = new JComboBox();
		AvgResponseTimeComboBox = new JComboBox();
		AvgResponsivenessComboBox = new JComboBox();
		StatusComboBox = new JComboBox();
		
		//combo box
		String [] symbolStrings = {"ALL","=","<",">","<=",">="};
		for(int i=0;i<symbolStrings.length;i++){
			NOofReqComboBox.addItem(symbolStrings[i]);
			AvgResponseTimeComboBox.addItem(symbolStrings[i]);
			AvgResponsivenessComboBox.addItem(symbolStrings[i]);
		}
				
		
		NoOfRequestTextField = new JTextField();
		NoOfRequestTextField.setColumns(10);
		
		AvgRespTimeTextField = new JTextField();
		AvgRespTimeTextField.setColumns(10);
		
		AvgResponsivenessTextField = new JTextField();
		AvgResponsivenessTextField.setColumns(10);
		
		StatusTextField = new JTextField();
		StatusTextField.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
						.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(StatusComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(AvgResponsivenessComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(AvgResponseTimeComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(NOofReqComboBox, 0, 50, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(NoOfRequestTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(AvgRespTimeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(AvgResponsivenessTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(StatusTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(127))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(NOofReqComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel)
						.addComponent(NoOfRequestTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(AvgResponseTimeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(AvgRespTimeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(AvgResponsivenessComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2)
						.addComponent(AvgResponsivenessTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(StatusComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3)
						.addComponent(StatusTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(64, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						filtStatement="";
						if (!NOofReqComboBox.getSelectedItem().equals("ALL")) {
							if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
							filtStatement=filtStatement+" RESPONSESIZE"+NOofReqComboBox.getSelectedItem().toString()+NoOfRequestTextField.getText();
						}
						if (!AvgResponseTimeComboBox.getSelectedItem().equals("ALL")) {
							if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
							filtStatement=filtStatement+" RESPONSETIME"+AvgResponseTimeComboBox.getSelectedItem().toString()+AvgRespTimeTextField.getText();
						}
						if (!AvgResponsivenessComboBox.getSelectedItem().equals("ALL")) {
							if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
							filtStatement=filtStatement+" RESPONSIVENESS"+AvgResponsivenessComboBox.getSelectedItem().toString()+AvgResponsivenessTextField.getText();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						dispose();
					}
				});
			}
		}
		
		
	}

}
