cd ..
call compile_libs.cmd
cd ender
call mvn clean install
call Xcopy /E /I .\resources\ .\target\resources\
cd target
RMDIR /S /Q classes
RMDIR /S /Q generated-sources
RMDIR /S /Q maven-archiver
RMDIR /S /Q maven-status
RMDIR /S /Q test-classes
RMDIR /S /Q classes
pause