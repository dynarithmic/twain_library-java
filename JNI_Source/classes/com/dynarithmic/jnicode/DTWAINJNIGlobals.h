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

#include "DSMCaller.h"
#include "DTWAINJNI_Version.h"

class JavaObjectCaller;

struct DTWAINJNIGlobals
{
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
