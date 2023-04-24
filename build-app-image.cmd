rem 附加的内存参数能将运行时内存使用量降到 140M 左右
%JDK_20%\bin\jpackage.exe --name hydrogen-pac ^
  --input target\dist ^
  --main-jar hydrogen-pac-0.0.1-SNAPSHOT.jar ^
  --type app-image ^
  --dest target^
  --icon .\assets\windows\hydrogen-pac.ico^
  --java-options "-XX:MaxMetaspaceSize=40m -XX:ReservedCodeCacheSize=15m -Xmx30m -XX:CompressedClassSpaceSize=7m -XX:NewSize=3m -XX:MaxNewSize=8m"
