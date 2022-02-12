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
import java.io.FileOutputStream;

//Import the required Java New IO classes
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//Import the required Java Net classes
import java.net.URL;

public class FileDown
{
    public final boolean downloadFile(String URL, String fileName)throws Exception
    {
        try
        {
            // if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("download"))
            // return false;

            if(URL == null || fileName == null || URL.equalsIgnoreCase("")  || fileName.equalsIgnoreCase(""))
            {
                System.out.println("[ ERROR ] : Invalid File Name. Enter a valid file name.");
                return false;
            }

            return downloadUsingNIO(URL, fileName);
        }
        catch(Exception e)
        {
            //Handle any exceptions thrown during runtime
            e.printStackTrace();
        }
        return false;
    }

    public final boolean downloadUpdate()throws Exception
    {
        boolean status = false;
        try
        {
            System.out.println("Downloading update file from : https://gitreleases.dev/gh/DAK404/Conch/latest/Conch.zip");
            status =  downloadUsingNIO("https://gitreleases.dev/gh/DAK404/Conch/latest/Conch.zip", "Update.zip");
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            status = false;
            E.printStackTrace();
        }
        return status;
    }

    private final boolean downloadUsingNIO(String urlStr, String file) throws Exception
    {
        boolean status = false;
        try
        {
            URL website = new URL(urlStr);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            System.gc();
            status = true;
        }
        catch (Exception E)
        {
            status = false;
        }
        return status;
    }
}
