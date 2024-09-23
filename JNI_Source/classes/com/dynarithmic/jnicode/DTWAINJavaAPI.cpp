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
#include <algorithm>
#include <array>
#include <map>
#include <memory>
#include <numeric>
#include <sstream>
#include <string>
#include <iostream>
#include <tchar.h>
#include <vector>
#include <windows.h>

#ifdef USING_DTWAIN_LOADLIBRARY
    #include "dtwainx2.h"
    #ifndef API_INSTANCE
        #define API_INSTANCE DYNDTWAIN_API::
    #endif
    HMODULE hModuleInst;
#else
    #include "dtwain.h"
    #ifndef API_INSTANCE
        #define API_INSTANCE
    #endif
#endif

#include "DTwainJavaAPI.h"
#include "JavaAdapter.h"
#include "jni.h"
#include "twain.h"
#pragma warning (disable:4297)
#pragma warning (disable:4715)
#pragma warning (disable:4996)
#include <fstream>

#include "DTWAINFunctionCaller.h"
#include "DTWAINJNIGlobals.h"
#include "DTWAINRAII.h"
#include "JavaArrayList.h"
#include "JavaArrayTraits.h"
#include "javaobjectcaller.h"
#include "UTFCharsHandler.h"
#include "DTWAINJNI_VerInfo.h"

#define TWRC_WRONGOBJECT     10000;

DTWAINJNIGlobals g_JNIGlobals;

#define DO_DTWAIN_TRY try \
                    {
#define DO_DTWAIN_CHECK_MODULE_LOAD  {\
                                    if (!g_JNIGlobals.g_DTWAINModule)\
                                    {\
                                    JavaExceptionThrower::ThrowJavaException(env, "DTwain DLL was not loaded successfully");\
                                    return{};\
                                    }\
                                   }

#define DO_DTWAIN_CHECK_MODULE_LOAD_EX(msg)  {\
                                        if (!g_JNIGlobals.g_DTWAINModule)\
                                        {\
                                        JavaExceptionThrower::ThrowJavaException(env, msg);\
                                        return{};\
                                        }\
                                   }

#define DO_DTWAIN_CATCH(env)   }  catch (...) {\
                                JavaExceptionThrower::ThrowJavaException(env);\
                                return {};\
                                }


bool IsModuleInitialized()
{
    return  g_JNIGlobals.g_DTWAINModule?true:false;
}

void AddToFunctionCounter(const std::string& fname)
{
    auto it = g_JNIGlobals.g_functionCounter.find(fname);
    if ( it == g_JNIGlobals.g_functionCounter.end())
        it = g_JNIGlobals.g_functionCounter.insert({fname, 0}).first;
    it->second++;
}

void CheckForDuplicateCalls()
{
   bool bDuplicateFound = false;
   auto it = g_JNIGlobals.g_functionCounter.begin();
   while (it != g_JNIGlobals.g_functionCounter.end())
   {
        if (it->second == 0)
        {
            std::ostringstream strm;
            strm << it->first << " not called " << "\n";
            OutputDebugStringA(strm.str().c_str());
        }
        else
        if (it->second > 1)
        {
            bDuplicateFound = true;
            std::ostringstream strm;
            strm << it->first << " called multiple times! (" << it->second << ")\n";
            OutputDebugStringA(strm.str().c_str());
        }
        ++it;
   }
   if ( !bDuplicateFound )
      OutputDebugStringA("No duplicate DTWAIN calls found\n");
}

class JavaCallback;
typedef std::shared_ptr<JavaCallback> JavaCallbackPtr;

class JavaCallback : public JavaAdapter
{
    public:
    #ifdef _WIN64
        typedef LONG_PTR callback_type;
    #else
        typedef LONG callback_type;
    #endif
        static JavaCallback* m_pThisObject;

        struct JCallbackInfo
        {
            std::string m_jClassName;
            std::string m_jFunctionName;
            std::string m_jFunctionSig;
            jclass m_jCallbackClass;
            jmethodID m_jCallbackMethodID;
        };

        enum { TWAINLISTENERFN, LOGGERFN, LASTJAVAFN };
        enum { JAVASRCPATH, JAVAFUNC, JAVASIG};

        std::map<std::string, JCallbackInfo> m_jCallbackInfo;

    public:
        // these functions are sent to DTWAIN for the callback setup
        static LRESULT CALLBACK DTWAINCallback(WPARAM w, LPARAM l, callback_type This);

        static LRESULT CALLBACK DTWAINLoggerCallback(LPCTSTR str, LONG64);

        // Constructor
        JavaCallback(JNIEnv * pEnv);
        void InitInfo(JNIEnv* pEnv);
};


JavaCallback *pNewCallback = nullptr;
JavaCallbackPtr g_pDTwainAPICallback;
JavaCallback* JavaCallback::m_pThisObject = nullptr;

struct JavaCallbackWrapper
{
    JavaCallbackPtr m_pCallBack;
    JavaCallbackWrapper(JavaCallbackPtr& p) : m_pCallBack(p) {}
    ~JavaCallbackWrapper()
    {
        m_pCallBack->releaseJNIEnv();
    }
};

LRESULT CALLBACK JavaCallback::DTWAINCallback(WPARAM w, LPARAM l, JavaCallback::callback_type This)
{
    static std::set<WPARAM> sGeneralErrors = {DTWAIN_TN_GENERALERROR};

    // First, get back the struct
    JavaCallbackPtr pCallback = g_pDTwainAPICallback;
    if ( !pCallback )
        return 1;

    // Now call the Java method
    auto iter = pCallback->m_jCallbackInfo.find("TwainMessageListener");
    JavaCallback::JCallbackInfo& pCallInfo = iter->second;
    JavaCallbackWrapper wrapper(pCallback);
    JNIEnv* pEnv = pCallback->getJNIEnv();

    // Only call this if the error is not a general error
    LRESULT retval = 0;

    // Re-establish callback information (method id's, class id's).  Eventually this should be moved to
    // RegisterMemberFunctions()
    if ( !sGeneralErrors.count(w))
    {
        retval = pEnv->CallStaticIntMethod(pCallInfo.m_jCallbackClass, pCallInfo.m_jCallbackMethodID, w, static_cast<jlong>(l),
                                           API_INSTANCE DTWAIN_IsSourceValid(reinterpret_cast<DTWAIN_SOURCE>(l))?TRUE:FALSE);
    }
    else
        retval = pEnv->CallStaticIntMethod(pCallInfo.m_jCallbackClass, pCallInfo.m_jCallbackMethodID, w, 0, FALSE);
    return static_cast<callback_type>(retval);
}

LRESULT CALLBACK JavaCallback::DTWAINLoggerCallback(LPCTSTR str, LONG64)
{
    JavaCallbackPtr pCallback = g_pDTwainAPICallback;
    if ( !pCallback )
        return 1;

    // Now call the Java method
    auto iter = pCallback->m_jCallbackInfo.find("TwainLogger");
    JavaCallback::JCallbackInfo& pCallInfo = iter->second;
    JavaCallbackWrapper wrapper(pCallback);
    JNIEnv* pEnv = pCallback->getJNIEnv();
    pEnv->CallStaticVoidMethod(pCallInfo.m_jCallbackClass,
                               pCallInfo.m_jCallbackMethodID,
                               CreateJStringFromCString(pEnv, str));
    return 0;
}

JavaCallback::JavaCallback(JNIEnv * pEnv) : JavaAdapter(pEnv)
{
    InitInfo(pEnv);
}

void JavaCallback::InitInfo(JNIEnv* pEnv)
{
    Init(pEnv);
    // Logger and listener callbacks are initialized here
    std::array<std::string, 2> categories = { "TwainLogger", "TwainMessageListener" };
    for (auto& s : categories)
    {
        auto iter = JavaFunctionNameMapInstance::getFunctionMap().find(s);
        auto& callback_info = iter->second;
        auto& callback_fn = iter->second.m_mapFunctions;
        auto& namesig = (*callback_fn.begin()).second;
        auto& jcallback = m_jCallbackInfo.insert({ s, {callback_info.m_className, namesig.funcName, namesig.funcSig} }).first;

        // Get the java class class
        jclass tempLocalClassRef = pEnv->FindClass(jcallback->second.m_jClassName.c_str());
        jclass globalClassRef = (jclass)pEnv->NewGlobalRef(tempLocalClassRef);
        pEnv->DeleteLocalRef(tempLocalClassRef);
        jcallback->second.m_jCallbackClass = globalClassRef;

        // get the method id stored in the class
        jcallback->second.m_jCallbackMethodID = pEnv->GetStaticMethodID(jcallback->second.m_jCallbackClass,
                                                                        namesig.funcName.c_str(),
                                                                        namesig.funcSig.c_str());
    }
    m_pThisObject = this;
}

void InitializeCallbacks(JNIEnv *env)
{
    if (!g_pDTwainAPICallback )
        g_pDTwainAPICallback = JavaCallbackPtr(new JavaCallback(env));
}

void InitializeFunctionCallerInfo(JNIEnv *pEnv)
{
    using JavaFunctionNameMap = std::map<std::string, JavaFunctionClassInfo>;

    std::array<std::string, 2> headerInfo;
    int numFuncs = 0;
    std::string line;

    std::ifstream txtRes(g_JNIGlobals.GetResourceFileName());
    if (!txtRes)
    {
        std::string sMsg = std::string(DTWAINJNI_INFOFILE) + " does not exist or could not be opened";
        JavaExceptionThrower::ThrowFileNotFoundError(pEnv, sMsg.c_str());
        return;
    }

    while (getline(txtRes, line))
    {
        if (stringjniutils::isAllBlank(line))
            continue;
        if (line.front() == ';') // This is a comment
            continue;
        // Get the header info
        std::istringstream strm(line);
        strm >> headerInfo[0] >> headerInfo[1] >> numFuncs;
        auto& globFuncMap = JavaFunctionNameMapInstance::getFunctionMap();
        auto iter = globFuncMap.insert({headerInfo[0],{headerInfo[1],{},{}}}).first;
        auto& funcMap = iter->second;
        int i = 0;
        while (i < numFuncs )
        {
            getline(txtRes, line);
            if ( stringjniutils::isAllBlank(line) )
                continue;
            std::istringstream strm2(line);
            std::string category, funcname, funcsig;
            strm2 >> category >> funcname >> funcsig;
            funcMap.m_mapFunctions.insert({category,{funcname, funcsig}});
            ++i;
        }
        funcMap.m_ObjectCallerTemplate = std::make_shared<JavaObjectCaller>(globFuncMap);
        DTWAINJNIGlobals::RegisterJavaFunctionInterface(funcMap.m_ObjectCallerTemplate.get(), funcMap.m_mapFunctions, DTWAINJNIGlobals::DEFINE_METHODS);
    }
}

std::string GetDirectory(const std::string& path)
{
    size_t found = path.find_last_of("/\\");
    return(path.substr(0, found));
}

///////////////////////////////////////////////////////////////////////////////////////////

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_LoadLibrary
* Signature: (Ljava/lang/String;Ljava/lang/String;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1LoadLibrary
(JNIEnv *env, jobject, jstring dllToLoad, jstring resPath)
{
    DO_DTWAIN_TRY
#ifdef USING_DTWAIN_LOADLIBRARY
    GetStringCharsHandler handler_loader(env, dllToLoad);
    const auto sDLLName = reinterpret_cast<LPCTSTR>(handler_loader.GetStringChars());
    HMODULE hDTwainModule = ::LoadLibrary(sDLLName);
    if (!hDTwainModule)
    {
        JavaExceptionThrower::ThrowFileNotFoundError(env, "DTWAIN DLL does not exist or could not be opened");
        return 0;
    }
    DYNDTWAIN_API::InitDTWAINInterface(hDTwainModule);
#endif
    GetStringCharsHandler pathHandler(env, resPath);
#ifdef UNICODE
    std::wstring resPathString = pathHandler.GetStringCharsNative();
#else
    std::string resPathString = pathHandler.GetStringCharsNative();
#endif
    std::string strToUse = stringjniutils::to_string(resPathString);
    if (strToUse == "<>" || strToUse.empty())
    {
        // Get the instance handle of the application
        #ifdef USING_DTWAIN_LOADLIBRARY
        HINSTANCE hInst = GetModuleHandle(sDLLName);
        #else
        HINSTANCE hInst = GetModuleHandleA(DTWAINJNI_DLLNAME);
        #endif
        char szName[1024];
        ::GetModuleFileNameA(hInst, szName, 1023);
        strToUse = GetDirectory(szName) + "\\" + std::string(DTWAINJNI_INFOFILE);
    }
    else
    {
        if (strToUse.back() != '\\' && strToUse.back() != '/')
            strToUse += "\\";
        strToUse += DTWAINJNI_INFOFILE;
    }
    g_JNIGlobals.SetResourceFileName(strToUse);

    std::ifstream txtRes(strToUse);
    if ( !txtRes )
    {
        std::string sMsg = std::string(DTWAINJNI_INFOFILE) + " does not exist or could not be opened";
        JavaExceptionThrower::ThrowFileNotFoundError(env, sMsg.c_str());
        return 0;
    }
    txtRes.close();

    InitializeFunctionCallerInfo(env);
    InitializeCallbacks(env);
    GetStringCharsHandler handler(env, dllToLoad);
    const auto s = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    if ( !g_JNIGlobals.g_DTWAINModule )
    {
        g_JNIGlobals.g_DTWAINModule = GetModuleHandle(s);
        if (g_JNIGlobals.g_DTWAINModule)
        {
            g_JNIGlobals.InitializeDSMCallerMap(env, JavaFunctionNameMapInstance::getFunctionMap()["LowLevelDirectory"].m_className);
        }
    }
    return g_JNIGlobals.g_DTWAINModule?1:0;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1FreeLibrary
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    if ( g_JNIGlobals.g_DTWAINModule )
    {
        CheckForDuplicateCalls();
        FreeLibrary(g_JNIGlobals.g_DTWAINModule);
        g_JNIGlobals.g_DTWAINModule = nullptr;
    }
    return 1;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsTwainAvailable
  (JNIEnv *env, jobject)
  {
     DO_DTWAIN_TRY
     return API_INSTANCE DTWAIN_IsTwainAvailable()?JNI_TRUE:JNI_FALSE;
     DO_DTWAIN_CATCH(env)
  }

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SysInitialize
 * Signature: ()I
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SysInitialize
  (JNIEnv *env, jobject)
  {
      DO_DTWAIN_TRY
      const jlong retValue = reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SysInitialize());
      if ( retValue )
      {
          // set the callback for the DTWAIN logger
          API_INSTANCE DTWAIN_SetLoggerCallback(JavaCallback::DTWAINLoggerCallback, 0LL);
          const JavaCallbackPtr pCallback = g_pDTwainAPICallback;
          if ( pCallback )
              API_INSTANCE DTWAIN_SetCallback(JavaCallback::DTWAINCallback, reinterpret_cast<JavaCallback::callback_type>(pCallback.get()));
      }
      return retValue;
      DO_DTWAIN_CATCH(env)
  }

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SysDestroy
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SysDestroy
  (JNIEnv *env, jobject)
  {
      DO_DTWAIN_TRY
      const BOOL bRetVal = API_INSTANCE DTWAIN_SysDestroy();
      if ( bRetVal )
          g_JNIGlobals.g_CurrentAcquireMap.clear();
      return bRetVal;
      DO_DTWAIN_CATCH(env)
  }

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectSource
 * Signature: ()I
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectSource
  (JNIEnv *env, jobject)
  {
      DO_DTWAIN_TRY
      return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectSource());
      DO_DTWAIN_CATCH(env)
  }

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectDefaultSource
 * Signature: ()I
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectDefaultSource
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectDefaultSource());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetLastError
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetLastError
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetLastError();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetTwainMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTwainMode
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetTwainMode();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsSessionEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsSessionEnabled
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsSessionEnabled()?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EndTwainSession
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EndTwainSession
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EndTwainSession();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCountry
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCountry
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCountry();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetLanguage
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetLanguage
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetLanguage();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsMsgNotifyEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsMsgNotifyEnabled
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsMsgNotifyEnabled()?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetTwainHwnd
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTwainHwnd
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_GetTwainHwnd());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAcquiring
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAcquiring
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAcquiring()?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_CreateAcquisitionArray
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CreateAcquisitionArray
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_CreateAcquisitionArray());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ClearErrorBuffer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ClearErrorBuffer
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ClearErrorBuffer();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetErrorBufferThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetErrorBufferThreshold
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetErrorBufferThreshold();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_InitOCRInterface
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1InitOCRInterface
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_InitOCRInterface();
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectOCREngine
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectOCREngine
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectOCREngine());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectDefaultOCREngine
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectDefaultOCREngine
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectDefaultOCREngine());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetTwainAvailability
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTwainAvailability
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetTwainAvailability();
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTwainMode
  (JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTwainMode(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCountry
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCountry
  (JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetCountry(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetLanguage
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetLanguage
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetLanguage(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableMsgNotify
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableMsgNotify
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableMsgNotify(a1);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_EnableTripletNotify
* Signature: (I)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableTripletsNotify
(JNIEnv* env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableTripletsNotify(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_OpenSourcesOnSelect
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1OpenSourcesOnSelect
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_OpenSourcesOnSelect(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetQueryCapSupport
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetQueryCapSupport
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetQueryCapSupport(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTwainTimeout
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTwainTimeout
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTwainTimeout(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetErrorBufferThreshold
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetErrorBufferThreshold
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetErrorBufferThreshold(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_AppHandlesExceptions
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AppHandlesExceptions
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_AppHandlesExceptions(a1);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTwainDSM
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTwainDSM
(JNIEnv *env, jobject, jint a1)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTwainDSM(a1);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1OpenSource
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_OpenSource(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_CloseSource
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CloseSource
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    BOOL bRetVal = API_INSTANCE DTWAIN_CloseSource(reinterpret_cast<DTWAIN_SOURCE>(src));
    if ( bRetVal )
        g_JNIGlobals.g_CurrentAcquireMap.erase(reinterpret_cast<DTWAIN_SOURCE>(src));
    return bRetVal;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_CloseSourceUI
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CloseSourceUI
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_CloseSourceUI(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetDefaultSource
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetDefaultSource
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetDefaultSource(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsSourceAcquiring
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsSourceAcquiring
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsSourceAcquiring(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsSourceOpen
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsSourceOpen
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsSourceOpen(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAllCapsToDefault
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAllCapsToDefault
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetAllCapsToDefault(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCurrentPageNum
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCurrentPageNum
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCurrentPageNum(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetMaxAcquisitions
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetMaxAcquisitions
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetMaxAcquisitions(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetMaxPagesToAcquire
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetMaxPagesToAcquire
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetMaxPagesToAcquire(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsUIControllable
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsUIControllable
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsUIControllable(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsUIEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsUIEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsUIEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsIndicatorSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsIndicatorSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsIndicatorSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsIndicatorEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsIndicatorEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsIndicatorEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsThumbnailSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsThumbnailSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsThumbnailSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsThumbnailEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsThumbnailEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsThumbnailEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsDeviceEventSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsDeviceEventSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsDeviceEventSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsUIOnlySupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsUIOnlySupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsUIOnlySupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ShowUIOnly
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ShowUIOnly
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ShowUIOnly(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPrinterSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPrinterSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPrinterSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFeederEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFeederEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFeederEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFeederLoaded
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFeederLoaded
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFeederLoaded(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFeederSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFeederSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFeederSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFeederSensitive
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFeederSensitive
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFeederSensitive(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_FeedPage
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1FeedPage
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_FeedPage(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_RewindPage
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1RewindPage
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_RewindPage(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ClearPage
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ClearPage
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ClearPage(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoFeedEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoFeedEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoFeedEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoFeedSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoFeedSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoFeedSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetFeederFuncs
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetFeederFuncs
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetFeederFuncs(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPaperDetectable
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPaperDetectable
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPaperDetectable(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsDuplexSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsDuplexSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsDuplexSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsDuplexEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsDuplexEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsDuplexEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsCustomDSDataSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsCustomDSDataSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsCustomDSDataSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ClearPDFText
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ClearPDFText
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ClearPDFText(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoDeskewSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoDeskewSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoDeskewSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoDeskewEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoDeskewEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoDeskewEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoBorderDetectSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoBorderDetectSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoBorderDetectEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoBorderDetectEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoBorderDetectEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsLightPathSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsLightPathSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsLightPathSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsLampSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsLampSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsLampSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsLampEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsLampEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsLampEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsLightSourceSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsLightSourceSupported
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsLightSourceSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetMaxRetryAttempts
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetMaxRetryAttempts
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetMaxRetryAttempts(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCurrentRetryCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCurrentRetryCount
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCurrentRetryCount(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsSkipImageInfoError
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsSkipImageInfoError
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsSkipImageInfoError(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsExtImageInfoSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsExtImageInfoSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsExtImageInfoSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_InitExtImageInfo
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1InitExtImageInfo
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_InitExtImageInfo(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetExtImageInfo
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetExtImageInfo
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetExtImageInfo(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_FreeExtImageInfo
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1FreeExtImageInfo
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_FreeExtImageInfo(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_FlushAcquiredPages
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1FlushAcquiredPages
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_FlushAcquiredPages(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFileSystemSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFileSystemSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFileSystemSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBlankPageAutoDetection
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBlankPageAutoDetection
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetBlankPageAutoDetection(reinterpret_cast<DTWAIN_SOURCE>(src));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsBlankPageDetectionOn
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsBlankPageDetectionOn
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsBlankPageDetectionOn(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoScanEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoScanEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoScanEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsDeviceOnLine
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsDeviceOnLine
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsDeviceOnLine(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoBrightEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoBrightEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsAutoRotateEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsAutoRotateEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsAutoRotateEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}
/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsRotationSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsRotationSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsRotationSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}
/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPatchCapsSupported
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPatchCapsSupported
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPatchCapsSupported(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPatchDetectEnabled
 * Signature: (J)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPatchDetectEnabled
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPatchDetectEnabled(reinterpret_cast<DTWAIN_SOURCE>(src))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumSources
 * Signature: ()[J
 */

JNIEXPORT jlongArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumSources
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    #ifdef _WIN64
    return CallFnReturnArray<JavaLong64ArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumSources, &arr);
    #else
    return CallFnReturnArray<JavaLongArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumSources, &arr);
    #endif
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumSupportedCaps
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumSupportedCaps, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumExtendedCaps
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumExtendedCaps
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumExtendedCaps, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumCustomCaps
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumCustomCaps
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumCustomCaps, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumSourceUnits
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumSourceUnits
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumSourceUnits, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumFileXferFormats
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumFileXferFormats
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumFileXferFormats, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumCompressionTypes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumCompressionTypes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumCompressionTypes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPrinterStringModes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPrinterStringModes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPrinterStringModes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumTwainPrintersArray
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumTwainPrintersArray
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumTwainPrintersArray, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumOrientations
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumOrientations
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumOrientations, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPaperSizes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPaperSizes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPaperSizes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPixelTypes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPixelTypes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPixelTypes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumBitDepths
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumBitDepths
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumBitDepths, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumJobControls
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumJobControls
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumJobControls, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumLightPaths
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumLightPaths
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumLightPaths, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumLightSources
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumLightSources
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumLightSources, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetLightSources
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetLightSources
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetLightSources, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumExtImageInfoTypes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumExtImageInfoTypes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumExtImageInfoTypes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumAlarms
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumAlarms
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumAlarms, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumNoiseFilters
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumNoiseFilters
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumNoiseFilters, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPatchMaxRetries
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPatchMaxRetries
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPatchMaxRetries, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPatchMaxPriorities
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPatchMaxPriorities
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPatchMaxPriorities, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPatchSearchModes
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPatchSearchModes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPatchSearchModes, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPatchTimeOutValues
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPatchTimeOutValues
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPatchTimeOutValues, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetPatchPriorities
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetPatchPriorities
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetPatchPriorities, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumPatchPriorities
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumPatchPriorities
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumPatchPriorities, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumTopCameras
 * Signature: (J)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumTopCameras
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_EnumTopCameras, reinterpret_cast<DTWAIN_SOURCE>(src), &arr );
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumBottomCameras
 * Signature: (J)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumBottomCameras
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_EnumBottomCameras, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumCameras
 * Signature: (J)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumCameras
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_EnumCameras, reinterpret_cast<DTWAIN_SOURCE>(src), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumCamerasEx
 * Signature: (JI)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumCamerasEx
(JNIEnv* env, jobject, jlong src, jint whichCamera)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_EnumCamerasEx, reinterpret_cast<DTWAIN_SOURCE>(src), whichCamera, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetVersionInfo
 * Signature: ()Lcom/dynarithmic/twain/DTwainVersionInfo;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetVersionInfo(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaDTwainVersionInfo vInfo(env);

    // Call the DTWAIN function to get the version information
    LONG majorV, minorV, patchV, versionType;
    const BOOL bRet = API_INSTANCE DTWAIN_GetVersionEx(&majorV, &minorV, &versionType, &patchV);
    if ( bRet )
    {
        // call DTWAIN function to get the string version
        LONG sLength = API_INSTANCE DTWAIN_GetVersionString(nullptr, 0);
        std::vector<TCHAR> vChars(sLength,0);
        API_INSTANCE DTWAIN_GetVersionString(vChars.data(), sLength);

        // Get the path of the DTWAIN DLL
        sLength = API_INSTANCE DTWAIN_GetLibraryPath(nullptr, 0);
        std::vector<TCHAR> exePathChars(sLength, 0);
        API_INSTANCE DTWAIN_GetLibraryPath(exePathChars.data(), sLength);

        // call DTWAIN function to get the short string version
        sLength = API_INSTANCE DTWAIN_GetShortVersionString(nullptr, 0);
        std::vector<TCHAR> vChars2(sLength, 0);
        API_INSTANCE DTWAIN_GetShortVersionString(vChars2.data(), sLength);

        // call DTWAIN function to get the short string version
        sLength = API_INSTANCE DTWAIN_GetVersionCopyright(nullptr, 0);
        std::vector<TCHAR> vChars3(sLength, 0);
        API_INSTANCE DTWAIN_GetVersionCopyright(vChars3.data(), sLength);

        // Call Java function to declare and init a new versionInfo object
        vInfo.setMajorVersion(majorV);
        vInfo.setMinorVersion(minorV);
        vInfo.setVersionType(versionType);
        vInfo.setPatchVersion(patchV);
        vInfo.setExePath(exePathChars.data());
        vInfo.setLongName(vChars.data());
        vInfo.setShortName(vChars2.data());
        vInfo.setVersionCopyright(vChars3.data());
    }
    return vInfo.getObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsCapSupported
 * Signature: (JI)I
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsCapSupported
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsCapSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetCapArrayType
* Signature: (JI)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapDataType
(JNIEnv *env, jobject, jlong source, jint cap)
{
    DO_DTWAIN_TRY
    LONG retVal = API_INSTANCE DTWAIN_GetCapDataType((DTWAIN_SOURCE)source, cap);
    return retVal;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapDataTypeAsClassName
 * Signature: (JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapDataTypeAsClassName
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    LONG retVal = API_INSTANCE DTWAIN_GetCapDataType(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    std::string ptrName;
    switch (retVal)
    {
        case TWTY_INT8:
        case TWTY_INT16:
        case TWTY_INT32:
        case TWTY_UINT8:
        case TWTY_UINT16:
        case TWTY_UINT32:
        case TWTY_BOOL:
            ptrName = "int";
        break;

        case TWTY_FIX32:
            ptrName = "double";
        break;

        case TWTY_STR32:
        case TWTY_STR64:
        case TWTY_STR128:
        case TWTY_STR255:
        case TWTY_STR1024:
            ptrName = "java/Lang/String";
        break;

        case TWTY_FRAME:
        {
            auto& funcMap = JavaFunctionNameMapInstance::getFunctionMap();
            const auto iter = funcMap.find("TwainFrame");
            if ( iter != funcMap.end())
                ptrName = iter->second.m_className;
            else
                ptrName = "<unknown>";
        }
        break;

        default:
            ptrName = "<unknown>";
    }
#ifdef UNICODE
    return static_cast<jstring>(CreateJStringFromCStringW(env, ptrName.c_str()));
#else
    return static_cast<jstring>(CreateJStringFromCStringA(env, ptrName.c_str()));
#endif
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetMaxAcquisitions
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetMaxAcquisitions
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetMaxAcquisitions(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetSourceUnit
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetSourceUnit
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetSourceUnit(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsFileXferSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsFileXferSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsFileXferSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableIndicator
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableIndicator
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableIndicator(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}


JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsCompressionSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsCompressionSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPrinterEnabled
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPrinterEnabled
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPrinterEnabled(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableFeeder
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableFeeder
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableFeeder(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnablePrinter
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnablePrinter
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnablePrinter(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableThumbnail
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableThumbnail
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableThumbnail(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ForceAcquireBitDepth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ForceAcquireBitDepth
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ForceAcquireBitDepth(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAvailablePrinters
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAvailablePrinters
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetAvailablePrinters(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetDeviceNotifications
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetDeviceNotifications
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetDeviceNotifications(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPrinterStartNumber
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPrinterStartNumber
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPrinterStartNumber(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoFeed
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoFeed
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoFeed(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableDuplex
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableDuplex
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableDuplex(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsOrientationSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsOrientationSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsOrientationSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPaperSizeSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPaperSizeSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPaperSizeSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsPixelTypeSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsPixelTypeSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsPixelTypeSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFCompression
 * Signature: (JZ)I
 */
/*JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFCompression
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    return API_INSTANCE DTWAIN_SetPDFCompression(reinterpret_cast<DTWAIN_SOURCE>(arg1), (LONG)arg2);
}
*/
/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFASCIICompression
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFASCIICompression
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFASCIICompression(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPostScriptType
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPostScriptType
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPostScriptType(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFJpegQuality
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFJpegQuality
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFJpegQuality(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTIFFInvert
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTIFFInvert
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTIFFInvert(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTIFFCompressType
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTIFFCompressType
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTIFFCompressType(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsJobControlSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsJobControlSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsJobControlSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}
/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableJobFileHandling
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableJobFileHandling
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableJobFileHandling(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoDeskew
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoDeskew
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoDeskew(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoBorderDetect
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoBorderDetect
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoBorderDetect(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetLightPath
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetLightPath
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetLightPath(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableLamp
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableLamp
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableLamp(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetMaxRetryAttempts
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetMaxRetryAttempts
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetMaxRetryAttempts(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCurrentRetryCount
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCurrentRetryCount
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetCurrentRetryCount(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SkipImageInfoError
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SkipImageInfoError
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SkipImageInfoError(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetMultipageScanMode
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetMultipageScanMode
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetMultipageScanMode(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAlarmVolume
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAlarmVolume
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetAlarmVolume(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoScan
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoScan
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoScan(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ClearBuffers
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ClearBuffers
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_ClearBuffers(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetFeederAlignment
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetFeederAlignment
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetFeederAlignment(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetFeederOrder
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetFeederOrder
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetFeederOrder(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetMaxBuffers
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetMaxBuffers
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetMaxBuffers(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsMaxBuffersSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsMaxBuffersSupported
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsMaxBuffersSupported(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoBright
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoBright
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoBright(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnableAutoRotate
 * Signature: (JZ)Z
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoRotate
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EnableAutoRotate(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetNoiseFilter
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetNoiseFilter
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetNoiseFilter(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPixelFlavor
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPixelFlavor
(JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPixelFlavor(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetRotation
 * Signature: (JD)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetRotation
(JNIEnv *env, jobject, jlong arg1, jdouble arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetRotation(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetSourceUnit
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetSourceUnit(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetDeviceNotifications
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetDeviceNotifications
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetDeviceNotifications(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetDeviceEvent
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetDeviceEvent
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    const BOOL bRet = API_INSTANCE DTWAIN_GetDeviceEvent(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCompressionSize
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCompressionSize
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetCompressionSize(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetPrinterStartNumber
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetPrinterStartNumber
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetPrinterStartNumber(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetDuplexType
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetDuplexType
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetDuplexType(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetLightPath
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetLightPath
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetLightPath(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetAlarmVolume
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetAlarmVolume
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetAlarmVolume(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBatteryMinutes
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBatteryMinutes
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetBatteryMinutes(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBatteryPercent
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBatteryPercent
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetBatteryPercent(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetFeederAlignment
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetFeederAlignment
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetFeederAlignment(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetFeederOrder
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetFeederOrder
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetFeederOrder(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetMaxBuffers
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetMaxBuffers
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetMaxBuffers(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetNoiseFilter
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetNoiseFilter
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetNoiseFilter(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetPixelFlavor
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetPixelFlavor
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetPixelFlavor(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetRotation
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetRotation
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_FLOAT val;
    BOOL bRet = API_INSTANCE DTWAIN_GetRotation(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jdouble JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetContrast
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_FLOAT val;
    BOOL bRet = API_INSTANCE DTWAIN_GetContrast(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBrightness
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBrightness
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_FLOAT val;
    BOOL bRet = API_INSTANCE DTWAIN_GetBrightness(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetResolution
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetResolution
(JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DTWAIN_FLOAT val;
    BOOL bRet = API_INSTANCE DTWAIN_GetResolution(reinterpret_cast<DTWAIN_SOURCE>(src), &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapArrayType
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapArrayType
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCapArrayType(reinterpret_cast<DTWAIN_SOURCE>(arg1), static_cast<LONG>(arg2));
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapOperations
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapOperations
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    DTWAIN_LONG val;
    BOOL bRet = API_INSTANCE DTWAIN_GetCapOperations(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, &val);
    if (bRet)
        return val;
    return 0;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumContrastValues
  (JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumContrastValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), &arr, arg2?TRUE:FALSE);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumBrightnessValues
 * Signature: (JZ)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumBrightnessValues
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumBrightnessValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), &arr, arg2 ? TRUE : FALSE);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumResolutionValues
 * Signature: (JZ)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumResolutionValues
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumResolutionValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), &arr, arg2 ? TRUE : FALSE);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumMaxBuffers
(JNIEnv *env, jobject, jlong arg1, jboolean arg2)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumMaxBuffers, reinterpret_cast<DTWAIN_SOURCE>(arg1), &arr, arg2 ? TRUE : FALSE);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ResetCapValues
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetCapValues(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, DTWAIN_CAPRESET, (DTWAIN_ARRAY)NULL);
    DO_DTWAIN_CATCH(env)
}


JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesInt
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jintArray arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaIntArrayTraits>(env, arg4);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValues(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, aTmp);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesDouble
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jdoubleArray arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaDoubleArrayTraits>(env, arg4);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValues(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, aTmp);
    DO_DTWAIN_CATCH(env)
}

DTWAIN_ARRAY CreateDTWAINStringArrayFromJArray(JNIEnv* env, jobjectArray arg, int arrayType=DTWAIN_ARRAYSTRING)
{
    jsize nCount = env->GetArrayLength(arg);
    if ( nCount >= 0 )
    {
        DTWAIN_ARRAY aTmp = API_INSTANCE DTWAIN_ArrayCreate(arrayType, nCount);
        if ( !aTmp )
            return nullptr;
        jstring javaString;
        LPCTSTR cString;
        int i;
        for ( i = 0; i < nCount; ++i )
        {
            javaString = static_cast<jstring>(env->GetObjectArrayElement(arg, i));
            GetStringCharsHandler handler(env, javaString);
            cString = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
            if ( !cString )
                API_INSTANCE DTWAIN_ArraySetAtString(aTmp, i, _T(""));
            else
                API_INSTANCE DTWAIN_ArraySetAtString(aTmp, i, cString);
        }
        return aTmp;
    }
    return nullptr;
}
/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesString
 * Signature: (JII[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesString
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jobjectArray arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINStringArrayFromJArray(env, arg4);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValues(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesStringEx
 * Signature: (JIII[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesStringEx
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jobjectArray arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINStringArrayFromJArray(env, arg5);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesStringEx2
 * Signature: (JIIII[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesStringEx2
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobjectArray arg6)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINStringArrayFromJArray(env, arg6);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesIntEx
 * Signature: (JIII[I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesIntEx
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaIntArrayTraits>(env, arg5);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapContainer
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapContainer
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCapContainer(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesDoubleEx
 * Signature: (JIII[D)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesDoubleEx
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jdoubleArray arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaDoubleArrayTraits>(env, arg5);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesIntEx2
 * Signature: (JIIII[I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesIntEx2
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaIntArrayTraits>(env, arg6);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, aTmp);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesDoubleEx2
 * Signature: (JIIII[D)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesDoubleEx2
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jdoubleArray arg6)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = CreateDTWAINArrayFromJArray<JavaDoubleArrayTraits>(env, arg6);
    DTWAINArray_RAII raii(aTmp);
    return API_INSTANCE DTWAIN_SetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, aTmp);
    return 0;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesInt
 * Signature: (JII)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesInt
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, &arr);
    DO_DTWAIN_CATCH(env)
}


jobjectArray CreateStringJArrayFromDTWAINArray(JNIEnv *env, DTWAIN_ARRAY arr)
{
    jobjectArray ret;
    LONG nCount = API_INSTANCE DTWAIN_ArrayGetCount(arr);
    nCount = (std::max)(0L, nCount);
    ret = static_cast<jobjectArray>(env->NewObjectArray(nCount, env->FindClass("java/lang/String"), env->NewStringUTF("")));
    LPCTSTR Val;
    GetStringCharsHandler handler;
    handler.setEnvironment(env);
    for ( LONG i = 0; i < nCount; i++ )
    {
        Val = API_INSTANCE DTWAIN_ArrayGetAtStringPtr(arr, i);
        env->SetObjectArrayElement(ret, i, handler.GetNewJString(reinterpret_cast<const GetStringCharsHandler::char_type*>(Val)));
    }
    return ret;
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesString
 * Signature: (JII)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesString
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, &arr);
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetCapValues
* Signature: (JII)Ljava/util/List;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValues
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = nullptr;
    API_INSTANCE DTWAIN_GetCapValues(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, &aTmp);
    DTWAINArray_RAII raii(aTmp);
    if ( aTmp )
    {
        auto arrayType = API_INSTANCE DTWAIN_ArrayGetType(aTmp);
        if ( arrayType == DTWAIN_ARRAYLONG )
        {
            // if this is a boolean, type is Boolean list
            if (API_INSTANCE DTWAIN_GetCapDataType(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2) == TWTY_BOOL)
            {
                auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
                std::vector<jboolean> allValues(sz);
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(buffer, buffer + sz, allValues.begin());
                JavaArrayListHandler<ArrayBooleanList> aHandler(env);
                jobject jReturn = aHandler.NativeToJava(allValues);
                return jReturn;
            }
            else
            {
                auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
                std::vector<int32_t> allValues(sz);
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(buffer, buffer + sz, allValues.begin());
                JavaArrayListHandler<ArrayIntegerList> aHandler(env);
                jobject jReturn = aHandler.NativeToJava(allValues);
                return jReturn;
            }
        }
        else
        if ( arrayType == DTWAIN_ARRAYFLOAT)
        {
            auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
            std::vector<double> allValues(sz);
            auto buffer = static_cast<double*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
            std::copy(buffer, buffer + sz, allValues.begin());
            JavaArrayListHandler<ArrayDoubleList> aHandler(env);
            jobject jReturn = aHandler.NativeToJava(allValues);
            return jReturn;
        }
        else
        if ( arrayType == DTWAIN_ARRAYSTRING)
        {
            auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
            std::vector<std::string> allValues(sz);
            for (LONG i = 0; i < sz; ++i)
                allValues[i] = API_INSTANCE DTWAIN_ArrayGetAtANSIStringPtr(aTmp, i);
            JavaArrayListHandler<ArrayStringList<ArrayStringCharTraitsA>> aHandler(env);
            jobject jReturn = aHandler.NativeToJava(allValues);
            return jReturn;
        }
    }
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetCapValues
* Signature: (JIILjava/util/List;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValues
(JNIEnv *env, jobject, jlong source, jint cap, jint setType, jobject values)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = API_INSTANCE DTWAIN_ArrayCreateFromCap((DTWAIN_SOURCE)source, cap, 0);
    if (aTmp)
    {
        DTWAINArray_RAII raii(aTmp);
        LONG arrayType = API_INSTANCE DTWAIN_ArrayGetType(aTmp);
        if (arrayType == DTWAIN_ARRAYLONG)
        {
            // if this is a boolean, type is Boolean list
            if ( API_INSTANCE DTWAIN_GetCapDataType((DTWAIN_SOURCE)source, cap) == TWTY_BOOL)
            {
                JavaArrayListHandler<ArrayBooleanList> aHandler(env);
                auto vect = aHandler.JavaToNative(values);
                API_INSTANCE DTWAIN_ArrayResize(aTmp, vect.size());
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(vect.begin(), vect.end(), buffer);
                return API_INSTANCE DTWAIN_SetCapValues(DTWAIN_SOURCE(source), cap, setType, aTmp);
            }
            else
            {
                JavaArrayListHandler<ArrayIntegerList> aHandler(env);
                auto vect = aHandler.JavaToNative(values);
                API_INSTANCE DTWAIN_ArrayResize(aTmp, vect.size());
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(vect.begin(), vect.end(), buffer);
                return API_INSTANCE DTWAIN_SetCapValues(DTWAIN_SOURCE(source),cap,setType,aTmp);
           }
        }
        else
        if (arrayType == DTWAIN_ARRAYFLOAT)
        {
            JavaArrayListHandler<ArrayDoubleList> aHandler(env);
            auto vect = aHandler.JavaToNative(values);
            API_INSTANCE DTWAIN_ArrayResize(aTmp, vect.size());
            auto buffer = static_cast<double*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
            std::copy(vect.begin(), vect.end(), buffer);
            return API_INSTANCE DTWAIN_SetCapValues(DTWAIN_SOURCE(source), cap, setType, aTmp);
        }
        else
        if (arrayType == DTWAIN_ARRAYSTRING)
        {
            JavaArrayListHandler<ArrayStringList<ArrayStringCharTraitsA>> aHandler(env);
            auto vect = aHandler.JavaToNative(values);
            API_INSTANCE DTWAIN_ArrayResize(aTmp, vect.size());
            for (size_t i = 0; i < vect.size(); ++i)
                API_INSTANCE DTWAIN_ArraySetAtANSIString(aTmp,i,vect[i].c_str());
            return API_INSTANCE DTWAIN_SetCapValues(DTWAIN_SOURCE(source), cap, setType, aTmp);
        }
        if (arrayType == DTWAIN_ARRAYFRAME)
        {
            JavaArrayListHandler<ArrayFrameList> aHandler(env);
            auto vect = aHandler.JavaToNative(values);
            API_INSTANCE DTWAIN_ArrayResize(aTmp, vect.size());
            for (size_t i = 0; i < vect.size(); ++i)
                API_INSTANCE DTWAIN_ArrayFrameSetAt(aTmp, i, vect[i].left, vect[i].top, vect[i].right, vect[i].bottom);
            return API_INSTANCE DTWAIN_SetCapValues(DTWAIN_SOURCE(source), cap, setType, aTmp);
        }
    }
    return 0;
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetCapValuesEx2
* Signature: (JIIIILjava/util/List;)Z
*/
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesEx2
(JNIEnv *env, jobject, jlong source, jint capType, jint getType, jint containerType, jint dataType, jobject retList)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY aTmp = nullptr;
    API_INSTANCE DTWAIN_GetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(source), capType, getType, containerType, dataType, &aTmp);
    DTWAINArray_RAII raii(aTmp);
    if (aTmp)
    {
        auto arrayType = API_INSTANCE DTWAIN_ArrayGetType(aTmp);
        if (arrayType == DTWAIN_ARRAYLONG)
        {
            // if this is a boolean, type is Boolean list
            if (API_INSTANCE DTWAIN_GetCapDataType(reinterpret_cast<DTWAIN_SOURCE>(source), capType) == TWTY_BOOL)
            {
                auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
                std::vector<jboolean> allValues(sz);
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(buffer, buffer + sz, allValues.begin());
                JavaArrayListHandler<ArrayBooleanList> aHandler(env);
                aHandler.NativeToJava(retList, allValues);
                return JNI_TRUE;
            }
            else
            {
                auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
                std::vector<int32_t> allValues(sz);
                auto buffer = static_cast<LONG*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
                std::copy(buffer, buffer + sz, allValues.begin());
                JavaArrayListHandler<ArrayIntegerList> aHandler(env);
                aHandler.NativeToJava(retList, allValues);
                return JNI_TRUE;
           }
        }
        else
        if (arrayType == DTWAIN_ARRAYFLOAT)
        {
            auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
            std::vector<double> allValues(sz);
            auto buffer = static_cast<double*>(API_INSTANCE DTWAIN_ArrayGetBuffer(aTmp, 0));
            std::copy(buffer, buffer + sz, allValues.begin());
            JavaArrayListHandler<ArrayDoubleList> aHandler(env);
            aHandler.NativeToJava(retList, allValues);
            return JNI_TRUE;
        }
        else
        if (arrayType == DTWAIN_ARRAYSTRING || arrayType == DTWAIN_ARRAYANSISTRING)
        {
            auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
            std::vector<std::string> allValues(sz);
            for (LONG i = 0; i < sz; ++i)
                allValues[i] = API_INSTANCE DTWAIN_ArrayGetAtANSIStringPtr(aTmp, i);
            JavaArrayListHandler<ArrayStringList<ArrayStringCharTraitsA>> aHandler(env);
            jobject jReturn = aHandler.NativeToJava(retList, allValues);
            return JNI_TRUE;
        }
        else
        if (arrayType == DTWAIN_ARRAYFRAME)
        {
            auto sz = API_INSTANCE DTWAIN_ArrayGetCount(aTmp);
            std::vector<FrameStruct> allValues(sz);
            for (LONG i = 0; i < sz; ++i)
                API_INSTANCE DTWAIN_ArrayFrameGetAt(aTmp, i, &allValues[i].left, &allValues[i].top, &allValues[i].right, &allValues[i].bottom );
            JavaArrayListHandler<ArrayFrameList> aHandler(env);
            jobject jReturn = aHandler.NativeToJava(retList, allValues);
            return JNI_TRUE;
        }
    }
    return JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesStringEx
 * Signature: (JIII)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesStringEx
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesStringEx2
 * Signature: (JIIII)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesStringEx2
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx2, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, &arr);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesDouble
 * Signature: (JII)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesDouble
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValues, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesIntEx
 * Signature: (JIII)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesIntEx
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, &arr);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesDoubleEx
 * Signature: (JIII)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesDoubleEx
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesIntEx2
 * Signature: (JIIII)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesIntEx2
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx2, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesDoubleEx2
 * Signature: (JIIII)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesDoubleEx2
(JNIEnv *env, jobject, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    DO_DTWAIN_TRY
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaDoubleArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetCapValuesEx2, reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3, arg4, arg5, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetFileXferFormat
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetFileXferFormat
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetFileXferFormat(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCompressionType
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCompressionType
  (JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetCompressionType(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPrinter
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPrinter
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPrinter(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPrinterStringMode
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPrinterStringMode
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPrinterStringMode(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetOrientation
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetOrientation
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetOrientation(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPaperSize
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPaperSize
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPaperSize(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetBitDepth
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetBitDepth
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetBitDepth(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetJobControl
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetJobControl
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetJobControl(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetManualDuplexMode
 * Signature: (JIZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetManualDuplexMode
(JNIEnv *env, jobject, jlong arg1, jint arg2, jboolean arg3)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetManualDuplexMode(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, arg3);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetAcquireArea
 * Signature: (JI)Lcom/dynarithmic/twain/highlevel/TwainAcquireArea;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetAcquireArea
  (JNIEnv *env, jobject, jlong arg1, jint arg2)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

  JavaTwainAcquireArea vArea(env);

  // Call the DTWAIN function to get the area information
  DTWAIN_ARRAY dArray = nullptr;
  BOOL bRet = API_INSTANCE DTWAIN_GetAcquireArea(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, &dArray);
  DTWAINArray_RAII raii(dArray);
  if ( bRet )
  {
      // Get the current unit of measure
      LONG unit = DTWAIN_INCHES;
      BOOL bRet2 = API_INSTANCE DTWAIN_GetSourceUnit(reinterpret_cast<DTWAIN_SOURCE>(arg1), &unit);
      // Call Java function to declare and init a new acquire area object
      auto pFloat = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dArray, 0));
      return vArea.createFullObject(*pFloat, *(pFloat + 1), *(pFloat + 2), *(pFloat + 3), unit);
  }

  return vArea.createDefaultObject();
  DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAcquireArea
 * Signature: (JILcom/dynarithmic/twain/highlevel/TwainAcquireArea;)Lcom/dynarithmic/twain/highlevel/TwainAcquireArea;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAcquireArea
  (JNIEnv* env, jobject, jlong arg1, jint arg2, jobject arg3)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaTwainAcquireArea vArea(env);
    vArea.setObject(arg3);

    typedef double (JavaTwainAcquireArea::*areaFn)(void);
    std::array<areaFn, 4> fnGet = {&JavaTwainAcquireArea::getLeft, &JavaTwainAcquireArea::getTop,
                                   &JavaTwainAcquireArea::getRight, &JavaTwainAcquireArea::getBottom};

    // Call the DTWAIN function to set the area information
    DTWAIN_ARRAY dSetArray = API_INSTANCE DTWAIN_ArrayCreate(DTWAIN_ARRAYFLOAT, 4);
    DTWAIN_ARRAY dReturnArray = API_INSTANCE DTWAIN_ArrayCreate(DTWAIN_ARRAYFLOAT, 4);
    DTWAINArray_RAII raii1(dSetArray);
    DTWAINArray_RAII raii2(dReturnArray);
    LONG srcUnit;
    if ( dSetArray && dReturnArray )
    {
        auto pBuf = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dSetArray, 0));
        for (int i = 0; i < 5; ++i )
        {
            if ( i < 4 )
            {
                auto fn = fnGet[i];
                *(pBuf + i) = (vArea.*fn)();
            }
            else
                srcUnit = vArea.getUnitOfMeasure();
        }

        // Set the source unit first
        BOOL bRet2 = API_INSTANCE DTWAIN_SetSourceUnit(reinterpret_cast<DTWAIN_SOURCE>(arg1), srcUnit);

        pBuf = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dReturnArray, 0));

        BOOL bRet = API_INSTANCE DTWAIN_SetAcquireArea(reinterpret_cast<DTWAIN_SOURCE>(arg1), arg2, dSetArray, dReturnArray);
        if ( bRet )
        {
            // Get the current unit of measure
            LONG unit = DTWAIN_INCHES;
            API_INSTANCE DTWAIN_GetSourceUnit(reinterpret_cast<DTWAIN_SOURCE>(arg1), &unit);
            pBuf = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dReturnArray, 0));
            return vArea.createFullObject(*pBuf, *(pBuf + 1), *(pBuf + 2), *(pBuf + 3), unit);
        }
    }
    return vArea.createDefaultObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetImageInfo
 * Signature: (J)Lcom/dynarithmic/twain/highlevel/TwainImageInfo;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetImageInfo
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaTwainImageInfo vInfo(env);

    // Call function to get the image info
    DTWAIN_FLOAT xResolution, yResolution;
    LONG imageWidth, imageLength, samplesPerPixel, bitsPerPixel, pixelType, compression, planar;
    DTWAIN_ARRAY bitsPerSample = nullptr;
    DTWAIN_BOOL bRet = API_INSTANCE DTWAIN_GetImageInfo(reinterpret_cast<DTWAIN_SOURCE>(src),
                                            &xResolution,
                                            &yResolution,
                                            &imageWidth,
                                            &imageLength,
                                            &samplesPerPixel,
                                            &bitsPerSample,  // array
                                            &bitsPerPixel,
                                            &planar,
                                            &pixelType,
                                            &compression);
    DTWAINArray_RAII raii(bitsPerSample);
    if ( bRet )
    {
        TW_IMAGEINFO iInfo{};
        iInfo.BitsPerPixel = bitsPerPixel;
        iInfo.ImageLength = imageLength;
        iInfo.ImageWidth = imageWidth;
        iInfo.SamplesPerPixel = samplesPerPixel;
        iInfo.Compression = compression;
        iInfo.PixelType = pixelType;
        iInfo.Planar = planar;
        iInfo.XResolution = JavaDTwainLowLevel_TW_FIX32::fromDouble(xResolution);
        iInfo.YResolution = JavaDTwainLowLevel_TW_FIX32::fromDouble(yResolution);
        auto dataBuffer = reinterpret_cast<LONG *>(API_INSTANCE DTWAIN_ArrayGetBuffer(bitsPerSample,0));
        std::copy(dataBuffer, dataBuffer + 8, iInfo.BitsPerSample);
        JavaTwainImageInfo javaObject(env);
        jobject newObject = javaObject.NativeToJava(iInfo);
        return newObject;
    }
    return nullptr;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTwainLog
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTwainLog
  (JNIEnv *env, jobject, jint arg1, jstring arg2)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    GetStringCharsHandler str(env, arg2);
    API_INSTANCE DTWAIN_SetTwainLog(arg1, reinterpret_cast<LPCTSTR>(str.GetStringChars()));
    return 1;
    DO_DTWAIN_CATCH(env)
}

static int CalculateUsedPaletteEntries(int bit_count) {
    if ((bit_count >= 1) && (bit_count <= 8))
        return 1 << bit_count;
    return 0;
}


static jobject getFullImageBMPData(HANDLE hDib, JavaAcquirerInfo& jacqInfo, JNIEnv *env, bool isBMP)
{
    HandleRAII raii(hDib);
    LPBYTE pDibData = raii.getData();
    jobject imgObject = jacqInfo.CreateJavaImageDataObject(env);

    // attach file header if this is a DIB
    if ( isBMP )
    {
        BITMAPFILEHEADER fileheader;
        LPBITMAPINFOHEADER lpbi= nullptr;
        memset((char *)&fileheader,0,sizeof(BITMAPFILEHEADER));
        fileheader.bfType='MB';
        lpbi = (LPBITMAPINFOHEADER)pDibData;
        unsigned int bpp = lpbi->biBitCount;
        fileheader.bfSize      = static_cast<DWORD>(GlobalSize(hDib)) + sizeof (BITMAPFILEHEADER);
        fileheader.bfReserved1 = 0;
        fileheader.bfReserved2 = 0;
        fileheader.bfOffBits   = static_cast<DWORD>(sizeof(BITMAPFILEHEADER)) +
                                  lpbi->biSize + CalculateUsedPaletteEntries(bpp) * sizeof(RGBQUAD);
        // we need to attach the bitmap header info onto the data
        unsigned int totalSize = GlobalSize(hDib) + sizeof(BITMAPFILEHEADER);
        std::vector<BYTE> bFullImage(totalSize);
        auto pFileHeader = reinterpret_cast<char *>(&fileheader);
        std::copy(pFileHeader, pFileHeader + sizeof(BITMAPFILEHEADER), &bFullImage[0]);
        std::copy(pDibData, pDibData + GlobalSize(hDib), &bFullImage[sizeof(BITMAPFILEHEADER)]);
        jacqInfo.setImageData(env, imgObject, &bFullImage[0], totalSize);
    }
    else
        jacqInfo.setImageData(env, imgObject, pDibData, GlobalSize(hDib));
    return imgObject;
}

typedef DTWAIN_ARRAY (DLLENTRY_DEF *DTWAIN_AcquireFn)(DTWAIN_SOURCE Source,
                                                      LONG PixelType,
                                                      LONG nMaxPages,
                                                      DTWAIN_BOOL bShowUI,
                                                      DTWAIN_BOOL bCloseSource,
                                                      LPLONG pStatus);

jobject AcquireHandler(DTWAIN_AcquireFn fn, JNIEnv *env, jlong src, jint pixelType,
                        jint maxPages, jboolean showUI, jboolean closeSource, bool isBMP)
{
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY acq;
    LONG nStatus;
    std::pair<DTWAINJNIGlobals::CurrentAcquireTypeMap::iterator, bool> ret;
    ret = g_JNIGlobals.g_CurrentAcquireMap.insert(std::make_pair(reinterpret_cast<DTWAIN_SOURCE>(src), isBMP));
    if ( !ret.second )
        ret.first->second = isBMP;
    acq = fn(reinterpret_cast<DTWAIN_SOURCE>(src), pixelType, maxPages, showUI, closeSource, &nStatus);
    JavaAcquirerInfo jacqInfo(env);
    jobject arrayObject = jacqInfo.CreateJavaAcquisitionArrayObject(env);
    if ( acq )
    {
        LONG nAcquisitions = API_INSTANCE DTWAIN_GetNumAcquisitions(acq);
        for (LONG i = 0; i < nAcquisitions; ++i)
        {
            jobject acquisitionObject = jacqInfo.CreateJavaAcquisitionDataObject(env);
            LONG nDibs = API_INSTANCE DTWAIN_GetNumAcquiredImages(acq, i);
            for ( LONG j = 0; j < nDibs; ++j )
            {
                HANDLE hDib = API_INSTANCE DTWAIN_GetAcquiredImage(acq, i, j);
                if ( hDib )
                {
                    jobject imgObject = getFullImageBMPData(hDib, jacqInfo, env, isBMP);
                    jacqInfo.addImageDataToAcquisition(acquisitionObject, imgObject);
                }
            }
            jacqInfo.addAcquisitionToArray(arrayObject,acquisitionObject);
        }
        jacqInfo.setStatus(arrayObject, nStatus );
        API_INSTANCE DTWAIN_DestroyAcquisitionArray(acq, TRUE);
    }
    else
        g_JNIGlobals.g_CurrentAcquireMap.erase(reinterpret_cast<DTWAIN_SOURCE>(src));
    return arrayObject;
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_AcquireNative
 * Signature: (JIIZZ)Lcom/dynarithmic/twain/highlevel/TwainAcquisitionArray;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AcquireNative
  (JNIEnv *env, jobject, jlong src, jint pixelType, jint maxPages, jboolean showUI, jboolean closeSource)
{
    DO_DTWAIN_TRY
    return AcquireHandler(API_INSTANCE DTWAIN_AcquireNative, env, src, pixelType, maxPages, showUI, closeSource, true);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_AcquireBuffered
 * Signature: (JIIZZ)Lcom/dynarithmic/twain/highlevel/TwainAcquisitionArray;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AcquireBuffered
(JNIEnv *env, jobject, jlong src, jint pixelType, jint maxPages, jboolean showUI, jboolean closeSource)
{
    DO_DTWAIN_TRY
    LONG cmpType;
    API_INSTANCE DTWAIN_GetCompressionType(reinterpret_cast<DTWAIN_SOURCE>(src), &cmpType, TRUE);
    bool isBMPType = (cmpType == TWCP_NONE);
    return AcquireHandler(API_INSTANCE DTWAIN_AcquireBuffered, env, src, pixelType, maxPages, showUI, closeSource, isBMPType);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_AcquireFile
 * Signature: (JLjava/lang/String;IIIIZZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AcquireFile
  (JNIEnv *env, jobject, jlong src, jstring filename, jint filetype, jint fileflags,
        jint pixeltype, jint maxpages, jboolean showui, jboolean closesource)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    GetStringCharsHandler str(env, filename);
    LONG status;
    bool isBMP = (fileflags & DTWAIN_USESOURCEMODE)?false:true;
    std::pair<DTWAINJNIGlobals::CurrentAcquireTypeMap::iterator, bool> ret;
    ret = g_JNIGlobals.g_CurrentAcquireMap.insert(std::make_pair(reinterpret_cast<DTWAIN_SOURCE>(src), isBMP));
    if ( !ret.second )
        ret.first->second = isBMP;

    DTWAIN_BOOL bRet = API_INSTANCE DTWAIN_AcquireFile(reinterpret_cast<DTWAIN_SOURCE>(src), reinterpret_cast<LPCTSTR>(str.GetStringChars()), filetype, fileflags,
                                           pixeltype, maxpages, showui, closesource, &status);
    if ( bRet )
        return status;
    g_JNIGlobals.g_CurrentAcquireMap.erase(reinterpret_cast<DTWAIN_SOURCE>(src));
    return -1;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCustomDSData
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCustomDSData
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    LONG actualSize;
    HANDLE h = API_INSTANCE DTWAIN_GetCustomDSData(reinterpret_cast<DTWAIN_SOURCE>(src), nullptr, 0, &actualSize, DTWAINGCD_COPYDATA);
    if ( h && actualSize > 0 )
    {
        // create a vector of the correct size
        std::vector<BYTE> vBytes(actualSize);
        API_INSTANCE DTWAIN_GetCustomDSData(reinterpret_cast<DTWAIN_SOURCE>(src), &vBytes[0], actualSize, &actualSize, DTWAINGCD_COPYDATA);
        return CreateJArrayFromCArray<JavaByteArrayTraits<char> >(env, (JavaByteArrayTraits<char>::api_base_type*)&vBytes[0], vBytes.size());
    }
    BYTE b;
    return CreateJArrayFromCArray<JavaByteArrayTraits<char> >(env, (JavaByteArrayTraits<char> ::api_base_type*)&b, 0);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCustomDSData
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCustomDSData
  (JNIEnv *env, jobject, jlong src, jbyteArray customData)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    std::vector<char> dArray = CreateCArrayFromJArray<JavaByteArrayTraits<char> >(env, customData);
    if ( dArray.empty() )
        return 1;
    return API_INSTANCE DTWAIN_SetCustomDSData(reinterpret_cast<DTWAIN_SOURCE>(src), nullptr, (LPBYTE)&dArray[0], dArray.size(), DTWAINSCD_USEDATA);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAcquireImageScale
 * Signature: (JDD)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAcquireImageScale
  (JNIEnv *env, jobject, jlong src, jdouble xScale, jdouble yScale)
{
  DO_DTWAIN_TRY
  return API_INSTANCE DTWAIN_SetAcquireImageScale(reinterpret_cast<DTWAIN_SOURCE>(src), xScale, yScale);
  DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFOrientation
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFOrientation
  (JNIEnv *env, jobject, jlong src, jint orientation)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFOrientation(reinterpret_cast<DTWAIN_SOURCE>(src), orientation);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFPageSize
 * Signature: (JIDD)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFPageSize
  (JNIEnv *env, jobject, jlong src, jint pageSize, jdouble customWidth, jdouble customHeight)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFPageSize(reinterpret_cast<DTWAIN_SOURCE>(src), pageSize, customWidth, customHeight);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFPageScale
 * Signature: (JIDD)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFPageScale
  (JNIEnv *env, jobject, jlong src, jint scaleOpts, jdouble xScale, jdouble yScale)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFPageScale(reinterpret_cast<DTWAIN_SOURCE>(src), scaleOpts, xScale, yScale);
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetAppInfo
* Signature: (Lcom/dynarithmic/twain/highlevel/TwainAppInfo;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAppInfo
(JNIEnv *env, jobject, jobject appInfoObj)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaTwainAppInfo appInfo(env);
    appInfo.setObject(appInfoObj);
    DTWAIN_BOOL bRet = API_INSTANCE DTWAIN_SetAppInfo(reinterpret_cast<LPCTSTR>(appInfo.getVersionInfo().c_str()),
                                        reinterpret_cast<LPCTSTR>(appInfo.getManufacturer().c_str()),
                                        reinterpret_cast<LPCTSTR>(appInfo.getProductFamily().c_str()),
                                        reinterpret_cast<LPCTSTR>(appInfo.getProductName().c_str()));
    return bRet;
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetAppInfo
* Signature: ()Lcom/dynarithmic/twain/highlevel/TwainAppInfo;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetAppInfo
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    TCHAR szInfo[4][1024];
    JavaTwainAppInfo appInfo(env);

    DTWAIN_BOOL bRet = API_INSTANCE DTWAIN_GetAppInfo(&szInfo[0][0],&szInfo[1][0],&szInfo[2][0],&szInfo[3][0]);

    if ( bRet )
        return appInfo.createDTwainAppInfo(&szInfo[0][0], &szInfo[1][0], &szInfo[2][0], &szInfo[3][0]);
    return appInfo.createDTwainAppInfo();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetSourceInfo
 * Signature: (J)Lcom/dynarithmic/twain/DTwainSourceInfo;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetSourceInfo
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaDTwainSourceInfo sInfo(env);

    TCHAR szInfo[4][1024];
    typedef LONG (__stdcall *SourceFn)(DTWAIN_SOURCE, LPTSTR, LONG);
    SourceFn allFn[4] = {API_INSTANCE DTWAIN_GetSourceVersionInfo, API_INSTANCE DTWAIN_GetSourceManufacturer, API_INSTANCE DTWAIN_GetSourceProductFamily,
                         API_INSTANCE DTWAIN_GetSourceProductName};

    BOOL bRet = TRUE;
    for (int i = 0; i < 4; ++i )
        bRet = (*allFn[i])(reinterpret_cast<DTWAIN_SOURCE>(src), &szInfo[i][0], 1023);
    if ( bRet )
    {
        LONG major, minor;
        API_INSTANCE DTWAIN_GetSourceVersionNumber(reinterpret_cast<DTWAIN_SOURCE>(src), &major, &minor);
        return sInfo.createFullObject(szInfo[0], szInfo[1], szInfo[2], szInfo[3], major, minor);
    }
    return sInfo.defaultConstructObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetAuthor
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetAuthor
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    TCHAR arg2[1024] = {0};
    API_INSTANCE DTWAIN_GetAuthor(reinterpret_cast<DTWAIN_SOURCE>(src), arg2);
    return CreateJStringFromCString(env, arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCaption
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCaption
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    TCHAR arg2[1024] = {0};
    API_INSTANCE DTWAIN_GetCaption(reinterpret_cast<DTWAIN_SOURCE>(src), arg2);
    return CreateJStringFromCString(env, arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetNameFromCap
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetNameFromCap
  (JNIEnv *env, jobject, jint capValue)
{
    DO_DTWAIN_TRY
    TCHAR arg2[1024] = {0};
    API_INSTANCE DTWAIN_GetNameFromCap(capValue, arg2, 1023);
    return CreateJStringFromCString(env, arg2);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_StartThread
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1StartThread
  (JNIEnv *env, jobject, jlong dllHandle)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_StartThread((DTWAIN_HANDLE)dllHandle)?JNI_TRUE:JNI_FALSE;;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EndThread
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EndThread
  (JNIEnv *env, jobject, jlong dllHandle)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_EndThread(reinterpret_cast<DTWAIN_HANDLE>(dllHandle))?JNI_TRUE:JNI_FALSE;;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectSourceByName
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectSourceByName
  (JNIEnv *env, jobject, jstring srcName)
{
    DO_DTWAIN_TRY
    return (jlong)API_INSTANCE DTWAIN_SelectSourceByName(reinterpret_cast<LPCTSTR>(GetStringCharsHandler(env, srcName).GetStringChars()));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapFromName
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapFromName
  (JNIEnv *env, jobject, jstring capName)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_GetCapFromName(reinterpret_cast<LPCTSTR>(GetStringCharsHandler(env, capName).GetStringChars()));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAuthor
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAuthor
  (JNIEnv *env, jobject, jlong src, jstring author)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetAuthor(reinterpret_cast<DTWAIN_SOURCE>(src), reinterpret_cast<LPCTSTR>(GetStringCharsHandler(env, author).GetStringChars()));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCaption
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCaption
  (JNIEnv *env, jobject, jlong src, jstring caption)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetCaption(reinterpret_cast<DTWAIN_SOURCE>(src), (LPCTSTR)GetStringCharsHandler(env, caption).GetStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFAuthor
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFAuthor
  (JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFAuthor(reinterpret_cast<DTWAIN_SOURCE>(src),
                                (LPCTSTR)GetStringCharsHandler(env, pdfStr).GetStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFCreator
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFCreator
(JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFCreator(reinterpret_cast<DTWAIN_SOURCE>(src),
        GetStringCharsHandler(env, pdfStr).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFTitle
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFTitle
(JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFTitle(reinterpret_cast<DTWAIN_SOURCE>(src),
        GetStringCharsHandler(env, pdfStr).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFSubject
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFSubject
(JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFSubject(reinterpret_cast<DTWAIN_SOURCE>(src),
        GetStringCharsHandler(env, pdfStr).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFKeywords
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFKeywords
(JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFKeywords(reinterpret_cast<DTWAIN_SOURCE>(src),
        GetStringCharsHandler(env, pdfStr).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPostScriptTitle
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPostScriptTitle
(JNIEnv *env, jobject, jlong src, jstring pdfStr)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPostScriptTitle(reinterpret_cast<DTWAIN_SOURCE>(src),
        GetStringCharsHandler(env, pdfStr).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetTempFileDirectory
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetTempFileDirectory
  (JNIEnv *env, jobject, jstring str)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetTempFileDirectory((LPCTSTR)GetStringCharsHandler(env, str).GetStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFEncryption
 * Signature: (JZLjava/lang/String;Ljava/lang/String;IZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFEncryption
  (JNIEnv *env, jobject, jlong src, jboolean useEncryption, jstring userPass, jstring ownerPass,
                jint permissions, jboolean useStrong)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetPDFEncryption(reinterpret_cast<DTWAIN_SOURCE>(src), useEncryption,
                                   GetStringCharsHandler(env, userPass).GetWindowsStringChars(),
                                   GetStringCharsHandler(env, ownerPass).GetWindowsStringChars(),
                                   permissions, useStrong);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsDIBBlank
 * Signature: (JD)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsDIBBlank
  (JNIEnv *env, jobject, jlong dibHandle, jdouble threshold)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_IsDIBBlank(reinterpret_cast<HANDLE>(dibHandle), threshold)?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetTempFileDirectory
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTempFileDirectory
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    TCHAR szDir[_MAX_PATH] = {0};
    API_INSTANCE DTWAIN_GetTempFileDirectory(szDir, _MAX_PATH);
    return CreateJStringFromCString(env, szDir);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetBlankPageDetection
(JNIEnv *env, jobject, jlong src, jdouble threshold, jint autodetect, jint detectOptions, jboolean bSet)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_SetBlankPageDetectionEx(reinterpret_cast<DTWAIN_SOURCE>(src), threshold,
                                            autodetect, detectOptions, bSet);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_LogMessage
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1LogMessage
  (JNIEnv *env, jobject, jstring str)
{
    DO_DTWAIN_TRY
    return API_INSTANCE DTWAIN_LogMessage(GetStringCharsHandler(env, str).GetWindowsStringChars());
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectSource2
 * Signature: (Ljava/lang/String;III)J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectSource2
  (JNIEnv *env, jobject, jstring title, jint xpos, jint ypos, jint flags)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectSource2(static_cast<HWND>(nullptr),
                                                        GetStringCharsHandler(env, title).GetWindowsStringChars(),
                                                        xpos, ypos, flags));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetErrorString
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetErrorString
  (JNIEnv *env, jobject, jint nError)
{
    DO_DTWAIN_TRY
    LONG nLen = API_INSTANCE DTWAIN_GetErrorString(nError, static_cast<LPTSTR>(0), 0);
    if ( nLen > 0 )
    {
        std::vector<TCHAR> sz(nLen+1);
        API_INSTANCE DTWAIN_GetErrorString(nError, &sz[0], nLen);
        return CreateJStringFromCString(env, &sz[0]);
    }
    return CreateJStringFromCString(env, _T(""));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_AcquireFileEx
 * Signature: (J[Ljava/lang/String;IIIIZZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AcquireFileEx
  (JNIEnv *env, jobject, jlong src, jobjectArray strArray, jint filetype, jint fileflags,
      jint pixeltype, jint numpages, jboolean showUI, jboolean closeSource)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    LONG nStatus;
    DTWAIN_ARRAY dArray = CreateDTWAINArrayFromJStringArray(env, strArray);
    DTWAINArray_RAII raii(dArray);
    BOOL bRet = API_INSTANCE DTWAIN_AcquireFileEx(reinterpret_cast<DTWAIN_SOURCE>(src), dArray, filetype, fileflags, pixeltype, numpages, showUI, closeSource, &nStatus);
    return nStatus;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCurrentAcquiredImage
 * Signature: (J)Lcom/dynarithmic/twain/highlevel/TwainImageData;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCurrentAcquiredImage
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    HANDLE hDib = API_INSTANCE DTWAIN_GetCurrentAcquiredImage(reinterpret_cast<DTWAIN_SOURCE>(src));
    JavaAcquirerInfo acqInfo(env);
    auto it = g_JNIGlobals.g_CurrentAcquireMap.find(reinterpret_cast<DTWAIN_SOURCE>(src));
    bool isBMP = true;
    if (it != g_JNIGlobals.g_CurrentAcquireMap.end())
        isBMP = it->second;
    jobject imgData = getFullImageBMPData(hDib, acqInfo, env, isBMP);
    return imgData;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesFrame
 * Signature: (JII[Lcom/dynarithmic/twain/highlevel/TwainFrameDouble;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesFrame
  (JNIEnv *env, jobject, jlong src, jint capValue, jint setType, jobjectArray FrameArray)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY dFrameArray = CreateDTWAINArrayFromJFrameArray(env, FrameArray);
    DTWAINArray_RAII raii(dFrameArray);
    return API_INSTANCE DTWAIN_SetCapValues(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, setType, dFrameArray);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesFrameEx
 * Signature: (JIII[Lcom/dynarithmic/twain/highlevel/TwainFrameDouble;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesFrameEx
  (JNIEnv *env, jobject, jlong src, jint capValue, jint setType, jint containerType, jobjectArray FrameArray)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY dFrameArray = CreateDTWAINArrayFromJFrameArray(env, FrameArray);
    DTWAINArray_RAII raii(dFrameArray);
    return API_INSTANCE DTWAIN_SetCapValuesEx(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, setType, containerType, dFrameArray);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetCapValuesFrameEx2
 * Signature: (JIIII[Lcom/dynarithmic/twain/highlevel/TwainFrameDouble;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCapValuesFrameEx2
(JNIEnv *env, jobject, jlong src, jint capValue, jint setType, jint containerType, jint nDataType, jobjectArray FrameArray)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY dFrameArray = CreateDTWAINArrayFromJFrameArray(env, FrameArray);
    DTWAINArray_RAII raii(dFrameArray);
    return API_INSTANCE DTWAIN_SetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, setType, containerType, nDataType, dFrameArray);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesFrame
 * Signature: (JII)[Lcom/dynarithmic/twain/DTwainFrame;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesFrame
  (JNIEnv *env, jobject, jlong src, jint capValue, jint getType)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArray_RAII raii(arr);
    API_INSTANCE DTWAIN_GetCapValues(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, getType, &arr);
    return CreateJFrameArrayFromDTWAINArray(env, arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesFrameEx
 * Signature: (JIII)[Lcom/dynarithmic/twain/highlevel/TwainFrameDouble;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesFrameEx
  (JNIEnv *env, jobject, jlong src, jint capValue, jint getType, jint containerType)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArray_RAII raii(arr);
    API_INSTANCE DTWAIN_GetCapValuesEx(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, getType, containerType, &arr);
    return CreateJFrameArrayFromDTWAINArray(env, arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetCapValuesFrameEx2
 * Signature: (JIIII)[Lcom/dynarithmic/twain/highlevel/TwainFrameDouble;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCapValuesFrameEx2
  (JNIEnv *env, jobject, jlong src, jint capValue, jint getType, jint containerType, jint dataType)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArray_RAII raii(arr);
    API_INSTANCE DTWAIN_GetCapValuesEx2(reinterpret_cast<DTWAIN_SOURCE>(src), capValue, getType, containerType, dataType, &arr);
    return CreateJFrameArrayFromDTWAINArray(env, arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetAcquireArea2
 * Signature: (JLcom/dynarithmic/twain/DTwainAcquireArea;I)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAcquireArea2
  (JNIEnv *env, jobject, jlong src, jobject jFrame, jint flags)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaTwainAcquireArea vArea(env);
    vArea.setObject(jFrame);

    // Call the DTWAIN function to set the area information
    DTWAIN_ARRAY dSetArray = API_INSTANCE DTWAIN_ArrayCreate(DTWAIN_ARRAYFLOAT, 4);
    DTWAINArray_RAII raii1(dSetArray);
    LONG srcUnit;
    if ( dSetArray )
    {
        const char* fnName[] = {"getLeft", "getTop", "getRight", "getBottom", "getUnitOfMeasure"};
        auto pBuf = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dSetArray, 0));
        for (int i = 0; i < 5; ++i )
        {
            if ( i < 4 )
                *(pBuf + i) = vArea.callDoubleMethod(fnName[i]);
            else
                srcUnit = vArea.callIntMethod(fnName[i]);
        }

        API_INSTANCE DTWAIN_SetAcquireArea2(reinterpret_cast<DTWAIN_SOURCE>(src),
                               (*pBuf), *(pBuf + 1), *(pBuf + 2), *(pBuf + 3), srcUnit, flags);
    }
    return FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetAcquireArea2
 * Signature: (J)Lcom/dynarithmic/twain/DTwainAcquireArea;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetAcquireArea2
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    JavaTwainAcquireArea vArea(env);

    // Call the DTWAIN function to get the area information
    DTWAIN_ARRAY dArray = nullptr;
    double dLoc[4];
    LONG unit = DTWAIN_INCHES;
    BOOL bRet = API_INSTANCE DTWAIN_GetAcquireArea2(reinterpret_cast<DTWAIN_SOURCE>(src), &dLoc[0], &dLoc[1], &dLoc[2], &dLoc[3], &unit);
    DTWAINArray_RAII raii(dArray);
    if ( bRet )
    {
        auto pFloat = static_cast<DTWAIN_FLOAT*>(API_INSTANCE DTWAIN_ArrayGetBuffer(dArray, 0));
        return vArea.createFullObject(*pFloat, *(pFloat + 1), *(pFloat + 2), *(pFloat + 3), unit);
    }

    // Call Java function to declare and init a new versionInfo object
    return vArea.defaultConstructObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SelectOCREngineByName
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectOCREngineByName
  (JNIEnv *env, jobject, jstring ocrName)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return reinterpret_cast<jlong>(API_INSTANCE DTWAIN_SelectOCREngineByName(GetStringCharsHandler(env,ocrName).GetWindowsStringChars()));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumOCRInterfaces
 * Signature: ()[J
 */
JNIEXPORT jlongArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumOCRInterfaces
  (JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaLongArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumOCRInterfaces, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumOCRSupportedCaps
 * Signature: (J)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumOCRSupportedCaps
  (JNIEnv *env, jobject, jlong ocr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_EnumOCRSupportedCaps, reinterpret_cast<DTWAIN_OCRENGINE>(ocr), &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetOCRCapValuesInt
 * Signature: (JII)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetOCRCapValuesInt
  (JNIEnv *env, jobject, jlong ocr, jint capValue, jint getType)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaIntArrayTraits>(env, &arr, API_INSTANCE DTWAIN_GetOCRCapValues, reinterpret_cast<DTWAIN_OCRENGINE>(ocr), capValue, getType, &arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetOCRCapValuesString
 * Signature: (JII)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetOCRCapValuesString
  (JNIEnv *env, jobject, jlong ocr, jint capValue, jint getType)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = nullptr;
    DTWAINArrayPtr_RAII raii(&arr);
    return CallFnReturnArray<JavaStringTraits>(env, &arr, API_INSTANCE DTWAIN_GetOCRCapValues, reinterpret_cast<DTWAIN_OCRENGINE>(ocr), capValue, getType, &arr);
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetOCRCapValuesInt
(JNIEnv *env, jobject, jlong ocr, jint capValue, jint setType, jintArray jarr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = CreateDTWAINArrayFromJArray<JavaIntArrayTraits>(env, jarr);
    DTWAINArray_RAII raii(arr);
    return API_INSTANCE DTWAIN_SetOCRCapValues(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), capValue, setType, arr);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetOCRCapValuesString
 * Signature: (JII[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetOCRCapValuesString
  (JNIEnv *env, jobject, jlong ocr, jint capValue, jint setType, jobjectArray jarr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    DTWAIN_ARRAY arr = CreateDTWAINArrayFromJStringArray(env, jarr);
    DTWAINArray_RAII raii(arr);
    return API_INSTANCE DTWAIN_SetOCRCapValues(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), capValue, setType, arr);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ShutdownOCREngine
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ShutdownOCREngine
  (JNIEnv *env, jobject, jlong ocr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_ShutdownOCREngine(reinterpret_cast<DTWAIN_OCRENGINE>(ocr));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_IsOCREngineActivated
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1IsOCREngineActivated
  (JNIEnv *env, jobject, jlong ocr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_IsOCREngineActivated(reinterpret_cast<DTWAIN_OCRENGINE>(ocr))?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFOCRConversion
 * Signature: (JIIIII)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFOCRConversion
  (JNIEnv *env, jobject, jlong ocr, jint pageType, jint fileType, jint pixelType, jint bitDepth, jint options)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_SetPDFOCRConversion(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), pageType, fileType, pixelType, bitDepth, options);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetPDFOCRMode
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetPDFOCRMode
  (JNIEnv *env, jobject, jlong ocr, jboolean bSet)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_SetPDFOCRMode(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), bSet);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_ExecuteOCR
 * Signature: (JLjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1ExecuteOCR
  (JNIEnv *env, jobject, jlong ocr, jstring szFileName, jint startPage, jint endPage)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_ExecuteOCR(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), GetStringCharsHandler(env, szFileName).GetWindowsStringChars(),
                                     startPage, endPage);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetOCRInfo
 * Signature: (J)Lcom/dynarithmic/twain/highlevel/TwainOCRInfo;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetOCRInfo
  (JNIEnv *env, jobject, jlong ocr)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaTwainOCRInfo vOCR(env);
    TCHAR szInfo[4][1024];

    typedef LONG (__stdcall *OCRFn)(DTWAIN_OCRENGINE, LPTSTR, LONG);
    OCRFn allFn[4] = {API_INSTANCE DTWAIN_GetOCRVersionInfo,
                    API_INSTANCE DTWAIN_GetOCRManufacturer,
                    API_INSTANCE DTWAIN_GetOCRProductFamily,
                    API_INSTANCE DTWAIN_GetOCRProductName};

    BOOL bRet = TRUE;
    for (int i = 0; i < 4; ++i )
        bRet = (*allFn[i])((DTWAIN_OCRENGINE)ocr, &szInfo[i][0], 1023);
    if ( bRet )
        return vOCR.createFullObject(szInfo[0], szInfo[1], szInfo[2], szInfo[3]);
    return vOCR.defaultConstructObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetOCRText
 * Signature: (JI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetOCRText
  (JNIEnv *env, jobject, jlong ocr, jint pageNum)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    LONG actualSize;
    HANDLE hReturn = API_INSTANCE DTWAIN_GetOCRText(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), pageNum, static_cast<LPTSTR>(0), 0, &actualSize, DTWAINOCR_COPYDATA);
    if (hReturn)
    {
        std::vector<TCHAR> ts(actualSize);
        API_INSTANCE DTWAIN_GetOCRText(reinterpret_cast<DTWAIN_OCRENGINE>(ocr), pageNum, &ts[0], ts.size(), &actualSize, DTWAINOCR_COPYDATA);
        return CreateJArrayFromCArray<JavaByteArrayTraits<TCHAR> >(env, &ts[0], ts.size()*sizeof(TCHAR));
    }
    return CreateJArrayFromCArray<JavaByteArrayTraits<TCHAR> >(env, nullptr,0);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_CreateBufferedStripInfo
 * Signature: (J)Lcom/dynarithmic/twain/highlevel/BufferedStripInfo;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CreateBufferedStripInfo
  (JNIEnv *env, jobject, jlong src)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaBufferedStripInfo sInfo(env);

    LONG minSize, maxSize, prefSize;
    BOOL bRet = API_INSTANCE DTWAIN_GetAcquireStripSizes(reinterpret_cast<DTWAIN_SOURCE>(src), &minSize, &maxSize, &prefSize);
    if ( bRet )
    {
        jobject jobj = sInfo.createFullObject(prefSize, minSize, maxSize);
        sInfo.setObject(jobj);
        sInfo.setBufferSize(prefSize);
        return sInfo.getObject();
    }
    return sInfo.defaultConstructObject();
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetBufferedTransferInfo
 * Signature: (JLcom/dynarithmic/twain/highlevel/BufferedStripInfo;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetBufferedTransferInfo__JLcom_dynarithmic_twain_highlevel_BufferedStripInfo_2
  (JNIEnv *env, jobject, jlong src, jobject jBufferedStripInfo)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaBufferedStripInfo jBufInfo(env);
    jBufInfo.setObject(jBufferedStripInfo);
    LONG nSize = jBufInfo.getBufferSize();
    bool appHandlesStrips = jBufInfo.isAppAllocatesBuffer();
    HANDLE hnd = {};
    if ( appHandlesStrips )
    {
        hnd = GlobalAlloc(GHND, nSize);
        jBufInfo.setBufferHandle(hnd);
        API_INSTANCE DTWAIN_SetAcquireStripBuffer(reinterpret_cast<DTWAIN_SOURCE>(src), hnd);
    }
    else
        API_INSTANCE DTWAIN_SetAcquireStripSize(reinterpret_cast<DTWAIN_SOURCE>(src), nSize);
    jBufInfo.setBufferSize(nSize);
    if ( appHandlesStrips )
        return hnd?JNI_TRUE:JNI_FALSE;
    return JNI_TRUE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_SetBufferedTransferInfo
 * Signature: (JLcom/dynarithmic/twain/highlevel/BufferedTileInfo;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetBufferedTransferInfo__JLcom_dynarithmic_twain_highlevel_BufferedTileInfo_2
                                (JNIEnv* env, jobject, jlong source, jobject bInfo)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    BOOL bRet = API_INSTANCE DTWAIN_SetBufferedTileMode(reinterpret_cast<DTWAIN_SOURCE>(source), bInfo?TRUE:FALSE);
    return bRet?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

static TW_IMAGEMEMXFER GetBufferedMemXFerData(DTWAIN_SOURCE source)
{
    TW_IMAGEMEMXFER memxferInfo{};
    DWORD allVals[9] = {};
    std::array<pTW_UINT32, 8> xferVals = { &memxferInfo.BytesPerRow, &memxferInfo.Columns, &memxferInfo.Rows,
                                          &memxferInfo.XOffset, &memxferInfo.YOffset, &memxferInfo.BytesWritten, &memxferInfo.Memory.Flags,
                                          &memxferInfo.Memory.Length };

    HANDLE bRetHandle = API_INSTANCE DTWAIN_GetBufferedTransferInfo(reinterpret_cast<DTWAIN_SOURCE>(source), &allVals[0], &allVals[1], &allVals[2],
        &allVals[3], &allVals[4], &allVals[5], &allVals[6], &allVals[7], &allVals[8]);
    memxferInfo.Compression = allVals[0];
    memxferInfo.Memory.TheMem = bRetHandle;
    for (int i = 0; i < 8; ++i)
        *(xferVals[i]) = allVals[i + 1];
    return memxferInfo;
}

/*
 * Class:   com_dynarithmic_twain_DTwainJavaAPI
 * Method : DTWAIN_GetBufferedTileInfo
 * Signature : (J)Lcom / dynarithmic / twain / highlevel / BufferedTileInfo;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBufferedTileInfo
(JNIEnv* env, jobject, jlong source)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaBufferedTileInfo jInfo(env);
    return jInfo.createFullObject(GetBufferedMemXFerData(reinterpret_cast<DTWAIN_SOURCE>(source)));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBufferedTransferInfo
 * Signature: (J)Lcom/dynarithmic/twain/lowlevel/TW_IMAGEMEMXFER;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBufferedTransferInfo
(JNIEnv* env, jobject, jlong source)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaDTwainLowLevel_TW_IMAGEMEMXFER jInfo(env);
    return jInfo.NativeToJava(GetBufferedMemXFerData(reinterpret_cast<DTWAIN_SOURCE>(source)));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetBufferedStripData
 * Signature: (JLcom/dynarithmic/twain/highlevel/BufferedStripInfo;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetBufferedStripData
  (JNIEnv *env, jobject obj, jlong src, jobject jBufferedStripInfo)
{
    DO_DTWAIN_TRY
    struct HandleRAII
    {
        HANDLE m_h;
        HandleRAII(HANDLE h) : m_h(h) {}
        ~HandleRAII() { GlobalUnlock(m_h); }
    };

    DO_DTWAIN_CHECK_MODULE_LOAD
    jobject imginfo = Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetImageInfo(env, obj, src);
    JavaBufferedStripInfo jInfo(env);
    jInfo.setObject(jBufferedStripInfo);
    jInfo.setImageInfo(imginfo);
    LONG Compression, BytesPerRow, Columns, Rows, XOffset, YOffset, BytesWritten;
    BOOL bRet = API_INSTANCE DTWAIN_GetAcquireStripData(reinterpret_cast<DTWAIN_SOURCE>(src), &Compression, &BytesPerRow, &Columns,
                                            &Rows, &XOffset, &YOffset, &BytesWritten);
    if ( bRet )
    {
        jInfo.setBufferStripInfo(Columns, Rows, XOffset, YOffset, BytesWritten, BytesPerRow);
        HANDLE h = jInfo.getBufferHandle();
        auto strip = static_cast<BYTE*>(GlobalLock(h));
        HandleRAII raii(h);
        if ( strip )
            jInfo.setBufferedStripData(strip, BytesWritten);
    }
    return bRet;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EndBufferedTransfer
 * Signature: (JLcom/dynarithmic/twain/highlevel/BufferedStripInfo;)I
 */
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EndBufferedTransfer
  (JNIEnv *env, jobject, jlong src, jobject jBufferedTransfer)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaBufferedStripInfo jInfo(env);
    jInfo.setObject(jBufferedTransfer);
    HANDLE h = jInfo.getBufferHandle();
    GlobalFree(h);
    return JNI_TRUE;
    DO_DTWAIN_CATCH(env)
}

JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetResourcePath
(JNIEnv *env, jobject, jstring sResourcePath)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    GetStringCharsHandler handler(env, sResourcePath);
    auto s = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    return static_cast<jint>(API_INSTANCE DTWAIN_SetResourcePath(s));
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetLibraryPath
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetLibraryPath
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    LONG retLength = API_INSTANCE DTWAIN_GetLibraryPath(nullptr, 0);
    if (retLength > 0)
    {
        std::vector<TCHAR> arg(retLength + 1);
        API_INSTANCE DTWAIN_GetLibraryPath(arg.data(), arg.size());
        return CreateJStringFromCString(env, arg.data());
    }
    TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetVersionString
* Signature: (J)Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetVersionString
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    LONG retLength = API_INSTANCE DTWAIN_GetVersionString(nullptr, 0);
    if (retLength > 0)
    {
        std::vector<TCHAR> arg(retLength + 1);
        API_INSTANCE DTWAIN_GetVersionString(arg.data(), arg.size());
        return CreateJStringFromCString(env, arg.data());
    }
    const TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetShortVersionString
* Signature: (J)Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetShortVersionString
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    LONG retLength = API_INSTANCE DTWAIN_GetShortVersionString(nullptr, 0);
    if (retLength > 0)
    {
        std::vector<TCHAR> arg(retLength + 1);
        API_INSTANCE DTWAIN_GetShortVersionString(arg.data(), arg.size());
        return CreateJStringFromCString(env, arg.data());
    }
    TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetDSMSearchOrderEx
* Signature: (I)Ljava/lang/String;Ljava/lang/String;
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetDSMSearchOrderEx
(JNIEnv *env, jobject, jstring arg1, jstring arg2)
{
    DO_DTWAIN_TRY
    GetStringCharsHandler handler1(env, arg1);
    GetStringCharsHandler handler2(env, arg2);
    auto s1 = reinterpret_cast<LPCTSTR>(handler1.GetStringChars());
    auto s2 = reinterpret_cast<LPCTSTR>(handler2.GetStringChars());
    return static_cast<jint>(API_INSTANCE DTWAIN_SetDSMSearchOrderEx(s1, s2));
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetTwainAppID
* Signature: ()Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTwainAppID
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    TW_IDENTITY twIdentity = {};
    auto tId = API_INSTANCE DTWAIN_GetTwainAppIDEx(&twIdentity);
    JavaDTwainLowLevel_TW_IDENTITY testClass(env);
    testClass.NativeToJava(twIdentity);
    return testClass.getObject();
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetSourceID
* Signature: (J)Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetSourceID
(JNIEnv *env, jobject, jlong theSource)
{
    DO_DTWAIN_TRY
    TW_IDENTITY twIdentity = {};
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto tId = API_INSTANCE DTWAIN_GetSourceIDEx(reinterpret_cast<DTWAIN_SOURCE>(theSource), &twIdentity);
    JavaDTwainLowLevel_TW_IDENTITY testClass(env);
    jobject jobj = testClass.defaultConstructObject();
    testClass.setObject(jobj);
    testClass.NativeToJava(twIdentity);
    return jobj;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_StartTwainSession
* Signature: ()Z
*/
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1StartTwainSession
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return API_INSTANCE DTWAIN_StartTwainSession(static_cast<HWND>(0), nullptr) ? JNI_TRUE : JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

struct DSMRawRAII
{
    JNITripletTranslatorBase* pBase;
    void *pRaw;
    DSMRawRAII(JNITripletTranslatorBase* p, JNIEnv* pEnv, jobject jobj) : pBase(p), pRaw(nullptr)
    {
        pRaw = pBase->TranslateRaw(pEnv, jobj, pBase->m_TwainClassName);
    }

    void *getRaw() const
    {
        void* rawPtr = pBase->GetRaw();
        if (!rawPtr)
            return pRaw;
        return rawPtr;
    }

    ~DSMRawRAII()
    {
        pBase->DestroyRaw(pRaw);
    }
};

static std::array<std::string, 3> GetTripletNames(TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 TMSG)
{
    std::array<std::string, 3> sTwainNames;
    char twainName[1024];
    std::array<std::string, 3> unknownNames = {"DG_<unknown>", "DAT_<unknown>", "<MSG_unknown>"};
    API_INSTANCE DTWAIN_GetTwainStringNameA(DTWAIN_DGNAME, DG, twainName, 1023);
    sTwainNames[0] = twainName;
    API_INSTANCE DTWAIN_GetTwainStringNameA(DTWAIN_DATNAME, DAT, twainName, 1023);
    sTwainNames[1] = twainName;
    API_INSTANCE DTWAIN_GetTwainStringNameA(DTWAIN_MSGNAME, TMSG, twainName, 1023);
    sTwainNames[2] = twainName;
    for (int i = 0; i < 3; ++i)
    {
        if (sTwainNames[i].empty())
            sTwainNames[i] = unknownNames[i];
    }
    return sTwainNames;
}

static jint CallDSMHelper(JNIEnv* env, jobject appID, jobject sourceID, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 TMSG, jobject twmemref)
{
    // Now check if the object being passed matches the triplet being used
    auto translator = g_JNIGlobals.CheckCallDSMObject(env, DG, DAT, TMSG, twmemref);
    if (!translator.m_tripletPtr)
    {
        auto sTwainNames = GetTripletNames(DG, DAT, TMSG);
        std::ostringstream strm;
        strm << "Incorrect object passed to low-level Data Source Manager for triplet ["
            << sTwainNames[0] << "="<< DG << ", "
            << sTwainNames[1] << "=" << DAT << ", "
            << sTwainNames[2] << "=" << TMSG << "]:\nSource object = [" << translator.m_sSourceType << "]  "
            << "Expected object = [" << translator.m_expectedType << "]";
        JavaExceptionThrower::ThrowJavaException(env, strm.str());
        return TWRC_WRONGOBJECT;
    }

    // First convert the TW_IDENTITY from Java class to C++ class
    TW_IDENTITY* pIdentities[] = { nullptr, nullptr };
    JavaDTwainLowLevel_TW_IDENTITY idproxy(env);
    TW_IDENTITY id1 = {};
    TW_IDENTITY id2 = {};

    if (appID)
    {
        idproxy.setObject(appID);
        id1 = idproxy.JavaToNative();
        pIdentities[0] = &id1;
    }
    if (sourceID)
    {
        idproxy.setObject(sourceID);
        id2 = idproxy.JavaToNative();
        pIdentities[1] = &id2;
    }

    // Convert the memref to the actual type
    DSMRawRAII raii(translator.m_tripletPtr.get(), env, twmemref);
    return API_INSTANCE DTWAIN_CallDSMProc(pIdentities[0], pIdentities[1], static_cast<TW_UINT32>(DG), static_cast<TW_UINT16>(DAT), static_cast<TW_UINT16>(TMSG), raii.getRaw());
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_CallDSMProc
* Signature: (Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;JIILjava/lang/Object;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CallDSMProc__Lcom_dynarithmic_twain_lowlevel_TW_1IDENTITY_2Lcom_dynarithmic_twain_lowlevel_TW_1IDENTITY_2JIILjava_lang_Object_2
(JNIEnv *env, jobject, jobject appID, jobject sourceID, jlong DG, jint DAT, jint TMSG, jobject twmemref)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return CallDSMHelper(env, appID, sourceID, static_cast<TW_UINT32>(DG), static_cast<TW_UINT16>(DAT), static_cast<TW_UINT16>(TMSG), twmemref);
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_CallDSMProc
* Signature: (Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;Lcom/dynarithmic/twain/lowlevel/TW_IDENTITY;Lcom/dynarithmic/twain/lowlevel/TW_UINT32;Lcom/dynarithmic/twain/lowlevel/TW_UINT16;Lcom/dynarithmic/twain/lowlevel/TW_UINT16;Ljava/lang/Object;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CallDSMProc__Lcom_dynarithmic_twain_lowlevel_TW_1IDENTITY_2Lcom_dynarithmic_twain_lowlevel_TW_1IDENTITY_2Lcom_dynarithmic_twain_lowlevel_TW_1UINT32_2Lcom_dynarithmic_twain_lowlevel_TW_1UINT16_2Lcom_dynarithmic_twain_lowlevel_TW_1UINT16_2Ljava_lang_Object_2
(JNIEnv *env, jobject, jobject appID, jobject sourceID, jobject dg, jobject dat, jobject msg, jobject twmemref)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    // Convert the triplet to C++
    JavaDTwainLowLevel_TW_UINT32 uProxy(env);
    JavaDTwainLowLevel_TW_UINT16 uProxy2(env);
    uProxy.setObject(dg);
    TW_UINT32 DG = uProxy.JavaToNative();
    uProxy2.setObject(dat);
    TW_UINT16 DAT = uProxy2.JavaToNative();
    uProxy2.setObject(msg);
    TW_UINT16 TMSG = uProxy2.JavaToNative();

    return CallDSMHelper(env, appID, sourceID, DG, DAT, TMSG, twmemref);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetTwainStringName
* Signature: (II)Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetTwainStringName
(JNIEnv *env, jobject, jint category, jint twainID)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    std::vector<TCHAR> name;
    LONG nLen = API_INSTANCE DTWAIN_GetTwainStringName(category, twainID, nullptr, 0);
    if (nLen >= 0)
    {
        name.resize((std::max)(static_cast<int>(nLen),1));
        API_INSTANCE DTWAIN_GetTwainStringName(category, twainID, name.data(), name.size());
        return static_cast<jstring>(CreateJStringFromCString(env, name.data()));
    }
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_CreateObjectFromTriplet
 * Signature: (JII)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CreateObjectFromTriplet
(JNIEnv *env, jobject, jlong DG, jint DAT, jint MSG)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto pr = g_JNIGlobals.CreateObjectFromTriplet(env, DG, DAT, MSG);
    if ( pr.second )
        return pr.first;

    auto sTwainNames = GetTripletNames(DG, DAT, MSG);
    std::ostringstream strm;
    strm << "Invalid or unknown TWAIN triplet combination used to create Java object ["
        << sTwainNames[0] << "=" << DG << ", "
        << sTwainNames[1] << "=" << DAT << ", "
        << sTwainNames[2] << "=" << MSG << "]";
    JavaExceptionThrower::ThrowJavaException(env, strm.str());
    return {};
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetActiveDSMPath
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetActiveDSMPath
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    std::vector<TCHAR> name;
    LONG nLen = API_INSTANCE DTWAIN_GetActiveDSMPath(nullptr, 0);
    if (nLen >= 0)
    {
        name.resize((std::max)(static_cast<int>(nLen), 1));
        API_INSTANCE DTWAIN_GetActiveDSMPath(name.data(), name.size());
        return static_cast<jstring>(CreateJStringFromCString(env, name.data()));
    }
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_LoadCustomStringResource
* Signature: (Ljava/lang/String;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1LoadCustomStringResource
(JNIEnv *env, jobject, jstring resString)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    GetStringCharsHandler handler(env, resString);
    auto sLang = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    LONG retVal = API_INSTANCE DTWAIN_LoadCustomStringResources(sLang);
    return retVal;
    DO_DTWAIN_CATCH(env)
}


/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetAcquireImageNegative
* Signature: (JZ)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAcquireImageNegative
(JNIEnv *env, jobject, jlong source, jboolean isNegative)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto tId = API_INSTANCE DTWAIN_SetAcquireImageNegative(
                                            reinterpret_cast<DTWAIN_SOURCE>(source), static_cast<DTWAIN_BOOL>(isNegative));
    return tId;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SelectSource2Ex
* Signature: (Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)J
*/
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SelectSource2Ex
(JNIEnv *env, jobject, jstring sTitle, jint xPos, jint yPos, jstring sIncludeNames, jstring sExcludeNames, jstring sMapping, jint options)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    NativeStringType strTitle;
    NativeStringType strInclude;
    NativeStringType strExclude;
    NativeStringType strMapping;

    if ( sTitle )
    {
        GetStringCharsHandler handler(env, sTitle);
        strTitle = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    }

    if ( sIncludeNames)
    {
        GetStringCharsHandler handler(env, sIncludeNames);
        strInclude = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    }

    if (sExcludeNames)
    {
        GetStringCharsHandler handler(env, sExcludeNames);
        strExclude = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    }

    if ( sMapping )
    {
        GetStringCharsHandler handler(env, sMapping);
        strMapping = reinterpret_cast<LPCTSTR>(handler.GetStringChars());
    }

    DTWAIN_SOURCE theSource = API_INSTANCE DTWAIN_SelectSource2Ex(nullptr, strTitle.c_str(), xPos, yPos, strInclude.c_str(), strExclude.c_str(), strMapping.c_str(), options);
    return reinterpret_cast<jlong>(theSource);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_CallDSMProc
* Signature: (Lcom/dynarithmic/twain/lowlevel/TwainTriplet;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1CallDSMProc__Lcom_dynarithmic_twain_lowlevel_TwainTriplet_2
(JNIEnv *env, jobject, jobject JavaTwainTriplet)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    // Convert the triplet to C++
    JavaDTwainLowLevel_TwainTriplet uProxy(env);
    uProxy.setObject(JavaTwainTriplet);
    TwainTripletFromJava twtriplet = uProxy.JavaToNative();

    // Convert the low level twain triplet dg, dat, msg to C++
    JavaDTwainLowLevel_TW_UINT32 uProxy32(env);
    JavaDTwainLowLevel_TW_UINT16 uProxy16(env);
    uProxy32.setObject(twtriplet.m_DG);
    TW_UINT32 DG = uProxy32.JavaToNative();
    uProxy16.setObject(twtriplet.m_DAT);
    TW_UINT16 DAT = uProxy16.JavaToNative();
    uProxy16.setObject(twtriplet.m_MSG);
    TW_UINT16 TMSG = uProxy16.JavaToNative();

    // Call the helper
    return CallDSMHelper(env, twtriplet.m_OriginID, twtriplet.m_DestinationID, DG, DAT, TMSG, twtriplet.m_memRef);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetFileAutoIncrement
* Signature: (JJZZ)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetFileAutoIncrement
(JNIEnv *env, jobject, jlong source, jlong value, jboolean resetVal, jboolean enable)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto tId = API_INSTANCE DTWAIN_SetFileAutoIncrement(reinterpret_cast<DTWAIN_SOURCE>(source), static_cast<LONG>(value), resetVal, enable);
    return tId;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_AllocateMemory
* Signature: (I)J
*/
JNIEXPORT jlong JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AllocateMemory
(JNIEnv *env, jobject, jint memSize)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto handle = API_INSTANCE DTWAIN_AllocateMemory(memSize);
    return reinterpret_cast<jlong>(handle);
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_FreeMemory
* Signature: (J)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1FreeMemory
(JNIEnv *env, jobject, jlong memoryHandle)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto ret = API_INSTANCE DTWAIN_FreeMemory(reinterpret_cast<HANDLE>(memoryHandle));
    return ret;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetAcquireStripBuffer
* Signature: (JJ)Z
*/
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetAcquireStripBuffer
(JNIEnv *env, jobject, jlong source, jlong memoryHandle)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    auto ret = API_INSTANCE DTWAIN_SetAcquireStripBuffer(reinterpret_cast<DTWAIN_SOURCE>(source),
                                            reinterpret_cast<HANDLE>(memoryHandle));
    return ret?JNI_TRUE:JNI_FALSE;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_EnableAutoFeedNotify
* Signature: (IZ)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnableAutoFeedNotify
(JNIEnv *env, jobject, jint latency, jboolean enable)
{
    DO_DTWAIN_TRY
        DO_DTWAIN_CHECK_MODULE_LOAD
        auto ret = 1; // API_INSTANCE DTWAIN_EnableAutoFeedNotify(latency, static_cast<LONG>(enable));
    return ret;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_SetCamera
* Signature: (JLjava/lang/String;)Z
*/
JNIEXPORT jboolean JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1SetCamera
(JNIEnv *env, jobject, jlong source, jstring cameraName)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    GetStringCharsHandler str(env, cameraName);
    auto ret = API_INSTANCE DTWAIN_SetCamera(reinterpret_cast<DTWAIN_SOURCE>(source), reinterpret_cast<LPCTSTR>(str.GetStringChars()));
    return ret;
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetExtendedImageInfo
* Signature: (J)Lcom/dynarithmic/twain/highlevel/ExtendedImageInfo;
*/
JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetExtendedImageInfo
(JNIEnv *env, jobject, jlong source)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaExtendedImageInfo extImageInfo(env);
#if 0
    ExtendedImageInfo_BarcodeInfoNative bcNative;
    extImageInfo.setBarcodeInfoCount(1);
    bcNative.xCoordinate = 100;
    bcNative.yCoordinate = 200;
    bcNative.rotation =    40;
    strcpy(bcNative.text, "abc123");
    extImageInfo.setBarcodeInfo(bcNative, 0);
    ExtendedImageInfo_ShadedAreaDetectionInfoNative sNative;
    extImageInfo.setShadedAreaInfoCount(1);
    sNative.top = 23;
    sNative.left = 34;
    sNative.height = 300;
    sNative.width = 600;
    extImageInfo.setShadedAreaDetectionInfo(sNative, 0);
/*    ExtendedImageInfo_SpeckleRemovalInfoNative sSpeckle;
    sSpeckle.specklesRemoved = 1000;
    sSpeckle.whiteSpecklesRemoved = 501;
    sSpeckle.blackSpecklesRemoved = 499;
    extImageInfo.setSpeckleRemovalInfo(sSpeckle);*/
#endif
    return extImageInfo.getObject();
    DO_DTWAIN_CATCH(env)
}

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_GetCurrentTwainTriplet
* Signature: ()Lcom/dynarithmic/twain/lowlevel/TwainTriplet;
*/
/*JNIEXPORT jobject JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetCurrentTwainTriplet
(JNIEnv *env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD

    TW_IDENTITY OriginID;
    TW_IDENTITY SourceID;
    LONG DG, DAT, MSG;
    LONG64 MemRef;
}*/

/*
* Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method:    DTWAIN_AddPDFText
* Signature: (JLcom/dynarithmic/twain/highlevel/PDFTextElement;)I
*/
JNIEXPORT jint JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1AddPDFText
(JNIEnv *env, jobject, jlong source, jobject pdfTextElement)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    JavaPDFTextElement pdfElement(env);
    pdfElement.setObject(pdfTextElement);
    auto fontName = pdfElement.getFontName();
    auto rgb = pdfElement.getRGB();
    auto opts = pdfElement.getRenderMode();
    auto xpos = pdfElement.getXPosition();
    auto ypos = pdfElement.getYPosition();
    auto fontheight = pdfElement.getFontSize();
    auto scaling = pdfElement.getScaling();
    auto charSpacing = pdfElement.getCharSpacing();
    auto wordSpacing = pdfElement.getWordSpacing();
    auto strokeWidth = pdfElement.getStrokeWidth();
    auto displayFlags = pdfElement.getDisplayOptions();
    auto text = pdfElement.getText();
    auto ret = API_INSTANCE DTWAIN_AddPDFText(reinterpret_cast<DTWAIN_SOURCE>(source),
                                           text.c_str(),
                                           xpos,
                                           ypos,
                                           fontName.c_str(),
                                           fontheight,
                                           RGB(rgb.getR(), rgb.getG(), rgb.getB()),
                                           opts,
                                           scaling,
                                           charSpacing,
                                           wordSpacing,
                                           strokeWidth,
                                           displayFlags);
    return JNI_TRUE;
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumSupportedSinglePageFileTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumSupportedSinglePageFileTypes
(JNIEnv* env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return CallFnReturnArray2<JavaIntArrayTraits>(env, API_INSTANCE DTWAIN_EnumSupportedSinglePageFileTypes);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_EnumSupportedMultiPageFileTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1EnumSupportedMultiPageFileTypes
(JNIEnv* env, jobject)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    return CallFnReturnArray2<JavaIntArrayTraits>(env, API_INSTANCE DTWAIN_EnumSupportedMultiPageFileTypes);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetFileTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetFileTypeName
(JNIEnv* env, jobject, jint iName)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    LONG nLen = API_INSTANCE DTWAIN_GetFileTypeName(iName, nullptr, 0);
    if (nLen > 0)
    {
        std::vector<TCHAR> sz(nLen + 1);
        API_INSTANCE DTWAIN_GetFileTypeName(iName, &sz[0], nLen);
        return CreateJStringFromCString(env, &sz[0]);
    }
    return CreateJStringFromCString(env, _T(""));
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetFileTypeExtension
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetFileTypeExtension
(JNIEnv* env, jobject, jint iName)
{
    DO_DTWAIN_TRY
    DO_DTWAIN_CHECK_MODULE_LOAD
    LONG nLen = API_INSTANCE DTWAIN_GetFileTypeExtensions(iName, nullptr, 0);
    if (nLen > 0)
    {
        std::vector<TCHAR> sz(nLen + 1);
        API_INSTANCE DTWAIN_GetFileTypeExtensions(iName, &sz[0], nLen);
        return CreateJStringFromCString(env, &sz[0]);
    }
    return CreateJStringFromCString(env, _T(""));
    DO_DTWAIN_CATCH(env)
}

/*
*Class:     com_dynarithmic_twain_DTwainJavaAPI
* Method : API_INSTANCE DTWAIN_GetVersionCopyright
* Signature : ()Ljava / lang / String;
*/
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetVersionCopyright
(JNIEnv* env, jobject)
{
    DO_DTWAIN_TRY
    LONG retLength = API_INSTANCE DTWAIN_GetVersionCopyright(nullptr, 0);
    if (retLength > 0)
    {
        std::vector<TCHAR> arg(retLength + 1);
        API_INSTANCE DTWAIN_GetVersionCopyright(arg.data(), arg.size());
        return CreateJStringFromCString(env, arg.data());
    }
    TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}

/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetSessionDetails
 * Signature: (IZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetSessionDetails
(JNIEnv* env, jobject, jint indentValue, jboolean bRefresh)
{
    DO_DTWAIN_TRY
    LONG retLength = API_INSTANCE DTWAIN_GetSessionDetails(nullptr, 0, indentValue, bRefresh);
    if (retLength > 0)
    {
        std::vector<TCHAR> arg(retLength + 1);
        API_INSTANCE DTWAIN_GetSessionDetails(arg.data(), arg.size(), indentValue, FALSE);
        return CreateJStringFromCString(env, arg.data());
    }
    TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}


/*
 * Class:     com_dynarithmic_twain_DTwainJavaAPI
 * Method:    DTWAIN_GetSourceDetails
 * Signature: (Ljava/lang/String;IZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynarithmic_twain_DTwainJavaAPI_DTWAIN_1GetSourceDetails
(JNIEnv* env, jobject, jstring sourceNames, jint indentValue, jboolean bRefresh)
{
    DO_DTWAIN_TRY
    GetStringCharsHandler str(env, sourceNames);
    int nSources = 0;
    #ifdef UNICODE
        std::wstring sourceString = str.GetStringCharsNative();
        std::wistringstream strm(sourceString);
        std::wstring oneSource;
        while (std::getline(strm, oneSource, L'|'))
            ++nSources;
    #else
        std::string sourceString = str.GetStringCharsNative();
        std::istringstream strm(sourceString);
        std::string oneSource;
        while (std::getline(strm, oneSource, '|'))
            ++nSources;
    #endif
    std::vector<TCHAR> vChars(100000 * nSources + 1, 0);
    LONG retLength = API_INSTANCE DTWAIN_GetSourceDetails(reinterpret_cast<LPCTSTR>(str.GetStringChars()), vChars.data(), vChars.size(), indentValue, bRefresh);
    if (retLength > 0)
        return CreateJStringFromCString(env, vChars.data());
    TCHAR szNothing[] = { 0 };
    return CreateJStringFromCString(env, szNothing);
    DO_DTWAIN_CATCH(env)
}
