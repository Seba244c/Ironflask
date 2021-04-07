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