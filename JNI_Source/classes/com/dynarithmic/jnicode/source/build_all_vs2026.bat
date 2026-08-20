call build_vs2026-x32-nocrt-ansi.bat
if errorlevel 1 exit /b 1
call build_vs2026-x32-nocrt-unicode.bat
if errorlevel 1 exit /b 1
call build_vs2026-x64-nocrt-ansi.bat
if errorlevel 1 exit /b 1
call build_vs2026-x64-nocrt-unicode.bat
if errorlevel 1 exit /b 1


