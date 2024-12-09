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
#ifndef DTWAINFUNCTIONCALLER_H
#define DTWAINFUNCTIONCALLER_H

#include <map>
#include <jni.h>
#include "DTWAINRAII.h"
#include "JavaArrayTraits.h"

struct JavaStringTraits 
{
    typedef jobjectArray array_type;
};

template <typename JavaTraits, typename apiFunc,  typename ...Params>
typename JavaTraits::array_type CallFnReturnArray(JNIEnv* env, LPDTWAIN_ARRAY arr, apiFunc fn, Params&& ...params)
{
    if (!IsModuleInitialized())
        throw "DTwain Module not loaded";
    bool bRet = false;
    bRet = fn(std::forward<Params>(params)...);
    if constexpr (std::is_same_v<JavaTraits, JavaStringTraits>)
    {
        if (arr && *arr)
            return CreateJStringArrayFromDTWAINArray(env, *arr);
        return CreateJStringArrayFromDTWAINArray(env, nullptr);
    }
    else
    {
        if (arr && *arr)
            return CreateJArrayFromDTWAINArray<JavaTraits>(env, *arr, bRet ? true : false);
        return CreateJArrayFromDTWAINArray<JavaTraits>(env, nullptr, bRet ? true : false);
    }
}

template <typename JavaTraits, typename apiFunc, typename ...Params>
typename JavaTraits::array_type CallFnReturnArray2(JNIEnv* env, apiFunc fn, Params&& ...params)
{
    if ( !IsModuleInitialized() )
        throw "DTwain Module not loaded";
    DTWAIN_ARRAY arr = fn(std::forward<Params>(params)...);
    DTWAINArray_RAII raii(arr);
    return CreateJArrayFromDTWAINArray<JavaIntArrayTraits>(env, arr, arr != nullptr);
}

#endif