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

        String message = """
        CONCH SETUP CHECKLIST
        =====================

        1. Legal and Important Information    : """ + _legalAndInfo + """
        2. Initialize Conch Dependencies      : """ + _createDirectories + """
        3. Initialize Database Files          : """ + _initializeDatabase + """
        4. Administrator Account Creation     : """ + _initializeAdminAccount + """
        5. Initialize Policies and BURN Files : """ + _initializePolicies + """

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
