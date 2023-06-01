/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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
#ifndef UTFCHARSHANDLER_H
#define UTFCHARSHANDLER_H

#include "jni.h"
#include <string>
#include "StringDefs.h"
#include <windows.h>
struct ANSICharTraits
{
    typedef char CharType;
    typedef char NativeType;
    static void ReleaseJavaChars(JNIEnv* pEnv, const jstring& jstr, const CharType* ptr)
            { pEnv->ReleaseStringUTFChars(jstr, ptr); }

    static const CharType *GetStringChars(JNIEnv* pEnv, const jstring& jstr)
    { return pEnv->GetStringUTFChars(jstr, nullptr); }

    static const NativeType *CharTypeToNative(const CharType* ptr)
    {
        return ptr;
    }

    static jstring GetNewJString(JNIEnv *pEnv, const CharType* str)
    { return pEnv->NewStringUTF(str); }
};

struct UnicodeCharTraits
{
    typedef jchar CharType;
    typedef wchar_t NativeType;
    static void ReleaseJavaChars(JNIEnv* pEnv, const jstring& jstr, const CharType* ptr)
            { pEnv->ReleaseStringChars(jstr, ptr); }

    static const CharType *GetStringChars(JNIEnv* pEnv, const jstring& jstr)
    {
        return pEnv->GetStringChars(jstr, nullptr);
    }

    static const NativeType *CharTypeToNative(const CharType* ptr)
    {
        return reinterpret_cast<const NativeType*>(ptr);
    }

    static jstring GetNewJString(JNIEnv *pEnv, const CharType* str)
    {
        const std::basic_string<jchar> strT(str);
        return pEnv->NewString(str, strT.size());
    }
};


template <typename CharTraits = ANSICharTraits>
struct GetStringCharsHandlerImpl
{
    const typename CharTraits::CharType* m_ptr;
    typedef typename CharTraits::CharType char_type;
    typedef typename CharTraits::NativeType native_type;
    JNIEnv *m_pEnv;
    jstring m_jString;

    GetStringCharsHandlerImpl(JNIEnv *pEnv, jstring jstr) :
    m_ptr(nullptr), m_pEnv(pEnv), m_jString(jstr) { }

    GetStringCharsHandlerImpl() : m_ptr(nullptr), m_pEnv(nullptr), m_jString(nullptr)
    {
    }

    void setEnvironment(JNIEnv *pEnv)
    {
        m_pEnv = pEnv;
    }

    void SetJString(jstring jstr)
    {
        m_jString = jstr;
    }

    jstring GetNewJString(const typename CharTraits::CharType* s)
    { return CharTraits::GetNewJString(m_pEnv, s); }

    ~GetStringCharsHandlerImpl()
    {
        if (m_ptr)
            CharTraits::ReleaseJavaChars(m_pEnv, m_jString, m_ptr);
    }

    const typename CharTraits::CharType *GetStringChars()
    {
        m_ptr = CharTraits::GetStringChars(m_pEnv, m_jString);
        return m_ptr;
    }

    LPCTSTR GetWindowsStringChars()
    {
        m_ptr = CharTraits::GetStringChars(m_pEnv, m_jString);
        return reinterpret_cast<LPCTSTR>(m_ptr);
    }

    const native_type *GetStringCharsNative()
    {
        m_ptr = CharTraits::GetStringChars(m_pEnv, m_jString);
        return CharTraits::CharTypeToNative(m_ptr);
    }
};

#ifdef UNICODE
    typedef GetStringCharsHandlerImpl<UnicodeCharTraits> GetStringCharsHandler;
#else
    typedef GetStringCharsHandlerImpl<> GetStringCharsHandler;
#endif
#endif