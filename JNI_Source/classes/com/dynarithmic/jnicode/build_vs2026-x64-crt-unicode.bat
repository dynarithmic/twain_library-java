@echo off
setlocal

echo Using:
cmake --version
echo.

cmake --preset vs2026-x64-crt-unicode
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-crt-unicode-release -- /m
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x64-crt-unicode-debug -- /m
if errorlevel 1 exit /b 1

endlocal
