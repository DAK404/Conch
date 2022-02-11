/*
* ███    ██ ██  ██████  ███    ██             ██████  ██████  ███    ██  ██████ ██   ██ 
* ████   ██ ██ ██    ██ ████   ██     ██     ██      ██    ██ ████   ██ ██      ██   ██ 
* ██ ██  ██ ██ ██    ██ ██ ██  ██            ██      ██    ██ ██ ██  ██ ██      ███████ 
* ██  ██ ██ ██ ██    ██ ██  ██ ██     ██     ██      ██    ██ ██  ██ ██ ██      ██   ██ 
* ██   ████ ██  ██████  ██   ████             ██████  ██████  ██   ████  ██████ ██   ██ 
*/

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

public class Main
{
    private static final String errorHeader = """
    ********************************************************
    PROGRAM ENVIRONMENT LOADER V2.44.3
    ********************************************************
    
    """;

    private static final String errorFooter = """
    
    ********************************************************
    """;
    
    private static final String INVALID_SYNTAX = """
    [ WARNING ] : INVALID USE OF LAUNCHER SYNTAX!
    TO BOOT THE PROGRAM, PLEASE USE THE FOLLOWING SYNTAX:
    
    java <arguments> Main <kernel_name> <boot_mode>
    
    FOR ANY ADDITIONAL INFORMATION REQUIRED, PLEASE REFER
    TO THE README FILE OR THE SOURCE CODE DOCUMENTATION.
    
    Press ENTER to Exit.""";
    
    private static final String KERNEL_NOT_FOUND = """
    [ ERROR ] : THE SPECIFIED KERNEL CANNOT BE FOUND OR LOADED.
    PLEASE CHECK THE KERNEL NAME OR SPECIFY AN EXISTING KERNEL TO BE LOADED.
    \nPress ENTER to Exit.""";
    
    private static final String UNDEFINED_BOOTMODE = """
    [ ATTENTION ] : THE SPECIFIED BOOT MODE IS NOT DEFINED.
    PLEASE ENTER A VALID BOOT MODE TO LOAD THE PROGRAM.
    \nPress ENTER to Exit.""";
    
    private static final String FATAL_ERROR_EXIT = """
    [ ERROR ] : TO RECOVER FROM FATAL ERRORS, THE PROGRAM NEEDS TO EXIT.
    \nPress ENTER to Exit.""";
    
    private static final String FATAL_ERROR_RESTART = """
    [ ERROR ] : RESTARTING THE PROGRAM TO RECOVER FROM ERROR.""";
    
    private static final String RESTART_UPDATE = """
    [ ATTENTION ] : THE PROGRAM IS UPDATING. PLEASE WAIT...""";
    
    private static final String AUTOMATIC_REPAIR_MODE = """
    [ WARNING ] : KERNEL FILES ARE CORRUPT! RESTARTING IN REPAIR MODE TO FIX ISSUES.""";
    
    public static void main(String[] args)throws Exception
    {
        ///////////////////////////////////////////////////////////////
        // DEBUG CODE! REMOVE BEFORE RELEASE!
        ///////////////////////////////////////////////////////////////
        if(args.length == 0)
        {
            System.out.println(INVALID_SYNTAX + "\n");
            System.exit(0);
        }
        
        if(args[0].equalsIgnoreCase("debug"))
        {
            System.out.println("PRINTING ALL STRINGS AVAILABLE");
            
            System.out.println(INVALID_SYNTAX + "\n");
            System.out.println(KERNEL_NOT_FOUND + "\n");
            System.out.println(UNDEFINED_BOOTMODE + "\n");
            System.out.println(FATAL_ERROR_EXIT + "\n");
            System.out.println(FATAL_ERROR_RESTART + "\n");
            System.out.println(RESTART_UPDATE + "\n");
            System.out.println(AUTOMATIC_REPAIR_MODE + "\n");
            System.exit(100);
        }
        
        ///////////////////////////////////////////////////////////////
        // DEBUG CODE! REMOVE BEFORE RELEASE!
        ///////////////////////////////////////////////////////////////
        
        if(args.length < 2)
        {
            System.err.println(errorHeader + INVALID_SYNTAX + errorFooter);
            System.in.read();
            System.exit(600);
        }
        
        while(true)
        {
            System.gc();
            ProcessBuilder sessionMonitor=new ProcessBuilder("java", args[0]+".Core.Loader", args[1]);
            Process processMonitor = sessionMonitor.inheritIO().start();
            
            processMonitor.waitFor();
            
            switch(processMonitor.exitValue())
            {            
                case 0:
                System.exit(0);
                
                case 1:
                System.err.println(errorHeader + KERNEL_NOT_FOUND + errorFooter);
                System.in.read();
                System.exit(1);
                
                // KERNEL RELATED EXIT CASES //
                
                case 0x1A0000:
                System.err.println(errorHeader + UNDEFINED_BOOTMODE + errorFooter);
                System.in.read();
                System.exit(2);
                
                case 0x1A0001:
                System.out.println("Restarting...");
                break;
                
                case 0x1A0002:
                System.err.println(errorHeader + RESTART_UPDATE + errorFooter);
                args[1] = "update";
                System.exit(4);
                
                /* Reserved for future implementations
                case 0x1A0004:
                break;
                */
                
                // KERNEL RELATED ERROR EXIT CASES //
                
                case 0x1A0100:
                System.err.println(errorHeader + FATAL_ERROR_EXIT + errorFooter);
                System.in.read();
                System.exit(3);
                
                case 0x1A0102:
                System.err.println(errorHeader + FATAL_ERROR_RESTART + errorFooter);
                System.in.read();
                break;
                
                case 0x1A0104:
                System.err.println(errorHeader + AUTOMATIC_REPAIR_MODE + errorFooter);
                args[1] = "maintenance";
                break;
                
                default:
                System.out.println("Default Unknown Exit");
                System.exit(100001);
            }
        }
    }
}
