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
    [E] : COMMAND NOT FOUND
    """;

    private final String FILE_EXISTS = """
    [E] : A file/directory with the same name already exists.
    """;

    // private final String <reserved for future use> = """
    // """;

    private String _username = "";
    private String _name = "";
    private String _currentDirectory = "";

    // private char _fileSeparator = File.separatorChar;

    private Console console = System.console();

    public void fileFlex()throws Exception
    {
        login();

        String temp;
        do
        {
            String displayShell = _name + "@" + _currentDirectory.replace(_username, _name) + "> ";
            temp = console.readLine(displayShell);
            fileFlexLogic(temp);
        }while(!temp.equalsIgnoreCase("exit"));

        deletionLogic(new File("./Users"));
    }

    private final void login()throws Exception
    {
        try
        {
            _username = new Conch.API.Scorpion.Cryptography().stringToSHA3_256(console.readLine("DEBUG: ENTER USERNAME> "));
            _name = console.readLine("DEBUG: ENTER NAME> ");

            _currentDirectory = "./Users/Conch/" + _username + '/';

            new File("./Users/Conch/"+_username).mkdirs();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void fileFlexLogic(String input)throws Exception
    {
        String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        switch(cmd[0])
        {
            ///////////////////////////////////////////////////////////////
            // Logic for navigation commands
            ///////////////////////////////////////////////////////////////

            case "reset_pwd":
            resetToHomeDir();
            break;

            case "cd":
            if(cmd.length < 2)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\ncd <path>");
            }
            else
            changeDir(cmd[1]);
            break;

            ///////////////////////////////////////////////////////////////
            // Logic to view contents of directory
            ///////////////////////////////////////////////////////////////

            case "dir":
            case "ls":
            listFiles();
            break;

            case "tree":
            treeViewer();
            break;

            ///////////////////////////////////////////////////////////////
            // Logic to create new directories
            ///////////////////////////////////////////////////////////////

            case "mkdir":
            if(cmd.length < 2)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\nmkdir <directory_name>");
            }
            else
            createDirectory(cmd[1]);
            break;

            ///////////////////////////////////////////////////////////////
            // Logics for file and directory management
            ///////////////////////////////////////////////////////////////

            // --------------------------------------------------------- //
            // Logic to remove a file or directory
            // --------------------------------------------------------- //
            case "rm":
            case "del":
            if(cmd.length < 2)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\nrm <file/directory>\ndel <file/directory>");
            }
            else
            deleteFilesDirs(cmd[1]);
            break;

            // --------------------------------------------------------- //
            // Logic to copy a file or directory
            // --------------------------------------------------------- //
            case "cp":
            case "copy":
            if(cmd.length < 3)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\ncp <source> <destination>\ncopy <source> <destination>");
            }
            else
            copyMove(false, cmd[1], cmd[2]);
            break;

            // --------------------------------------------------------- //
            // Logic to move a file or directory
            // --------------------------------------------------------- //
            case "mv":
            case "move":
            if(cmd.length < 3)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\nmv <source> <destination>\nmove <source> <destination>");
            }
            else
            copyMove(true, cmd[1], cmd[2]);
            break;

            // --------------------------------------------------------- //
            // Logic to rename a file or directory
            // --------------------------------------------------------- //
            case "rename":
            if(cmd.length < 3)
            {
                System.out.println(ARGUMENT_MISMATCH);
                System.out.println("Syntax:\nrename <old_name> <new_name>");
            }
            else
            renameFilesDirs(cmd[1], cmd[2]);
            break;

            ///////////////////////////////////////////////////////////////
            // Miscellaneous logic to handle inputs
            ///////////////////////////////////////////////////////////////

            case "":
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

            if(_currentDirectory.equals("./Users/Conch/"))
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
            System.out.println("---------------------");
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
            for (int i = 0; i < indent; ++i)
            System.out.print(' ');

            System.out.println("- " + fileName.getName().replace(_username, _name + " [ USER ROOT DIRECTORY ]"));

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
        System.out.println(ARGUMENT_PATH_INVALID);
        System.gc();
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO COPY/MOVE A FILE OR DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void copyMove(boolean move, String sourceFile, String destinationFile)throws Exception
    {
        try
        {
            if(checkFile(_currentDirectory + "/" + sourceFile))
            {
                if(sourceFile.equalsIgnoreCase(destinationFile))
                System.out.println(SOURCE_DEST_SAME);

                else
                {
                    if(checkFile(destinationFile))
                    System.out.println(DEST_FILE_EXISTS);
                    else
                    copyMoveLogic(new File(_currentDirectory + sourceFile), new File(_currentDirectory + destinationFile + "/" + sourceFile));

                    if(move)new File(_currentDirectory + sourceFile).delete();
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private final void deletionLogic(File delFile)throws Exception
    {
        try
        {
            if (delFile.listFiles() != null)
            {
                for (File fn : delFile.listFiles())
                deletionLogic(fn);
            }
            delFile.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////
    // LOGIC TO CREATE A NEW DIRECTORY
    ///////////////////////////////////////////////////////////////

    private final void createDirectory(String fileName)throws Exception
    {
        try
        {
            fileName = _currentDirectory + '/' + fileName;

            if(checkFile(fileName))
            System.out.println(FILE_EXISTS);
            else
            new File(fileName).mkdir();
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
