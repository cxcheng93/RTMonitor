package pageMaintenance;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class startMaintenance {
	public static void main(String[] args) {
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JDialog.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame("Database Queries Analyser");
	    frame.getContentPane().setLayout(new FlowLayout());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JButton button = new JButton("Select Web Page");
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          File selectedFile = fileChooser.getSelectedFile();
	          dbQueryAnalyser dqa = new dbQueryAnalyser();
	          
	          try {
	            dqa.getQuery(selectedFile.getPath());
	          }
	          catch (IOException ioe){}          
	        }
	      }
	    });
	    frame.getContentPane().add(button);
	    frame.pack();
	    frame.setVisible(true);
	  }

}
