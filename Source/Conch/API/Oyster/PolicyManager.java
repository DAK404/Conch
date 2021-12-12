package Conch.API.Oyster;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;


public final class PolicyManager
{
    private final String FileName="./System/Private/Settings/Settings.burn";
    private String pName="";
    private String pValue="";
    private File file=null;

    Properties props = null;
    Console console=System.console();
    private boolean stat=false;

    public PolicyManager() throws Exception
    {
        file=new File(FileName);
        if(file.exists())
            resetInterface();               
    }

    protected void Menu()
    {
        try
        {
            while(true)
            {
                //File is loaded every iteration
                props=new Properties();
                DisplaySettings();
                switch(console.readLine("\nWhat do you want to do?\n\n[ Modify | Reset | Help | Exit ]\n>> ").toLowerCase())
                {
                    case "modify":
                                    Modify();
                                    break;

                    case "reset":
                                    resetInterface();
                                    console.readLine("Press enter to return to Main Menu.");
                                    break;

                    case "help": //TODO  - Change Help file location
                                    new Conch.API.Fish.ReadFile().ShowHelp("Help/PolicyEnforcement.manual");
                                    break;

                    case "":
                                    break;
                    case "exit":
                                    return;

                    default:
                                    console.readLine("Unrecognized command.\nPlease enter a valid option.");
                                    break;
                }
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
            console.readLine();
        }
    }

    protected void resetInterface()throws Exception
    {
        try
        {
            props=new Properties();
            FileOutputStream o = new FileOutputStream(FileName);
            file=new File(FileName);
            if(file.exists()==true)
                file.delete();

            String [] Settings =
            {
                "update",
                "download",
                "editor",
                "chat",
                "filemanager",
                "no_debug"
            };
            for(int i=0; i<Settings.length;++i)
            {
                props.setProperty(Settings[i], "on");
            }
            props.storeToXML(o, "GlobalSettings");
            o.close();
            System.out.println("[ SYSTEM ] > Default Program Settings Saved Successfully!");
            return;
        }
        catch(Exception E)
        {
            System.out.println("Error resetting policy file.");
            E.printStackTrace();
        }
    }

    private void Modify()
    {
        try
        {

            do
            {
                DisplaySettings();
                System.out.println("Which Policy do you want to modify?\n[ TIP : TO ADD A NEW POLICY, JUST TYPE IN THE NEW POLICY NAME AND VALUE! ]");
                pName=console.readLine("Policy Name:\n>> ").toLowerCase();
                pValue=console.readLine("Policy Value:\n>> ").toLowerCase();
                System.out.println("\nWriting policy to file. DO NOT TURN OFF THE SYSTEM!\n");
                //logic to save the file
                savePolicy();
            }
            while(console.readLine("Do you want to modify another policy? [ Y | N ]\n>> ").equalsIgnoreCase("Y"));
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private void DisplaySettings()throws Exception
    {
        Conch.API.BuildInfo.viewBuildInfo();
        System.gc();
        System.out.println("          Conch Policy Editor 1.3\n");
        System.out.println("--------------------------------------------");
        System.out.println("      - Current Policy Configuration -      ");
        System.out.println("--------------------------------------------");
        System.out.println("\nPolicy File  : "+FileName);
        System.out.println("Policy Format: XML\n");
        FileInputStream configStream = new FileInputStream(FileName);
        props.loadFromXML(configStream);
        configStream.close();
        props.list(System.out);
        System.out.println("\n--------------------------------------------\n");
        return;
    }

    private void savePolicy()throws Exception
    {
        try
        {
            props.setProperty(pName, pValue);
            FileOutputStream output = new FileOutputStream(FileName);
            props.storeToXML(output, "GlobalSettings");
            output.close();
            System.out.println("Policy "+pName+" was saved successfully.");
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
            console.readLine("[ ERROR ] : Policy could not be saved.");
            return;
        }
    }

    public boolean checkPolicy(String policyName)throws Exception
    {
        try
        {
            System.gc();
            Properties prop = new Properties();
            String propsFileName="./System/Private/Settings/Settings.burn";
            FileInputStream configStream = new FileInputStream(propsFileName);
            prop.loadFromXML(configStream);
            if(prop.getProperty(policyName).equalsIgnoreCase("off"))
                console.readLine("The Administrator has disabled this feature. Contact the Administrator for more information.\nPress enter to continue.");
            else if(prop.getProperty(policyName).equalsIgnoreCase("on"))
                stat=true;

            configStream.close();
        }
        catch(Exception E)
        {
            console.readLine("[ ATTENTION ] : This policy or module is either not configured or has an issue with it. Contact your administrator for more info.");
            stat = false;
        }
        return stat;
    }
}