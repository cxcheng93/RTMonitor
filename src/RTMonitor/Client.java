package RTMonitor;

import java.util.*;
import java.io.*;

public class Client {
	private String name;
    private Date timeStamp;
    private int responseTime;
    private int responsiveness;
    private int noOfObjects;
    private long responseSize;
        
    /** Creates a new instance of Client */
    public Client() {
        name = "";
        timeStamp = null;
        responseTime = 0;
        responsiveness = 0;
        noOfObjects = 0;
        responseSize = 0;
    }
    
    public Client(String n, Date ts, int rt, int rv, int noo, long rs) {
        name = n;
        timeStamp = ts;
        responseTime = rt;
        responsiveness = rv;
        noOfObjects = noo;
        responseSize = rs;
    }
    
    public String getName() {
        return name;
    }
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public int getResponseTime() {
        return responseTime;
    }

    public int getResponsiveness() {
        return responsiveness;
    }

    public int getNoOfObjects() {
        return noOfObjects;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setName(String n) {
        name = n;
    }
    
    public void setTimeStamp(Date ts) {
        timeStamp = ts;
    }
    
    public void setResponseTime(int rt) {
        responseTime = rt;
    }
    
    public void setResponsiveness(int rv) {
        responsiveness = rv;
    }

    public void setNoOfObjects(int noo) {
        noOfObjects = noo;
    }

    public void setResponseSize(long rs) {
        responseSize = rs;
    }

}
