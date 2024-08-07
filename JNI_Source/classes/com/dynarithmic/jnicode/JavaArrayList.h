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
#ifndef JAVAARRAYLIST_H
#define JAVAARRAYLIST_H

#include <jni.h>
#include <vector>
#include <string>
#include <map>
#include <iterator>
#include <array>
#include <iostream>
#include <tchar.h>
#include "StringDefs.h"
#include "JavaArrayTraits.h"
#include "UTFCharsHandler.h"
#include "javaobjectcaller.h"

#define JavaStringSignature "Ljava/lang/String;"
#define JavaIntegerSignature "Ljava/lang/Integer;"
#define JavaDoubleSignature "Ljava/lang/Double;"
#define JAVA_ARRAY_LIST_SIGNATURE  "Ljava/util/ArrayList;"

struct ArrayStringCharTraitsA
{
    using value_type = std::string;
    using charshandler_type = GetStringCharsHandlerImpl<>;
    using newstringparam_type = char;
};

struct ArrayStringCharTraitsW
{
    using value_type = std::wstring;
    using charshandler_type = GetStringCharsHandlerImpl<UnicodeCharTraits>;
    using newstringparam_type = jchar;
};

template <typename ArrayTraits>
struct ArrayStringList
{
    using value_type = typename ArrayTraits::value_type;
    using java_type = jstring;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, java_type value)
    {
        typename ArrayTraits::charshandler_type charsHandler(pEnv, value);
        auto* pchars = charsHandler.GetStringChars();
        cont.push_back(pchars);
    }

    static jstring GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        typename ArrayTraits::charshandler_type charsHandler{};
        charsHandler.setEnvironment(pEnv);
        return charsHandler.GetNewJString(reinterpret_cast<const typename ArrayTraits::newstringparam_type*>(v.c_str()));
    }
};

//using ArrayList_TWSTR32 = ArrayLowLevelTwainList<TW_STR32, JavaDTwainLowLevel_TW_STR32>;

template <typename NativeType, typename CPPJavaType>
struct ArrayLowLevelTwainList
{
    using value_type = NativeType;
    using cpp_java_type = CPPJavaType;
    using java_type = jobject;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, jobject value)
    {
        cpp_java_type java_cpp(pEnv);
        java_cpp.setObject(value);
        auto val = java_cpp.JavaToNative();
        cont.push_back(val);
    }

    static jobject GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        cpp_java_type java_cpp(pEnv);
        return java_cpp.NativeToJava(v);
    }
};

template <typename CopyTraits>
class JavaArrayListHandler
{
    static constexpr int defConstructor = 0;
    static constexpr int addFn = 1;
    static constexpr int getFn = 2;
    static constexpr int sizeFn = 3;
    static constexpr int clearFn = 4;

    jclass m_JavaArrayList = nullptr;
    std::array<jmethodID, 5> m_JavaMethods;
    JNIEnv *m_pEnv = nullptr;

public:
    JavaArrayListHandler(JNIEnv *pEnv) : m_JavaMethods{}
    {
        if ( !Init(pEnv) )
            throw "Could not get Java environment";
    }

    bool Init(JNIEnv *pEnv)
    {
        m_JavaArrayList = static_cast<jclass>(pEnv->NewGlobalRef(pEnv->FindClass(JAVA_ARRAY_LIST_SIGNATURE)));
        m_JavaMethods[defConstructor] = pEnv->GetMethodID(m_JavaArrayList, "<init>", "(I)V");
        m_JavaMethods[addFn] = pEnv->GetMethodID(m_JavaArrayList, "add", "(Ljava/lang/Object;)Z");
        m_JavaMethods[sizeFn] = pEnv->GetMethodID(m_JavaArrayList, "size", "()I");
        m_JavaMethods[getFn] = pEnv->GetMethodID(m_JavaArrayList, "get", "(I)Ljava/lang/Object;");
        m_JavaMethods[clearFn] = pEnv->GetMethodID(m_JavaArrayList, "clear", "()V");
        m_pEnv = pEnv;
        if (m_pEnv == nullptr ||
            m_JavaArrayList == nullptr ||
            std::any_of(m_JavaMethods.begin(), m_JavaMethods.end(),
                        [](jmethodID id) { return id == nullptr; }))
            return false;
        return true;
    }

    std::vector<typename CopyTraits::value_type> JavaToNative(jobject arrayList)
    {
        jint len = m_pEnv->CallIntMethod(arrayList, m_JavaMethods[sizeFn]);
        std::vector<typename CopyTraits::value_type> result;
        result.reserve(len);
        for (jint i = 0; i < len; i++)
        {
            typename CopyTraits::java_type element =
                static_cast<typename CopyTraits::java_type>(m_pEnv->CallObjectMethod(arrayList, m_JavaMethods[getFn], i));
            CopyTraits::AddValueToCPPList(m_pEnv, result, element);
            m_pEnv->DeleteLocalRef(element);
        }
        return result;
    }

    jobject NativeToJava(std::vector<typename CopyTraits::value_type>& vItems)
    {
        jobject result = m_pEnv->NewObject(m_JavaArrayList, m_JavaMethods[defConstructor], vItems.size());
        return NativeToJava(result, vItems);
    }

    jobject NativeToJava(jobject result, std::vector<typename CopyTraits::value_type>& vItems)
    {
        m_pEnv->CallVoidMethod(result, m_JavaMethods[clearFn]);
        for (auto s : vItems)
        {
            auto element = CopyTraits::GetValueFromCPPList(m_pEnv, s);
            m_pEnv->CallBooleanMethod(result, m_JavaMethods[addFn], element);
            m_pEnv->DeleteLocalRef(element);
        }
        return result;
    }
};

#endif