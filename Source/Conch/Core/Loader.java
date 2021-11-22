package Conch.Core;

import java.io.Console;
import java.io.File;

public class Loader 
{
    Console console = System.console();

    String status = "OK";

    public static void main(String[] args)throws Exception
    {
        switch(args[0])
        {
            case "debug":
                if(args.length < 2)
                    new Loader().debugDefault();
                else
                switch(args[1])
                {
                    case "noerror":
                    new Loader().debugger();
                    break;

                    default:
                    System.exit(400);
                }
                break;
                
            case "normal":
                new Loader().debugLoad();
                break;

            default:
                System.exit(0x500000);
        }
    }

    public void loadLogic()
    {
        debugLoad();
    }

    private void debugLoad()
    {
        Conch.API.BuildInfo.viewBuildInfo();
        System.out.println("Program Loader Started Successfully.");
        checkFiles();
        System.out.println("Checking Files... " + (checkFiles()? "OK" : "FAILED"));
        while(loaderMenu());
    }
    
    private boolean checkFiles()
    {
        boolean result = false;

        String[] dirs = {"./Users", "./System", "./System/Private/Conch", "./System/Public/Conch"};

        for (String string : dirs) 
        {
            if (! new File(string).exists())
            {
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

            case "clear":
                Conch.API.BuildInfo.clearScreen();
                break;

            case "setup":
                //pass on the program control to the setup program
                System.out.println("WORK IN PROGRESS");
                break;

            default:
                System.out.println("COMMAND NOT RECOGNIZED");
        }
        return true;
    }

    private void debugger()throws Exception
    {
        Conch.API.BuildInfo.viewBuildInfo();
        console.readLine("~DBGR_CRAB_v0.0.12%%: ");
    }

    private void debugDefault()throws Exception
    {
        try
        {
            throw new Exception("CANNOT DEBUG PROGRAM WITHOUT PERMISSIONS");
        }
        catch(Exception e)
        {
            new Conch.API.ExHandler().handleException(e);
        }
    }
}
