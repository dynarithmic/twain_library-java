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
#ifndef GET_DLL_VERSION_H
#define GET_DLL_VERSION_H

#include <windows.h>
#include <string>
#include <vector>

struct VersionNumbers
{
    std::wstring FileVersion;
    std::wstring ProductVersion;
    std::wstring FileVersionRequired;
    std::wstring DLLName;
    std::string FileVersionA;
    std::string ProductVersionA;
};

bool GetDLLVersionNumbers(HMODULE hModule, VersionNumbers& out);
#endif