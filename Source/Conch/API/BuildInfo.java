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

package Conch.API;

/**
 * A program to print the version and information of the shell
 * Contains methods to clear the screen, based on the OS
 *
 * @author Deepak Anil Kumar (@DAK404)
 * @version 1.0
 * @since 0.0.1
 */
public class BuildInfo
{
    public static String _Branding = """
    __     ____  ___   _   _   ____  _   _    __
    \\ \\   / ___|/ _ \\ | \\ | | / ___|| | | |  / /
     \\ \\ | |   | | | ||  \\| || |    | |_| | / /
     / / | |___| |_| || |\\  || |___ |  _  | \\ \\
    /_/   \\____|\\___/ |_| \\_| \\____||_| |_|  \\_\\
    """;

    public static String _Version = "1.0.0";
    public static String _BuildDate = "12-Feb-2022";
    public static String _BuildID = "NION_12_FEB_22_2120_ALPHA";
    public static String _BuildType = "ALPHA";

    public static void viewBuildInfo()
    {
        try
        {
            clearScreen();
            System.out.println(_Branding);
            System.out.println(" Prototype Build v" + _Version);
            System.out.println(" Build Date : " + _BuildDate);
            System.out.println(" Build ID   : " + _BuildID);
            System.out.println(" Build Type : " + _BuildType);
            System.out.println("------------------------------\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void clearScreen()
    {
        try
        {
            /*
            * Clear Screen Notes:

            * The program is reliant on clearing the screen based on the OS being run
            * Clear screen has been tested on Windows and Linux platforms only
            * Clear screen should have the IO Flush right after clearing the screen

            */

            if(System.getProperty("os.name").contains("Windows"))

            //Spawns a new process within cmd to clear the screen
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else
            //invokes bash to clear the screen
            new ProcessBuilder("/bin/bash", "-c" ,"reset").inheritIO().start().waitFor();

            System.out.flush();
        }
        catch(Exception e)
        {
            System.err.println("\n\nERROR WHILE CLEARING SCREEN");
            System.err.println("ERROR: " + e + "\n\n");
        }
    }
}
