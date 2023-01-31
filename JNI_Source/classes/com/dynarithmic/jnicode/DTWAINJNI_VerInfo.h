/*
This file is part of the Dynarithmic TWAIN Library (DTWAIN).
Copyright (c) 2002-2022 Dynarithmic Software.

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
#ifndef DTWAINJNI_VERINFO_H
#define DTWAINJNI_VERINFO_H

#include "DTWAINJNI_Version.h"

#ifdef _DEBUG
#define DTWAINJNI_DEVELOP_SUFFIX "d"
#else
#define DTWAINJNI_DEVELOP_SUFFIX ""
#endif

#ifdef _WIN32
#ifdef UNICODE
#define UNICODEJNI_VERSION "(Unicode)"
#define UNICODEJNI_SUFFIX  "u"
#else
#define UNICODEJNI_VERSION
#define UNICODEJNI_SUFFIX ""
#endif

#if defined (WIN64) || (_WIN64)
#define DTWAINJNI_DLLNAME      "dtwainjni64" UNICODE_SUFFIX DTWAIN_DEVELOP_SUFFIX ".dll"
#define DTWAINJNI_FILEDESCRIPTION  "dtwainjni64" UNICODE_SUFFIX DTWAIN_DEVELOP_SUFFIX
#define DTWAINJNI_OSPLATFORM "64"
#else
#define DTWAINJNI_DLLNAME      "dtwainjni32" UNICODEJNI_SUFFIX DTWAINJNI_DEVELOP_SUFFIX ".dll"
#define DTWAINJNI_FILEDESCRIPTION  "dtwainjni32" UNICODEJNI_SUFFIX DTWAINJNI_DEVELOP_SUFFIX
#define DTWAINJNI_OSPLATFORM "32"
#endif
#endif

#ifndef _WIN32
#if defined (UNIX64)
#define DTWAINJNI_DLLNAME      "libdtwainjni64" DTWAINJNI_DEVELOP_SUFFIX ".a"
#define DTWAINJNI_FILEDESCRIPTION  "libdtwainjni64" DTWAINJNI_DEVELOP_SUFFIX
#define DTWAINJNI_OSPLATFORM "64"
#else
#define DTWAINJNI_DLLNAME      "libdtwainjni32" DTWAINJNI_DEVELOP_SUFFIX ".a"
#define DTWAINJNI_FILEDESCRIPTION  "libdtwainjni32" DTWAINJNI_DEVELOP_SUFFIX
#define DTWAINJNI_OSPLATFORM "32"
#endif
#endif


#define DTWAINJNI_VERINFO_COMMENTS             "Patch Level " DTWAINJNI_VERINFO_PATCHLEVEL_VERSION "\0"
#define DTWAINJNI_VERINFO_COMPANYNAME          "Dynarithmic Software\0"
#define DTWAINJNI_VERINFO_LEGALCOPYRIGHT       "Copyright © 2020-2022\0"
#define DTWAINJNI_VERINFO_PRODUCTNAME          "Dynarithmic Software Twain Library JNI Layer" UNICODEJNI_VERSION "\0"
#define DTWAINJNI_VERINFO_INTERNALNAME         DTWAINJNI_DLLNAME
#define DTWAINJNI_VERINFO_ORIGINALFILENAME     DTWAINJNI_DLLNAME
#define DTWAINJNI_VERINFO_LEGALTRADEMARKS      "Dynarithmic Software\0"
#define DTWAINJNI_VERINFO_FILEDESCRIPTION      DTWAINJNI_FILEDESCRIPTION
#define DTWAINJNI_VERINFO_SPECIALBUILD         "Open Source Distribution (Special) Version\0"
#define DTWAINJNI_VERINFO_PRIVATEBUILD         "Open Source Distribution (Special) Version\0"

#define DTWAINJNI_NUMERIC_FILE_VERSION         DTWAINJNI_MAJOR_VERSION,DTWAINJNI_SUBVERSION_VERSION,DTWAINJNI_PLACEHOLDER_VERSION,DTWAINJNI_VERINFO_PATCHLEVEL_VERSION
#define DTWAINJNI_NUMERIC_PRODUCT_VERSION      DTWAINJNI_NUMERIC_FILE_VERSION

#define DTWAINJNI_VERINFO_PRODUCTVERSION       DTWAINJNI_VERINFO_SPECIALBUILD
#endif

