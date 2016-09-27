package pageMaintenance;

import java.util.ArrayList;

public class dbQuery {
	private ArrayList listOfDataFields;
    private String name;
    private String dataSource;
    private boolean hasAsterisk;
    private boolean isSelect; //select from query?
    
    /** Creates a new instance of query */
    public dbQuery() {
        name = "";
        dataSource = "";
        hasAsterisk = false;
        isSelect = false;
        listOfDataFields = new ArrayList();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public String getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(String ds) {
        dataSource = ds;
    }

    public boolean getHasAsterisk() {
        return hasAsterisk;
    }
    
    public void setHasAsterisk(boolean ha) {
        hasAsterisk = ha;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean is) {
        isSelect = is;
    }

    public void addDataField(dataField df) {
        listOfDataFields.add(df);        
    }
        
    public dataField getDataField(int i) {
        return (dataField) listOfDataFields.get(i);
    }

    public void setDataField(int i, dataField df) {
	  listOfDataFields.set(i, df);
    }
  
    public ArrayList getListOfDataFields() {
        return listOfDataFields;
    }
    
    public int getNumberOfDataFields() {
        return listOfDataFields.size();
    }

}
