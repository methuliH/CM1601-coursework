package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader; //opens file
import java.io.FileWriter;
import java.io.IOException; //error handling
import java.util.ArrayList;
import java.util.List;

// read lines and saves it into a list
public class FileParser {
    //Existing file variables
    private static final String INVENTORY_LEGACY = "data/inventory_legacy.txt";
    private static final String DEALERS_LEGACY= "data/dealers_legacy.txt";
    private static final String INVENTORY_CLEAN = "data/inventoryData.txt";
    private static final String DEALERS_CLEAN = "data/dealerData.txt";

    //public method to be opened
    public static void initializeData(){
        if(!cleanFileExist()){
            List<String> rawInventory = readFile(INVENTORY_LEGACY);
            List<String> cleanedInventory = cleanData(rawInventory);
            writeCleanData(INVENTORY_CLEAN, cleanedInventory);

            List<String> rawDealers = readFile(DEALERS_LEGACY);
            List<String> cleanedDealers = cleanData(rawDealers);
            writeCleanData(DEALERS_CLEAN, cleanedDealers);
        }

    }
    // check if clean file exists
    private static boolean cleanFileExist(){
        File inv = new File(INVENTORY_CLEAN);
        File dealers = new File(DEALERS_CLEAN);
        return inv.exists() && dealers.exists();
    }

    //read file and save it  into a list
    private static List<String> readFile(String filePath){
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
            reader.close();
        }
        catch(IOException e){
            System.out.println("Error reading file: "+ e.getMessage());
        }
        return lines;
    }

// replace everything with commas
    private static List<String> cleanData(List<String> lines){
        List<String> cleanedList = new ArrayList<>();

       for (int i=0; i<lines.size(); ++i){
           String line = lines.get(i);
           line = line.replace("|",",");
           line = line.replace(";",",");
           line = line.trim();

           cleanedList.add(line);
       }
       return cleanedList;
    }
    //Writing to new file
    private static void writeCleanData(String filePath, List<String> cleanedlist){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (int i=0; i<cleanedlist.size(); ++i){
                String line = cleanedlist.get(i);
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println("Error writing file: " + e.getMessage());
        }
    }



}

