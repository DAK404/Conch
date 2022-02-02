package Conch.API.FileFlex;

import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import Conch.API.PrintStreams;

public class FlexLogic 
{
    private final String ARGUMENT_MISMATCH = """
    [E] : Invalid argument length.
    """;
    private final String ARGUMENT_PATH_INVALID = """
    [A] : The source/destination path/file does not exist.
    """;
    private final String DEST_FILE_EXISTS = """
    [A] : A file with the same name exists in the destination path.
    """;

    private final String SOURCE_DEST_SAME = """
    [A] : The source and destination paths are the same.
    """;

    private final String INVALID_COMMAND = """
    [E] : COMMMAND NOT FOUND.
    """;

    // private final String <reserved for future use> = """
    // """;

    private String _username = "";
    private String _name = "";
    private String _currentDirectory = "";

    private Console console = System.console();

    public void fileFlex()throws Exception
    {
        String temp;
        do
        {
            temp = console.readLine();
            fileFlexLogic(temp);
        }while(!temp.equalsIgnoreCase("exit"));
    }

    private void fileFlexLogic(String input)throws Exception
    {
        String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        switch(cmd[0])
        {
            case "cp":
            case "copy":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: copy <source> <destination>":"Copying File...");
                break;
            
            case "mv":
            case "move":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: move <source> <destination>":"Moving File...");
                break;

            default:
                System.out.println(INVALID_COMMAND);
                break;
        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO NAVIGATE THE USER HOME DIRECTORY
    ///////////////////////////////////////////////////////////////

    private void changeDir(String fileName)throws Exception
    {

    }

    private void resetToHomeDir()throws Exception
    {

    }

    private void prevDir()throws Exception
    {

    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO CHECK FOR A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final boolean checkFile(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO PRINT THE DIRECTORY IN A TREE FORMAT
    ///////////////////////////////////////////////////////////////

    private final void treeViewer()throws Exception
    {
        try
        {
            File tree=new File(_currentDirectory);
            System.out.println("\n--- [ TREE VIEW ] ---\n");
            treeViewerLogic(0, tree);
            System.out.println();
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private final void treeViewerLogic(int indent, File fileName)throws Exception
    {
        try
        {
            System.out.print('|');

            for (int i = 0; i < indent; ++i)
                System.out.print('=');

            System.out.println(fileName.getName().replace(_username, _name + " [ USER ROOT DIRECTORY ]"));

            if (fileName.isDirectory())
            {
                File[] files = fileName.listFiles();

                for (int i = 0; i < files.length; ++i)
                treeViewerLogic(indent + 2, files[i]);
            }
        }
        catch(Exception e)
        {
            PrintStreams.printError(e.toString());
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO LIST ALL THE FILES IN A DIRECTORY
    ///////////////////////////////////////////////////////////////

    public void listFiles()throws Exception
    {
        String format = "%1$-32s| %2$-24s| %3$-10s\n";
        String c = "-";
        if(checkFile(_currentDirectory))
        {
            File dPath=new File(_currentDirectory);
            System.out.println("\n");
            String disp = (String.format(format, "Directory/File Name", "File Size [In KB]","Type"));
            System.out.println(disp + c.repeat(disp.length()) + "\n");
            for(File file : dPath.listFiles())
            {
                //System.out.format(String.format(format, file.getPath().replace(User,Name), file.getName().replace(User,Name), file.length()/1024+" KB"));
                System.out.format(String.format(format, file.getName().replace(_username, _name), file.length()/1024+" KB", file.isDirectory()?"Directory":"File"));
            }
            System.out.println();
        }
        else
        System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO COPY/MOVE A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void copyMoveFile(boolean move, String sourceFile, String destinationFile)throws Exception
    {
        try
        {
            if(checkFile(sourceFile))
            {
                if(sourceFile.equalsIgnoreCase(destinationFile))
                {
                    System.out.println();

                }
                else
                {
                    if(checkFile(destinationFile))
                        System.out.println(DEST_FILE_EXISTS);
                }
            }
            else
            {

            }

        }
        catch(Exception E)
        {

        }
    }
}
