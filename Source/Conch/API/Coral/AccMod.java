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

package Conch.API.Coral;

import java.io.Console;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Conch.API.BuildInfo;
import Conch.API.PrintStreams;

public class AccMod 
{
    private String _username = "";
    private String _name = "";
    private String _password = "";
    private String _securityKey = "";
    private String _PIN = "";
    private String _admin = "";

    private boolean _status = false;
    
    Console console = System.console();

    public void modifyAccount(String usn)throws Exception
    {
        _username = usn;
        if(!login())
        {
            PrintStreams.println("Authentication Failure. Aborting...");
            return;
        }
        getAdditionalInfo();
        modifyAccountDetails();   
    }

    private boolean login()throws Exception
    {
        boolean status = false;
        try
        {
            _username = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("Username: "));
            String password = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String secKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            status = new Conch.API.Coral.LoginAuth(_username).authenticationLogic(password, secKey);
        }
        catch(Exception e)
        {
            status = false;
        }
        return status;
    }

    private void getAdditionalInfo()throws Exception
    {
        _name = new Conch.API.Coral.LoginAuth(_username).getNameLogic();
        _admin = new Conch.API.Coral.LoginAuth(_username).checkPrivilegeLogic()?"Yes":"No";
        _PIN = new Conch.API.Coral.LoginAuth(_username).getPINLogic();
    }

    private void modifyAccountDetails()throws Exception
    {
        String message = """
        User Credential Modification Dashboard 1.4
        ------------------------------------------

        Enter the credential parameter to change value
        * Open Help
        * User Account Password
        * User Account Security Key
        * User Account PIN
        * Exit

        ------------------------------------------
            [ HELP | PSW | KEY | PIN | EXIT ]    
        ------------------------------------------
        """;

        String adminTools = """
        Administrator rights detected.

        ------------------------------------------
         ADMINISTRATOR ACCOUNT MANAGEMENT TOOLKIT
        ------------------------------------------
        
        ! Promote Account to Administrator
        ! Demote Account to Standard

        ------------------------------------------
        [ PROMOTE | DEMOTE ]
        ------------------------------------------
        """;
        BuildInfo.viewBuildInfo();
        while(true)
        {
            switch(console.readLine(message + (_admin.equals("Yes")?adminTools:"") + "> ").toLowerCase())
            {
                case "psw":
                while(! getPassword());
                updateValues("Password", _password, _username);
                break;

                case "key":
                while(! getSecurityKey());
                updateValues("SecurityKey", _password, _username);
                break;

                case "pin":
                while(! getPIN());
                updateValues("PIN", _password, _username);
                break;

                case "name":
                while(! getName());
                updateValues("Name", _password, _username);
                break;

                case "help":
                break;

                case "promote":
                    userStatusChange("promote");
                break;

                case "demote":
                userStatusChange("demote");
                break;

                case "exit":
                return;

                case "":
                break;

                default:
                System.out.println("Invalid Input. Try again.");
                break;
            }
        }
    }

    private void userStatusChange(String status)throws Exception
    {
        try
        {
            //The functionality will not work if the account has a non administrator status
            if(! _admin.equalsIgnoreCase("Yes"))
                return;

            //Logic to either promote or demote the user.
            String user = console.readLine("Enter the name of the user to " + status + ": ");
            
            //Reject any attempts to promote or demote the user
            if(user.equalsIgnoreCase("Administrator"))
            {
                System.out.println("Cannot promote or demote the user Administrator.");
                return;
            }

            //Confirm if the user selected is to be promoted to or demoted from an account with administrator rights
            if(console.readLine("[ ATTENTION ] : ARE YOU SURE YOU WANT TO " + status.toUpperCase() + " " + user + "? [ Y | N ]\n\n} ").equalsIgnoreCase("y"))
            {
                //encode the selected user to a hashed format
                user = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(user);
                
                //Update the values in the database to reflect the changes
                switch(status)
                {
                    case "promote":
                    updateValues("Privileges", "Yes", user);
                    break;

                    case "demote":
                    updateValues("Privileges", "No", user);
                    break;
                }
            }
            System.gc();
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    private void displayDetails()
    {
        System.gc();
        Conch.API.BuildInfo.viewBuildInfo();
        System.out.println("Administrator Rights Granted? : " + _admin);
        System.out.println("Account Name: " + _name);
        System.out.println("Account Password: ********");
        System.out.println("Account Security Key: ********");
        System.out.println("Account PIN: ****");
    }

    private boolean getName()
    {
        displayDetails();

        String message = """
        Account Name Policy Information
        -------------------------------
        * Name cannot be \'Administrator\'
        * Name must contain English Alphabet, can have numbers
        * Name must have atleast 2 characters or more
        * Name cannot contain spaces
        -------------------------------

        Account Name> """;

        _name = console.readLine(message + " ");

        if(_name == null | _name.contains(" ") | _name.equals("") | !(_name.matches("^[a-zA-Z0-9]*$")) | _name.equalsIgnoreCase("Administrator") | _name.length() < 2)
        {
            _name = "";
            _status = false;
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        }
        else
            _status = true;

        return _status;
    }

    private boolean getPassword()throws Exception
    {
        displayDetails();

        String message = """
        Account Password Policy Information
        -----------------------------------
        * Password must contain atleast 8 characters
        * Password is recommended to have special characters and numbers
        -----------------------------------

        Account Password>""";
        
        _password = String.valueOf(console.readPassword(message + " "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        if(_password == null | _password.equals("") | _password.length() < 8 | !(_password.equals(confirmPassword)))
        {
            _password = "";
            confirmPassword = "";
            _status = false;
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _password = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_password);
        }

        return _status;
    }

    private boolean getSecurityKey()throws Exception
    {
        displayDetails();

        String message = """
        Account Security Key Policy Information
        -----------------------------------
        * Security Key must contain atleast 8 characters
        * Security Key is recommended to have special characters and numbers
        -----------------------------------

        Account Security Key> """;

        _securityKey = String.valueOf(console.readPassword(message + " "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        if(_securityKey == null | !(_securityKey.equals(confirmKey)))
        {
            _securityKey = "";
            confirmKey = "";
            _status = false;
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _securityKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_securityKey);
        }

        return _status;
    }

    private boolean getPIN()throws Exception
    {
        displayDetails();

        String message = """
        Account PIN Policy Information
        -------------------------------
        * PIN must contain atleast 4 characters
        * PIN is recommended to have special characters and numbers
        -------------------------------

        Account PIN> """;

        _PIN = String.valueOf(console.readPassword(message + " "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        if(_PIN == null | !(_PIN.equals(confirmPIN)))
        {
            _PIN = "";
            confirmPIN = "";
            _status = false;
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _PIN = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_PIN);
        }

        return _status;
    }

    private void updateValues(String credential, String value, String targetUser)throws Exception
    {
        try
        {
            //Initialize and open the connection to database
            String url = "jdbc:sqlite:./System/Conch/Private/mud.dbx";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            //set the values to be updated in the database
            String sql = "UPDATE MUD SET " + credential + " = ? WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, targetUser);

            //Execute the update statement
            pstmt.executeUpdate();

            //Close the streams and clean up memory allocated
            pstmt.close();
            conn.close();
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
            console.readLine();
        }
    }
}
