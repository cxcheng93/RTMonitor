package RTMonitor;

import java.util.*;

public class ReqObject {
	private String name;
    private long size;
    private Date time;
    
    /** Creates a new instance of object */
    public ReqObject() {
        name = null;
        size = 0;
        time = null;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setSize(long s) {
        size = s;
    }
    
    public void setTime(Date t) {
        time = t;
    }
    
    public String getName(){
        return name;
    }
    
    public long getSize() {
        return size;
    }
    
    public Date getTime() {
        return time;
    }

}
