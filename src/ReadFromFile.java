import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ReadFromFile {

    public ArrayList<List<Integer>> fileToIntArray(String filepath){
        String strLine = "";
        ArrayList<List<String>> fileArray = new ArrayList<>();
        ArrayList<List<Integer>> intArray = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while (strLine != null) {
                strLine = br.readLine();
                if (strLine == null) {
                    break;
                }
                List<String> strList = Arrays.asList(strLine.trim().split("\\s+"));
                fileArray.add(strList);
            }
            for (int i=0; i<fileArray.size(); i++){
                List<Integer> lineOfIntegers = new ArrayList<>();
                for (int j=0; j<fileArray.get(i).size(); j++){
                    lineOfIntegers.add(Integer.parseInt(fileArray.get(i).get(j)));
                }
                intArray.add(lineOfIntegers);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Unable to read the file.");
        }
        return intArray;
    }

    public void initialize(ArrayList<List<Integer>> intArray){
        for (int i=0; i<intArray.size(); i++){
            List<Integer> intList = intArray.get(i);
            if (intList.size() == 3){
                //General depot info, 1 line
            }
            else if (intList.size() == 2){
                //Specs for each 1...n depot
            }
            else if (intList.size() == 7){
                //Depot coordinates for each 1...n depot
            }
            else {
                //Specs for each 1...m customer. Initialize customer per line
            }
        }
    }

    public static void main(String[] args) {
        String filepath = "./Files/DataFiles/p01";
        ReadFromFile rff = new ReadFromFile();
        System.out.println(rff.fileToIntArray(filepath));
    }
}