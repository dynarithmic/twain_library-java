/*
This file is part of the Twainsave-opensource version
Copyright (c) 2002-2026 Dynarithmic Software.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
OF THIRD PARTY RIGHTS.
*/
#include "get_dll_version.h"

#include <windows.h>
#include <string>

static std::wstring MakeVersionString(DWORD ms, DWORD ls)
{
    return std::to_wstring(HIWORD(ms)) + L"." +
        std::to_wstring(LOWORD(ms)) + L"." +
        std::to_wstring(HIWORD(ls)) + L"." +
        std::to_wstring(LOWORD(ls));
}

static std::string MakeVersionStringA(DWORD ms, DWORD ls)
{
    return std::to_string(HIWORD(ms)) + "." +
        std::to_string(LOWORD(ms)) + "." +
        std::to_string(HIWORD(ls)) + "." +
        std::to_string(LOWORD(ls));
}

bool GetDLLVersionNumbers(HMODULE hModule, VersionNumbers& out)
{
    if (!hModule)
        return false;

    std::wstring modulePath(32768, L'\0');

    DWORD len = GetModuleFileNameW(hModule, modulePath.data(), static_cast<DWORD>(modulePath.size()));

    if (len == 0 || len >= modulePath.size())
        return false;

    modulePath.resize(len);

    DWORD dummy = 0;
    DWORD size = GetFileVersionInfoSizeW(modulePath.c_str(), &dummy);

    if (size == 0)
        return false;

    std::wstring versionData;
    versionData.resize((size + sizeof(wchar_t) - 1) / sizeof(wchar_t));

    if (!GetFileVersionInfoW(modulePath.c_str(), 0, size, versionData.data()))
    {
        return false;
    }

    VS_FIXEDFILEINFO* fixedInfo = nullptr;
    UINT fixedInfoSize = 0;

    if (!VerQueryValueW(versionData.data(), L"\\", reinterpret_cast<LPVOID*>(&fixedInfo), &fixedInfoSize))
    {
        return false;
    }

    if (!fixedInfo || fixedInfoSize < sizeof(VS_FIXEDFILEINFO))
        return false;

    if (fixedInfo->dwSignature != 0xFEEF04BD)
        return false;

    out.FileVersion = MakeVersionString(fixedInfo->dwFileVersionMS, fixedInfo->dwFileVersionLS);
    out.ProductVersion = MakeVersionString(fixedInfo->dwProductVersionMS, fixedInfo->dwProductVersionLS);
    out.FileVersionA = MakeVersionStringA(fixedInfo->dwFileVersionMS, fixedInfo->dwFileVersionLS);
    out.ProductVersionA = MakeVersionStringA(fixedInfo->dwProductVersionMS, fixedInfo->dwProductVersionLS);
    return true;
}