cd ..
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