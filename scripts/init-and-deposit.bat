@echo off
rem Registers the birth of a family member, creates a piggybank for them and deposits 20 euros three times with a withdrawal of 10 euros after the second deposit, through the Spring Shell CLI adapter.
rem Usage: scripts\init-and-deposit.bat [first-name] [last-name]   (Emma Peeters when none are given)
rem Needs the database from infrastructure\docker-compose.yml to be running.
setlocal

cd /d "%~dp0.."

set "FIRST_NAME=%~1"
if "%FIRST_NAME%"=="" set "FIRST_NAME=Emma"
set "LAST_NAME=%~2"
if "%LAST_NAME%"=="" set "LAST_NAME=Peeters"
for /f %%i in ('powershell -NoProfile -Command "[guid]::NewGuid().ToString()"') do set "OWNER=%%i"
for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyy-MM"') do set "MONTH=%%i"
set "JAR=target\piggybank-modulith-0.0.1-SNAPSHOT.jar"

if not exist "%JAR%" (
    call mvnw.cmd -q -DskipTests package || exit /b 1
)

set "COMMANDS=%TEMP%\piggybank-commands-%RANDOM%.txt"
(
    echo born --member %OWNER% --first-name %FIRST_NAME% --last-name %LAST_NAME%
    echo init --owner %OWNER%
    echo deposit --owner %OWNER% --amount 20
    echo deposit --owner %OWNER% --amount 20
    echo withdraw --owner %OWNER% --amount 10
    echo deposit --owner %OWNER% --amount 20
    echo grandchildren
    echo piggybanks
    echo balance --owner %OWNER%
    echo balance --owner %OWNER% --month %MONTH%
    echo exit
) > "%COMMANDS%"

echo Piggybank owner: %FIRST_NAME% %LAST_NAME% (%OWNER%)
java -jar "%JAR%" "@%COMMANDS%" --spring.shell.interactive.enabled=false --spring.main.web-application-type=none
set "EXIT_CODE=%ERRORLEVEL%"

del "%COMMANDS%"
exit /b %EXIT_CODE%