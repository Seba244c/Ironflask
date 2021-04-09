for /r %%x in (*macos.jar) do del "%%x"
for /r %%x in (*linux.jar) do del "%%x"

del editor-windows.zip
del editor-windows.zip.tmp
del bin/imgui.ini
call 7z a -tzip editor-windows.zip bin
timeout 5
call 7z a -tzip editor-windows.zip libs
timeout 5
call 7z a -tzip editor-windows.zip jdk-11.0.8