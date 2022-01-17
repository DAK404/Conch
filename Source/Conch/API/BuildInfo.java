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

    public static String _Version = "0.0.73";
    public static String _BuildDate = "14-Jan-2022";
    public static String _BuildID = "NION_14_JAN_22_0842_DEBUG";
    public static String _BuildType = "DEBUG";

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
