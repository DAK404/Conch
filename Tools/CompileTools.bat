@ECHO OFF
CLS
ECHO -----------------------
ECHO     CONCH TOOLS 0.6   
ECHO -----------------------
ECHO.
ECHO -REFERENCE INFORMATION-
ECHO ***********************
ECHO 000 1AC 07F 3B2 0C6 09E
ECHO 07B 8AE 63C 909 512 111
ECHO ***********************
ECHO.
ECHO Compiling Launcher...
javac -d ../Binaries Main.java
ECHO Compiling Program...
javac -d ../Binaries ./Conch/Core/Loader.java
ECHO Done.
pause