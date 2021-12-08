package Conch.API;

import java.util.*;
import java.io.*;

public final class PolicyEnforce
{
    Console console=System.console();
    private boolean stat=false;

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