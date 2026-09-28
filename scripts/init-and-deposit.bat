@echo off
rem Creates a piggybank for an owner and deposits 20 euros three times with a withdrawal of 10 euros after the second deposit, through the Spring Shell CLI adapter.
rem Usage: scripts\init-and-deposit.bat [owner-uuid]   (a random owner is used when none is given)
rem Needs the database from infrastructure\docker-compose.yml to be running.
setlocal

cd /d "%~dp0.."

set "OWNER=%~1"
if "%OWNER%"=="" (
    for /f %%i in ('powershell -NoProfile -Command "[guid]::NewGuid().ToString()"') do set "OWNER=%%i"
)
set "JAR=target\piggybank-modulith-0.0.1-SNAPSHOT.jar"

if not exist "%JAR%" (
    call mvnw.cmd -q -DskipTests package || exit /b 1
)

set "COMMANDS=%TEMP%\piggybank-commands-%RANDOM%.txt"
(
    echo init --owner %OWNER%
    echo deposit --owner %OWNER% --amount 20
    echo deposit --owner %OWNER% --amount 20
    echo withdraw --owner %OWNER% --amount 10
    echo deposit --owner %OWNER% --amount 20
) > "%COMMANDS%"

echo Piggybank owner: %OWNER%
java -jar "%JAR%" "@%COMMANDS%" --spring.shell.interactive.enabled=false --spring.main.web-application-type=none
set "EXIT_CODE=%ERRORLEVEL%"

del "%COMMANDS%"
exit /b %EXIT_CODE%