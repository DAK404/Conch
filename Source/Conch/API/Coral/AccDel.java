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

//Import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Conch.API.PrintStreams;

public class AccDel
{
    private String _currentUsername = "";

    private Console console = System.console();

    public void userDeletionLogic(String currentUser)throws Exception
    {
        if(currentUser.equals(new Conch.API.Scorpion.Cryptography().stringToSHA3_256("Administrator")))
        {
            PrintStreams.printError("You cannot delete the Administrator account!");
            return;
        }

        _currentUsername = currentUser;

        if(! login())
        {
            PrintStreams.printError("Invalid Login Credentials. Please Try Again.");
            return;
        }
        else
        {
            if(console.readLine("Are you sure you wish to delete your user account?").equalsIgnoreCase("yes"))
            {
                deleteFromDatabase();
                PrintStreams.printAttention("Account Successfully Deleted. Press ENTER to continue.");
                console.readLine();
                System.exit(0);
            }
            else
                return;
        }

    }

    private boolean login()
    {
        boolean status = false;
        try
        {
            System.out.println("Username: " + new Conch.API.Coral.LoginAuth(_currentUsername).getNameLogic());
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

    private void deleteFromDatabase()
    {
        try
        {
            String databasePath = "jdbc:sqlite:./System/Conch/Private/mud.dbx";
            String sqlCommand = "DELETE FROM MUD WHERE Username = ?";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, _currentUsername);
            preparedStatement.executeUpdate();

            preparedStatement.closeOnCompletion();
            dbConnection.close();
            System.gc();

            deleteDirectories(new File("./Users/Conch/" + _currentUsername));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void deleteDirectories(File delFile)throws Exception
    {
        try
        {
            if (delFile.listFiles() != null)
            {
                for (File fn : delFile.listFiles())
                deleteDirectories(fn);
            }
            delFile.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}