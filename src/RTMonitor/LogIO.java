package RTMonitor;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.JOptionPane;

import java.sql.*;

public class LogIO {
	private static final int LOG_FIELD_COUNT = 10;
    private ArrayList listOfPages;
    private ArrayList result=new ArrayList<>();
    private ProcessProgressDialog progDiag;
    
    public LogIO() {
        listOfPages = new ArrayList();
    }
    
    private int getPageIndex(String n) {
        Iterator it = listOfPages.iterator();
        int index = -1;
        while (it.hasNext()) {
            Page pg = (Page) it.next();
            index++;
            if (pg.getName().equals(n)) {
                return index;
            }
            System.out.println(index);
        }
        index = -1;
        return index;
    }
    
    private static class ExtractorProcessThread extends Thread {
    	ExtractLogInfo ext;
    	ArrayList<String> l;
    	int count=0;
    	boolean flag=false;
    	
    	public void run () {
    		for (String s : l) {
    			count++;
    			ext.extractLines(s);
    		}
    		flag=true;
    	}
    }
    
    /* Parses the log file */
    public void parseLog(String logName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(logName));
        String line;
        
        final int threadCount=Runtime.getRuntime().availableProcessors()*2;
        ExtractLogInfo [] extractor = new ExtractLogInfo[threadCount];
        for (int i=0;i<extractor.length;i++) extractor[i]=new ExtractLogInfo();
        
        ArrayList<String> [] list=new ArrayList [threadCount];
        for (int i=0;i<list.length;i++) list[i]=new ArrayList<String>();
        
        line = in.readLine();
        StringTokenizer st = new StringTokenizer(line);
        if (st.countTokens() != LOG_FIELD_COUNT) {
        	JOptionPane.showMessageDialog(null,"Incompatible log file format: " ); 
            System.out.println("Incompatible log file format: " + st.countTokens());
            System.exit(0);
        }
        int count=0;
        while (line != null) {
        	list[count%threadCount].add(line);
            line = in.readLine();
            count++;
            
        }
        
        final ExtractorProcessThread [] th=new ExtractorProcessThread[threadCount];
        for (int i=0;i<threadCount;i++) {
        	th[i]=new ExtractorProcessThread();
        	th[i].ext=extractor[i];
        	th[i].l=list[i];
        	th[i].start();
        }
        
        progDiag = new ProcessProgressDialog();
        progDiag.progressBar.setStringPainted(true);
        progDiag.setLocationRelativeTo(null);
        progDiag.progressBar.setMaximum(count);
        final int progDiagCount=count;
        Thread t = new Thread() {
        	public void run() {
        		while (true) {
	        		int totalC = 0;
	            	boolean temp = true;
	            	for (int i=0;i<threadCount;i++) {
	            		totalC += th[i].count;
	            		temp &= th[i].flag;
	            	}
	            	progDiag.lblText.setText("("+new DecimalFormat("0.00").format((totalC*100)/(double)progDiagCount)+"%)");
	            	progDiag.progressBar.setValue(totalC);
	            	progDiag.progressBar.setString(totalC+"/"+progDiagCount);
	            	if (temp) break;
	            	try { Thread.sleep(200); } catch (InterruptedException e) {}
        		}
        		progDiag.lblText.setText("Please wait");
            	try { Thread.sleep(1000); } catch (InterruptedException e) {}
        		progDiag.setVisible(false);
        		progDiag.dispose();
        	}
        };
        t.start();
        progDiag.setVisible(true);
        
        while (true) {
        	boolean temp=true;
        	for (int i=0;i<threadCount;i++) {
        		temp&=th[i].flag;
        	}
        	if (temp) break;
        	try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
        
        result.clear();
        for (int i=0;i<threadCount;i++) {
        	result.addAll(extractor[i].listOfRequests);
        }
        
        System.out.println();
        
    }
    
    public void logToDB() throws IOException{
      	Connection dbconn;
      	Statement stmt;
      	String sql;
      	
      	try {
			Class.forName("org.hsqldb.jdbcDriver" );
      	}
      	catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
      	}

        try {
        	
            dbconn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
            stmt = dbconn.createStatement();
            //clear the table
            sql = "CREATE TABLE TBLREQUESTINFO ("+
					"PAGE VARCHAR(100),"+
					"CLIENT VARCHAR(200),"+
					"REQDATE TIMESTAMP,"+
					"RESPONSETIME BIGINT,"+
					"RESPONSIVENESS BIGINT,"+
					"NOOFOBJECTS BIGINT,"+
					"RESPONSESIZE BIGINT);";
            
            stmt.executeUpdate(sql);
            
            Iterator iter = result.iterator();
            Request req = new Request();
            while (iter.hasNext()) {
                PreparedStatement ps=dbconn.prepareStatement("INSERT INTO tblRequestInfo (page, client, reqDate, responseTime, responsiveness, noOfObjects, RESPONSESIZE) "
                						+ "VALUES (?,?,?,?,?,?,?)");
                req = (Request)iter.next();
                if (req.getNumberOfObjects() > 0) {
//                    if(req.getRemoteHost().isEmpty()){
//                    	req.getRemoteHost().equalsIgnoreCase("-");
//                    }
                	ps.setString(1,req.getRequestedPage());
                	ps.setString(2,req.getRemoteHost());
                	ps.setDate(3,new java.sql.Date(req.getRequestingDateAndTime().getTime()));
                	ps.setInt(4,req.getResponseTime());
                	ps.setInt(5,req.getResponsiveness());
                	ps.setInt(6,req.getNumberOfObjects());
                	ps.setLong(7,req.getResponseSize());
                	ps.execute();
                }
            }
            stmt.close();
	    dbconn.close();
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }          
    }
     
    /*
    public void logToDB() throws IOException{
      	Connection dbconn;
      	Statement stmt;
      	String sql;
      	try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
      	}
      	catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
      	} 

        try {
            dbconn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "cxcheng", "123456");
            stmt = dbconn.createStatement();
            //clear the table
            sql = "TRUNCATE TABLE tblRequestInfo";
            stmt.executeUpdate(sql);
            
            Iterator iter = extractor.getListOfRequests();
            Request req = new Request();
            Calendar cld = Calendar.getInstance();
            while (iter.hasNext()) {
                sql = "";
                req = (Request)iter.next();
                if (req.getNumberOfObjects() > 0) {
                    sql += "INSERT INTO tblRequestInfo (page, client, reqDate, responseTime, ";
                    sql += "responsiveness, noOfObjects, responseSize) ";
//                    if(req.getRemoteHost().isEmpty()){
//                    	req.getRemoteHost().equalsIgnoreCase("-");
//                    }
                    sql += "VALUES('" + req.getRequestedPage() + "', '" + req.getRemoteHost() + "', ";
                    cld.setTime(req.getRequestingDateAndTime());
                    int month = cld.get(Calendar.MONTH)+1;
                    sql += "to_date('" + cld.get(Calendar.YEAR) + "-" + month + "-";
                    sql += cld.get(Calendar.DAY_OF_MONTH) + " " + cld.get(Calendar.HOUR_OF_DAY) 
                                + ":";
                    sql += cld.get(Calendar.MINUTE) + ":" + cld.get(Calendar.SECOND) + "', '";
                    sql += "yyyy/mm/dd:hh24:mi:ss'), '";
                    sql += req.getResponseTime() + "', '" + req.getResponsiveness() + "', '";
                    sql += req.getNumberOfObjects() + "', '" +req.getResponseSize() + "')";
                    stmt.executeUpdate(sql);
                }
            }
            stmt.close();
	    dbconn.close();
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }          
    }*/
     
}


 


