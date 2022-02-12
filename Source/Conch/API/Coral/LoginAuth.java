
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginAuth
{
    private String _username;

    public LoginAuth(String usn)
    {
        _username = usn;
    }

    public boolean authenticationLogic(String psw, String key)
    {
        return retrieveDatabaseEntry("SELECT Password FROM MUD WHERE Username = ?", "Password").equals(psw) & retrieveDatabaseEntry("SELECT SecurityKey FROM MUD WHERE Username = ?", "SecurityKey").equals(key);
    }

    public boolean checkPrivilegeLogic()
    {
        return retrieveDatabaseEntry("SELECT Administrator FROM MUD WHERE Username = ?", "Administrator").equals("Yes");
    }

    public String getNameLogic()
    {
        return retrieveDatabaseEntry("SELECT Name FROM MUD WHERE Username = ?", "Name");
    }

    public String getPINLogic()
    {
        return retrieveDatabaseEntry("SELECT PIN FROM MUD WHERE Username = ?", "PIN");
    }

    private String retrieveDatabaseEntry(String sqlCommand, String parameter)
    {
        String result = "DEFAULT_STRING";
        try
        {
            Class.forName("org.sqlite.JDBC");

            String databasePath = "jdbc:sqlite:./System/Conch/Private/mud.dbx";

            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, _username);

            ResultSet resultSet = preparedStatement.executeQuery();

            result = resultSet.getString(parameter);

            resultSet.close();
            preparedStatement.close();
            dbConnection.close();

            System.gc();
        }
        catch(Exception e)
        {
            result = "ERROR";
            e.printStackTrace();
        }
        return result;
    }    
}
