cd ..
rem LIBS
cd engine/
call mvn clean install
cd ..
cd networking/
call mvn clean install
cd ..
rem END
cd ender
call mvn clean install
call Xcopy /E /I .\resources\ .\target\resources\
call Xcopy /E /I .\resources\ .\4j\target\bin\resources\
call Xcopy /E /I .\target\libs\ .\4j\target\libs\
copy /Y /v	.\target\ender-0.0.1-SNAPSHOT.jar .\progaurd\ender-0.0.1-SNAPSHOT.jar
cd target
RMDIR /S /Q classes
RMDIR /S /Q generated-sources
RMDIR /S /Q maven-archiver
RMDIR /S /Q maven-status
RMDIR /S /Q test-classes
RMDIR /S /Q classes
cd ..
cd progaurd
call proguard.bat @proguard.pro
copy /Y /v ender-0.0.1-SNAPSHOT-pro.jar ..\4j\target\bin\ender-0.0.1-SNAPSHOT-pro.jar
pause