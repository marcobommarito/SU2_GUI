package io;
 
import java.util.*;
import java.io.*;

//This class serves as a means of parsing the user's entries into the file su2/user_conf.cfg
public class Config{
	
    public static void write(Map<String, String> data, String problemType, String turbModel){
        
        //System.out.println("User input:" + data); 
        //System.out.println("Implementing user input...");
        
        
        /**Exports the user input to an SU2 configuration file**/
        String oldFileName = "su2/user_conf.cfg";
        
        File oldFile = new File("su2/user_conf.cfg");
        
        if ( !oldFile.exists() || oldFile.isDirectory() ) oldFileName = "su2/default_conf.cfg";
        
        String tmpFileName = "su2/temp_conf.cfg";
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
            String line;
            outerloop:
            while ((line = br.readLine()) != null) {
                /**Uses keys in the "data" map to locate the SU2 variables in the .cfg file.**/
                for ( Map.Entry<String, String> entry: data.entrySet() ) {
                    if (line.contains(entry.getKey() + "=")){
                        line = (entry.getKey() + "= " + entry.getValue());
                        bw.write(line + System.getProperty("line.separator"));
                        continue outerloop;
                    }
                }
                
                //parameters that cannot be handled in the loop
                if (line.contains("PHYSICAL_PROBLEM= ")){
                	line = ("PHYSICAL_PROBLEM= " + problemType);
                }
                if (line.contains("KIND_TURB_MODEL= ")){
                	line = ("KIND_TURB_MODEL= " + turbModel);
                }
                //default:
                bw.write(line + System.getProperty("line.separator"));
            } 
        } catch (Exception e) {
            return;
        } finally {
            try {
                if( br != null){
                    br.close();
                }      
            } catch (IOException e){
                //
            }
            try {
                if ( bw != null ){
                    bw.close();
                }
            } catch (IOException e) {
                //
            }
        }
        
        /**Only delete the old file if it isn't the default one*/
        oldFile = new File("su2/user_conf.cfg");
        oldFile.delete();
        
        File newFile = new File(tmpFileName);
        newFile.renameTo(oldFile);
    }
    

}
