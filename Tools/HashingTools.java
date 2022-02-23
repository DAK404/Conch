import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;

public class HashingTools 
{
    private static int fileCount = 0;
    private static List<String> filePaths = new  ArrayList<String>();

    static
    {
        System.out.println("Conch Hashing Tool");
        System.out.println("DEBUG BUILD v0.2.5\n");
    }

    public static void main(String[] Args)
    {
        try
        {
            new File("./.Manifest/Conch").mkdirs();
            new HashingTools().enumerateFiles(new File("./"));
            new HashingTools().hashFiles();
        }
        catch(Exception e)
        {

        }
    }

    private void enumerateFiles(File directory)
    {
        try
        {
            File[] filesList = directory.listFiles();
            for (File f: filesList)
            {
                if(ignoreFiles(f.getName()))
                    continue;
                    
                if (f.isDirectory())
                {
                    if(f.getName().equals(".Manifest"))
                        continue;
                    else
                    {
                        fileCount = fileCount + f.list().length;
                        enumerateFiles(f);
                    }
                }
                if (f.isFile()) 
                {
                    String a = f.getPath();
                    filePaths.add(a);
                }
            }
        }
        catch(Exception e)
        {

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

    private void hashFiles()
    {
        try
        {
            Properties props = new Properties();
            FileOutputStream output = new FileOutputStream("./.Manifest/Conch/Manifest.m1");
            System.out.println(filePaths);

            for(String fileName: filePaths)
            {
                String temp = System.getProperty("os.name").contains("Linux")?fileName.replaceAll(File.separator, "\\\\"):fileName;
                System.out.println(temp);
                props.setProperty(temp, fileToMD5(fileName));
            }

            props.storeToXML(output, "FileManifest");
            output.close();
            System.gc();            
        }
        catch(Exception e)
        {

        }
    }

    private final String fileToMD5(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "MD5");
    }

    private final String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++)
        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        return stringBuffer.toString();
    }

    private final String hashFile(File file, String algorithm)throws Exception
    {
        if(file.exists())
        {
            try (FileInputStream inputStream = new FileInputStream(file))
            {
                MessageDigest digest = MessageDigest.getInstance(algorithm);

                byte[] bytesBuffer = new byte[1024];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(bytesBuffer)) != -1)
                {
                    digest.update(bytesBuffer, 0, bytesRead);
                }

                byte[] hashedBytes = digest.digest();

                return convertByteArrayToHexString(hashedBytes);
            }
            catch (NoSuchAlgorithmException E)
            {
                System.out.println("Unsupported Algorithm.\n\n");
                E.printStackTrace();
            }
        }
        return null;
    }
}
