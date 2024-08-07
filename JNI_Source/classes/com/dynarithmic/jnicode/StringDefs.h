/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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
#ifndef STRINGDEFS_H
#define STRINGDEFS_H

#include <jni.h>
#include <string>
#include <locale>
#include <codecvt>
#include <algorithm>
#include <cctype>
#pragma warning (disable:4244)
#ifdef UNICODE
    typedef std::basic_string<jchar> StringType;
    typedef std::wstring NativeStringType;
    #define EmptyString_ L""
#else
    typedef std::string StringType;
    typedef std::string NativeStringType;
    #define EmptyString_ ""
#endif
    template <typename Iter1, typename Iter2>
    void copy_until_null_check(Iter1 it1, Iter1 it2, Iter2 destIter)
    {
        while ((it1 != it2) && *it1)
        {
            *destIter = *it1;
            ++destIter;
            ++it1;
        }
    }

    template <typename Iter1, typename Iter2>
    void copy_until_null(Iter1 it1, Iter2 destIter)
    {
        while (*it1)
        {
            *destIter = *it1;
            ++destIter;
            ++it1;
        }
    }

struct stringjniutils
{
    static bool isAllBlank(const std::string& s)
    {
        return s.empty() || std::all_of(s.begin(), s.end(), [&](unsigned char ch){ return std::isspace(ch); });
    }

    static bool isAllBlank(const std::wstring& s)
    {
        return s.empty() || std::all_of(s.begin(), s.end(), [&](unsigned int ch) { return std::isspace(ch); });
    }

    using convert_t = std::codecvt_utf8<wchar_t>;

    static std::string to_string(const std::wstring& wstr)
    {
        static std::wstring_convert<convert_t, wchar_t> strconverter;
        return strconverter.to_bytes(wstr);
    }

    static std::string& to_string(std::string& str)
    {
        return str;
    }

    static std::wstring to_wstring(const std::string& str)
    {
        static std::wstring_convert<convert_t, wchar_t> strconverter;
        return strconverter.from_bytes(str);
    }

    static std::wstring& to_wstring(std::wstring& str)
    {
        return str;
    }
};
#endif
