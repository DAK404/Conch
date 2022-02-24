// ========================================================================================= //
// | ATTENTION!     ATTENTION!     ATTENTION!     ATTENTION!     ATTENTION!     ATTENTION! | //
// ========================================================================================= //
//                                                                                           //
//      THE SOURCE CODE FOR THE PROGRAM USES THE GNU GPL 3.0 LICENSE. IF YOU DECIDE TO       //
//     MODIFY, COMPILE AND DISTRIBUTE THE SOURCE CODE, YOU MUST INCLUDE THIS DISCLAIMER,     //
//     ANY MODIFICATIONS, AND ANY CHANGES MADE TO THE PROGRAM. THE GNU GPL 3.0 LICENSE       //
//     CAN BE FOUND HERE: https://www.gnu.org/licenses/gpl-3.0.en.html                       //
//                                                                                           //
//     NOTE: THE SOFTWARE MUST HAVE A LINK TO THE PROGRAM SOURCE CODE OR MUST BE BUNDLED     //
//     ALONG WITH THE PROGRAM BINARIES. IF YOU DO NOT AGREE TO THE TERMS, DO NOT USE THE     //
//      SOURCE CODE OR THE BINARIES. THE SOURCE CODE MODIFICATIONS WILL INHERIT THE GNU      //
//     GPL 3.0 LICENSE AND THE CODE MUST BE MADE OPEN SOURCE.                                //
//                                                                                           //
// ========================================================================================= //

package Conch.Core;

//import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import Conch.API.PrintStreams;

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
        prerequisites();
        legal();
        createDirectories();
        initializeDatabase();
        initializeAdminAccount();
        initializePolicies();

        PrintStreams.printAttention("Setup Complete. Do you want to check for updates? [Y | N ]");
        if(console.readLine("CHECK_UPDATE?> ").equalsIgnoreCase("y"))
            new Conch.API.FileFlex.Parcel.UpdateLogic().updateProgram();
        
        System.exit(0x1A0001);
        
    }

    private void displayStatus()throws Exception
    {
        //Clear the screen and display the Truncheon Build Information
        Conch.API.BuildInfo.viewBuildInfo();

        String message = """
        CONCH SETUP CHECKLIST
        =====================

        1. Legal and Important Information    : """ + _legalAndInfo + """
        \n2. Initialize Conch Dependencies      : """ + _createDirectories + """
        \n3. Initialize Database Files          : """ + _initializeDatabase + """
        \n4. Administrator Account Creation     : """ + _initializeAdminAccount + """
        \n5. Initialize Policies and BURN Files : """ + _initializePolicies + """

        =====================""";
        
        System.out.println(message);
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

            _legalAndInfo = "COMPLETE";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _legalAndInfo = "ERROR";
        }
    }

    private void createDirectories()
    {
        try
        {
            displayStatus();
            PrintStreams.printInfo("Creating Directories...");

            String[] fileList = {"./System/Conch/Public", "./System/Conch/Private", "./Users/Conch", "./Logs"};
            for(String files:fileList)
                PrintStreams.printAttention(files + (new File(files).mkdirs()?" Created":" Already Exists. Skipping..."));

            _createDirectories = "COMPLETE";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _createDirectories = "ERROR";
        }
        console.readLine("Press ENTER to Continue.");
    }

    private void initializeDatabase()
    {
        try
        {
            displayStatus();

            String message = """
            IMPORTANT INFORMATION: Master User Database

            The Master User Database contains all the users who have an
            account in Conch. This database securely stores the credentials.

            This step will initialize the Master User Database.

            Initializing Database...""";

            System.out.println(message);

            //check if the Master User Database exists
            if(new File("./System/Conch/Private/mud.dbx").exists())
            {
                //Do not continue if the database file exists
                PrintStreams.printError("MASTER USER DATABASE ALREADY EXISTS! ABORTING...");
                _initializeDatabase = "ERROR";
                return;
            }

            String databasePath = "jdbc:sqlite:./System/Conch/Private/mud.dbx";
            
            String sqlCommand = "CREATE TABLE IF NOT EXISTS MUD (Username TEXT, Name TEXT NOT NULL, Password TEXT NOT NULL, SecurityKey TEXT NOT NULL, PIN	TEXT NOT NULL, Privileges TEXT NOT NULL, PRIMARY KEY(Username));";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            Statement statement = dbConnection.createStatement();

            statement.execute(sqlCommand);

            statement.close();
            dbConnection.close();
            System.gc();
            _initializeDatabase = "COMPLETE";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _initializeDatabase = "ERROR";
        }
        PrintStreams.printInfo("Master User Database has been Initialized Successfully!");
        console.readLine("Press ENTER to Continue.");
    }

    private void initializeAdminAccount()
    {
        try
        {
            displayStatus();

            String message = """
            IMPORTANT INFORMATION: Administrator Account

            A master user account is required to administrate, maintain and configure Conch.
            This account cannot be deleted. The Administrator account can then be used to
            create other administrator and user accounts.
            
            Press ENTER to continue.""";

            console.readLine(message);

            new Conch.API.Coral.AccAdd().setupAdminAccount();

            _initializeAdminAccount = "COMPLETE";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _initializeAdminAccount = "ERROR";
        }
    }

    private void initializePolicies()
    {
        try
        {
            displayStatus();

            String message = """
            IMPORTANT INFORMATION: Policy System

            Conch uses a policy system to define which modules are useable by a certain user group.
            A certain policy can block or allow a user to use certain features and functionalities.
            By default, all the policies are initialized to allow all available features.
            
            An Administrator account will need to then define the policy and its value to
            administrate the system.""";
            
            System.out.println(message);

            new Conch.API.Oyster.PolicyEditor().policySetupMode();
            console.readLine("Press ENTER to Continue.");

            _initializePolicies = "COMPLETE";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _initializePolicies = "ERROR";
        }
    }
}
