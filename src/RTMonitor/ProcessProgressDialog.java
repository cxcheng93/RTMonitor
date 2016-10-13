package RTMonitor;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

public class ProcessProgressDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -444633001920892243L;
	public JLabel lblText;
	public JProgressBar progressBar;

	public ProcessProgressDialog() {
		setModal(true);
		setUndecorated(true);
		setBounds(100, 100, 450, 100);
		
		lblText = new JLabel("Loading");
		lblText.setHorizontalAlignment(SwingConstants.CENTER);
		
		progressBar = new JProgressBar();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblText, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(17, Short.MAX_VALUE)
					.addComponent(lblText, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(groupLayout);

	}
}
