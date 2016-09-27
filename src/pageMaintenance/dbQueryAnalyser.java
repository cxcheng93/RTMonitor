package pageMaintenance;

import java.io.*;
import java.util.*;

public class dbQueryAnalyser {
	private ArrayList listOfQueries;
    private boolean hasQuery;
    
    public dbQueryAnalyser() {
        hasQuery = false;
        listOfQueries = new ArrayList();
    }

    //remove comments from in and write the results to temp
    public void removeComments(BufferedReader in, BufferedWriter temp) throws IOException  {
        String line = in.readLine();
        boolean commentFound = false;
        int index1, index2, beginIndex;
        while (line != null) {
            line = line.trim();
            beginIndex = 0;
            boolean repeat = true;
            while (repeat == true) {
                if (commentFound == true) { //comment spans from previous line
                   index2 = line.indexOf("-->");
                   if (index2 != -1) { //closing comment tag found
                      commentFound = false;
                      if ((index2 + 3) == line.length()) { //no more string after the closing comment tag
                         temp.write("\n");
                         repeat = false;
                         break;
                      }     
                      else { //more strings after the closing comment tag
                         beginIndex = index2 + 3;
                         line = line.substring(beginIndex, line.length()); //search remaining strings
                      }
                   }
                   else { //closing comment tag not found
                      temp.write("\n");
                      repeat = false;
                      break;
                   }
                }
                index1 = line.indexOf("<!--");
                if (index1 != -1) { //comment found
                   commentFound = true;
                   index2 = line.indexOf("-->", index1+1);
                   if (index2 != -1) { //closing comment tag found
                      if (index1 > 0) {
                       temp.write(line.substring(beginIndex, index1-1)); //copy parts before comment
                      }
                      commentFound = false;
                      if ((index2 + 3) == line.length()) { //no more string after closing comment tag
                         temp.write("\n");
                         repeat = false;
                         break;
                      }
                      else { //more strings after closing comment tag
                         beginIndex = index2 + 3;
                         line = line.substring(beginIndex, line.length()); //search remaining strings
                      }
                   }
                   else { //closing comment tag not found
                      temp.write("\n");
                      repeat = false;
                      break;
                   }
                }
                else { //no comment found in this line
                    temp.write(line + "\n");
                    repeat = false;
                    break;
                }                    
            }
            line = in.readLine();          
        }	
    }
    
    //search in the query for the given string (find)
    //mainly to identify query name and datasource
    private String search(String query, String find) {
        int index1, index2;
        String queryName = null;
        index1 = query.toLowerCase().indexOf(find.toLowerCase());
        if (index1 != -1) {
            index1 = query.indexOf('=', index1+1);
            index1 = query.indexOf('"', index1+1);
            index2 = query.indexOf('"', index1+1);
            if ((index1 != -1) && (index2 != -1)) {
              queryName = query.substring(index1+1, index2).trim();
             }
        }
        return queryName;
    }
    
    //extract query name, data source, and data fields retrieved from the query
    public void extractDataFields(String query) {
        int index1, index2;
        dbQuery dbq = new dbQuery();
        String queryName = "";
        String dataSource = "";
        String temp = "";
        String table = "";
        String dataField = "";
        String alias = "";
        //extract query name
        if ((queryName = search(query, " name")) == null) {
            dbq.setName("");
        }
        else {
            dbq.setName(queryName);
        }
            
        //Extract datasource name
        if ((dataSource = search(query, " datasource")) == null) {
            System.out.println("Invalid query. Datasource name not specified.");
            System.exit(0);
        }
        else {
            dbq.setDataSource(dataSource);
        }
        
        //Extract data fields and table names
        index1 = query.toLowerCase().indexOf("select ");
        if (index1 != -1) {
            index2 = query.toLowerCase().indexOf("from ", index1);
        }
        else {
            index2 = -1;
        }
        while ((index1 != -1) && (index2 != -1)) {
            //string between select and from
            dbq.setIsSelect(true);
            temp = query.substring(index1+7, index2).trim();
            StringTokenizer st1 = new StringTokenizer(temp, ",");  
            String current;
            int index3, index4;
            //identified fields separated by comma (,)
            while (st1.hasMoreTokens()) {
                current = st1.nextToken().trim();
                alias = "";
                table = "";
                dataField = current;
                //SQL functions, like count, max, etc
                index3 = current.indexOf('(');
                if (index3 != -1) {
                    index4 = current.indexOf(')', index3+1); 
                    if (index4 != -1) {
                        dataField = current.substring(index3+1, index4).trim();
                        index3 = current.indexOf(' ', index4+1); // more strings
                    }
                    else {
                        System.out.println("Query syntax error");
                        System.exit(0);
                    }
                }
                //data field has alias
                else if ((index3 = current.indexOf(' ')) != -1) {
                    dataField = current.substring(0, index3);
                }
                else {
                    dataField = current;
                }
                //data field qualified with table name
                index4 = dataField.indexOf('.');
                if (index4 != -1) {
                    table = dataField.substring(0, index4);
                    dataField = dataField.substring(index4+1, dataField.length());
                }
                //extract alias
                if (index3 != -1) { 
                    alias =  current.substring(index3+1, current.length()).trim();
                    if (alias.toLowerCase().startsWith("as ")) {
                        alias = alias.substring(2, alias.length()).trim();
                    }
                    else {
                        System.out.println("Query syntax error");
                        System.exit(0);                       
                    }                    
                }
                //wildcard used in the query
                if (dataField.equals("*") && alias.equals("")) {
                   dbq.setHasAsterisk(true);
                }

                //set the data field and add to the query
                dataField field = new dataField(dataField, alias, table);
                dbq.addDataField(field);
            }
            //search for other select ... from statement in the query
            index1 = query.toLowerCase().indexOf(" select ", index2);
            index2 = query.toLowerCase().indexOf(" from ", index1);                
        }
        //add the query to the list
        listOfQueries.add(dbq);
    }
    
    //check if the given character is an alphabet, digit, or underscore
    public boolean isAlphaNumericUnderscore(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
            (c >= '0' && c <= '9') || (c == '_')) {
             return true;
        }
        else {
            return false;
        }        
    }
    
    //check the occurrences of queries and data fields in the file
    //line numbers for the occurrences are identified
    public void checkOccurrence(File tempFile) throws IOException {
        String queryName, line, query, data;
        int index1, index2, index3, lineNumber, listIndex, dataIndex;
        Iterator it = listOfQueries.iterator();
        listIndex = 0;
	//search for each query in the list
        while (it.hasNext()) {
            BufferedReader in = new BufferedReader(new FileReader(tempFile)); 
	    dbQuery dbq = (dbQuery) it.next();
            if (dbq.getIsSelect() == true) {
            queryName = dbq.getName();
            line = in.readLine();
            lineNumber = 1;
            while ((line != null) && (!queryName.equals(""))) {
               query = queryName.toLowerCase();
               index1 = line.toLowerCase().indexOf(query);
               //make sure the query found is not just a substring 
               if ((index1 > 0) && (index1+query.length() < line.length())) {
                   if(isAlphaNumericUnderscore(line.charAt(index1-1)) ||
                      isAlphaNumericUnderscore(line.charAt(index1+query.length()))) {
                       index1 = -1;
                   }
               }
               //query found
               while (index1 != -1) {
                  //data field specified after the query? 
                  index2 = line.indexOf('.', index1);
                  if ((index2 != -1) && (index2 == index1 + query.length())) {
                     index3 = index2 + 1;
                     //possible data field?
                     if ((index3 < line.length()) && (isAlphaNumericUnderscore(line.charAt(index3)))) {
                        while (isAlphaNumericUnderscore(line.charAt(index3))) {  
                            index3++;
                            if (index3 >= line.length()) {
                                break;
                            }
                        }
                        //extract possible data field
                        data = line.substring(index2+1, index3);
                        dataIndex = -1;
                        //a data field specified in the query?
                        for (int i = 0; i < dbq.getNumberOfDataFields(); i++) {
                            if (((dataField)dbq.getListOfDataFields().get(i)).getName().equals(data) || 
                                ((dataField)dbq.getListOfDataFields().get(i)).getAlias().equals(data)) {
                                dataIndex = i;
                                break;
                            }
                        }
                        //is a data field specified in the query
                        if (dataIndex != -1) {
                        	((dataField)dbq.getListOfDataFields().get(dataIndex)).addOccurrence(lineNumber);
                        }
                        //not a data field but wildcard was specified in the query
                        else if (dbq.getHasAsterisk() == true) {
                            	dataField df = new dataField(data, "", "");
                            	df.addOccurrence(lineNumber);
                            	((dbQuery)listOfQueries.get(listIndex)).addDataField(df);
                        }
                        //search remaining of the line
                        index1 = line.toLowerCase().indexOf(query, index1+1);                  
                     }
                     //not a data field after the period (.) following query name
                     else {
                        index1 = line.toLowerCase().indexOf(query, index1+1);                  
                     }
                  }
                  //query name found, but not followed by a period (.)
                  else { 
                    dataIndex = -1;
                    //query treated as a whole
                    //found before this in the file?
                    for (int i = 0; i < dbq.getNumberOfDataFields(); i++) {
                        if (((dataField)dbq.getListOfDataFields().get(i)).getName().equals("-")) {  
                            dataIndex = i;
                            break;
                        }
                    }
                    //first occurrence in the file, created an entry in the query
                    if (dataIndex == -1) {
                        dataField df1 = new dataField("-", "", "");
                        df1.addOccurrence(lineNumber);
                        ((dbQuery)listOfQueries.get(listIndex)).addDataField(df1);
                    }
                    //occurred in earlier part(s), update the entry
                    else {
                        dataField df1 = ((dbQuery)listOfQueries.get(listIndex)).getDataField(dataIndex);
                        df1.addOccurrence(lineNumber);
                        ((dbQuery)listOfQueries.get(listIndex)).setDataField(dataIndex, df1);
                    }
                    //search remaining of the line
                    index1 = line.toLowerCase().indexOf(query, index1+1);                  
                  }
                  //if query name found in remaining of the line
                  //make sure it is not just a substring
                  while ((index1 > 0) && (index1+query.length() < line.length())) {
                      if(isAlphaNumericUnderscore(line.charAt(index1-1)) ||
                         isAlphaNumericUnderscore(line.charAt(index1+query.length()))) {
                        index1 = line.toLowerCase().indexOf(query, index1+1); 
                      }
                      //just a substring
                      else {
                          break;
                      }
                  }
               }
               //read the next line of the file
               line = in.readLine();
               lineNumber++;
            }
            }
            //search for the next query in the list
            listIndex++;
            in.close(); 
        }

    }
    
    //write query and data field occurrences to output file
    public void writeOccurrence(BufferedWriter out) throws IOException {
        Iterator it = listOfQueries.iterator();
        while (it.hasNext()) {
            dbQuery dbq = (dbQuery)it.next();
            if (dbq.getIsSelect() == true) {
            out.write("Query Name: " + dbq.getName());
            out.write("\nDatasource: " + dbq.getDataSource());
            Iterator it1 = dbq.getListOfDataFields().iterator();
            while (it1.hasNext()) {
                dataField df = (dataField)it1.next();
                out.write("\nData Field: " + df.getName());
                if (!df.getAlias().equals("")) {
                    out.write("    Alias: " + df.getAlias());
                }
                if (!df.getTable().equals("")) {
                    out.write("    Table: " + df.getTable());
                }
                out.write("\nOccurs at line(s): ");
                Iterator it2 = df.getListOfOccurrence().iterator();
                while (it2.hasNext()) {
                    out.write(((Integer)it2.next()).intValue() + "  " );
                }
            }
            out.write("\n\n");            
            }
        }
    }
            
    //examine the queries, including their existence, in the given file
    public void getQuery(String fName) throws IOException {
        File outFile = new File ("D:\\out.dat");
        FileWriter fout = new FileWriter(outFile);
        BufferedWriter out = new BufferedWriter(fout);
        File inFile = new File (fName);
        BufferedReader in = new BufferedReader(new FileReader(inFile)); 
        boolean found = false;
        String query = "";
        //check if it is a .cfm file
        if (fName.endsWith(".cfm") || fName.endsWith(".CFM")) {
           out.write("Page Name: " + fName + ":\n");
           out.write("Queries found:\n");
           String buffer = in.readLine();
           while (buffer != null) {
                StringTokenizer words = new StringTokenizer(buffer, " ");
                while (words.hasMoreTokens()) {
                    String nextTok = words.nextToken();
                    //System.out.println(nextTok);
                    //<CFQUERY> or <CFSTOREPROC> found
                    if (nextTok.trim().toUpperCase().startsWith("<CFQUERY") ||
                        nextTok.trim().toUpperCase().startsWith("<CFSTOREPROC")) {
                        found = true;
                        hasQuery = true;
                        out.write(nextTok + " ");
                        query = query + nextTok + " ";
                        while (words.hasMoreTokens() && found) {
                            nextTok = words.nextToken();
                            if (!(nextTok.trim().toUpperCase().equals("</CFQUERY>")) &&
                                !(nextTok.trim().toUpperCase().equals("</CFSTOREPROC>"))){
                                out.write(nextTok + " ");
                                query = query + nextTok + " ";
                            }
                            else {
                                found = false;
                                out.write(nextTok + " \n\n");
                                query = query + nextTok + "\n";
                                extractDataFields(query);
                                query = "";
                            }
                        }                 
                    }
                    //Query spans to the next line
                    else if (found){
                        while (!(nextTok.trim().toUpperCase().equals("</CFQUERY>")) &&
                               !(nextTok.trim().toUpperCase().equals("</CFSTOREPROC>")) &&
                               found){
                            out.write(nextTok + " ");
                            query = query + nextTok + " ";
                            if (words.hasMoreTokens()) {
                                nextTok = words.nextToken();
                            }
                            else {
                                break;
                            }
                        }
                        if (nextTok.trim().toUpperCase().equals("</CFQUERY>") ||
                            nextTok.trim().toUpperCase().equals("</CFSTOREPROC>")) {
                            out.write(nextTok + " \n\n");
                            query = query + nextTok + "\n";
                            found = false;
                            extractDataFields(query);
                            query = "";
                        }
                        }    
                     }
                     buffer = in.readLine();
               }
            }
        else {
            System.out.println("File type not match");
            System.exit(0);
        }
        //Database query found in the file
        if (hasQuery) {
           inFile = new File (fName);
           in = new BufferedReader(new FileReader(inFile)); 
           File tempFile = new File ("D:\\temp.dat");
           FileWriter tempout = new FileWriter(tempFile);
           BufferedWriter temp = new BufferedWriter(tempout);
 	   //remove comments from in, return the file without comments as temp
           removeComments(in, temp);
           temp.close(); 
           in.close(); 
	   //check the occurrences of data fields in temp
           checkOccurrence(tempFile);   
           //write occurrences to output file
           out.write("Number of queries: " + listOfQueries.size() + "\n\n");
           writeOccurrence(out);
        } 
        else {
           out.write("No query found:\n");
        }
        out.close();
     }

}
