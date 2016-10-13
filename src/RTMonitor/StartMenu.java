package RTMonitor;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class StartMenu extends JFrame {
	private static final long serialVersionUID = -5873130412866899973L;
	public JComboBox<Integer> comboBox;
	public JButton btnImport;

	public StartMenu() {
		setTitle("Response Time Monitor");
		setResizable(false);
		setBounds(100, 100, 299, 114);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		btnImport = new JButton("Import & Process");
		btnImport.setBounds(10, 11, 273, 23);
		getContentPane().add(btnImport);
		
		comboBox = new JComboBox<>();
		comboBox.setBounds(66, 47, 217, 20);
		int max=Runtime.getRuntime().availableProcessors()*10;
		for (int i=1;i<=max;i++) {
			comboBox.addItem(i);
		}
		comboBox.setSelectedItem(Runtime.getRuntime().availableProcessors());
		getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Thread :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(10, 50, 46, 14);
		getContentPane().add(lblNewLabel);

	}
}
