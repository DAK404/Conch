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

package Conch.API.FileFlex.Parcel;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Conch.API.BuildInfo;
import Conch.API.PrintStreams;

public class UpdateLogic
{
    List <String> fileList;

    public final void updateProgram()
    {
        boolean status = false;
        try
        {
            Console console = System.console();
            // if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("update"))
            // return;

            String header = """
            ---------- CONCH PROGRAM UPDATER 1.0.0 ----------
            """;

            String DOWNLOAD_ERROR = """
            Failed to download the update.

            Possible Causes:
            * Limited/restricted network access, or firewall rules which prevented downloading the file.
            * The download session was interrupted by a network change.
            * The update resource has been moved to a new URL.

            Possible Solutions:
            * Try running the updater again.
            * Restart network devices and check the network configuration for Internet Access.
            * Contact the Administrator for more assistance.
            """;

            String INSTALL_ERROR = """
            Failed to install the update.

            Possible Causes:
            * The downloaded update file was unreadable, malformed, corrupt or was not able to loaded or read.
            * The update file was deleted by a 3rd party program.
            * Installation was aborted due to a system error.

            Possible Solutions:
            * Try running the updater again.
            * Disable any instrusive applications which may have interfered with the update process.
            * Manually install the update if the updater fails repeatedly.
            * Contact the Administrator for more assistance.
            """;

            BuildInfo.viewBuildInfo();
            PrintStreams.println(header);
            PrintStreams.printAttention("The updater shall download and install the latest version from the official repository.");
            PrintStreams.printInfo("Downloading Update File...");
            PrintStreams.printInfo("Source: https://gitreleases.dev/gh/DAK404/Conch/latest/Conch.zip");
            status = (downloadUpdate()?true:false);
            PrintStreams.printInfo("Download " + (status?"Successful":"Failed"));
            if(!status)
            {
                PrintStreams.printError(DOWNLOAD_ERROR);
                console.readLine();
                return;
            }
            System.out.println();
            PrintStreams.printInfo("Installing Update...");
            status = installerLogic()?true:false;
            PrintStreams.printInfo("Install " + (status?"Successful":"Failed"));
            if(!status)
            {
                PrintStreams.printError(INSTALL_ERROR);
                console.readLine();
                return;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }    

    private final boolean downloadUpdate()throws Exception
    {
        boolean status = false;
        try
        {
            status = new Conch.API.FileFlex.FileDown().downloadUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    private final boolean installerLogic()
    {
        boolean status = false;
        try
        {
            String _currentDirectory = System.getProperty("user.dir");
            status = unzipper("./Update.zip", _currentDirectory);
        }
        catch(Exception e)
        {

        }
        return status;
    }

    private final boolean unzipper(String updateFile, String targetDirectory)
    {
        boolean status = false;
        try
        {
            //Initialize a byte stream to read the update file
            byte[] buffer = new byte[1024];

            //create output directory is not exists
            File folder = new File(targetDirectory);

            //Create the directory if it does not exist
            if (!folder.exists())
            folder.mkdir();

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(updateFile));

            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while (ze != null)
            {
                if (ze.isDirectory())
                {
                    ze = zis.getNextEntry();
                    continue;
                }

                String fileName = ze.getName();
                File newFile = new File(targetDirectory + File.separator + fileName);

                if (newFile.exists())
                {
                    newFile.delete();
                    continue;
                }

                //Print the file being extracted
                PrintStreams.printInfo("Installed : " + newFile.getAbsoluteFile());
                
                //create all non exists folders
                //else you will encounter FileNotFoundException

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0)
                fos.write(buffer, 0, len);

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            //Delete the redundant update file
            new File(updateFile).delete();

            //Run the garbage collector to free up memory
            System.gc();

            status = true;
        }
        catch(Exception e)
        {
            status = false;
        }
        return status;
    }
}
