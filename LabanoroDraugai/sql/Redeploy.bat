@ECHO OFF
REM ! config.bat faile pasikeičiam reikšmes ir uždedam gitignore
REM ! db skriptus dedam į "Scripts/", editinam tik "Scripts/Redeploy.sql"

REM get mysql login information
CALL config.bat

REM Call main script file
CALL %MYSQL_EXE% -f --user=%DB_USER% --password=%DB_PWD% < Scripts/_Redeploy.sql
IF %ERRORLEVEL% NEQ 0 ECHO Error executing SQL file

ECHO End of batch. 
PAUSE