package sample;

import java.io.*;

public class ReadFile {

    public static void main(String[] args){

    }

    public String[][] readFile2Array (boolean enemy, int id){
        String sid;
        String path;

        if (id<10){
            sid="0"+ id;
        }
        else{
            sid=String.valueOf(id);
        }
        if (enemy) {
            path ="src/sample/enemy_SCENARIO-"+sid+".txt";
        }
        else{
            path ="src/sample/player_SCENARIO-"+sid+".txt";
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
