/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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
#ifndef DTWAINJNI_VERSION_H
#define DTWAINJNI_VERSION_H

#define DTWAINJNI_MAJOR_VERSION                1
#define DTWAINJNI_SUBVERSION_VERSION           0
#define DTWAINJNI_PLACEHOLDER_VERSION          6

#define DTWAINJNI_STRINGER_2_(x) #x
#define DTWAINJNI_STRINGER_(x) DTWAINJNI_STRINGER_2_(x)

#define DTWAINJNI_VERINFO_BASEVERSION          DTWAINJNI_STRINGER_(DTWAINJNI_MAJOR_VERSION) "." DTWAINJNI_STRINGER_(DTWAINJNI_SUBVERSION_VERSION)
#define DTWAINJNI_VERINFO_FILEVERSION          DTWAINJNI_VERINFO_BASEVERSION "." DTWAINJNI_STRINGER_(DTWAINJNI_PLACEHOLDER_VERSION)

#define DTWAINJNI_BUILDVERSION_TOP 0
#define DTWAINJNI_BUILDVERSION_MIDDLE 0
#define DTWAINJNI_BUILDVERSION_BOTTOM 1
#define DTWAINJNI_BUILDVERSION DTWAINJNI_STRINGER_(DTWAINJNI_BUILDVERSION_TOP) DTWAINJNI_STRINGER_(DTWAINJNI_BUILDVERSION_MIDDLE) DTWAINJNI_STRINGER_(DTWAINJNI_BUILDVERSION_BOTTOM)

#ifdef _MSC_VER
#pragma message ( "Compiling with DTWAIN-JNI Version " DTWAINJNI_VERINFO_FILEVERSION " Build " DTWAINJNI_BUILDVERSION)
#endif

#endif

