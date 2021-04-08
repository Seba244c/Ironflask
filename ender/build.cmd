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
cd ender/.scripts/
call build_jar.cmd

rem PROGAURD
cd .\.scripts\
call obfuscate.cmd

rem Fix libs Build
cd ..\.scripts\
call fix_libs.cmd

rem Windows Build
call package_windows.cmd

rem Mac build
cd .\.scripts\
call package_mac.cmd

rem Final
cd .\.scripts\
call distribute.cmd

pause