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
#include <windows.h>
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
        return s.empty() || std::all_of(s.begin(), s.end(), [&](unsigned char ch) { return std::isspace(ch); });
    }

    static bool isAllBlank(const std::wstring& s)
    {
        return s.empty() || std::all_of(s.begin(), s.end(), [&](unsigned int ch) { return std::isspace(ch); });
    }

    static std::wstring string_to_wide_string(const std::string& narrow_string)
    {
        if (narrow_string.empty())
        {
            return L"";
        }

        const auto size_needed = MultiByteToWideChar(CP_UTF8, 0, narrow_string.data(), (int)narrow_string.size(), nullptr, 0);
        if (size_needed <= 0)
        {
            throw std::runtime_error("MultiByteToWideChar() failed: " + std::to_string(size_needed));
        }

        std::wstring result(size_needed, 0);
        MultiByteToWideChar(CP_UTF8, 0, narrow_string.data(), (int)narrow_string.size(), result.data(), size_needed);
        return result;
    }

    static std::string wide_string_to_string(const std::wstring& wide_string)
    {
        if (wide_string.empty())
        {
            return "";
        }

        const auto size_needed = WideCharToMultiByte(CP_UTF8, 0, wide_string.data(), (int)wide_string.size(), nullptr, 0, nullptr, nullptr);
        if (size_needed <= 0)
        {
            throw std::runtime_error("WideCharToMultiByte() failed: " + std::to_string(size_needed));
        }

        std::string result(size_needed, 0);
        WideCharToMultiByte(CP_UTF8, 0, wide_string.data(), (int)wide_string.size(), result.data(), size_needed, nullptr, nullptr);
        return result;
    }

    static std::string to_string(const std::wstring& wstr)
    {
        return wide_string_to_string(wstr);
    }

    static std::string& to_string(std::string& str)
    {
        return str;
    }

    static std::wstring to_wstring(const std::string& str)
    {
        return string_to_wide_string(str);
    }

    static std::wstring& to_wstring(std::wstring& str)
    {
        return str;
    }
};
#endif
