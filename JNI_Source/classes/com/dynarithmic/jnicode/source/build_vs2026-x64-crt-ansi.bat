@echo off
setlocal

echo Using:
cmake --version
echo.

cmake --preset vs2026-x64-crt-ansi
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-crt-ansi-release -- /m
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-crt-ansi-debug -- /m
if errorlevel 1 exit /b 1

endlocal
