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