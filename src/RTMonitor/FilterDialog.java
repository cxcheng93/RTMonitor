package RTMonitor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.InputMethodEvent;
//import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
//import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
//import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;

//import javax.swing.GroupLayout;
//import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
//import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
//import javax.swing.JToolTip;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class FilterDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1832411082482038481L;
	private final JPanel contentPanel = new JPanel();
	private JTextField AvgRespTimeTextField;
	private JTextField AvgResponsivenessTextField;
	public String symbolStrings;
	public String filtStatement="";
	private JComboBox <String> AvgResponseTimeComboBox;
	private JComboBox <String> comboBoxEndDate;
	private JComboBox <String> AvgResponsivenessComboBox;
	private JComboBox <String>comboBoxStartDate;
	private JXDatePicker btnStartDate;
	private JXDatePicker btnEndDate;
	private JCheckBox chckbxDateFilter;
	private JLabel lblStartDate;
	private JLabel lblEndDate;
	private JLabel AvgRespTimeTextFieldInfo;
	private JLabel AvgResponsivenessTextFieldInfo;
	public boolean okButtonPressed=false;
	
	public FilterDialog(Date startDate, Date endDate) {
		setModal(true);
		setBounds(100, 100, 450, 300);
		setTitle("Filter Dialog");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		lblStartDate = new JLabel("Start Date :");
		lblStartDate.setEnabled(false);
		lblStartDate.setBounds(23, 45, 72, 14);
		JLabel lblNewLabel_1 = new JLabel("Avg Response Time");
		lblNewLabel_1.setBounds(23, 107, 121, 14);
		JLabel lblNewLabel_2 = new JLabel("Average Responsiveness");
		lblNewLabel_2.setBounds(23, 145, 121, 14);
		String [] s={"AM","PM"};
		String [] time={"12","1","2","3","4","5","6","7","8","9","10","11"};
		final int [] hourValue={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
		comboBoxEndDate = new JComboBox <String>() ;
		comboBoxEndDate.setEnabled(false);
		comboBoxEndDate.setBounds(242, 73, 72, 20);
		for (int i=0;i<s.length;i++) {
			for (int i2=0;i2<time.length;i2++) {
				comboBoxEndDate.addItem(time[i2]+" "+s[i]);
			}
		}
		AvgResponseTimeComboBox = new JComboBox<>();
		AvgResponseTimeComboBox.setBounds(148, 104, 70, 20);
		AvgResponsivenessComboBox = new JComboBox<>();
		AvgResponsivenessComboBox.setBounds(148, 142, 72, 20);
		//combo box
		String [] symbolStrings = {"ALL","=","<",">","<=",">="};
		for(int i=0;i<symbolStrings.length;i++){
			AvgResponseTimeComboBox.addItem(symbolStrings[i]);
			AvgResponsivenessComboBox.addItem(symbolStrings[i]);
		}
		
		AvgRespTimeTextField = new JTextField();
		AvgRespTimeTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (!AvgResponseTimeComboBox.getSelectedItem().equals("ALL")) {
					if (validateAvgRespTimeTextField())
						AvgRespTimeTextFieldInfo.setText("");
					else
						AvgRespTimeTextFieldInfo.setText("Must be a number!");
				}
			}
			
		});
		AvgRespTimeTextField.setBounds(228, 104, 86, 20);
		AvgRespTimeTextField.setColumns(10);
		
		AvgResponsivenessTextField = new JTextField();
		AvgResponsivenessTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!AvgResponsivenessComboBox.getSelectedItem().equals("ALL")) {
					if (validateAvgResponsivenessTextField())
						AvgResponsivenessTextFieldInfo.setText("");
					else
						AvgResponsivenessTextFieldInfo.setText("Must be a number!");
				}
			}
			
		});
		AvgResponsivenessTextField.setBounds(228, 142, 86, 20);
		AvgResponsivenessTextField.setColumns(10);
		
		comboBoxStartDate = new JComboBox<>();
		comboBoxStartDate.setEnabled(false);
		for (int i=0;i<s.length;i++) {
			for (int i2=0;i2<time.length;i2++) {
				comboBoxStartDate.addItem(time[i2]+" "+s[i]);
			}
		}
		comboBoxStartDate.setBounds(242, 42, 72, 20);
		contentPanel.setLayout(null);
		
		chckbxDateFilter = new JCheckBox("Between Date");
		chckbxDateFilter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				comboBoxStartDate.setEnabled(chckbxDateFilter.isSelected());
				comboBoxEndDate.setEnabled(chckbxDateFilter.isSelected());
				btnStartDate.setEnabled(chckbxDateFilter.isSelected());
				btnEndDate.setEnabled(chckbxDateFilter.isSelected());
				lblStartDate.setEnabled(chckbxDateFilter.isSelected());
				lblEndDate.setEnabled(chckbxDateFilter.isSelected());;
			}
		});
		chckbxDateFilter.setBounds(23, 12, 93, 23);
		contentPanel.add(chckbxDateFilter);
		contentPanel.add(lblNewLabel_2);
		contentPanel.add(lblNewLabel_1);
		contentPanel.add(lblStartDate);
		contentPanel.add(comboBoxStartDate);
		contentPanel.add(AvgResponsivenessComboBox);
		contentPanel.add(AvgResponseTimeComboBox);
		contentPanel.add(AvgRespTimeTextField);
		contentPanel.add(AvgResponsivenessTextField);
		contentPanel.add(comboBoxEndDate);
		
		lblEndDate = new JLabel("End Date :");
		lblEndDate.setEnabled(false);
		lblEndDate.setBounds(23, 76, 72, 14);
		contentPanel.add(lblEndDate);
		
		btnStartDate = new JXDatePicker(startDate);
		btnStartDate.setEnabled(false);
		btnStartDate.setBounds(87, 42, 145, 23);
		contentPanel.add(btnStartDate);
		
		btnEndDate = new JXDatePicker(endDate);
		btnEndDate.setEnabled(false);
		btnEndDate.setBounds(87, 72, 145, 23);
		contentPanel.add(btnEndDate);
		
		AvgRespTimeTextFieldInfo = new JLabel("");
		AvgRespTimeTextFieldInfo.setBounds(324, 107, 93, 14);
		contentPanel.add(AvgRespTimeTextFieldInfo);
		
		AvgResponsivenessTextFieldInfo = new JLabel("");
		AvgResponsivenessTextFieldInfo.setBounds(324, 145, 100, 14);
		contentPanel.add(AvgResponsivenessTextFieldInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						filtStatement="";
						if (chckbxDateFilter.isSelected()) {
							if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
							Date d=btnStartDate.getDate();
							LocalDateTime dt=LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
							dt=dt.withHour(hourValue[comboBoxStartDate.getSelectedIndex()]);
							System.out.println(dt);

							d=btnEndDate.getDate();
							LocalDateTime dt2=LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
							dt=dt.withHour(hourValue[comboBoxEndDate.getSelectedIndex()]);
							DateTimeFormatter form=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							System.out.println(dt2);
							
							filtStatement=filtStatement+" REQDATE>='"+form.format(dt)+"' AND REQDATE<='"+form.format(dt2)+"'";
						}
						if (!AvgResponseTimeComboBox.getSelectedItem().equals("ALL")) {
							 if (validateAvgRespTimeTextField()) {
								 if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
								 filtStatement=filtStatement+" RESPONSETIME"+AvgResponseTimeComboBox.getSelectedItem().toString()+AvgRespTimeTextField.getText();
							 } else {
								 JOptionPane.showMessageDialog(null,"Invalid input", "Error",JOptionPane.ERROR_MESSAGE);
							 }
						}
						if (!AvgResponsivenessComboBox.getSelectedItem().equals("ALL")) {
							
							if (validateAvgResponsivenessTextField()) {
								if (filtStatement.equals("")) filtStatement+=" WHERE "; else filtStatement+=" AND ";
								filtStatement=filtStatement+" RESPONSIVENESS"+AvgResponsivenessComboBox.getSelectedItem().toString()+AvgResponsivenessTextField.getText();
							} else {
								 JOptionPane.showMessageDialog(null,"Invalid input", "Error",JOptionPane.ERROR_MESSAGE);
							}
						}
						okButtonPressed=true;
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
	
	public boolean validateAvgRespTimeTextField() {
		if (AvgRespTimeTextField.getText()==null || AvgRespTimeTextField.getText().isEmpty()) return false;
		
		try {
			Double.parseDouble(AvgRespTimeTextField.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public boolean validateAvgResponsivenessTextField() {
		if (AvgResponsivenessTextField.getText()==null || AvgResponsivenessTextField.getText().isEmpty()) return false;
		
		try {
			Double.parseDouble(AvgResponsivenessTextField.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
