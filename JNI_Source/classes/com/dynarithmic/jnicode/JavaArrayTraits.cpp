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
#include "UTFCharsHandler.h"
#include "JavaArrayTraits.h"
#include "javaobjectcaller.h"
#include "DTWAINRAII.h"
#include <tchar.h>

jstring CreateJStringFromCString(JNIEnv *env, LPCTSTR str)
{
    GetStringCharsHandler handler;
    handler.setEnvironment(env);
    return handler.GetNewJString(reinterpret_cast<const GetStringCharsHandler::char_type*>(str));
}

jstring CreateJStringFromCStringA(JNIEnv *env, LPCSTR str)
{
    GetStringCharsHandlerImpl<> handler;
    handler.setEnvironment(env);
    return handler.GetNewJString(reinterpret_cast<const GetStringCharsHandlerImpl<>::char_type*>(str));
}

jstring CreateJStringFromCStringA(JNIEnv *env, LPCWSTR str)
{
    GetStringCharsHandlerImpl<> handler;
    handler.setEnvironment(env);
    std::wstring sW = str;
    const std::string sWCopy(sW.begin(), sW.end());
    return handler.GetNewJString(reinterpret_cast<const GetStringCharsHandlerImpl<>::char_type*>(sWCopy.c_str()));
}

jstring CreateJStringFromCStringW(JNIEnv *env, LPCWSTR str)
{
    GetStringCharsHandlerImpl<UnicodeCharTraits> handler;
    handler.setEnvironment(env);
    return handler.GetNewJString(reinterpret_cast<const GetStringCharsHandlerImpl<UnicodeCharTraits>::char_type*>(str));
}

jstring CreateJStringFromCStringW(JNIEnv *env, LPCSTR str)
{
    GetStringCharsHandlerImpl<UnicodeCharTraits> handler;
    handler.setEnvironment(env);
    std::string sA = str;
    const std::wstring sWCopy(sA.begin(), sA.end());
    return handler.GetNewJString(reinterpret_cast<const GetStringCharsHandlerImpl<UnicodeCharTraits>::char_type*>(str));
}

jclass GetJavaClassID(JNIEnv* env, const char* javaClass)
{
    return env->FindClass( javaClass );
}

jmethodID GetJavaClassConstructor(JNIEnv *env, const char* javaClass, const char* constructorSig)
{
    const jclass theClass = GetJavaClassID( env, javaClass );
    if ( theClass )
        return env->GetMethodID(theClass, "<init>", constructorSig);
    return nullptr;
}


jobjectArray CreateJStringArrayFromDTWAINArray(JNIEnv *env, DTWAIN_ARRAY arr)
{
    const LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(arr);
    jobjectArray ret;
    if ( nCount > 0 )
    {
        ret = static_cast<jobjectArray>(env->NewObjectArray(nCount, env->FindClass("java/lang/String"),
                                                            env->NewStringUTF("")));
        const LONG maxChars = API_INSTANCE DTWAIN_ArrayGetMaxStringLength(arr);
        std::vector<char> Val(maxChars + 1, 0);
        for ( LONG i = 0; i < nCount; i++ )
        {
            API_INSTANCE DTWAIN_ArrayGetAtANSIString(arr, i, &Val[0]);
            env->SetObjectArrayElement(ret, i, env->NewStringUTF(&Val[0]));
        }
    }
    else
        ret = env->NewObjectArray(0, env->FindClass("java/lang/String"),
    env->NewStringUTF(""));
    return ret;
}

DTWAIN_ARRAY CreateDTWAINArrayFromJStringArray(JNIEnv *env, jobjectArray strArray)
{
    const jsize stringCount = env->GetArrayLength(strArray);
    const DTWAIN_ARRAY arr = API_INSTANCE DTWAIN_ArrayCreate(DTWAIN_ARRAYSTRING, 0);
    if ( arr )
    {
        for (int i = 0; i < stringCount; ++i)
            API_INSTANCE DTWAIN_ArrayAddString(arr,
                                  reinterpret_cast<LPCTSTR>(GetStringCharsHandler(env,
                                  static_cast<jstring>(env->GetObjectArrayElement(strArray, i))).GetStringChars()));
    }
    return arr;
}

DTWAIN_ARRAY CreateDTWAINArrayFromJIntArray(JNIEnv *env, jintArray arr)
{

    jsize nCount = env->GetArrayLength(arr);
    DTWAIN_ARRAY dArray = API_INSTANCE  DTWAIN_ArrayCreate(DTWAIN_ARRAYLONG, static_cast<LONG>(nCount));
    if (dArray)
    {
        jint* pElement = env->GetIntArrayElements(arr, nullptr);
        LONG *pDBuffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dArray, 0));
        std::copy_n(pElement, nCount, pDBuffer);
        return dArray;
    }
    return nullptr;
}

DTWAIN_ARRAY CreateDTWAINArrayFromJFrameArray(JNIEnv *env, jobjectArray frameArray)
{
    const jsize frameCount = env->GetArrayLength(frameArray);
    const DTWAIN_ARRAY arr = API_INSTANCE DTWAIN_ArrayCreate(DTWAIN_ARRAYFRAME, 0);
    if ( arr )
    {
        JavaFrameInfo jInfo(env);
        double left, top, right, bottom;
        for (int i = 0; i < frameCount; ++i)
        {
            const jobject jFrame = env->GetObjectArrayElement(frameArray, i);
            jInfo.setObject(jFrame);
            jInfo.getJFrameDimensions(&left, &top, &right, &bottom);
            DTWAIN_FRAME f = API_INSTANCE DTWAIN_FrameCreate(left, top, right, bottom);
            DTWAINFrame_RAII raii(f);
            API_INSTANCE DTWAIN_ArrayAdd(arr, f);
        }
    }
    return arr;
}

jobjectArray CreateJFrameArrayFromDTWAINArray(JNIEnv *env, DTWAIN_ARRAY arr)
{
    jsize frameCount = 0;
    JavaFrameInfo jInfo(env);
    if ( arr )
        frameCount = API_INSTANCE DTWAIN_ArrayGetCount(arr);
    const jobjectArray jFrameArray = jInfo.CreateJFrameObjectArray(frameCount);
    double left, top, right, bottom;
    for (int i = 0; i < frameCount; ++i)
    {
        API_INSTANCE DTWAIN_ArrayGetAtFrame(arr, i, &left, &top, &right, &bottom);
        const jobject jObj = env->GetObjectArrayElement(jFrameArray, i);
        jInfo.setObject(jObj);
        jInfo.setJFrameDimensions(left, top, right, bottom);
    }
    return jFrameArray;
}
