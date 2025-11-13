@echo off
echo Kompilerar Dragon Treasure...
cd /d "%~dp0"
javac -encoding UTF-8 *.java
if %errorlevel% == 0 (
    echo.
    echo Kompileringen lyckades!
    echo.
    echo Du kan nu köra spelet med run.bat eller:
    echo java ltu.fksyg.d0019n.DragonTreasure
    echo.
) else (
    echo.
    echo Kompileringen misslyckades!
    echo.
)
pause
