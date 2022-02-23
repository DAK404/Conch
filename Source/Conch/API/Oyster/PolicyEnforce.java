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
                Conch.API.PrintStreams.printAttention("Policy Enforcement System -> Cannot access module due to the configuration.\nContact the Administrator for more information.");
                console.readLine();
                break;

                //Set the stat value as true if the policy value returned is true
                case "on":
                stat = true;
                break;

                //When a policy is not found or cannot be loaded, display an error message about the misconfigured policy
                case "error":
                Conch.API.PrintStreams.printWarning("Policy Enforcement System -> Malformed or Misconfigured Policy detected!\nContact the Administrator for more information.");
                console.readLine();
                break;

                //Handle any other inputs returned after checking the policy
                default:
                Conch.API.PrintStreams.printError("Policy Enforcement System -> Unrecognized Policy value.\nContact the Administrator for more information.");
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