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
#ifndef DTWAINJNIGLOBALS_H
#define DTWAINJNIGLOBALS_H

#include <memory>
#include <unordered_map>
#include <string>
#include <set>
#include <utility>

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

#include "DTWAINGlobalFn.h"
#include "DSMCaller.h"
#include "DTWAINJNI_Version.h"

#define DECLARE_DTWAIN_SMART_POINTER_MAP(Name) FnGlobal##Name##Ptr g_##Name##Map;

class JavaObjectCaller;

struct DTWAINJNIGlobals
{
    typedef std::multiset<FnGlobalBase*> GlobalSetType;
    GlobalSetType g_ptrBase;
    DECLARE_DTWAIN_SMART_POINTER_MAP(LV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LS)
    DECLARE_DTWAIN_SMART_POINTER_MAP(SV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(AV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HandleV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSF)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSl)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSf)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLl)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSaB)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LHt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSFF)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLFF)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LST)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLTL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLtLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSFLLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(St)
    DECLARE_DTWAIN_SMART_POINTER_MAP(Lt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(Ot)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LTL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(Ltt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLttLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(SHtLLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSFLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LHF)
    DECLARE_DTWAIN_SMART_POINTER_MAP(BH)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LOLLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LO)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LOLLA)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LOLLLLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LOtLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSB)

    DECLARE_DTWAIN_SMART_POINTER_MAP(Ltttt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LTTTT)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLLLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLA)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLLA)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLLLLA)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSLBB)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLt)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HandleS)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSffffl)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSFFFFLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSaLLLLLLl)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSlll)
    DECLARE_DTWAIN_SMART_POINTER_MAP(La)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLLLa)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLLLA)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LLLLLLL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HLLTLlL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSH)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LSlllllll)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LStLLLLLLl)
    DECLARE_DTWAIN_SMART_POINTER_MAP(OV)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HOLTLlL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(WW)
    DECLARE_DTWAIN_SMART_POINTER_MAP(WSW)
    DECLARE_DTWAIN_SMART_POINTER_MAP(HL)
    DECLARE_DTWAIN_SMART_POINTER_MAP(LH)

    HMODULE g_DTWAINModule;
    typedef std::unordered_map<std::string, int> FunctionCounterMap;
    typedef std::unordered_map<DTWAIN_SOURCE, bool> CurrentAcquireTypeMap;
    FunctionCounterMap g_functionCounter;
    CurrentAcquireTypeMap g_CurrentAcquireMap;
    DSMCallerMap g_DSMCallerMap;
    std::string g_ResourceFileName;
    DTWAINJNIGlobals();

    public:
        struct DSMObjectChecker
        {
            std::shared_ptr<JNITripletTranslatorBase> m_tripletPtr = nullptr;
            std::string m_sSourceType;
            std::string m_expectedType;
        };

        void InitializeDSMCallerMap(JNIEnv* pEnv, const std::string& javaLowLevelDir);
        DSMObjectChecker CheckCallDSMObject(JNIEnv* pEnv, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 MSG, jobject jobj);
        std::pair<jobject, bool> CreateObjectFromTriplet(JNIEnv* pEnv, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 TMSG);
        enum { DEFINE_METHODS = 1, INIT_METHODS = 2, DEFINE_INIT_METHODS = 4 };
        static void RegisterJavaFunctionInterface(JavaObjectCaller* pObject,
                                                  const JavaFunctionInfoCategoryMap& fnList,
                                                  int whichAction = DEFINE_METHODS);
        std::string GetResourceFileName() const { return g_ResourceFileName; }
        void SetResourceFileName(std::string sName) { g_ResourceFileName = sName; }
};
#endif
