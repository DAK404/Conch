package Conch.API.Oyster;

//Import the required Java IO classes
import java.io.Console;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

public class PolicyEnforce
{
    public final boolean checkPolicy(String Policy)throws Exception
    {
        //Initialize the policy value as false by default
        boolean stat = false;

        Console console = System.console();
        try
        {
            //Feeds the retrievePolicyValue() with the policy to be checked
            switch(retrievePolicyValue(Policy).toLowerCase())
            {
                //If the policy value returned is false, print an error statement about the policy value
                case "off":
                Conch.API.PrintStreams.printAttention("This module access is denied due to the policy configuration.\nPlease contact the Administrator for more information.");
                console.readLine();
                break;

                //Set the stat value as true if the policy value returned is true
                case "on":
                stat = true;
                break;

                //When a policy is not found or cannot be loaded, display an error message about the misconfigured policy
                case "error":
                Conch.API.PrintStreams.printWarning("Module Policy is not configured. Please contact the system administrator to initialize the policy.");
                console.readLine();
                break;

                //Handle any other inputs returned after checking the policy
                default:
                Conch.API.PrintStreams.printError("POLICY CONFIGURATION ERROR!");
                console.readLine();
                break;
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
        return stat;
    }

    public final String retrievePolicyValue(String policyParameter)throws Exception
    {
        //Initialize the policy value as an empty string
        String policyValue = "";
        try
        {
            //Open the properties streams
            Properties prop = new Properties();
            String propsFileName="./System/Conch/Private/Policy.burn";

            //Load the file stream containing the program properties
            FileInputStream configStream = new FileInputStream(propsFileName);

            //Load the properties from an XML formatted file
            prop.loadFromXML(configStream);

            //Get the property value specified in the file
            policyValue = prop.getProperty(policyParameter);

            //Close the streams
            configStream.close();
        }
        catch(Exception E)
        {
            //Set the string value to "error" if the given property is not found, unreadable or is misconfigured
            policyValue = "ERROR";
        }

        //return the policy value in the string format
        return policyValue;
    }
}