package pageMaintenance;

import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

public class dataField {
	private ArrayList listOfOccurrence;
    private String name;  //data field name
    private String alias; //alais given to the data field
    private String table; //table in which the data field is stored
    
    /** Creates a new instance of fieldName */
    public dataField(String n, String a, String t) {
        name = n;
        alias = a;
        table = t;
        listOfOccurrence = new ArrayList();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String a) {
        alias = a;
    }

    public String getTable() {
        return table;
    }
    
    public void setTable(String t) {
        table = t;
    }

    public void addOccurrence(int lineNumber) {
        listOfOccurrence.add(new Integer(lineNumber));
    }
    
    public ArrayList getListOfOccurrence() {
        return listOfOccurrence; 
    }
    
    public int numberOfOccurrence() {
        return listOfOccurrence.size();
    }

}
