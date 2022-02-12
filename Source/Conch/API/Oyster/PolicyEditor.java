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

package Conch.API.Oyster;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.Properties;

import Conch.API.PrintStreams;

public class PolicyEditor 
{
    private final String[] _resetValues = {"update", "download", "fileflex", "read", "edit", "usermgmt"};
    private final String _policyFileName = "./System/Conch/Private/Policy.burn";
    private String _currentUsername = "";

    private Console console = System.console();
    private Properties props = null;

    public void policyManager()
    {
        if(!login())
        {
            PrintStreams.printError("Invalid Credentials. Aborting...");
            return;
        }

        if(!getAdminStatus())
        {
            PrintStreams.printError("Administrator Privileges missing. Aborting...");
            return;
        }

        policyEditorLogic();
    }

    private boolean login()
    {
        boolean status = false;
        try
        {
            _currentUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username     :"));
            String password = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password     :")));
            String secKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key :")));

            status = new Conch.API.Coral.LoginAuth(_currentUsername).authenticationLogic(password, secKey);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    private boolean getAdminStatus()
    {
        return new Conch.API.Coral.LoginAuth(_currentUsername).checkPrivilegeLogic();
    }

    private void policyEditorLogic()
    {
        while(true)
        {
            props = new Properties();
            if(!new File(_policyFileName).exists())
                resetPolicyValues();

            viewPolicies();

            switch(console.readLine("[ MODIFY | RESET | HELP | EXIT ]\n\nOyster#> ").toLowerCase())
            {
                case "modify":
                editPolicy();
                break;

                case "reset":
                resetPolicyValues();
                break;

                case "help":
                break;

                case "exit":
                return;

                case "":
                break;

                default:
                    PrintStreams.printError("Invalid command.");
            }
        }        
    }

    private void viewPolicies()
    {
        try
        {
            Conch.API.BuildInfo.viewBuildInfo();


            String message = """
            Oyster Policy Management System 3.0
            -----------------------------------
                Oyster Policy Editor v1.8
            -----------------------------------     

            Policy File """ + _policyFileName + """
            Policy Format: XML
            """;

            FileInputStream policyFileLoadStream = new FileInputStream(_policyFileName);
            props.loadFromXML(policyFileLoadStream);
            policyFileLoadStream.close();

            System.out.println(message);
            props.list(System.out);
            System.out.println("-----------------------------------\n");

            System.gc();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void editPolicy()
    {
        try
        {
            do
            {
                viewPolicies();
                String policyName = console.readLine("Enter Policy Name to Modify : ");
                String policyValue = console.readLine("Enter the new Policy Value  : ");

                PrintStreams.printInfo("Saving Policy Changes to File...");
                savePolicyToFile(policyName, policyValue);
            }
            while(console.readLine("Modify Another Policy? [ Y | N ]\nOyster#> ").toLowerCase().equals("y"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void savePolicyToFile(String policyName, String policyValue)
    {
        try
        {
            props.setProperty(policyName, policyValue);
            FileOutputStream policyFileSaveStream = new FileOutputStream(_policyFileName);
            props.storeToXML(policyFileSaveStream, "ConchPolicies");
            policyFileSaveStream.close();

            PrintStreams.printInfo("Policy Saved.");
            PrintStreams.printInfo("Policy Name: " + policyName + " | Policy Value: " + policyValue);

            System.gc();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void resetPolicyValues()
    {
        props = new Properties();
        new File(_policyFileName).delete();

        savePolicyToFile("sysname", "SYSTEM");
        for(String policy: _resetValues)
            savePolicyToFile(policy, "on");
    }

    public void policySetupMode()
    {
        if(new File(_policyFileName).exists())
            PrintStreams.printError("Policy File already initialized! Aborting...");
        else
            resetPolicyValues();
    }
}
