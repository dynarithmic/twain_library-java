@echo off
setlocal

echo Using:
cmake --version
echo.

cmake --preset vs2026-x64-nocrt-ansi
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-nocrt-ansi-release -- /m
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-nocrt-ansi-debug -- /m
if errorlevel 1 exit /b 1

endlocal
