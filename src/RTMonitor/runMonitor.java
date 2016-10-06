package RTMonitor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.hsqldb.Database;



public class runMonitor {
	
	public static String filtStatement="";
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JDialog.setDefaultLookAndFeelDecorated(true);
	    final JFrame frame = new JFrame("Response Time Monitor");
	    frame.getContentPane().setLayout(new FlowLayout());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setMinimumSize(new Dimension(400,75));
	    frame.setLocationRelativeTo(null);
	
	    JButton button = new JButton("Select Server Access Log");
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Cheng\\Documents\\sem7\\FYP2\\");
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	frame.dispose();
	          File selectedFile = fileChooser.getSelectedFile();
	          LogIO aLogIO = new LogIO();
	          try {
	        	  	JProgressBar progressBar = new JProgressBar();
	        	  	progressBar.setValue(0);
	        	  	progressBar.setStringPainted(true);
	        	    Border border = BorderFactory.createTitledBorder("Reading...");
	        	    progressBar.setBorder(border);
	        	    
		        	long l1=System.currentTimeMillis();
		            aLogIO.parseLog(selectedFile.getPath());
		            long l2=System.currentTimeMillis();
		            System.out.println("ZZZ1 : "+(l2-l1)/1000);
		            aLogIO.logToDB();
		            long l3=System.currentTimeMillis();
		            System.out.println("ZZZ2 : "+(l3-l2)/1000);
		            VisualiseLog vl = new VisualiseLog();
		            vl.retrievePageInfo(runMonitor.filtStatement);
		            vl.createTable();
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



