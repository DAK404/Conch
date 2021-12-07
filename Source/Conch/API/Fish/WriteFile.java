package Conch.API.Fish;

import java.io.Console;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.text.*;


public final class WriteFile {
    private String message = "";
    private File file = null;

    Console console = System.console();

    //Get date for logging purposes
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();


    public WriteFile()
    {
    }

    //main method to write to file(FOR TESTING ONLY)
    // public static void main(String[] args) throws Exception {
    //     WriteFile writeFile = new WriteFile();
    //     String dir=System.getProperty("user.dir");
    //     writeFile.editScript(dir);
    //     writeFile.Log("TEST STRING", "LOG1");
    // }

    public void editScript(String dir) throws Exception
    {
        try
        {
            file = new File(dir+console.readLine("Enter the name of file to be saved: "));
            writeContent();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    public void Log(String PrintToFile, String FileName) throws Exception {
        try
        {
          System.gc();
          BufferedWriter obj = new BufferedWriter(new FileWriter("./" + FileName + ".log", true));
          PrintWriter pr = new PrintWriter(obj);
          pr.println(dateFormat.format(date) + ": " + PrintToFile);
          pr.close();
          obj.close();
        }
        catch(Exception E)
        {
          System.out.println("Cannot Write to file. Change the path or grant permission.");
          return;
        }
    }

    private void writeContent() throws Exception
    {
        try
        {
            boolean writeMode=true;
            if(file.exists()==true)
            {
                System.out.println("Do you want to overwrite the file or append to the file?\n[ Overwrite | Append ]");
                if(console.readLine().toLowerCase().equals("overwrite"))
                    writeMode=false;
            }

            BufferedWriter obj = new BufferedWriter(new FileWriter(file, writeMode));
            PrintWriter pr =  new PrintWriter(obj);



            System.out.println("Conch Text Editor 1.7");
            System.out.println("______________________\n\n");

            while(true)
            {
                message=console.readLine();

                //Keep receiving inputs until the user types <exit>
                if(message.equalsIgnoreCase("<exit>"))
                    break;

                pr.println(message);
            }

            pr.close();
            obj.close();
            System.gc();
            return;

        } catch (Exception E)
        {
            System.out.println("Error.");
            E.printStackTrace();
        }
    }


}