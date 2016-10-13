package RTMonitor;

import java.text.*;
import java.util.*;	


public class ExtractLogInfo {
	//the max time interval to be considered a request session = 60s 
    public static final int SEQUENCE_RANGE = 60000; //in ms
    public ArrayList<Request> listOfRequests = new ArrayList<Request>();
    
    /** Creates a new instance of ExtractProxy */
    public ExtractLogInfo() {}
         
    //Convert date and time string into an instance of Date
    //in the format of "dd/MMM/yyyy:HH:mm:ss"
    private Date getDateAndTime(String dateString) { //throws ParseException{
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
        Date reqDateTime = null;
        ParsePosition pp = new ParsePosition(0);
        try {
            dateString = dateString.substring(1);
            reqDateTime = dateTimeFormat.parse(dateString, pp);                       
        }
        catch (NullPointerException pe) {
            System.out.println("Incompatible Date Format");
        }
        return reqDateTime;
    }
    
    //Extract the requested object's name from the given string
    /*private String getObjectName(String s) {
        StringTokenizer st;
        String t = "";
        //split the string based on '/'
        st = new StringTokenizer(s, "/");
        //get the last token, i.e. the object's name
        while (st.hasMoreTokens()) {
            t = st.nextToken();
        }
        return t;        
    }*/
    
    //Determine if it is a request for a page or base object
    private boolean isAPage(String s) {
        String [] extensions = {".htm", ".html", ".shtm", ".dhtm", ".php", ".asp", ".aspx", 
                                ".jsp", ".cgi", ".xml", ".cfm", ".cfml", ".pl", "/"};                             
        //StringTokenizer st;
        for (int i = 0; i < extensions.length; i++) {
            if (s.endsWith(extensions[i])) {
                return true;
            }
        }
        return false;
    }
    
    //Extract the line read into corresponding components
    public void extractLines(String line) {
        StringTokenizer st;
        Request aRequest = new Request();
        Date dateAndTime;
        String remoteHost;
        String requestedPage;
        String dateString;
        String method;
        String protocol;
        String tmp;
        int status = 0;
        long bytes = 0;
        //Split based on white space.
        //The fields are  remote host; date and time of request; first line of request;
        //status code; response size in bytes; 
        st = new StringTokenizer(line);
        remoteHost = st.nextToken();
        //rfc931(remote user logname) and authuser
        tmp = st.nextToken();
        tmp = st.nextToken();
        dateString = st.nextToken();
        dateAndTime = getDateAndTime(dateString);
        //Discard time locale
        st.nextToken();
        //Method: Get, Post etc
        method = st.nextToken();
        //Discard leading "
        method = method.substring(1,method.length());
        requestedPage = st.nextToken();
        //HTTP/1.0, HTTP/1.1 etc
        protocol = st.nextToken();
        //Discard tailing "
        protocol = protocol.substring(0,protocol.length()-1);
        //Get status code and bytes       
        try {
            tmp = st.nextToken();
            if (!(tmp.equals("-"))) {
                status = Integer.parseInt(tmp);
            }
            if(st.hasMoreTokens()){
            	tmp = st.nextToken();
                if (!(tmp.equals("-"))) {
                    bytes = Long.parseLong(tmp);
                }
            }
            
        }
        catch (NumberFormatException nfe) { }

        //Discard ? and tailing string for dynamic pages
        String[] s = requestedPage.split("\\?");
        //Record if a page or base object is requested
        if (isAPage(s[0])) {
            aRequest.setRemoteHost(remoteHost);
            aRequest.setRequestedPage(s[0]);
            aRequest.setRequestingDateAndTime(dateAndTime);
            aRequest.setResponseSize(bytes);
            aRequest.setStatusCode(status);
            listOfRequests.add(aRequest);
        }
        //Assign the non-page request to the last matching host/user if possible
        else {
            //There is previous request from the host/user
            if (!listOfRequests.isEmpty()) {
                int size = listOfRequests.size();
                boolean found = false;
                for (int i = size-1; i >-1; i--){
                    aRequest = (Request) listOfRequests.get(i);
                    if (aRequest.getRemoteHost().equals(remoteHost)) {
                        found = true;
                        break;
                    }                    
                }
                //Found the matching host
                if (found) {
                    //Within the session range
                    if ((Math.abs(dateAndTime.getTime() -
                       aRequest.getRequestingDateAndTime().getTime()))< SEQUENCE_RANGE) {
                         ReqObject temp = new ReqObject();
                         temp.setName(s[0]);
                         temp.setSize(bytes);
                         temp.setTime(dateAndTime);
                         if (aRequest.isFirstEmbeddedObject()){
                             aRequest.setSecondAccessTime(dateAndTime);
                             aRequest.setFirstEmbeddedObject(false);
                             aRequest.setResponsiveness((int)Math.abs(aRequest.getSecondAccessTime().getTime() -
                                    aRequest.getRequestingDateAndTime().getTime())/1000);
                         }
                         aRequest.setLastAccessTime(dateAndTime);                         
                         aRequest.setResponseTime((int)Math.abs(aRequest.getLastAccessTime().getTime() -
                                    aRequest.getRequestingDateAndTime().getTime())/1000);
                         aRequest.addObjects(temp);     
                    }
                }
            }
        }
        //Discard the request if no previous host/user matched
    }  
    
    public Iterator<Request> getListOfRequests() {
        return listOfRequests.iterator();
    }

}
