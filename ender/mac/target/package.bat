for /r %%x in (*windows.jar) do del "%%x"
for /r %%x in (*linux.jar) do del "%%x"

del ender-mac.zip
del ender-mac.zip.tmp
del bin/imgui.ini
call 7z a -tzip ender-mac.zip bin
timeout 5
call 7z a -tzip ender-mac.zip libs
timeout 5
call 7z a -tzip ender-mac.zip jdk-11.0.8