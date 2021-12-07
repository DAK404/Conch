package Conch.API.Fish;

import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public final class ReadFile {

    private File file;

    Console console=System.console();

    public void ShowHelp(String helpFile)throws Exception
    {
        try
        {
            file=new File("./Information/Conch/"+helpFile);
            readFile();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    public void userReadFile(String FileName)throws Exception
    {
        // if(new Conch.API.policyEnforce("editor").checkPolicy()==false)
        // {
        //     return;
        // }

        try
        {
            file=new File(FileName+console.readLine("Enter the name of the file to be read: "));
            readFile();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    * Method containing the logic to read the file, either a help file or a user file
    *
    * @throws Exception : Throws any exception caught during runtime/execution
    */
    private void readFile() throws Exception
    {
        //A link to show the build info to the user's terminal
        Conch.API.BuildInfo.viewBuildInfo();

        //A condition to check if the given file is found or not. This prevents exception, which may or may not disrupt the program.

        //This checks if the file doesnt exist. If it doesnt exist, the error text is shown on terminal.
        if (file.exists() == false)
            System.out.println("SYSTEM> The specified file cannot be read, found or loaded.");

        //If the file exists, the file is displayed on the terminal.
        else {
            //Open the method to read files and read it
            FileReader f = new FileReader(file);
            BufferedReader ob = new BufferedReader(f);

            //Initialize the string to be null
            String p = "";

            //Logic to read the file line by line.
            while ((p = ob.readLine()) != null)

                //Read the line loaded
                System.out.println(p);

            //After reading the file, close the streams opened.
            ob.close();
            f.close();
            System.gc();
        }

        //Finally, the program awaits the user input to continue, returning to the main program.
        console.readLine("\nPress enter to continue.");
        return;
    }
}