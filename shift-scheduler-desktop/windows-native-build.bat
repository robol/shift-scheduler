set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_31
set ANT=C:\Program Files (x86)\apache-ant-1.9.4\bin\ant
set RES_HACK=C:\Program Files (x86)\Resource Hacker\ResHacker.exe
set DEST_DIR=dist\bundles\ShiftScheduler
set PYTHON=C:\Python27\bin\python.exe

%ANT%" -Djar.archive.disabled=true -Dnative.bundling.type=image build-native
copy glpk_4_52.dll "%DEST_DIR%\app"
copy glpk_4_52_java.dll "%DEST_DIR%\app"
copy msvcr100.dll "%DEST_DIR%\"
copy msvcp100.dll "%DEST_DIR%\"