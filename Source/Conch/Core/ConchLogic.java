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
    private String _tempUsername = "";
    private char _promptChar = '*';

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
            _username = _tempUsername;
            System.gc();
            getUserDetails();
            _promptChar = _admin?'!':'*';

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
        _tempUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username: "));
        String password = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String key = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return challenge(_tempUsername, password, key);
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

        if(_attemptsRemaining == 0)
        {
            PrintStreams.printError("Too Many Attempts! Locking Inputs for 10 minutes...");
            Thread.sleep(900000);
            _attemptsRemaining = 1;
        }
        PrintStreams.printAttention(_attemptsRemaining + " attempts remaining. Press ENTER to continue.");
        console.readLine();
    }

    private void getUserDetails()throws Exception
    {
        _name = new Conch.API.Coral.LoginAuth(_username).getNameLogic();
        _admin = new Conch.API.Coral.LoginAuth(_username).checkPrivilegeLogic();
        _PIN = new Conch.API.Coral.LoginAuth(_username).getPINLogic();
    }

    private void startShell()
    {
        verViewerMM();
        while(true)
            commandProcessor(console.readLine(_name + "@" + _systemName + _promptChar + "> "));
    }

    private void commandProcessor(String cmd)
    {
        try
        {
            String[] cmdArr = cmd.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            switch(cmdArr[0])
            {
                case "fileflex":
                    new Conch.API.FileFlex.FlexLogic().fileFlex(_username);
                    break;

                case "update":
                    new Conch.API.FileFlex.Parcel.UpdateLogic().updateProgram(_username);
                    break;

                case "exit":
                    System.exit(0);
                    break;

                case "restart":
                    System.exit(3);
                    break;

                case "clear":
                    verViewerMM();
                    break;

                case "policy":
                    new Conch.API.Oyster.PolicyEditor().policyManager(_username);
                    break;

                case "echo":
                    if(cmdArr.length < 2)
                        PrintStreams.printAttention("Syntax: echo <string>");
                    else
                        PrintStreams.println(cmdArr[1]);
                    break;

                case "syshell":
                    if(! _admin)
                        PrintStreams.printError("SYSHELL not available to standard users! Aborting...");
                    else
                    {
                        try
                        {
                            if(System.getProperty("os.name").contains("Windows"))
                                new ProcessBuilder("cmd").inheritIO().start().waitFor();
                            else
                                new ProcessBuilder("/bin/bash").inheritIO().start().waitFor();
                        }
                        catch(Exception e)
                        {
                            PrintStreams.printError("SYSTEM SHELL ERROR!");
                            PrintStreams.printError("Error Details: " + e.getStackTrace().toString());
                        }
                    }
                    break;

                case "sys":
                    if(! _admin)
                        PrintStreams.printError("SYS not available to standard users! Aborting...");
                    else
                    {
                        try
                        {
                            if(System.getProperty("os.name").contains("Windows"))
                                new ProcessBuilder("cmd", "/c", cmdArr[1]).inheritIO().start().waitFor();
                            else
                                new ProcessBuilder("/bin/bash", "-c" , cmdArr[1]).inheritIO().start().waitFor();
                        }
                        catch(Exception e)
                        {
                            PrintStreams.printError("SYSTEM SHELL ERROR!");
                            PrintStreams.printError("Error Details: " + e.getStackTrace().toString());
                        }
                    }
                    break;

                case "":
                    break;

                case "pseudo":
                    elevatePermissions();
                    break;

                case "lock":
                    lockConsole();
                    break;

                case "usermgmt":
                    switch(cmdArr[1])
                    {
                        case "add":
                            new Conch.API.Coral.AccAdd().addAccount(_username);
                            break;

                        case "delete":
                            new Conch.API.Coral.AccDel().userDeletionLogic(_username);
                            break;

                        case "modify":
                            new Conch.API.Coral.AccMod().modifyAccount(_username);
                            break;
                    }
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
        if(_admin)
        {
            PrintStreams.printAttention("Elevated Permissions Already Granted! Aborting...");
            return;
        }
        String adminUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username: "));
        String adminPassword = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String adminKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        _admin = challenge(adminUsername, adminPassword, adminKey) & new Conch.API.Coral.LoginAuth(adminUsername).checkPrivilegeLogic()?true:false;
        PrintStreams.printAttention("Privilege Elevation: " + (_admin?"Successful":"Failed"));
        _promptChar = _admin?'!':'*';
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
        BuildInfo.clearScreen();
        System.out.println(BuildInfo._Branding);
        System.out.println("Version: " + BuildInfo._Version);
        System.out.println("--------------------------------------------\n");
        System.out.println("Administrator Privileges: " + (_admin?"Granted":"Not Granted"));
        System.out.println();
    }
}