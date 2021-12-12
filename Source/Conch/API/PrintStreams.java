package Conch.API;

public class PrintStreams
{
    private static String _error = "[    ERROR    ] : ";
    private static String _warning = "[   WARNING   ] : ";
    private static String _attention = "[  ATTENTION  ] : ";
    private static String _info = "[ INFORMATION ] : ";

    public static void printError(String _message)
    {
        System.out.println(_error + _message);        
    }

    public static void printWarning(String _message)
    {
        System.out.println(_warning + _message);
    }

    public static void printCritical(String _message)
    {
        System.out.println(_attention + _message);
    }

    public static void printInfo(String _message)
    {
        System.out.println(_info + _message);
    }

    public static void printMsg(String _message)
    {
        System.out.println(_message);
    }
}
