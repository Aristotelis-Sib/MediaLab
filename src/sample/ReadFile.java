package sample;

import java.io.*;

public class ReadFile {

//    public static void main(String[] args){
//    }
    //Checks if files with given id exist,returns true or false
    public static boolean checkForFile(String id){
        String sid;
        String pathEnemy,pathUser;
        if (Integer.parseInt(id)<10){
            sid="0"+ id; }
        else{
            sid=id; }
        pathEnemy ="src/sample/medialab/enemy_SCENARIO-"+sid+".txt";
        pathUser ="src/sample/medialab/player_SCENARIO-"+sid+".txt";
        File f1 = new File(pathEnemy);
        File f2 = new File(pathUser);
        return f1.exists() && f2.exists();
    }
    //Get and parse files for given id and return as array
    public String[][] readFile2Array (boolean enemy, String id){
        String sid;
        String path;

        if (Integer.parseInt(id)<10){
            sid="0"+ id;
        } else{
            sid=id;
        }
        if (enemy) {
            path ="src/sample/medialab/enemy_SCENARIO-"+sid+".txt";
        } else{
            path ="src/sample/medialab/player_SCENARIO-"+sid+".txt";
        }
        String[][] strArray = new String[5][4];
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0;
            while((strLine = br.readLine())!= null) {
                strArray[i]=strLine.split(",");
                i++;
            }
        } catch(FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strArray;
    }
}
