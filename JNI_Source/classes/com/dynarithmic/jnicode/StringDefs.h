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
