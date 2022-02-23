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
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AccAdd 
{
    private boolean _status = false;

    private String _currentUsername = "DEFAULT";
    private boolean _currentAccountAdmin = false;

    //Store the new account information
    private String _newAccountName = null;
    private String _newAccountUsername = null;
    private String _newAccountPassword = null;
    private String _newAccountSecurityKey = null;
    private String _newAccountPIN = null;
    private boolean _newAccountAdmin = false;

    private Console console = System.console();

    public void addAccount()throws Exception
    {
        //main logic to add an account
        if(!login())
        {
            Conch.API.PrintStreams.printError("Invalid Credentials. Aborting...");
            return;
        }

        _currentAccountAdmin = getAdminStatus();
        if(_currentAccountAdmin)
            grantAdminRights();

        getAccountDetails();        
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

    private boolean getAdminStatus()throws Exception
    {
        return new Conch.API.Coral.LoginAuth(_currentUsername).checkPrivilegeLogic();
    }

    private boolean grantAdminRights()
    {
        String message = """
        Granting Administrator Rights:
        ------------------------------

        The new account can be granted administrator rights.
        Administrator rights are privileges used to access extra features to
        maintain and administrate the system.

        By granting Administrator rights, the new account will be promoted to an administrator account.

        Do you want to continue? Type \'YES\' to grant Administrator rights or RETURN to continue.
        ?> """;

        return console.readLine(message).toLowerCase().equals("yes");
    }

    private void displayDetails()
    {
        System.gc();
        Conch.API.BuildInfo.viewBuildInfo();
        System.out.println("Administrator Rights Granted? : " + (_newAccountAdmin?"YES":"NO"));
        System.out.println(_newAccountName == null || _newAccountName.equals("")?"":"Account Name: " + _newAccountName);
        System.out.println(_newAccountUsername == null || _newAccountUsername.equals("")?"":"Account Username: " + _newAccountUsername);
        System.out.println(_newAccountPassword == null || _newAccountPassword.equals("")?"":"Account Password: ********");
        System.out.println(_newAccountSecurityKey == null || _newAccountSecurityKey.equals("")?"":"Account Security Key: ********");
        System.out.println(_newAccountPIN == null || _newAccountPIN.equals("")?"":"Account PIN: ****");
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

        _newAccountName = console.readLine(message);

        if(_newAccountName == null | _newAccountName.contains(" ") | _newAccountName.equals("") | !(_newAccountName.matches("^[a-zA-Z0-9]*$")) | _newAccountName.equalsIgnoreCase("Administrator") | _newAccountName.length() < 2)
        {
            _newAccountName = "";
            _status = false;
            console.readLine("Invalid Account Name. Press ENTER to try again.");
        }
        else
            _status = true;

        return _status;
    }

    private boolean getUsername()throws Exception
    {
        displayDetails();

        String message = """
        Account Username Policy Information
        -----------------------------------
        * Username cannot contain the word \'Administrator\'
        * Username can contain numbers, special characters and symbols.
        -----------------------------------

        Account Username> """;

        _newAccountUsername = (console.readLine(message));

        if(_newAccountUsername == null | _newAccountUsername.equals("") | _newAccountUsername.equalsIgnoreCase("Administrator"))
        {
            _newAccountUsername = "";
            _status = false;
            console.readLine("Invalid Account Username. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_newAccountUsername);
        }
        
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
        
        _newAccountPassword = String.valueOf(console.readPassword(message + " "));
        String confirmPassword = String.valueOf(console.readPassword("Confirm Password> "));

        if(_newAccountPassword == null | _newAccountPassword.equals("") | _newAccountPassword.length() < 8 | !(_newAccountPassword.equals(confirmPassword)))
        {
            _newAccountPassword = "";
            confirmPassword = "";
            _status = false;
            console.readLine("Invalid Account Password. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountPassword = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_newAccountPassword);
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

        _newAccountSecurityKey = String.valueOf(console.readPassword(message + " "));
        String confirmKey = String.valueOf(console.readPassword("Confirm Security Key> "));

        if(_newAccountSecurityKey == null | !(_newAccountSecurityKey.equals(confirmKey)))
        {
            _newAccountSecurityKey = "";
            confirmKey = "";
            _status = false;
            console.readLine("Invalid Account Security Key. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountSecurityKey = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_newAccountSecurityKey);
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

        _newAccountPIN = String.valueOf(console.readPassword(message + " "));
        String confirmPIN = String.valueOf(console.readPassword("Confirm PIN> "));

        if(_newAccountPIN == null | !(_newAccountPIN.equals(confirmPIN)))
        {
            _newAccountPIN = "";
            confirmPIN = "";
            _status = false;
            console.readLine("Invalid Account PIN. Press ENTER to try again.");
        }
        else
        {
            _status = true;
            _newAccountPIN = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(_newAccountPIN);
        }

        return _status;
    }

    private void addAccountLogic()
    {
        try
        {
            String databasePath = "jdbc:sqlite:./System/Conch/Private/mud.dbx";
            String sqlCommand = "INSERT INTO MUD(Username, Name, Password, SecurityKey, PIN, Privileges) VALUES(?,?,?,?,?,?)";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);

            preparedStatement.setString(1, _newAccountUsername);
            preparedStatement.setString(2, _newAccountName);
            preparedStatement.setString(3, _newAccountPassword);
            preparedStatement.setString(4, _newAccountSecurityKey);
            preparedStatement.setString(5, _newAccountPIN);
            preparedStatement.setString(6, (_newAccountAdmin?"Yes":"No"));

            preparedStatement.executeUpdate();

            preparedStatement.close();
            dbConnection.close();

            new File("./Users/Conch/"+_newAccountUsername).mkdirs();

            System.gc();

            console.readLine("Account Creation Successful. Press ENTER to continue.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getAccountDetails()throws Exception
    {
        while(!getName());
        while(!getUsername());
        while(!getPassword());
        while(!getSecurityKey());
        while(!getPIN());

        displayDetails();
        addAccountLogic();
    }

    public void setupAdminAccount()throws Exception
    {
        _newAccountAdmin = true;
        _newAccountName = "Administrator";
        _newAccountUsername = new Conch.API.Scorpion.Cryptography().stringToSHA3_256("Administrator");

        while(!getPassword());
        while(!getSecurityKey());
        while(!getPIN());

        displayDetails();
        addAccountLogic();
    }
}
