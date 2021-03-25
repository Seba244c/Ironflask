cd ..
call compile_libs.cmd
cd sandbox
call mvn clean install
call Xcopy /E /I .\resources\ .\target\resources\
pause