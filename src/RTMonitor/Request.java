package RTMonitor;

import java.util.*;
import java.io.*;

public class Request {
	private String remoteHost; //requesting host (name or IP)
    private String requestedPage; //requested page
    private ArrayList objects;  //embedded objects of the page
    private Date requestingDateAndTime; //requesting date and time for the page
    private long responseSize; //response size for the page in bytes
    private Date secondAccessTime; //date and time when the first embedded 
                                   //object was requested
    private Date lastAccessTime; //date and time when the last object was requested
    private boolean firstEmbeddedObject = true; //is there any embedded object found earlier
    private int responseTime;
    private int responsiveness;
    private int status;
    
    /** Creates a new instance of request */
    public Request() {
        remoteHost = null;
        requestedPage = null;
        objects = new ArrayList();
        requestingDateAndTime = null;
        responseSize = 0;  
        secondAccessTime = null;
        lastAccessTime = null;
    }
    
    public void setRemoteHost(String rh) {
        remoteHost = rh;
    }
    
    public void setRequestedPage(String rp) {
        requestedPage = rp;
    }

    public void addObjects(ReqObject ob) {
        responseSize += ob.getSize();
        objects.add(ob);
    }
    
    public void setRequestingDateAndTime(Date rdt) {
        requestingDateAndTime = rdt;
    }
    
    public void setSecondAccessTime(Date sa)  {
        secondAccessTime = sa;
    }
    
    public void setResponseSize(long rs) {
        responseSize = rs;
    }

    public void setLastAccessTime(Date la) {
        lastAccessTime = la;
    }

    public void setFirstEmbeddedObject(boolean feo) {
        firstEmbeddedObject = feo;
    }
   
    public boolean isFirstEmbeddedObject() {
        return firstEmbeddedObject;
    }
    
    public void setResponseTime(int rt) {
        responseTime = rt;
    }
    
    public void setResponsiveness(int rv) {
        responsiveness = rv;
    }
    
    public void setStatusCode(int st){
    	status = st;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
    
    public String getRequestedPage() {
        return requestedPage;
    }

    //return list of objects
    public Iterator getObjects() {
        return objects.iterator();
    }

    //return object at index i
    public ReqObject getObjects(int index) {
        return (ReqObject)objects.get(index);
    }
    
    public Date getRequestingDateAndTime() {
        return requestingDateAndTime;
    }
    
    public Date getSecondAccessTime() {
        return secondAccessTime;
    }
    
    public long getResponseSize() {
        return responseSize;
    }
    
    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public int getNumberOfObjects() {
        return objects.size();
    }
    
    public long getTotalSize() {
        long size = responseSize;
        Iterator it = objects.iterator();
        while (it.hasNext()) {
            ReqObject temp = (ReqObject) it.next();
            size += temp.getSize();
        }
        return size;
    }

    public int getResponseTime() {
        return responseTime;
    }
    
    public int getResponsiveness() {
        return responsiveness;
    }
    
    public int getStatuscode(){
    	return status;
    }
       
    //(first embedded object request time - page request time) 
    public long getPerceivedResponsiveness() {
        double time;
        try {
            if (secondAccessTime != null) {
                return Math.abs(secondAccessTime.getTime() 
                       - requestingDateAndTime.getTime());
            }
            else {
                return 0;
            }
         }catch (NullPointerException npe) {
                return -1;
         }
    }

    //(last embedded object request time - page request time) 
    public long getPerceivedResponseTime() {
        double time;
        try {
            if (lastAccessTime != null) {
                return Math.abs(lastAccessTime.getTime() 
                       - requestingDateAndTime.getTime());
            }
            else {
                return 0;
            }
         }catch (NullPointerException npe) {
                return -1;
         }
    }

}
