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

package Conch.API;

public class PrintStreams
{
    public static void printInfo(String message)
    {
        System.out.println((char)27 + "[32m[ INFORMATION ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printError(String message)
    {
        System.err.println((char)27 + "[31m[    ERROR    ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printWarning(String message)
    {
        System.err.println((char)27 + "[33m[   WARNING   ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printAttention(String message)
    {
        System.out.println((char)27 + "[36m[  ATTENTION  ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void println(String message)
    {
        System.out.println(message);
        message = null;
    }
}
