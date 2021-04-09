rem Copy jar to tmp folder
mkdir .\tmp\
mkdir .\tmp\jar\
copy /Y /v ..\progaurd\editor-0.0.1-SNAPSHOT-pro.jar .\tmp\editor-0.0.1-SNAPSHOT-pro.jar
cd .\tmp\jar\
jar xf ..\editor-0.0.1-SNAPSHOT-pro.jar

rem Replace
cd META-INF/

powershell -Command "(gc MANIFEST.MF) -replace ' libs/', ' ../libs/' | Out-File -encoding ASCII MANIFEST.MF"

rem To Jar
cd ..
del ..\eneditorder-0.0.1-SNAPSHOT-pro.jar
FORFILES /P . /C "cmd /c 7z a -tzip ..\editor-0.0.1-SNAPSHOT-pro.jar .\@file"

rem Copy to 4j and mac folder
cd ..
cd ..
copy /Y /v .\tmp\editor-0.0.1-SNAPSHOT-pro.jar ..\4j\target\bin\editor-0.0.1-SNAPSHOT-pro.jar
copy /Y /v .\tmp\editor-0.0.1-SNAPSHOT-pro.jar ..\mac\target\bin\editor-0.0.1-SNAPSHOT-pro.jar

rem Undo
rmdir .\tmp\ /s /q
