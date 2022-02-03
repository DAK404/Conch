package Conch.API.FileFlex;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import Conch.API.PrintStreams;

public class FlexLogic 
{
    ///////////////////////////////////////////////////////////////
    // STRINGS TO DISPLAY INFORMATION ON TERMINAL
    ///////////////////////////////////////////////////////////////

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
    [E] : COMMAND NOT FOUND.
    """;

    private final String FILE_EXISTS = """
    [E] : A file/directory with the same name already exists.
    """;

    // private final String <reserved for future use> = """
    // """;

    private String _username = "";
    private String _name = "";
    private String _currentDirectory = "";

    private char _fileSeparator = File.separatorChar;

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
            case "":
                break;

            case "cp":
            case "copy":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: copy <source> <destination>":"Copying File...");
                break;
            
            case "mv":
            case "move":
                System.out.println((cmd.length)<3?ARGUMENT_MISMATCH + "Syntax: move <source> <destination>":"Moving File...");
                break;

            case "dir":
            case "ls":
                listFiles();
                break;

            case "exit":
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
        try
        {
            if(fileName.equals(".."))
            {
                prevDir();
                return;
            }

            fileName = _currentDirectory + fileName + "/";
            if(checkFile(fileName))
                _currentDirectory=fileName;
            else
                System.out.println(ARGUMENT_PATH_INVALID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void resetToHomeDir()throws Exception
    {
        String homeDir = "./Users/Conch/"+_username+"/";
        _currentDirectory = homeDir;
    }

    private void prevDir()throws Exception
    {
        try
        {
            _currentDirectory = _currentDirectory.substring(0, _currentDirectory.length() - 1);
            _currentDirectory = _currentDirectory.replace(_currentDirectory.substring(_currentDirectory.lastIndexOf('/'), _currentDirectory.length()), "/");

            if(_currentDirectory.equals("./Users/Truncheon/"))
            {
                System.out.println("[W] : Permission Denied.");
                resetToHomeDir();
            }
            System.gc();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    private final void copyMove(boolean move, String sourceFile, String destinationFile)throws Exception
    {
        try
        {
            if(checkFile(sourceFile))
            {
                if(sourceFile.equalsIgnoreCase(destinationFile))
                    System.out.println(SOURCE_DEST_SAME);

                else
                {
                    if(checkFile(destinationFile))
                        System.out.println(DEST_FILE_EXISTS);
                    else
                        copyMoveLogic(new File(sourceFile), new File(destinationFile));
                    System.out.print(move?new File(sourceFile).delete():"");
                }
            }
            else
                System.out.println(ARGUMENT_PATH_INVALID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private final void copyMoveLogic(File src, File dest)throws Exception
    {
        try
        {
            if( src.isDirectory() )
            {
                dest.mkdirs();
                for( File sourceChild : src.listFiles() )
                {
                    File destChild = new File( dest, sourceChild.getName() );
                    copyMoveLogic( sourceChild, destChild );
                }
            }
            else
            {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);

                in.close();
                out.close();
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO DELETE A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void deleteFilesDirs(String fileName)throws Exception
    {
        try
        {
            fileName = _currentDirectory + fileName;

            if(checkFile(fileName))
            {
                File del = new File(fileName);
                if(del.isDirectory())
                    deletionLogic(del);
                else
                    del.delete();
            }
            else
                System.out.println(ARGUMENT_PATH_INVALID);
        }
        catch(Exception E)
        {

        }
    }

    private final void deletionLogic(File delfile)throws Exception
    {
        if (delfile.listFiles() != null)
        {
            for (File fn : delfile.listFiles())
            deletionLogic(fn);
        }
        delfile.delete();
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO CREATE A NEW DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void createDirectory(String fileName)throws Exception
    {
        try
        {
            fileName = _currentDirectory + fileName;
            
            if(checkFile(fileName))
                System.out.println(FILE_EXISTS);
            else
                new File(fileName).mkdir();
        }
        catch(Exception E)
        {

        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO RENAME A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void renameFilesDirs(String oldFileName, String newFileName)throws Exception
    {
        try
        {
            oldFileName = _currentDirectory + oldFileName;
            newFileName = _currentDirectory + newFileName;

            if(checkFile(oldFileName))
            {
                if(checkFile(newFileName))
                    System.out.println(DEST_FILE_EXISTS);
                else
                {
                    File oldFile = new File(oldFileName);
                    File newFile = new File(newFileName);
                    oldFile.renameTo(newFile);
                }
            }
            else
                System.out.println(ARGUMENT_PATH_INVALID);
        }
        catch(Exception E)
        {

        }
    }
}
