package Conch.API.FileFlex;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FlexLogic 
{
    private final String ARGUMENT_MISMATCH = """
    [E] : Invalid argument length.
    """;
    private final String ARGUMENT_PATH_INVALID = """
    [A] : The source/destination path/file does not exist.
    """;
    private final String DEST_FILE_EXISTS = """
    [A] : A file with the same name exists in the destination path.
    """;

    private Console console = System.console();

    public void fileFlex()throws Exception
    {
        String temp;
        do
        {
            temp = console.readLine();
            fileFlexLogic(temp);
        }while(!temp.equalsIgnoreCase("exit"));
    }

    private void fileFlexLogic(String input)throws Exception
    {
        String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        switch(cmd[0])
        {
            case "cp":
            case "copy":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: copy <source> <destination>":"Copying File...");
                break;
            
            case "mv":
            case "move":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: move <source> <destination>":"Moving File...");
                break;
        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO NAVIGATE THE USER HOME DIRECTORY
    ///////////////////////////////////////////////////////////////

    private void changeDir(String fileName)throws Exception
    {

    }

    private void resetToHomeDir()throws Exception
    {

    }

    private void prevDir()throws Exception
    {

    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO CHECK FOR A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final boolean checkFile(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO PRINT THE DIRECTORY IN A TREE FORMAT
    ///////////////////////////////////////////////////////////////

    

}
