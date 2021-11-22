package Conch.API;

/**
 * A program which can be used to handle exceptions.
 * The same can be logged into a log file for debugging.
 * 
 * @author Deepak Anil Kumar (@DAK404)
 * @version 1.0
 * @since 0.0.1
 */
public class ExHandler
{
    public void handleException(Exception e)throws Exception
    {
        String message = """
            The program has stopped executing due to an internal exception.
            The execution cannot continue. Any unsaved data will be lost. 
            Please send the error report to the developer.
            The following details describe the exception:""";
        System.out.println(message);
        e.printStackTrace();
        System.in.read();
    }
}
