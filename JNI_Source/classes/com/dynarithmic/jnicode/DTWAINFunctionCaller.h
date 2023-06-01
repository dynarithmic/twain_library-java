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
#ifndef DTWAINFUNCTIONCALLER_H
#define DTWAINFUNCTIONCALLER_H

#include <map>
#include <jni.h>
#include "DTWAINGlobalFn.h"
#include "DTWAINRAII.h"
#include "JavaArrayTraits.h"

template <typename FnGlobalVPtrType, typename FnGlobalType, typename JavaTraits>
typename JavaTraits::array_type CallFnReturnArray0(JNIEnv* env, FnGlobalVPtrType& gType, const std::string& funcName)
{
    if (!IsModuleInitialized())
        throw "DTwain Module not loaded";
    AddToFunctionCounter(funcName);
    typename FnGlobalType::DTWAINFN_Map::iterator it = gType->m_FnMap.find(funcName);
    if (it != gType->m_FnMap.end())
    {
        DTWAIN_ARRAY A = 0;
        BOOL bRet = (*it->second)(&A);
        DTWAINArray_RAII arr(A);
        return CreateJArrayFromDTWAINArray<JavaTraits>(env, A, bRet ? true : false);
    }
    throw "Function Not Found";
}

template <typename FnGlobalVPtrType, typename FnGlobalType, typename Arg, typename JavaTraits>
typename JavaTraits::array_type CallFnReturnArray1(JNIEnv* env, FnGlobalVPtrType& gType,
                                                   const std::string& funcName, Arg a1)
{
    if ( !IsModuleInitialized() )
        throw "DTwain Module not loaded";
    AddToFunctionCounter(funcName);
    typename FnGlobalType::DTWAINFN_Map::iterator it = gType->m_FnMap.find(funcName);
    if ( it != gType->m_FnMap.end() )
    {
        DTWAIN_ARRAY A=0;
        BOOL bRet = (*it->second)(a1, &A);
        DTWAINArray_RAII arr(A);
        return CreateJArrayFromDTWAINArray<JavaTraits>(env, A, bRet?true:false);
    }
    throw "Function Not Found";
}

template <typename FnGlobalVPtrType, typename FnGlobalType, typename Arg1, typename Arg2, typename JavaTraits>
typename JavaTraits::array_type CallFnReturnArray2(JNIEnv* env, FnGlobalVPtrType& gType,
                                                   const std::string& funcName, Arg1 a1, Arg2 a2)
{
    if ( !IsModuleInitialized() )
        throw "DTwain Module not loaded";
    AddToFunctionCounter(funcName);
    typename FnGlobalType::DTWAINFN_Map::iterator it = gType->m_FnMap.find(funcName);
    if ( it != gType->m_FnMap.end() )
    {
        DTWAIN_ARRAY A=0;
        BOOL bRet = (*it->second)(a1, &A, a2);
        DTWAINArray_RAII arr(A);
        return CreateJArrayFromDTWAINArray<JavaTraits> (env, A, bRet?true:false);
    }
    throw "Function Not Found";
}
#endif