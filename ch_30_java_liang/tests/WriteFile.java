package tests;

import java.io.FileWriter;
import java.io.IOException;


public class WriteFile {
    public static void main(String[] args) {
        try{
            FileWriter myText = new FileWriter("temp.txt", true);
            myText.write("\n Let move to the next line");
            myText.close();
        } catch (IOException e){
            System.out.println("An error occured" + e);
        }
    }
}
