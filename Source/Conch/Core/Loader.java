package Conch.Core;

import java.io.Console;
import java.io.File;

public class Loader 
{
    Console console = System.console();

    String status = "OK";
    public void loadLogic()
    {
        debugLoad();
    }

    private void debugLoad()
    {
        System.out.println("Conch Booted successfully.");
        checkFiles();
        System.out.println("Checking Files... " + status);
        while(loaderMenu());
    }
    
    private boolean checkFiles()
    {
        boolean result = false;

        String[] dirs = {"./Users", "./System", "./System/Private", "./System/Public"};

        for (String string : dirs) 
        {
            if (! new File(string).exists())
            {
                status = "FAILED";
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean loaderMenu()
    {
        String input = console.readLine("~DBG@CONCH: ");
        switch(input)
        {
            case "exit":
                return false;

            case "":
                break;

            default:
                System.out.println("COMMAND NOT RECOGNIZED");
        }
        return true;
    }
}
