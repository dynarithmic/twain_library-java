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
#ifndef DTWAINRAII_H
#define DTWAINRAII_H

#include <windows.h>

#ifdef USING_DTWAIN_LOADLIBRARY
    #include "dtwainx2.h"
    #define API_INSTANCE DYNDTWAIN_API::
#else
    #include "dtwain.h"
    #define API_INSTANCE
#endif

template <typename T, typename DestroyTraits>
struct DTWAIN_RAII
{
    T m_a;
    DTWAIN_RAII(T a) : m_a(a) {}
    DTWAIN_RAII(const DTWAIN_RAII&) = delete;
    DTWAIN_RAII& operator=(const DTWAIN_RAII&) = delete;
    DTWAIN_RAII(DTWAIN_RAII&& rhs) : m_a(rhs.m_a) { rhs.m_a = {} };
    DTWAIN_RAII& operator=(DTWAIN_RAII&& rhs)
    {
        if (&rhs != this)
        {
            m_a = rhs.m_a;
            rhs.m_a = {};
        }
        return *this;
    }

    void SetObject(T a) { m_a = a; }
    void Disconnect() { m_a = 0 ; }
    virtual ~DTWAIN_RAII() { DestroyTraits::Destroy(m_a); }
};

struct HANDLE_DestroyTraits
{
    static void Destroy(HANDLE a)
    {
        if (a)
            GlobalUnlock(a);
    }
};

struct HandleRAII : public DTWAIN_RAII<HANDLE, HANDLE_DestroyTraits>
{
    LPBYTE m_pByte;
    HandleRAII(HANDLE h) : DTWAIN_RAII(h), m_pByte((LPBYTE)GlobalLock(h)) {}
    LPBYTE getData() { return m_pByte; }
};


struct DTWAINArray_DestroyTraits
{
    static void Destroy(DTWAIN_ARRAY a)
    {
        if (a)
            API_INSTANCE DTWAIN_ArrayDestroy(a);
    }
};

struct DTWAINArrayPtr_DestroyTraits
{
    static void Destroy(DTWAIN_ARRAY* a)
    {
        if (a && *a)
            API_INSTANCE DTWAIN_ArrayDestroy(*a);
    }
};

struct DTWAINFrame_DestroyTraits
{
    static void Destroy(DTWAIN_FRAME f)
    {
        if (f)
            API_INSTANCE DTWAIN_FrameDestroy(f);
    }
};

// RAII Class for DTWAIN_ARRAY
typedef DTWAIN_RAII<DTWAIN_ARRAY, DTWAINArray_DestroyTraits> DTWAINArray_RAII;
typedef DTWAIN_RAII<DTWAIN_ARRAY*, DTWAINArrayPtr_DestroyTraits> DTWAINArrayPtr_RAII;
typedef DTWAIN_RAII<DTWAIN_FRAME, DTWAINFrame_DestroyTraits> DTWAINFrame_RAII;

#endif