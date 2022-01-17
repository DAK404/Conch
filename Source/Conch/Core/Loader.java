package Conch.Core;

//Import the Java classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

//Import the Conch classes
import Conch.API.BuildInfo;
import Conch.API.PrintStreams;

public class Loader 
{
    Console console = System.console();

    //generic string to print the status of the program on screen
    //private String _status = "Failed";

    //collect the list of file paths in the Conch environment
    private static List<String> filePaths = new  ArrayList<String>();

    //a block to handle the code to check if the file integrity is matching with the filelist
    static
    {
        try
        {
            //Run the file checking logic here before running the boot args check
            //BuildInfo.clearScreen();
            BuildInfo.viewBuildInfo();
            if(! new Loader().integrityCheck())
            {
                //pass the control to the setup program
            }
        }
        catch(Exception e)
        {

        }
    }

    //Main logic of the loader program
    public static void main(String[] args)throws Exception
    {
        switch(args[0].toLowerCase())
        {
            case "debug_ps":
                new Loader().debugPS();
                break;
            
            case "normal":
                break;

            default:
                System.exit(0x1A0000);
        }
        new Loader().debugShell();
    }

    private void debugShell()
    {
        PrintStreams.printInfo("Shell loaded successfully!");
        while(true)
            debugShellCmdProcessor(console.readLine("??? ~DBG_CONCH > "));
    }

    private void debugShellCmdProcessor(String cmd)
    {
        String[] splitCmd = cmd.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        switch(splitCmd[0].toLowerCase())
        {
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

    private void debugPS()
    {
        BuildInfo.viewBuildInfo();

        System.out.println("TESTING ALL CONCH PRINTSTREAMS");
        System.out.println("------------------------------\n");

        PrintStreams.printInfo("INFORMATION PRINTSTREAM TEST");
        PrintStreams.printAttention("ATTENTION PRINTSTREAM TEST");
        PrintStreams.printWarning("WARNING PRINTSTREAM TEST");
        PrintStreams.printError("ERROR PRINTSTREAM TEST");
        PrintStreams.println("NORMAL PRINTSTREAM TEST");
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

    //Logic to check the file with the hashes present in the filelist
    private boolean checkFiles()
    {
        listAllFiles(new File("./"));

        boolean fileCheckStatus = false;
        String fileHash = "";

        try
        {
            System.out.println("Checking Kernel Integrity...");

            //check if the manifest file exists
            if(!new File("./.Manifest").exists() || !new File("./.Manifest/Manifest.m1").exists())
            {
                PrintStreams.printError("MANIFEST FILE PARSING ERROR: 0x00A101");
                System.out.println("The Manifest file is either corrupt, malformed, missing or cannot be loaded.");
                System.out.println("Running the program in repair mode should help in fixing the issue.\n");
                return false;
            }

            Properties props = new Properties();
            FileInputStream configStream = new FileInputStream("./.Manifest/Manifest.m1");
            props.loadFromXML(configStream);
            configStream.close();

            //props.list(System.out);


            // use File.separator to get the correct file separator for the current OS
            for(String fileNames: filePaths)
            {
                String manifestHash = (System.getProperty("os.name").contains("Linux")?props.get(fileNames.replaceAll(File.separator, "\\\\")):props.get(fileNames)).toString();
                fileHash = new Conch.API.Scorpion.Cryptography().fileToMD5(fileNames);

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
                if(f.getName().equals(".Manifest"))
                    continue;
                if (f.isDirectory())
                    listAllFiles(f);

                if (f.isFile()) 
                {
                    ///////////////////////////////////////////////////////////////
                    // DEBUG CODE! REMOVE BEFORE RELEASE!
                    ///////////////////////////////////////////////////////////////
                    
                    if(f.getName().equals("HashingTools.java"))
                        continue;

                    ///////////////////////////////////////////////////////////////
                    // DEBUG CODE! REMOVE BEFORE RELEASE!
                    ///////////////////////////////////////////////////////////////

                    else
                    {
                        String a = f.getPath();
                        filePaths.add(a);
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
        }
    }
}
