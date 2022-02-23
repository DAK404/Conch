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
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

//Import the required Java Util classes
import java.util.Date;

//Import the required Java Text Formatting classes
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class FileWrite
{
    public final void logToFile(String PrintToFile, String fileName)
    {
        try
        {
            if(checkFileValidity(fileName))
            {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                File logFile = new File("./Logs");
                if(! logFile.exists())
                    logFile.mkdir();

                BufferedWriter obj = new BufferedWriter(new FileWriter("./Logs" + fileName + ".log", true));
                PrintWriter pr = new PrintWriter(obj);
                pr.println(dateFormat.format(date) + ": " + PrintToFile);
                pr.close();
                obj.close();

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    /**
    *
    *
    * @param fn
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private boolean checkFileValidity(String fn)throws Exception
    {
        if(fn == null || fn.equals("") || fn.startsWith(" "))
        {
            System.out.println("Please enter a valid file name to open.");
            return false;
        }
        return true;
    }

    /**
    *
    * @param fileName
    * @param dir
    */
    public final void editFile(String fileName, String dir, String usn)
    {
        try
        {
            if(! new Conch.API.Oyster.PolicyEnforce().checkPolicy("write") & ! new Conch.API.Coral.LoginAuth(usn).checkPrivilegeLogic())
            {
                Conch.API.PrintStreams.printError("Policy Enforcement System -> Cannot access module due to the configuration.\nContact the Administrator for more information.");
                return;
            }
            // if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("write"))
            // return;

            if(checkFileValidity(fileName))
            {
                boolean appendFile = true;
                String message = "";

                System.out.println("Conch Text Editor 1.5");
                System.out.println("_____________________\n");

                Console console=System.console();

                File writeToFile = new File(dir + fileName);
                System.out.println("\nEditing File : " + fileName + "\n\n");

                if(writeToFile.exists())
                {
                    switch(console.readLine("[ ATTENTION ] : A file with the same name has been found in this directory. Do you want to OVERWRITE it, APPEND to the file, or GO BACK? \n\nOptions:\n[ OVERWRITE | APPEND | RETURN | HELP ]\n\n> ").toLowerCase())
                    {
                        case "overwrite":

                        appendFile = false;
                        System.out.println("The new content will overwrite the previous content present in the file!");
                        break;

                        case "append":

                        System.out.println("The new content will be added to the end of the file! Previous data will remain unchanged.");
                        break;

                        case "return":

                        return;

                        case "help":

                        System.out.println("Work in Progress");
                        break;

                        default:

                        System.out.println("Invalid choice. Exiting...");
                        return;
                    }
                }

                BufferedWriter obj = new BufferedWriter(new FileWriter(writeToFile, appendFile));
                PrintWriter pr = new PrintWriter(obj);

                do
                {
                    pr.println(message);
                    message = console.readLine();
                }
                while(!(message.equalsIgnoreCase("<exit>")));

                pr.close();
                obj.close();
                System.gc();
            }
        }
        catch(Exception e)
        {
            //Handle any exceptions thrown during runtime
            e.printStackTrace();
        }
    }
}
