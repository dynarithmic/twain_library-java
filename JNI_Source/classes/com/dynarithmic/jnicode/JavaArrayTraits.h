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
#ifndef JAVAARRAYTRAITS_H
#define JAVAARRAYTRAITS_H

#include <jni.h>
#include <vector>
#include <algorithm>
#ifdef USING_DTWAIN_LOADLIBRARY
    #include "dtwainx2.h"
    #ifndef API_INSTANCE
        #define API_INSTANCE DYNDTWAIN_API::
    #endif
#else
#include "dtwain.h"
#ifndef API_INSTANCE
    #define API_INSTANCE
#endif
#endif

struct JavaLongArrayTraits
{
    typedef jlongArray array_type;
    typedef jlong base_type;
    typedef LONG api_base_type;
    enum {dtwain_array_type = DTWAIN_ARRAYLONG};

    static array_type CreateJArray(JNIEnv* env, int nCount)
    { return env->NewLongArray(nCount); }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, const std::vector<base_type>& vj)
    { env->SetLongArrayRegion(ret, 0, nCount, vj.data()); }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    { return env->GetLongArrayElements(arr, nullptr); }
};

struct JavaLong64ArrayTraits
{
    typedef jlongArray array_type;
    typedef jlong base_type;
    typedef int64_t api_base_type;
    enum { dtwain_array_type = DTWAIN_ARRAYLONG64 };

    static array_type CreateJArray(JNIEnv* env, int nCount)
    {
        return env->NewLongArray(nCount);
    }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, const std::vector<base_type>& vj)
    {
        env->SetLongArrayRegion(ret, 0, nCount, vj.data());
    }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    {
        return env->GetLongArrayElements(arr, nullptr);
    }
};

struct JavaShortArrayTraits
{
    typedef jshortArray array_type;
    typedef jshort base_type;
    typedef LONG api_base_type;
    enum { dtwain_array_type = DTWAIN_ARRAYINT16 };

    static array_type CreateJArray(JNIEnv* env, int nCount)
    {
        return env->NewShortArray(nCount);
    }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, const std::vector<base_type>& vj)
    {
        env->SetShortArrayRegion(ret, 0, nCount, vj.data());
    }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    {
        return env->GetShortArrayElements(arr, nullptr);
    }
};

 struct JavaIntArrayTraits
{
    typedef jintArray array_type;
    typedef jint base_type;
    typedef LONG api_base_type;
    enum {dtwain_array_type = DTWAIN_ARRAYLONG};

    static array_type CreateJArray(JNIEnv* env, int nCount)
    { return env->NewIntArray(nCount); }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, const std::vector<base_type>& vj)
    { env->SetIntArrayRegion(ret, 0, nCount, vj.data()); }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    { return env->GetIntArrayElements(arr, nullptr); }
};

struct JavaDoubleArrayTraits
{
    typedef jdoubleArray array_type;
    typedef jdouble base_type;
    typedef double api_base_type;
    enum {dtwain_array_type = DTWAIN_ARRAYFLOAT};

    static jdoubleArray CreateJArray(JNIEnv* env, int nCount)
    { return env->NewDoubleArray(nCount); }

    static void SetJArrayRegion(JNIEnv* env, jdoubleArray ret, int nCount,
        const std::vector<jdouble>& vj)
    { env->SetDoubleArrayRegion(ret, 0, nCount, vj.data()); }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    { return env->GetDoubleArrayElements(arr, nullptr); }
};

template <typename cchar_type = char>
struct JavaByteArrayTraits
{
    typedef jbyteArray array_type;
    typedef jbyte base_type;
    typedef cchar_type api_base_type;
    enum {dtwain_array_type = DTWAIN_ARRAYLONG};

    static array_type CreateJArray(JNIEnv* env, int nCount)
    { return env->NewByteArray(nCount); }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, const std::vector<base_type>& vj)
    { env->SetByteArrayRegion(ret, 0, nCount, vj.data()); }

    static void SetJArrayRegion(JNIEnv* env, array_type ret, int nCount, base_type* vj)
    { env->SetByteArrayRegion(ret, 0, nCount, vj); }

    static base_type* GetJArrayElements(JNIEnv *env, array_type arr)
    { return env->GetByteArrayElements(arr, nullptr); }
};

template <typename JavaTraits>
DTWAIN_ARRAY CreateDTWAINArrayFromJArray(JNIEnv *env, typename JavaTraits::array_type arr)
{
    jsize nCount = env->GetArrayLength(arr);
    const DTWAIN_ARRAY dArray = API_INSTANCE DTWAIN_ArrayCreate(JavaTraits::dtwain_array_type, static_cast<LONG>(nCount));
    if (dArray)
    {
        typename JavaTraits::base_type* pElement = JavaTraits::GetJArrayElements(env, arr);
        typename JavaTraits::api_base_type *pDBuffer =
            static_cast<typename JavaTraits::api_base_type*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dArray, 0));
        std::copy(pElement, pElement + nCount, pDBuffer);
        return dArray;
    }
    return nullptr;
}

template <typename JavaTraits>
typename JavaTraits::array_type CreateJArrayFromDTWAINArray(JNIEnv* env, DTWAIN_ARRAY theArray,
                                                            int numEntriesWhenNull = 0,
                                                            bool bCreateAll=true)
{
    typename JavaTraits::array_type ret;
    if ( bCreateAll && theArray )
    {
        LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(theArray);
        std::vector<typename JavaTraits::base_type> vj(nCount);
        typename JavaTraits::api_base_type* pBuf =
            static_cast<typename JavaTraits::api_base_type*>(API_INSTANCE DTWAIN_ArrayGetBuffer(theArray, 0));
        std::copy(pBuf, pBuf + nCount, vj.begin());
        ret = static_cast<typename JavaTraits::array_type>(JavaTraits::CreateJArray(env, nCount));
        JavaTraits::SetJArrayRegion(env, ret, nCount, vj);
    }
    else
        ret = static_cast<typename JavaTraits::array_type>(JavaTraits::CreateJArray(env, numEntriesWhenNull));
    return ret;
}

template <typename JavaTraits>
typename JavaTraits::array_type CreateJArrayFromCArray(JNIEnv* env,
                                                       typename JavaTraits::api_base_type* theArray,
                                                       unsigned long numElements,
                                                       int numEntriesWhenNull = 0,
                                                       bool bCreateAll=true)
{
    typename JavaTraits::array_type ret;
    if ( bCreateAll && theArray )
    {
        ret = static_cast<typename JavaTraits::array_type>(JavaTraits::CreateJArray(env, numElements));
        JavaTraits::SetJArrayRegion(env, ret, numElements, reinterpret_cast<typename JavaTraits::base_type*>(theArray));
    }
    else
        ret = static_cast<typename JavaTraits::array_type>(JavaTraits::CreateJArray(env, numEntriesWhenNull));
    return ret;
}

template <typename JavaTraits>
std::vector<typename JavaTraits::api_base_type>
CreateCArrayFromJArray(JNIEnv *env, typename JavaTraits::array_type arr)
{
    jsize nCount = env->GetArrayLength(arr);
    std::vector<typename JavaTraits::api_base_type> dArray( nCount );
    typename JavaTraits::base_type* pElement = JavaTraits::GetJArrayElements(env, arr);
    if ( nCount > 0 )
    {
        typename JavaTraits::api_base_type *pDBuffer = static_cast<typename JavaTraits::api_base_type*>(dArray.data());
        std::copy(pElement, pElement + nCount, pDBuffer);
    }
    return dArray;
}

jstring CreateJStringFromCString(JNIEnv *env, LPCTSTR str);
jstring CreateJStringFromCStringA(JNIEnv *env, LPCSTR str);
jstring CreateJStringFromCStringA(JNIEnv *env, LPCWSTR str);
jstring CreateJStringFromCStringW(JNIEnv *env, LPCSTR str);
jstring CreateJStringFromCStringW(JNIEnv *env, LPCWSTR str);
jclass GetJavaClassID(JNIEnv* env, const char* javaClass);
jmethodID GetJavaClassConstructor(JNIEnv *env, const char* javaClass, const char* constructorSig);
jobjectArray CreateJStringArrayFromDTWAINArray(JNIEnv *env, DTWAIN_ARRAY arr);
DTWAIN_ARRAY CreateDTWAINArrayFromJStringArray(JNIEnv *env, jobjectArray strArray);
DTWAIN_ARRAY CreateDTWAINArrayFromJIntArray(JNIEnv *env, jintArray arr);
DTWAIN_ARRAY CreateDTWAINArrayFromJFrameArray(JNIEnv *env, jobjectArray frameArray);
jobjectArray CreateJFrameArrayFromDTWAINArray(JNIEnv *env, DTWAIN_ARRAY arr);
#endif
