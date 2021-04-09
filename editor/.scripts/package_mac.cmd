@echo off
cd ..\mac\target\

for /r %%x in (*windows.jar) do del "%%x"
for /r %%x in (*linux.jar) do del "%%x"

del editor-mac.zip
del editor-mac.zip.tmp
del bin/imgui.ini
call 7z a -tzip editor-mac.zip bin
timeout 5
call 7z a -tzip editor-mac.zip libs
timeout 5
call 7z a -tzip editor-mac.zip jdk-11.0.10
cd ..
cd ..
del .\.builds\editor-mac.zip
copy .\mac\target\editor-mac.zip .\.builds\editor-mac.zip