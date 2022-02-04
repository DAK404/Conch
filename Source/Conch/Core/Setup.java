package Conch.Core;

import java.io.Console;
import java.io.File;

public class Setup
{
    private String _legalAndInfo = "INCOMPLETE";
    private String _createDirectories  = "INCOMPLETE";
    private String _initializeDatabase = "INCOMPLETE";
    private String _initializeAdminAccount = "INCOMPLETE";
    private String _initializePolicies = "INCOMPLETE";

    private Console console = System.console();

    public void setupLogic()
    {

    }

    private void displayStatus()throws Exception
    {
        //Clear the screen and display the Truncheon Build Information
        Conch.API.BuildInfo.viewBuildInfo();

        //Display the setup status and information.
        System.out.println("SETUP CHECKLIST");
        System.out.println("===============\n");
        System.out.println("1. Legal and Important Information    : " + _legalAndInfo);
        System.out.println("2. Initialize Truncheon Dependencies  : " + _createDirectories);
        System.out.println("3. Initialize Database Files          : " + _initializeDatabase);
        System.out.println("4. Administrator account creation     : " + _initializeAdminAccount);
        System.out.println("5. Initialize Policies and BURN Files : " + _initializePolicies);
        System.out.println("\n===============");
    }

    private void prerequisites()
    {
        String output = """
        Welcome to Conch!

        The shell needs to be setup before it can be used.

        NOTE:

        * If you are an Administrator, press ENTER to continue.
        * If you are a normal user, please contact the Administrator for help.
        * The shell cannot be used until the setup is complete.

        The following steps will be taken to setup the Conch shell:

        ---------------------------------------
        |          SETUP INFORMATION          |
        ---------------------------------------
            A. LEGAL AND IMPORTANT INFORMATION
        \t1. EULA [END USER LICENSE AGREEMENT]
        \t2. Readme
        \t3. What's New!
            C. Create Truncheon Dependencies
        \t1. Create Truncheon Directories
        \t2. Create Multi User Database
        \t3. Create Administrator Account
            D. INITIALIZE THE SYSTEM NAME
            E. INITIALIZE PROGRAM POLICIES
            F. CHECK FOR UPDATES
            G. END SETUP
        \n---------------------------------------

        To exit the setup, press the CTRL + C keys. Else, press ENTER key.
        """;

        console.readLine(output + "DEFAULT@SETUP_MODE> ");
    }

    private void legal()
    {
        try
        {
            new Conch.API.FileFlex.FileRead().readManualDocument("EULA.txt");
            if(console.readLine("EULA?> ").equalsIgnoreCase("y"))
            {
                new Conch.API.FileFlex.FileRead().readManualDocument("Readme.txt");
                new Conch.API.FileFlex.FileRead().readManualDocument("Credits.txt");
            }
            else
                System.exit(0x1A0100);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        _legalAndInfo = "ACCEPTED & DONE";
    }

    private void createDirectories()
    {
        String[] fileList = {"./System/Conch/Public", "./System/Conch/Private", "./Users/Conch"};
        for(String files:fileList)
            new File(files).mkdirs();

        _createDirectories = "COMPLETE";

    }

    private void initializeDatabase()
    {

    }

    private void initializeAdminAccount()
    {

    }

    private void initializePolicies()
    {

    }
}
