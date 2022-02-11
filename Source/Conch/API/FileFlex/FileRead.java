/*
* ███    ██ ██  ██████  ███    ██             ██████  ██████  ███    ██  ██████ ██   ██ 
* ████   ██ ██ ██    ██ ████   ██     ██     ██      ██    ██ ████   ██ ██      ██   ██ 
* ██ ██  ██ ██ ██    ██ ██ ██  ██            ██      ██    ██ ██ ██  ██ ██      ███████ 
* ██  ██ ██ ██ ██    ██ ██  ██ ██     ██     ██      ██    ██ ██  ██ ██ ██      ██   ██ 
* ██   ████ ██  ██████  ██   ████             ██████  ██████  ██   ████  ██████ ██   ██ 
*/

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

package Conch.API.FileFlex;

//Import the required Java IO classes
import java.io.Console;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
*
*/
public final class FileRead
{
    File file = null;

    /**
    *
    * @param helpFile
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void readManualDocument(String helpFile)throws Exception
    {
        try
        {
            if(checkFileValidity(helpFile))
            {
                file = new File("./Information/Conch/" + helpFile);
                readFile(true);
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @param fn
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private boolean checkFileValidity(String fn)throws Exception
    {
        boolean status = true;
        if(fn == null || fn.equals("") || fn.startsWith(" "))
        {
            System.out.println("Please enter a valid file name to open.");
            status = false;
        }
        return status;
    }

    /**
    *
    * @param fileName
    * @param dir
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void readUserFile(String fileName, String dir)throws Exception
    {
        try
        {
            // if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("read") )
            // return;
            if(checkFileValidity(fileName))
            {
                file = new File( dir + fileName);
                readFile(false);
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @param helpMode
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void readFile(boolean helpMode) throws Exception
    {
        //A link to show the build info to the user's terminal
        Conch.API.BuildInfo.viewBuildInfo();

        Console console = System.console();

        //A condition to check if the given file is found or not. This prevents exception, which may or may not disrupt the program.

        //This checks if the file doesn't exist. If it doesn't exist, the error text is shown on terminal.
        if (! file.exists())
        System.out.println("[ ERROR ] : Unable to locate file: The specified file cannot be read, found or loaded.");

        //This checks if the filename points to a directory
        else if (file.isDirectory())
        System.out.println("[ ERROR ] : Unable to read file : The specified file name is a directory.");

        //If the file exists, the file is displayed on the terminal.
        else
        {
            //Open the method to read files and read it
            BufferedReader ob = new BufferedReader(new FileReader(file));

            //Initialize the string to be null
            String p = "";

            if(! helpMode)
            {
                //Logic to read the file line by line.
                while ((p = ob.readLine()) != null)
                System.out.println(p);
            }
            else
            {
                //Logic to read the help file.
                while ((p = ob.readLine()) != null)
                {
                    if(p.equalsIgnoreCase("<end of page>"))
                    {
                        if(console.readLine("\nPress ENTER to Continue, else type EXIT to quit help viewer.\n~DOC_HLP?> ").equalsIgnoreCase("exit"))
                            break;
                            Conch.API.BuildInfo.viewBuildInfo();
                        continue;
                    }
                    else if(p.equalsIgnoreCase("<end of help>"))
                    {
                        System.out.println("\n\nEnd of Help File.");
                        break;
                    }
                    else if(p.startsWith("#"))
                        continue;

                    System.out.println(p);
                }
            }

            //After reading the file, close the streams opened.
            ob.close();
        }
        console.readLine("Press ENTER to continue.");
    }
}