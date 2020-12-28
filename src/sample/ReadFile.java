package sample;

import java.io.*;
import java.util.Arrays;

public class ReadFile {
    private String path;

    public static void main(String[] args){
        ReadFile readFile=new ReadFile();
        String[][] test;
        test=readFile.readFile2Array(true,1);
        System.out.println( Arrays.deepToString( test ) );
    }

    public String[][] readFile2Array (boolean enemy, int id){
        String sid;
        if (id<10){
            sid="0"+String.valueOf(id);
        }
        else{
            sid=String.valueOf(id);
        }
        if (enemy) {
            path="src/sample/enemy_SCENARIO-"+sid+".txt";
        }
        else{
            path="src/sample/player_SCENARIO-"+sid+".txt";
        }
        String[][] strArray = new String[5][4];
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0;
            while((strLine = br.readLine())!= null) {
                strArray[i]=strLine.split(",");;
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
