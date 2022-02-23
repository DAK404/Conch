package Conch.Core;

import java.io.Console;

import Conch.API.BuildInfo;
import Conch.API.PrintStreams;

class ConchLogic
{

    private byte _attemptsRemaining = 5; 
    private boolean _admin = false;
    private String _name = "";
    private String _username = "";
    private String _PIN = "";
    private String _systemName = "";

    Console console = System.console();

    ConchLogic()
    {
        try
        {
            _systemName = new Conch.API.Oyster.PolicyEnforce().retrievePolicyValue("sysname");
        }
        catch(Exception E)
        {
            
        }
    }

    public final void startConch()
    {
        try
        {
            while(! login() & _attemptsRemaining > 0 & _attemptsRemaining <= 5)
                decrementAttempts();
            _attemptsRemaining = 5;
            System.gc();
            getUserDetails();

            //start execution of Conch
            startShell();
        }
        catch(Exception e)
        {

        }
    }

    private boolean login()throws Exception
    {
        verViewerMM();
        String username = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username: "));
        String password = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String key = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return challenge(username, password, key);
    }

    private boolean challenge(String username, String password, String key)
    {
        boolean status = false;
        try
        {
            status = new Conch.API.Coral.LoginAuth(username).authenticationLogic(password, key);
        }
        catch(Exception e)
        {
            status = false;
        }
        return status;
    }

    private void decrementAttempts()throws Exception
    {
        --_attemptsRemaining;

        if(_attemptsRemaining == 1)
        {
            Thread.sleep(900000);
            _attemptsRemaining = 1;
        }
        PrintStreams.printAttention(_attemptsRemaining + " attempts remaining. Press ENTER to continue.");
        console.readLine();
    }

    private void getUserDetails()
    {
        _name = new Conch.API.Coral.LoginAuth(_username).getNameLogic();
        _admin = new Conch.API.Coral.LoginAuth(_username).checkPrivilegeLogic();
        _PIN = new Conch.API.Coral.LoginAuth(_username).getPINLogic();
    }

    private void startShell()
    {
        verViewerMM();
        while(true)
            commandProcessor(console.readLine(_name + "@" + _systemName + "> "));
    }

    private void commandProcessor(String cmd)
    {
        try
        {
            String[] cmdArr = cmd.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            switch(cmdArr[0])
            {
                case "exit":
                    System.exit(0);
                    break;

                case "restart":
                    System.exit(0x1A0001);
                    break;

                case "clear":
                    verViewerMM();
                    break;

                case "":
                    break;

                case "pseudo":
                    elevatePermissions();
                    break;

                case "lock":
                    lockConsole();
                    break;
                
                default:
                    PrintStreams.printError("Unrecognized Command/Module: " + cmdArr[0] + ".");
                    break;
            }
        }
        catch(Exception e)
        {

        }
    }

    private void elevatePermissions()throws Exception
    {
        String adminUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username: "));
        String adminPassword = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String adminKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        _admin = challenge(adminUsername, adminPassword, adminKey) & new Conch.API.Coral.LoginAuth(adminUsername).checkPrivilegeLogic()?true:false;
        PrintStreams.printAttention("Privilege Elevation: " + (_admin?"Successful":"Failed"));
    }

    private final void lockConsole()throws Exception
    {
        verViewerMM();
        PrintStreams.printAttention("Session Locked.");
        PrintStreams.printInfo("Enter \'unlock\' to resume session.");
        while(! console.readLine(_name + "@AFK>").equalsIgnoreCase("unlock"));
        
        while(! unlockLogic())
            decrementAttempts();
        _attemptsRemaining = 5;
    }

    private final boolean unlockLogic()throws Exception
    {
        verViewerMM();
        return new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("PIN: "))).equals(_PIN);
    }

    private void verViewerMM()
    {
        System.out.println(BuildInfo._Branding);
        System.out.println("------------------------------\n");
        System.out.println("Administrator Privileges: " + (_admin?"Granted":"Not Granted"));
    }
}