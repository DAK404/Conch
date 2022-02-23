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

package Conch.Core;

//Import the required Java IO classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Import the required Java NIO classes
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//Import the required Java Util classes
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.net.URL;

//Import the required Conch classes
import Conch.API.BuildInfo;
import Conch.API.PrintStreams;

public class Loader
{
    static
    {
        BuildInfo.viewBuildInfo();
    }

    private boolean _repair = false;

    private Console console = System.console();

    //collect the list of file paths in the Conch environment
    private static List<String> filePaths = new  ArrayList<String>();

    //Main logic of the loader program
    public static void main(String[] args)throws Exception
    {
        System.gc();
        switch(args[0].toLowerCase())
        {
            case "normal":
                break;

            case "repair":
                new Loader().repairMode();                
                System.exit(0xAFF014);
                
            default:
                System.exit(0x1A0000);
        }

        if(!new Loader().systemAsserts())
            new Conch.Core.Setup().setupLogic();
        
        if(!new Loader().integrityCheck())
            System.exit(0x1A0104);

        new Loader().debugShell();
    }

    private void repairMode()
    {
        try
        {
            _repair = true;
            System.out.println("Booted Shell to Repair Mode.");
            
            while(true)
                repairModeShell(console.readLine("!REPAIR@CONCH> "));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void repairModeShell(String cmd)
    {
        try
        {
            String[] splitCmd = cmd.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            switch(splitCmd[0].toLowerCase())
            {
                case "exit":
                    System.exit(0);
                    break;

                case "scan":
                    System.out.println("Scanning Files...");
                    checkFiles();
                    break;

                case "restore":
                    System.out.println("Deleting Binaries...");
                    deleteExistingBinaries(new File("./Conch"));

                    if(downloadBuildFromCloud())
                        if(installBuild())
                            System.out.println("Restore Complete.");
                        else
                            System.out.println("Restore Failed.");
                    else
                        System.out.println("Download Failed.");

                    break;

                default:
                    System.out.println("Invalid Command.");
                    break;
            }
        }
        catch(Exception E)
        {

        }
    }

    private boolean downloadBuildFromCloud()throws Exception
    {
        System.out.println("Downloading Build From Cloud...");
        return downloadUsingNIO("https://gitreleases.dev/gh/DAK404/Conch/latest/Conch.zip", "Update.zip");
    }

    private final boolean downloadUsingNIO(String urlStr, String file) throws Exception
    {
        try
        {
            URL website = new URL(urlStr);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            System.gc();
            return true;
        }
        catch (Exception E)
        {
            return false;
        }
    }

    private final void deleteExistingBinaries(File delFile)throws Exception
    {
        if (delFile.listFiles() != null)
        {
            for (File fn : delFile.listFiles())
                deleteExistingBinaries(fn);
        }
        delFile.delete();
    }

    private final boolean installBuild()
    {
        System.out.println("Installing...");
        boolean status = false;
        try
        {
            String _currentDirectory = System.getProperty("user.dir");
            status = unzipper("./Update.zip", _currentDirectory);
        }
        catch(Exception e)
        {
            status = false;
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
                System.out.println("Installed : " + newFile.getAbsoluteFile());
                
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

    private void debugShell()throws Exception
    {
        try
        {
            PrintStreams.printInfo("Shell loaded successfully!");
            while(true)
                debugShellCmdProcessor(console.readLine("Guest@ProgramLoader :: X > "));
        }
        catch(Exception e)
        {
            System.exit(0x1A0104);
        }
    }

    private void debugShellCmdProcessor(String cmd)throws Exception
    {
        String[] splitCmd = cmd.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        switch(splitCmd[0].toLowerCase())
        {           
            case "login":
                new Conch.Core.ConchLogic().startConch();
                break;

            case "doc":
                PrintStreams.printInfo("Help Document Viewer 1.0: WORK IN PROGRESS...");
                break;

            case "clear":
                BuildInfo.viewBuildInfo();
                break;

            case "exit":
                System.exit(0);

            case "restart":
                System.exit(0x1A0001);

            case "":
                break;

            default:
                PrintStreams.printAttention("Undefined Command: " + splitCmd[0]);
                break;
        }
    }

    //A method which handles the logics and sublogics to verify if the program integrity is true.
    private boolean integrityCheck()throws Exception
    {
        boolean status = false;
        
        //list all the files in the Conch directories
        //Load the hash filelist. If the file list is not found, download the latest build by default

        //Begin checking the files
        status = checkFiles();
        PrintStreams.printAttention("KERNEL INTEGRITY: " + (status?"OK":"FAILED"));

        return status;
    }

    private final boolean systemAsserts()throws Exception
    {
        boolean status = true;
        try
        {
            String [] fileList = {"./.Manifest/Conch/Manifest.m1", "./System/Conch", "./System/Conch/Public", "./System/Conch/Private", "./Users/Conch"};
            for(String asserts : fileList)
            {
                if(! new File(asserts).exists())
                {
                    status = false;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }

    //Logic to check the file with the hashes present in the filelist
    private boolean checkFiles()
    {
        listAllFiles(new File("./"));

        boolean fileCheckStatus = false;
        String fileHash = "";

        try
        {
            System.out.println("Checking Kernel Integrity...\n");

            //check if the manifest file exists
            if(!new File("./.Manifest/Conch").exists() | !new File("./.Manifest/Conch/Manifest.m1").exists())
            {
                PrintStreams.printError("MANIFEST FILE PARSING ERROR: 0x00A101");
                System.out.println("The Manifest file is either corrupt, malformed, missing or cannot be loaded.");
                System.out.println("Running the program in repair mode should help in fixing the issue.\n");
                return false;
            }

            Properties props = new Properties();
            FileInputStream configStream = new FileInputStream("./.Manifest/Conch/Manifest.m1");
            props.loadFromXML(configStream);
            configStream.close();

            //props.list(System.out);


            // use File.separator to get the correct file separator for the current OS
            for(String fileNames: filePaths)
            {
                fileHash = new Conch.API.Scorpion.Cryptography().fileToMD5(fileNames);
                try
                {
                    String manifestHash = (System.getProperty("os.name").contains("Linux")?props.get(fileNames.replaceAll(File.separator, "\\\\")):props.get(fileNames)).toString();
                    if(manifestHash.equalsIgnoreCase(fileHash))
                    {
                        fileCheckStatus = true;
                        continue;
                    }
                    else
                    {
                        fileCheckStatus = false;
                        PrintStreams.printError("Failure at Integrity Check: " + fileNames);
                        PrintStreams.printError("File Hash Output: " + fileHash);
                        break;
                    }
                }
                catch(NullPointerException hashingException)
                {
                    PrintStreams.printAttention("Unknown File Found : " + fileNames);
                    PrintStreams.printAttention("Unknown File Hash  : " + fileHash);
                }
            }
        }
        catch(Exception e)
        {
            fileCheckStatus = false;
            e.printStackTrace();
        }
        System.gc();
        return fileCheckStatus;
    }

    //Method to list all the files in the Conch environment and store them in a List
    private void listAllFiles(File directory)
    {
        try
        {
            File[] filesList = directory.listFiles();
            for (File f: filesList)
            {
                //Dont bother checking the manifest folder content hashes
                if(!_repair)
                    if(ignoreFiles(f.getName()))
                        continue;
                
                if (f.isDirectory())
                    listAllFiles(f);

                if (f.isFile())
                {
                    String a = f.getPath();
                    filePaths.add(a);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
    }

    private boolean ignoreFiles(String fileName)
    {
        boolean status = false;
        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "BootShell.cmd"};
        for(String files : ignoreList)
        {
            if(fileName.equalsIgnoreCase(files))
            {
                status = true;
                break;
            }
        }
        return status;
    }
}
