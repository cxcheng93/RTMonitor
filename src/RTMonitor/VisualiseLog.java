package RTMonitor;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class VisualiseLog {
	private ArrayList <Page>listOfPages = new ArrayList<>();
	private java.util.Date fromDate;
	private java.util.Date toDate;

		public VisualiseLog() {
	    }
	     
	    private int getPageIndex(String n) {
	        Iterator<Page> it = listOfPages.iterator();
	        int index = -1;
	        while (it.hasNext()) {
	            Page pg = (Page) it.next();
	            index++;
	            if (pg.getName().equals(n)) {
	                return index;
	            }
	        }
	        index = -1;
	        return index;
	    }
	    
	    //return the page at the given index
	    public Page getPage(int index) {
	        if (index > -1 && index < listOfPages.size()) {
	            return (Page) listOfPages.get(index);
	        }
	        else {
	            return null;
	        }
	    }
	    
	    //create table view of the page requesting information
	    public void createTable() {
	        JFrame.setDefaultLookAndFeelDecorated(true);
	        //JFrame frame = new JFrame("Page Requests Summary");
	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //TableCreator newContentPane = new TableCreator(listOfPages, fromDate, toDate);
	        //newContentPane.setOpaque(true); 
	        //frame.setContentPane(newContentPane);
	        //frame.pack();
	        //frame.setVisible(true);
	        FrameFilter f=new FrameFilter(listOfPages,fromDate,toDate);
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);

	    }   
	  
	    /** Retrieve page request information from the database*/
	    public void retrievePageInfo(String filtStatement) {
	      	Connection dbconn;
	      	Statement stmt;
	        ResultSet result;
	        String sql;
	        Page pg;
	        int counter, index;
	   
	        try {
	            Class.forName("org.hsqldb.jdbcDriver");
	        }
	        catch (ClassNotFoundException cnfe) {
	            cnfe.printStackTrace();
	        } 

	        try {
	            dbconn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
	            stmt = dbconn.createStatement();
	            sql = "SELECT * from tblRequestInfo "+filtStatement+" ORDER BY responseTime ASC";
	            System.out.println(sql);
	            result = stmt.executeQuery(sql);
	            
	            while (result.next()) {
	                 index = getPageIndex(result.getString("page"));
	                 if (index == -1) { //A page was first requested
	                    pg = new Page();
	                    pg.setValues(result.getString("page"), result.getString("client"), 
	                       result.getTimestamp("reqDate"), result.getInt("responseTime"),
	                       result.getInt("responsiveness"), result.getInt("noOfObjects"),
	                       result.getLong("responseSize"),result.getInt("STATUS"));
	                    listOfPages.add(pg);
	                }
	                else {
	                    pg = (Page) listOfPages.get(index);
	                    pg.addClient(result.getString("client"), result.getTimestamp("reqDate"), 
	                       result.getInt("responseTime"), result.getInt("responsiveness"), 
	                       result.getInt("noOfObjects"), result.getLong("responseSize"),
	                       result.getInt("STATUS"));
	                    listOfPages.set(index, pg);
	                }
	            } 
	            sql = "SELECT Min(reqDate) as MinDate from tblRequestInfo";
	            result = stmt.executeQuery(sql);
	            if (result.next()) {
	                fromDate = result.getTimestamp("MinDate");
	            }
	            sql = "SELECT Max(reqDate) as MaxDate from tblRequestInfo";
	            result = stmt.executeQuery(sql);
	            if (result.next()) {
	                toDate = result.getTimestamp("MaxDate");
	            }
	            //Analyse page access information
	            counter = listOfPages.size();
	            index = 0;
	            while (index < counter) {
	               pg = (Page) listOfPages.get(index);
	               pg.analyse();
	               listOfPages.set(index, pg);
	               index++;
	            }
	        }
	        catch (SQLException sqle) {
	            sqle.printStackTrace();
	        } 
	    

	    	}
	    }
