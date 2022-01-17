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

    public static void main(String[] Args)
    {
        try
        {
            new File("./.Manifest").mkdir();
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
                    if(f.getName().equals("HashingTools.java"))
                        continue;
                    String a = f.getPath();
                    filePaths.add(a);
                }
            }
        }
        catch(Exception e)
        {

        }
    }

    private void hashFiles()
    {
        try
        {
            System.out.println(File.separator);
            String a = File.separator;
            Properties props = new Properties();
            FileOutputStream output = new FileOutputStream("./.Manifest/Manifest.m1");
            for(String fileName: filePaths)
            {
                //Receive the file name, hash it and compare it with the file list hashes
                props.setProperty(fileName.replaceAll(a, Matcher.quoteReplacement("\\")), fileToMD5(fileName));
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
