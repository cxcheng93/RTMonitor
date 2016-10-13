package RTMonitor;

import java.util.*;
import java.lang.Math;

public class Page {
	private String name;
    private int requestCount;
    private ArrayList <Client> listOfClients;
    private double averageResponseTime;
    private double medianResponseTime;
    private double responseTime_90; //90th percentile response time
    private double averageResponsiveness;
    private double medianResponsiveness;
    private double responsiveness_90; //90th percentile responsiveness
    private double medianNoOfObjects;
    private int minNoOfObjects;    
    private int maxNoOfObjects;    
    private double medianResponseSize;
    private long minResponseSize;    
    private long maxResponseSize;
    //private int status;
    //private Date requestingDateAndTime;
    
    /** Creates a new instance of Page */
    public Page() {
        name = "";
        requestCount = 0;
        listOfClients = new ArrayList<>();
    }
    
    public Page(Page pg) {
        name = pg.getName();
        requestCount = pg.getRequestCount();
        listOfClients = pg.getClients();
    }
    
    public String getName() {
        return name;
    }
    
    public int getRequestCount() {
        return requestCount;
    }
     
    public Iterator <Client> getListOfClients() {
        return listOfClients.iterator();
    }
        
    public ArrayList <Client> getClients() {
        return listOfClients;
    }
    
    public int getStatusCode(){
    	if (listOfClients.size()==0) return 0;
    	return ((Client)listOfClients.get(0)).getStatusCode();
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }
    public double getAverageResponsiveness() {
        return averageResponsiveness;
    }

    public double getMedianResponseTime() {
        return medianResponseTime;
    }

    public double getMedianResponsiveness() {
        return medianResponsiveness;
    }

    public double getMedianNoOfObjects() {
        return medianNoOfObjects;
    }

    public double getResponseTime_90() {
        return responseTime_90;
    }

    public double getResponsiveness_90() {
        return responsiveness_90;
    }

    public int getMinNoOfObjects() {
        return minNoOfObjects;
    }

    public int getMaxNoOfObjects() {
        return maxNoOfObjects;
    }

    public double getMedianResponseSize() {
        return medianResponseSize;
    }

    public long getMinResponseSize() {
        return minResponseSize;
    }

    public long getMaxResponseSize() {
        return maxResponseSize;
    }
    
    public Date getRequestingDateAndTime() {
    	if (listOfClients.size()==0) return new java.sql.Date(0);
    	return ((Client)listOfClients.get(0)).getTimeStamp();
    }

    public void setName(String n) {
        name = n;
    }
    
    public void setRequestCount(int rc) {
        requestCount = rc;
    }
     
    public void setAverageResponseTime(double avg) {
        averageResponseTime = avg;
    }
    
    public void setAverageResponsiveness(double avg) {
        averageResponsiveness = avg;
    }
    public void setMedianResponseTime(double med) {
        medianResponseTime = med;
    }

    public void setMedianResponsiveness(double med) {
        medianResponsiveness = med;
    }

    public void setMedianNoOfObjects(double med) {
        medianNoOfObjects = med;
    }

    public void setResponseTime_90(double nin) {
        responseTime_90 = nin;
    }

    public void setResponsiveness_90(double nin) {
        responsiveness_90 = nin;
    }

    public void setMinNoOfObjects(int min) {
        minNoOfObjects = min;
    }

    public void setMaxNoOfObjects(int max) {
        maxNoOfObjects = max;
    }

    public void setMedianResponseSize(double rs) {
        medianResponseSize = rs;
    }

    public void setMinResponseSize(long rs) {
        minResponseSize = rs;
    }

    public void setMaxResponseSize(long rs) {
        maxResponseSize = rs;
    }

    public void addClient(Client c) {
        listOfClients.add(c);
    }    
    
    public void setValues(String p, String c, Date ts, int rt, int rv, int noo, long rs, int st) {
        name = p;
        Client cl = new Client(c, ts, rt, rv, noo, rs, st);
        addClient(cl);
        requestCount++;
    }
  
    public void addClient(String c, Date ts, int rt, int rv, int noo, long rs, int st) {
        Client cl = new Client(c, ts, rt, rv, noo, rs, st);
        addClient(cl);
        requestCount++;
    }
    
    private double calculateAverage(int[] intArray){
        int size = intArray.length;
        int sum = 0;
        for (int i=0; i<size; i++) {
            sum += intArray[i];
        }
        return (double)sum/(double) size;    
    }
    
    private void insertionSort(int[] intArray) {
        int size = intArray.length;
        int i, j, index;
        for (i=1; i<size; i++) {
            index = intArray[i];
            j = i;
            while (j>0 && intArray[j-1]>index) {
                intArray[j] = intArray[j-1];                
                j--;
            }       
            intArray[j] = index;
        }
    }
    
    private void insertionSort(long[] longArray) {
        int size = longArray.length;
        int i, j;
        long index;
        for (i=1; i<size; i++) {
            index = longArray[i];
            j = i;
            while (j>0 && longArray[j-1]>index) {
                longArray[j] = longArray[j-1];                
                j--;
            }       
            longArray[j] = index;
        }
    }

    public void analyse() {
       int count = listOfClients.size();
       int[] tempRT = new int[count]; 
       int[] tempRV = new int[count]; 
       int[] tempNOO = new int[count]; 
       long[] tempRS = new long[count];
       int i = 0, temp;
       double nin;
       Client cl;
       Iterator <Client> it = listOfClients.iterator();
       while (it.hasNext()) {
            cl = (Client)it.next();
            tempRT[i] = cl.getResponseTime();
            tempRV[i] = cl.getResponsiveness();
            tempNOO[i] = cl.getNoOfObjects();
            tempRS[i] = cl.getResponseSize();
            i++;
       }
       if (i > 0) { //there are requests recorded
           setAverageResponseTime(calculateAverage(tempRT));
           setAverageResponsiveness(calculateAverage(tempRV));
           insertionSort(tempRT);
           insertionSort(tempRV);
           insertionSort(tempNOO);
           insertionSort(tempRS);
           temp = i / 2;
           if (i % 2 == 0) { //even number of array elements
                setMedianResponseTime(((double)tempRT[temp] + (double)tempRT[temp-1])/2.0);
                setMedianResponsiveness(((double)tempRV[temp] + (double)tempRV[temp-1])/2.0);
                setMedianNoOfObjects(((double)tempNOO[temp] + (double)tempNOO[temp-1])/2.0);
                setMedianResponseSize(((double)tempRS[temp] + (double)tempRS[temp-1])/2.0);
            }
            else { //odd number of array elements
                setMedianResponseTime((double)tempRT[temp]);
                setMedianResponsiveness((double)tempRV[temp]);
                setMedianNoOfObjects((double)tempNOO[temp]);           
                setMedianResponseSize((double)tempRS[temp]);
            }
            nin = ((double) i) * 0.9;
            if (Math.rint(nin) == nin) { // nin is a whole number
                temp = (int) nin;
                setResponseTime_90((double)tempRT[temp]);
                setResponsiveness_90((double)tempRV[temp]);
            }
            else {
                temp = (int) Math.floor(nin);
                if (temp+1 >= i) {
                    setResponseTime_90((double)tempRT[temp]);
                    setResponsiveness_90((double)tempRV[temp]);
                }
                else {
                    setResponseTime_90(((double)tempRT[temp] + (double)tempRT[temp+1])/2.0);
                    setResponsiveness_90(((double)tempRV[temp] + (double)tempRV[temp+1])/2.0);
                }
            }
            setMinNoOfObjects(tempNOO[0]);
            setMaxNoOfObjects(tempNOO[tempNOO.length-1]);
            setMinResponseSize(tempRS[0]);
            setMaxResponseSize(tempRS[tempNOO.length-1]);
       }
    }

}
