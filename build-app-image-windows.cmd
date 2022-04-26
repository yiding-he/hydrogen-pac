REM run this script after `maven package` command
COPY /Y target\*.jar target\lib\
RMDIR /S /Q target\hydrogen-pac

REM add '--win-console' parameter for debugging purpose
"%JDK_17_HOME%\bin\jpackage.exe" -n hydrogen-pac ^
  --dest .\target ^
  --main-class com.hyd.hydrogenpac.HydrogenPacMain ^
  --main-jar hydrogen-pac-0.0.1-SNAPSHOT.jar ^
  --type app-image ^
  --icon .\assets\windows\hydrogen-pac.ico ^
  --input .\target\lib

target\hydrogen-pac\hydrogen-pac.exe
