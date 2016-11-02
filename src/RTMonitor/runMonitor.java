package RTMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;


public class runMonitor {
	
	public static String filtStatement="";
	public static int threadCount=0;
    public static long timeTaken=0;
	
	public static void main(String[] args) {
		try {
			System.out.println(Integer.MIN_VALUE);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	
		final StartMenu frame=new StartMenu();
		frame.setLocationRelativeTo(null);
	    frame.btnImport.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Cheng\\Documents\\sem7\\FYP2\\");
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	runMonitor.threadCount=(Integer)frame.comboBox.getSelectedItem();
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
		            timeTaken=l2-l1;
		            aLogIO.logToDB();
		            long l3=System.currentTimeMillis();
		            timeTaken+=(l3-l2);
		            VisualiseLog vl = new VisualiseLog();
		            vl.retrievePageInfo(runMonitor.filtStatement);
		            vl.createTable();
	          }
	          catch (IOException ioe){}          
	        }
	      }
	    });
	    frame.setVisible(true);
	  }
	
}



