@echo off
rem LIBS
cd ..
cd engine/
call mvn clean install
cd ..
cd networking/
call mvn clean install
cd ..

rem BUILD ENDER
cd ender
call mvn clean install
cd target
RMDIR /S /Q classes
RMDIR /S /Q generated-sources
RMDIR /S /Q maven-archiver
RMDIR /S /Q maven-status
RMDIR /S /Q test-classes
RMDIR /S /Q classes
cd ..

rem Copy resources and libs
call Xcopy /E /I .\resources\ .\4j\target\bin\resources\
call Xcopy /E /I .\resources\ .\mac\target\bin\resources\

call Xcopy /E /I .\target\libs\ .\4j\target\libs\
call Xcopy /E /I .\target\libs\ .\mac\target\libs\

copy /Y /v	.\target\ender-0.0.1-SNAPSHOT.jar .\progaurd\ender-0.0.1-SNAPSHOT.jar
call Xcopy /E /I .\target\libs\ .\progaurd\libs\

rem PROGAURD
cd progaurd
call proguard.bat @proguard.pro
copy /Y /v ender-0.0.1-SNAPSHOT-pro.jar ..\4j\target\bin\ender-0.0.1-SNAPSHOT-pro.jar
copy /Y /v ender-0.0.1-SNAPSHOT-pro.jar ..\mac\target\bin\ender-0.0.1-SNAPSHOT-pro.jar

rem Windows Build
cd ../4j/target/

for /r %%x in (*macos.jar) do del "%%x"
for /r %%x in (*linux.jar) do del "%%x"

del ender-windows.zip
del ender-windows.zip.tmp
del bin/imgui.ini
call 7z a -tzip ender-windows.zip bin
timeout 5
call 7z a -tzip ender-windows.zip libs
timeout 5
call 7z a -tzip ender-windows.zip jdk-11.0.8
cd ..
cd ..
del .\.builds\ender-windows.zip
copy .\4j\target\ender-windows.zip .\.builds\ender-windows.zip

rem Mac build
cd .\mac\target\

for /r %%x in (*windows.jar) do del "%%x"
for /r %%x in (*linux.jar) do del "%%x"

del ender-mac.zip
del ender-mac.zip.tmp
del bin/imgui.ini
call 7z a -tzip ender-mac.zip bin
timeout 5
call 7z a -tzip ender-mac.zip libs
timeout 5
call 7z a -tzip ender-mac.zip jdk-11.0.10
cd ..
cd ..
del .\.builds\ender-mac.zip
copy .\mac\target\ender-mac.zip .\.builds\ender-mac.zip

pause