cd ../progaurd
call proguard.bat @proguard.pro
copy /Y /v ender-0.0.1-SNAPSHOT-pro.jar ..\4j\target\bin\ender-0.0.1-SNAPSHOT-pro.jar
copy /Y /v ender-0.0.1-SNAPSHOT-pro.jar ..\mac\target\bin\ender-0.0.1-SNAPSHOT-pro.jar