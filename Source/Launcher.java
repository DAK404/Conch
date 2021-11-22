public class Launcher 
{

    private static String KERNEL_NOT_FOUND = """
    [ ERROR ] : THE SPECIFIED KERNEL CANNOT BE FOUND OR LOADED.
    PLEASE CHECK THE KERNEL NAME OR SPECIFY AN EXISTING KERNEL TO BE LOADED.
    \nPress Enter to Exit.
    """;

    private static String UNDEFINED_BOOTMODE = """
    [ ATTENTION ] : THE SPECIFIED BOOT MODE IS NOT DEFINED.
    PLEASE ENTER A VALID BOOT MODE TO LOAD THE MODULES.
    \nPress Enter to Exit.""";

    private static String FATAL_ERROR_EXIT = """
    [ ERROR ] : TO RECOVER FROM FATAL ERRORS, THE PROGRAM NEEDS TO EXIT.
    \nPress Enter to Exit.""";

    public static void main(String[] args)throws Exception
    {
        ProcessBuilder sessionMonitor=new ProcessBuilder("java", args[0]+".Core.Boot", args[1]);
        Process processMonitor= sessionMonitor.inheritIO().start();
        processMonitor.waitFor();

        switch(processMonitor.exitValue())
        {
            case 0:
                System.exit(0);

            case 1:
                System.err.println(KERNEL_NOT_FOUND);
                System.in.read();
                System.exit(1);
            
            default:
                System.exit(100001);

        }
    }
}
