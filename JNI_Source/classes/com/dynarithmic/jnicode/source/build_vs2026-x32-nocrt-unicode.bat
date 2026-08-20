@echo off
setlocal

echo Using:
cmake --version
echo.

cmake --preset vs2026-x32-nocrt-unicode
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x32-nocrt-unicode-release -- /m
if errorlevel 1 exit /b 1

cmake --build --preset vs2026-x32-nocrt-unicode-debug -- /m
if errorlevel 1 exit /b 1

endlocal
