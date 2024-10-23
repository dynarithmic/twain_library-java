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
#ifndef JAVAOBJECTCALLER_H
#define JAVAOBJECTCALLER_H

#include <jni.h>
#include <utility>
#include <vector>
#include <string>
#include <map>
#include <iterator>
#include <tchar.h>
#include <sstream>
#include <memory>

#include "StringDefs.h"
#include "JavaArrayList.h"
#include "JavaArrayTraits.h"
#include "UTFCharsHandler.h"
#include "ExtendedImageInfo_Types.h"

struct JavaFunctionInfo
{
    std::string funcName;
    std::string funcSig;
};

using JavaFunctionInfoCategoryMap = std::map<std::string, JavaFunctionInfo>;

class JavaObjectCaller;

struct JavaFunctionClassInfo
{
    std::string m_className;
    JavaFunctionInfoCategoryMap m_mapFunctions;
    std::shared_ptr<JavaObjectCaller> m_ObjectCallerTemplate;
};

using JavaFunctionNameMap = std::map<std::string, JavaFunctionClassInfo>;


struct JavaFunctionNameMapInstance
{
public:
    static JavaFunctionNameMap& getFunctionMap()
    {
        static JavaFunctionNameMap g_JavaFunctionNameMap;
        return g_JavaFunctionNameMap;
    }
};
#include "DTWAINJNIGlobals.h"

#define JavaStringSignature "Ljava/lang/String;"

struct JavaExceptionThrower
{
    static jint ThrowJavaExceptionImpl(JNIEnv* env, const char *className, const std::string& exceptionMessage)
    {
        const jclass clazz = env->FindClass(className);
        return env->ThrowNew(clazz, exceptionMessage.c_str());
    }

    static jint ThrowJavaException(JNIEnv* env, const std::string& exceptionMessage = "Unrecoverable error in JNI layer")
    {
        auto& funcMap = JavaFunctionNameMapInstance::getFunctionMap();
        const auto iter = funcMap.find("ExceptionClass");
        if ( iter != funcMap.end())
            return ThrowJavaExceptionImpl(env,
                                          iter->second.m_className.c_str(),
                                          exceptionMessage);
        return ThrowGeneralException(env, exceptionMessage);
    }

    static jint ThrowFileNotFoundError(JNIEnv *env, const std::string& exceptionMessage)
    {
        return ThrowJavaExceptionImpl(env, "java/lang/io.FileNotFoundException", exceptionMessage);
    }

    static jint ThrowGeneralException(JNIEnv *env, const std::string& exceptionMessage)
    {
        return ThrowJavaExceptionImpl(env, "java/lang/Exception", exceptionMessage);
    }
};

struct TW_RESPONSETYPE
{
    std::vector<TW_ELEMENT8> ResponseVector;
    int NumItems;
    TW_ELEMENT8* getData() { return ResponseVector.data(); }
};

struct TW_NULL
{
};

struct TwainLowLevel
{
    jobject m_TwainObject;
};

struct TwainTripletFromJava
{
    jobject m_OriginID;
    jobject m_DestinationID;
    jobject m_DG;
    jobject m_DAT;
    jobject m_MSG;
    jobject m_memRef;
};


class JavaObjectCaller
{
private:
    jclass m_jClass;
    jobject m_jObject;

    struct JObjectCallerDefs
    {
        std::string m_jMethodName;
        std::string m_jMethodSig;
        jmethodID m_jMethodID;
        JObjectCallerDefs(std::string methodName = "", std::string methodSig="") :
        m_jMethodName(std::move(methodName)), m_jMethodSig(std::move(methodSig)), m_jMethodID(nullptr) {}
    };

    std::vector<JObjectCallerDefs> m_jGetMethods;
    std::vector<JObjectCallerDefs> m_jSetMethods;
    std::vector<JObjectCallerDefs> m_jConstructorMethods;

    std::string m_jClassName;
    std::string m_jObjectName;
    size_t m_nDefaultConstructorPos;

protected:
    std::string m_lowLevelDirectory;
    JNIEnv *m_pJavaEnv;
    typedef std::map<std::string, JObjectCallerDefs> NameToFuncDefsMap;
    typedef std::map<std::string, JObjectCallerDefs> ConstructorList;
    NameToFuncDefsMap m_nNameToFuncDefs;
    ConstructorList m_nConstructorList;
    const JavaFunctionNameMap& m_pFunctionMap;

    template <typename ProxyType>
    typename ProxyType::value_type getProxyData(ProxyType& proxy, const char *funcName)
    {
        jobject jobj = callObjectMethod(funcName);
        if (jobj)
        {
            proxy.setObject(jobj);
            return proxy.getValue();
        }
        return{};
    }

    template <typename ProxyType, typename arg1>
    typename ProxyType::value_type getProxyData(ProxyType& proxy, const char *funcName, const arg1& arg)
    {
        jobject jobj = callObjectMethod(funcName, arg);
        if (jobj)
        {
            proxy.setObject(jobj);
            return proxy.getValue();
        }
        return{};
    }

    template <typename ProxyType, typename arg1, typename arg2>
    typename ProxyType::value_type getProxyData(ProxyType& proxy, const char *funcName, const arg1& a1, const arg2& a2)
    {
        jobject jobj = callObjectMethod(funcName, a1, a2);
        if (jobj)
        {
            proxy.setObject(jobj);
            return proxy.getValue();
        }
        return{};
    }

    template <typename ProxyType, typename valType=typename ProxyType::value_type>
    void setProxyData(ProxyType& proxy, const char *funcName, const valType& val)
    {
        jobject jobj = proxy.defaultConstructObject();
        if (jobj)
        {
            proxy.setObject(jobj);
            proxy.setValue(val);
            callObjectMethod(funcName, proxy.getObject());
        }
    }

    template <typename ProxyType, typename valType = typename ProxyType::value_type, typename callArg1 = typename ProxyType::value_type>
    void setProxyData(ProxyType& proxy, const char *funcName, const valType& val, const callArg1& a1)
    {
        jobject jobj = proxy.defaultConstructObject();
        if (jobj)
        {
            proxy.setObject(jobj);
            proxy.setValue(val);
            callObjectMethod(funcName, jobj, a1);
        }
    }

    template <typename ProxyType, typename valType = typename ProxyType::value_type,
              typename callArg1 = typename ProxyType::value_type,
              typename callArg2 = typename ProxyType::value_type >
    void setProxyData(ProxyType& proxy, const char *funcName, const valType& val, const callArg1& a1, const callArg1& a2)
    {
        jobject jobj = proxy.defaultConstructObject();
        if (jobj)
        {
            proxy.setObject(jobj);
            proxy.setValue(val);
            callObjectMethod(funcName, jobj, a1, a2);
        }
    }

public:
    jclass getClass() const { return m_jClass; }
    void setObject(jobject jObj) { m_jObject = jObj; }
    jobject getObject() const { return m_jObject; }
    JNIEnv* getEnvironment() const { return m_pJavaEnv; }
    virtual ~JavaObjectCaller() = default;
    void setEnvironment(JNIEnv* pEnv) { m_pJavaEnv = pEnv; }
    std::string getObjectName() const { return m_jObjectName; }

    JavaObjectCaller(const JavaFunctionNameMap& pNameMap) :
                        m_jClass{},
                        m_jObject{},
                        m_nDefaultConstructorPos(-1),
                        m_pJavaEnv{},
                        m_pFunctionMap(pNameMap) {}

    JavaObjectCaller(JNIEnv* pEnv, const JavaFunctionNameMap& pNameMap, const std::string& objectName,
                     const std::vector<std::string>& funcNames = {}) :
                    m_jClass(nullptr),
                    m_jObject(nullptr),
                    m_jObjectName(objectName),
                    m_nDefaultConstructorPos(-1),
                    m_pJavaEnv(pEnv),
                    m_pFunctionMap(pNameMap)
    {
        const auto mapIter = pNameMap.find(objectName);
        if ( mapIter == pNameMap.end())
        {
            std::ostringstream strm;
            strm << "Fatal Error: object named \'" << objectName << "' was not found in the dtwainjni.info file";
            JavaExceptionThrower::ThrowJavaException(pEnv, strm.str());
            return;
        }
        m_jClassName = mapIter->second.m_className;
        m_lowLevelDirectory = m_jClassName;
        auto& funcMap = getClassInfo().m_mapFunctions;
        for (auto& s : funcNames)
        {
            if (funcMap.find(s) == funcMap.end())
            {
                std::ostringstream strm;
                strm << "Invalid category name given for " << objectName << " in JNI Layer: " << s;
                JavaExceptionThrower::ThrowJavaException(pEnv, strm.str());
                return;
            }
        }
    }

    const JavaFunctionNameMap& getFunctionNameMap() const { return m_pFunctionMap; }
    const JavaFunctionClassInfo& getClassInfo() const { return m_pFunctionMap.find(m_jObjectName)->second; }
    const JavaFunctionInfoCategoryMap& getFunctionMap() const { return getClassInfo().m_mapFunctions; }
    std::string getFunctionName(const char* functionTag) const
    {
        auto& theMap = getFunctionMap();
        auto iter = theMap.find(functionTag);
        if (iter != theMap.end())
            return iter->second.funcName;
        return {};
    }

    void setJavaClass(const std::string& s) { m_jClassName = s; }

    std::string getJavaClass() const { return m_jClassName; }

    static std::string getJavaClassName(JNIEnv *pEnv, jobject jobj);

    void registerMethod(const std::string& methodName, const std::string& methodSig)
    {
        JObjectCallerDefs def(methodName, methodSig);
        if (methodName.empty())
        {
            size_t nextNumber = m_nConstructorList.size() + 1;
            if ( methodSig == "()V")
                setConstructor("Default", methodSig);
            else
                setConstructor("Sig" + std::to_string(nextNumber), methodSig);
        }
        else
            m_nNameToFuncDefs.insert(make_pair(methodName, def));
    }

    void registerMethod(const std::string& methodName, const std::string& methodSig, const std::string& methodReturn)
    {
        return registerMethod(methodName, methodSig + methodReturn);
    }

    bool unRegisterMethod(const std::string& methodName, const std::string& methodSig)
    {
        bool bRemoved = false;
        if (methodName.empty())
        {
            // Search the constructor list for the methodSig
            const auto iter = std::find_if(m_nConstructorList.begin(), m_nConstructorList.end(), [&](auto& pr)
            {
                return pr.second.m_jMethodSig == methodSig;
            });
            if (iter != m_nConstructorList.end())
            {
                m_nConstructorList.erase(iter);
                bRemoved = true;
            }
        }
        else
        {
            const auto iterName = m_nNameToFuncDefs.find(methodName);
            if (iterName == m_nNameToFuncDefs.end())
                return false;
            const auto iter = std::find_if(m_nNameToFuncDefs.begin(), m_nNameToFuncDefs.end(), [&](auto& pr)
            {
                return pr.second.m_jMethodSig == methodSig;
            });
            if (iter != m_nNameToFuncDefs.end())
            {
                m_nNameToFuncDefs.erase(iter);
                bRemoved = true;
            }
        }
        return bRemoved;
    }

    JObjectCallerDefs getFunctionDef(const std::string& methodName)
    {
        auto it = m_nNameToFuncDefs.find(methodName);
        if ( it != m_nNameToFuncDefs.end())
            return it->second;
        return {};
    }

    void copyMethodInfo(JavaObjectCaller& dest) const
    {
        dest. m_nDefaultConstructorPos = m_nDefaultConstructorPos;
        dest.m_nNameToFuncDefs = m_nNameToFuncDefs;
        dest.m_nConstructorList = m_nConstructorList;
    }

    void initializeMethods()
    {
        if ( !m_pJavaEnv )
            return;
        if ( m_jClassName.empty())
            return;
        m_jClass = m_pJavaEnv->FindClass(m_jClassName.c_str());
        if ( !m_jClass )
            return;
        auto it = m_nNameToFuncDefs.begin();
        while (it != m_nNameToFuncDefs.end())
        {
            NameToFuncDefsMap::value_type& pr = *it;
            JObjectCallerDefs& defs = pr.second;
            defs.m_jMethodID = m_pJavaEnv->GetMethodID(m_jClass, defs.m_jMethodName.c_str(), defs.m_jMethodSig.c_str());
            ++it;
        }
        // now do the constructors
        auto it2 = m_nConstructorList.begin();
        while (it2 != m_nConstructorList.end())
        {
            JObjectCallerDefs& defs = it2->second;
            defs.m_jMethodID = GetJavaClassConstructor(m_pJavaEnv, m_jClassName.c_str(), defs.m_jMethodSig.c_str());
            ++it2;
        }
    }

    std::vector<JObjectCallerDefs> verifyAllFunctionMethods()
    {
        std::vector<JObjectCallerDefs> vBadDefs;
        for (auto& funcDef : m_nNameToFuncDefs)
        {
            if (!funcDef.second.m_jMethodID)
                vBadDefs.push_back(funcDef.second);
        }

        for (auto& cList : m_nConstructorList)
        {
            if (!cList.second.m_jMethodID)
                vBadDefs.push_back(cList.second);
        }
        return vBadDefs;
    }

    void setConstructor(const std::string& id, std::string sig) { m_nConstructorList.insert({id, {"", std::move(sig)}}); }

    std::string getDefaultConstructor() const
    {
        const auto iter = m_nConstructorList.find("Default");
        if (iter != m_nConstructorList.end())
            return iter->first;
        return {};
    }

    jobject defaultConstructObject()
    {
        const auto defConstructor = getDefaultConstructor();
        if ( !defConstructor.empty())
            return m_jObject = m_pJavaEnv->NewObject(m_jClass, m_nConstructorList[defConstructor].m_jMethodID);
        return {};
    }

    std::string getConstructorKey(size_t constructorNumber) const
    {
        std::string key = "Sig" + std::to_string(constructorNumber);
        const auto iter = m_nConstructorList.find(key);
        if (iter != m_nConstructorList.end())
            return key;
        return {};
    }

    template <typename ...Params>
    jobject constructObject(size_t constructorNumber, Params&& ...params)
    {
        const std::string key = getConstructorKey(constructorNumber);
        if (!key.empty())
            return m_jObject = m_pJavaEnv->NewObject(m_jClass, m_nConstructorList[key].m_jMethodID, std::forward<Params>(params)...);
        return {};
    }

    template <typename ...Params>
    void callVoidMethod(const std::string& methodName, Params&&... params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if ( !defs.m_jMethodName.empty() )
            m_pJavaEnv->CallVoidMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
    }

    template <typename ...Params>
    jobject callObjectMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallObjectMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return nullptr;
    }


    template <typename ...Params>
    int callIntMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if ( !defs.m_jMethodName.empty() )
            return m_pJavaEnv->CallIntMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }

    template <typename ...Params>
    int64_t callLongMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallLongMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }

    template <typename ...Params>
    double callDoubleMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallDoubleMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }

    template <typename ...Params>
    char callByteMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallByteMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }

    template <typename ...Params>
    short callShortMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallShortMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }

    template <typename ...Params>
    int callBooleanMethod(const std::string& methodName, Params&& ...params)
    {
        const JObjectCallerDefs defs = getFunctionDef(methodName);
        if (!defs.m_jMethodName.empty())
            return m_pJavaEnv->CallBooleanMethod(m_jObject, defs.m_jMethodID, std::forward<Params>(params)...);
        return 0;
    }
};

struct JavaArrayTraitsInt
{
    static jintArray Construct(JNIEnv* env, JavaObjectCaller* parent, int nCount)
    {
        return env->NewIntArray(nCount);
    }

    static void GetArrayRegion(JNIEnv *env, jintArray jarr, int nCount, jint* region)
    {
        env->GetIntArrayRegion(jarr, jsize{ 0 }, nCount, region);
    }

    static void SetArrayRegion(JNIEnv* env, jintArray jarr, int nCount, jint* region)
    {
        env->SetIntArrayRegion(jarr, 0, nCount, region);
    }
};

struct JavaArrayTraitsObject
{
    std::string m_className;
    JavaArrayTraitsObject(const std::string& className)
    {
        auto& funcMap = JavaFunctionNameMapInstance::getFunctionMap();
        const auto iter = funcMap.find(className);
        if ( iter != funcMap.end())
            m_className = iter->second.m_className;
    }

    jobjectArray Construct(JNIEnv* env, JavaObjectCaller* parent,  int nCount) const
    {
        if ( m_className.empty())
            return {};
        return static_cast<jobjectArray>(env->NewObjectArray(nCount, env->FindClass(m_className.c_str()),
                                                             parent->defaultConstructObject()));
    }

    static void GetArrayRegion(JNIEnv *env, jobjectArray jarr, int nCount, jobject* region)
    {
        for (int i = 0; i < nCount; ++i)
        region[i] = env->GetObjectArrayElement(jarr, i);
    }

    static void SetArrayRegion(JNIEnv* env, jobjectArray jarr, int nCount, jobject* region)
    {
        for (int i = 0; i < nCount; ++i)
            env->SetObjectArrayElement(jarr, i, region[i]);
    }
};

template <typename JavaArrayClass, typename NativeArrayClass, typename ArrayTraits>
struct JavaArrayInterface : JavaObjectCaller
{
    typedef JavaArrayClass array_type;
    int m_arrayCount;
    ArrayTraits m_traits;
    JavaArrayInterface(JNIEnv* env, int aCount, ArrayTraits& traits, std::string sClass) :
                        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), sClass, {}), m_arrayCount(aCount), m_traits(traits)
    {
        registerMethod("", "()V");
        initializeMethods();
    }

    array_type createDefaultObject()
    {
        return m_traits.Construct(getEnvironment(), this, m_arrayCount);
    }

    array_type defaultConstructObject(JNIEnv* env, int nCount = 0)
    {
        m_arrayCount = nCount;
        return createDefaultObject();
    }

    std::vector<NativeArrayClass> JavaToNative()
    {
        std::vector<NativeArrayClass> input(m_arrayCount);
        jsize size = getEnvironment()->GetArrayLength(static_cast<array_type>(getObject()));
        m_traits.GetArrayRegion(getEnvironment(), static_cast<array_type>(getObject()), (std::min)(size, static_cast<jsize>(input.size())), input.data());
        return input;
    }

    array_type NativeToJava(std::vector<NativeArrayClass>& nativeArray)
    {
        setObject(createDefaultObject());
        m_traits.SetArrayRegion(getEnvironment(), static_cast<array_type>(getObject()), nativeArray.size(), nativeArray.data());
        return static_cast<array_type>(getObject());
    }
};

using JavaDTwainLowLevel_JavaIntArray = JavaArrayInterface<jintArray, jint, JavaArrayTraitsInt>;

struct JavaDTwainLowLevel_TW_INT8_ArrayTraits : JavaArrayTraitsObject
{
    JavaDTwainLowLevel_TW_INT8_ArrayTraits() : JavaArrayTraitsObject("TW_INT8") {}
};

using JavaDTwainLowLevel_INT8Array = JavaArrayInterface<jobjectArray, jobject, JavaDTwainLowLevel_TW_INT8_ArrayTraits>;

/////////////////////////////////////////////////////////////////////

template <typename HandleClassTraits>
struct MemoryHandleJavaInterface : JavaObjectCaller
{
    static constexpr const char *GetHandle = "GetHandle";
    static constexpr const char *SetHandle = "SetHandle";

    using value_type = typename HandleClassTraits::value_type;
    MemoryHandleJavaInterface(JNIEnv* env) :
        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), typename HandleClassTraits::className,
        {GetHandle, SetHandle})
    {
        DTWAINJNIGlobals::RegisterJavaFunctionInterface(this, getFunctionMap(), DTWAINJNIGlobals::INIT_METHODS);
        defaultConstructObject();
    }

    jobject createDefaultObject()
    {
        return defaultConstructObject();
    }

    jobject createFullObject(int var1)
    {
        return createDefaultObject();
    }

    void* getHandle()
    {
        const jlong val = callLongMethod(getFunctionName(GetHandle));
        return reinterpret_cast<void*>(val);
    }

    void setHandle(void *theValue)
    {
        const auto val = reinterpret_cast<jlong>(theValue);
        callObjectMethod(getFunctionName(SetHandle), val);
    }

    typename HandleClassTraits::value_type JavaToNative()
    {
        return getHandle();
    }

    typename HandleClassTraits::value_type getValue()
    {
        return JavaToNative();
    }

    jobject NativeToJava(typename HandleClassTraits::value_type h)
    {
        setHandle(h);
        return getObject();
    }

    void setValue(typename HandleClassTraits::value_type h)
    {
        NativeToJava(h);
    }
};

template <typename CharTraits = ANSICharTraits>
class JavaTwainAppInfoImpl : public JavaObjectCaller
{
    static constexpr const char *GetVersionInfo = "GetVersionInfo";
    static constexpr const char *GetManufacturer = "GetManufacturer";
    static constexpr const char *GetProductFamily = "GetProductFamily";
    static constexpr const char *GetProductName = "GetProductName";
    static constexpr const char *SetVersionInfo = "SetVersionInfo";
    static constexpr const char *SetManufacturer = "SetManufacturer";
    static constexpr const char *SetProductFamily = "SetProductFamily";
    static constexpr const char *SetProductName = "SetProductName";

public:
    JavaTwainAppInfoImpl(JNIEnv* pEnv) :
            JavaObjectCaller(pEnv, JavaFunctionNameMapInstance::getFunctionMap(), "TwainAppInfo",
        {GetVersionInfo, GetManufacturer, GetProductFamily, GetProductName,
            SetVersionInfo, SetManufacturer, SetProductFamily, SetProductName}
            )
    {
        const auto iter = JavaFunctionNameMapInstance::getFunctionMap().find("TwainAppInfo");
        iter->second.m_ObjectCallerTemplate->copyMethodInfo(*this);
        DTWAINJNIGlobals::RegisterJavaFunctionInterface(this, getFunctionMap(), DTWAINJNIGlobals::INIT_METHODS);
        defaultConstructObject();
    }

    jobject createDTwainAppInfo()
    {
        if ( m_pJavaEnv )
            return defaultConstructObject();
        return nullptr;
    }

    jobject createDTwainAppInfo(LPCTSTR str1, LPCTSTR str2, LPCTSTR str3, LPCTSTR str4)
    {
        return constructObject(1,
            CreateJStringFromCString(m_pJavaEnv, str1),
            CreateJStringFromCString(m_pJavaEnv, str2),
            CreateJStringFromCString(m_pJavaEnv, str3),
            CreateJStringFromCString(m_pJavaEnv, str4));
    }

    void setVersionInfo(const StringType& str)
    { callObjectMethod(getFunctionName(SetVersionInfo), CreateJStringFromCString(m_pJavaEnv,str.c_str())); }

    void setManufacturer(const StringType& str)
    { callObjectMethod(getFunctionName(SetManufacturer), CreateJStringFromCString(m_pJavaEnv,str.c_str())); }

    void setProductFamily(const StringType& str)
    { callObjectMethod(getFunctionName(SetProductFamily), CreateJStringFromCString(m_pJavaEnv,str.c_str())); }

    void setProductName(const StringType& str)
    { callObjectMethod(getFunctionName(SetProductName), CreateJStringFromCString(m_pJavaEnv,str.c_str())); }

    StringType getVersionInfo()
    { return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetVersionInfo)))).GetStringChars(); }

    StringType getManufacturer()
    { return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetManufacturer)))).GetStringChars(); }

    StringType getProductFamily()
    { return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetProductFamily)))).GetStringChars(); }

    StringType getProductName()
    { return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetProductName)))).GetStringChars(); }
};
#ifdef UNICODE
    typedef JavaTwainAppInfoImpl<UnicodeCharTraits> JavaTwainAppInfo;
#else
    typedef JavaTwainAppInfoImpl<> JavaTwainAppInfo;
#endif


class JavaBufferedStripInfo : public JavaObjectCaller
{
    static constexpr const char * GetBufferHandle = "GetBufferHandle";
    static constexpr const char * GetBufferedStripData = "GetBufferedStripData";
    static constexpr const char * GetCompressionType = "GetCompressionType";
    static constexpr const char * GetBufferSize = "GetBufferSize";
    static constexpr const char * GetImageInfo = "GetImageInfo";
    static constexpr const char * GetColumns = "GetColumns";
    static constexpr const char * GetRows = "GetRows";
    static constexpr const char * GetXOff = "GetXOff";
    static constexpr const char * GetYOff = "GetYOff";
    static constexpr const char * GetBytesWritten = "GetBytesWritten";
    static constexpr const char * GetBytesPerRow = "GetBytesPerRow";
    static constexpr const char * GetPreferredSize = "GetPreferredSize";
    static constexpr const char * IsAppAllocatesBuffer = "IsAppAllocatesBuffer";
    static constexpr const char * SetBufferHandle = "SetBufferHandle";
    static constexpr const char * SetBufferedStripData = "SetBufferedStripData";
    static constexpr const char * SetCompressionType = "SetCompressionType";
    static constexpr const char * SetBufferSize = "SetBufferSize";
    static constexpr const char * SetImageInfo = "SetImageInfo";
    static constexpr const char * SetColumns = "SetColumns";
    static constexpr const char * SetRows = "SetRows";
    static constexpr const char * SetXOff = "SetXOff";
    static constexpr const char * SetYOff = "SetYOff";
    static constexpr const char * SetBytesWritten = "SetBytesWritten";
    static constexpr const char * SetBytesPerRow = "SetBytesPerRow";

public:
    JavaBufferedStripInfo(JNIEnv* env);

    void setBufferHandle(HANDLE handle);
    HANDLE getBufferHandle();
    int getBufferSize();
    int getPreferredSize();
    bool isAppAllocatesBuffer();
    void setBufferedStripData(LPBYTE pBytes, LONG size);
    void setImageInfo(jobject jImageInfo);
    void setBufferStripInfo(LONG columns, LONG rows, LONG xOffset, LONG yOffset, LONG bytesWritten, LONG BytesPerRow);
    void setBufferSize(LONG size);
    jobject createFullObject(LONG prefSize, LONG minimumSiz, LONG maximumSiz);
};

class JavaBufferedTileInfo : public JavaObjectCaller
{
    static constexpr const char* SetInfo = "SetInfo";
    static constexpr const char* SetTileData = "SetTileData";

public:
    JavaBufferedTileInfo(JNIEnv* env);

    jobject createFullObject(TW_IMAGEMEMXFER memXFer);
};

struct FrameStruct;

class JavaFrameInfo : public JavaObjectCaller
{
    static constexpr const char*  SetLeft = "SetLeft";
    static constexpr const char*  SetTop = "SetTop";
    static constexpr const char*  SetBottom = "SetBottom";
    static constexpr const char*  SetRight = "SetRight";
    static constexpr const char*  GetLeft = "GetLeft";
    static constexpr const char*  GetTop = "GetTop";
    static constexpr const char*  GetRight = "GetRight";
    static constexpr const char*  GetBottom = "GetBottom";

public:
    JavaFrameInfo(JNIEnv *pEnv);

    jobject CreateJFrameObject();
    jobject createDefaultObject();
    jobjectArray CreateJFrameObjectArray(jsize numElements);
    void setJFrameDimensions(double left, double top, double right, double bottom);
    void getJFrameDimensions(double* left, double* top, double* right, double* bottom);
    jobject NativeToJava(FrameStruct& fs);
    FrameStruct JavaToNative();
};

class JavaDTwainVersionInfo : public JavaObjectCaller
{
    static constexpr const char*  SetMajorVersion = "SetMajorVersion";
    static constexpr const char*  SetMinorVersion = "SetMinorVersion";
    static constexpr const char*  SetExePath = "SetExePath";
    static constexpr const char*  SetLongName = "SetLongName";
    static constexpr const char*  SetShortName = "SetShortName";
    static constexpr const char*  SetPatchVersion = "SetPatchVersion";
    static constexpr const char*  SetVersionType = "SetVersionType";
    static constexpr const char*  SetVersionCopyright = "SetVersionCopyright";

public:
    JavaDTwainVersionInfo(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(LONG var1, LONG var2, LONG var3, LONG var4, jstring exePath);
    void setMajorVersion(int32_t majorVersion);
    void setMinorVersion(int32_t minorVersion);
    void setExePath(LPCTSTR str);
    void setLongName(LPCTSTR str);
    void setShortName(LPCTSTR str);
    void setVersionCopyright(LPCTSTR str);
    void setVersionType(int32_t versionType);
    void setPatchVersion(int32_t patchVersion);
};

class JavaTwainAcquireArea : public JavaObjectCaller
{

public:
    static constexpr const char * SetAll = "SetAll";
    static constexpr const char * Left = "Left";
    static constexpr const char * Top = "Top";
    static constexpr const char * Right = "Right";
    static constexpr const char * Bottom = "Bottom";
    static constexpr const char * GetUnitMeasure = "GetUnitMeasure";

    JavaTwainAcquireArea(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(double var1, double var2, double var3, double var4, int var5);
    double getLeft();
    double getTop();
    double getRight();
    double getBottom();
    int    getUnitOfMeasure();
};

class JavaDTwainSourceInfo : public JavaObjectCaller
{
    public:
        JavaDTwainSourceInfo(JNIEnv* env);
        jobject createFullObject(LPCTSTR arg1, LPCTSTR arg2, LPCTSTR arg3, LPCTSTR arg4, int arg5, int arg6);
};

class JavaTwainOCRInfo : public JavaObjectCaller
{
    public:
        JavaTwainOCRInfo(JNIEnv* env);
        jobject createFullObject(LPCTSTR arg1, LPCTSTR arg2, LPCTSTR arg3, LPCTSTR arg4);
};
/////////////////////////////////////////////////////////////////////////////
class JavaTwainAcquisitionArray : public JavaObjectCaller
{
    static constexpr const char *AddData = "AddData";
    static constexpr const char *SetStatus= "SetStatus";
public:
    JavaTwainAcquisitionArray(JNIEnv* env);
    void addData(jobject acquisition);
    void setStatus(int32_t status);
};
//////////////////////////////////////////////////////////////////////////////
class JavaTwainAcquisitionData : public JavaObjectCaller
{
    static constexpr const char* AddImageData = "AddImageData";

public:
    JavaTwainAcquisitionData(JNIEnv* env);
    void addImageData(jobject ImageObject);
};

class JavaTwainImageData : public JavaObjectCaller
{
    static constexpr const char *SetImageData = "SetImageData";
    static constexpr const char *SetDibHandle = "SetDibHandle";

public:
    JavaTwainImageData(JNIEnv* env);
    void setImageData(jbyteArray imageData);
    void setDibHandle(HANDLE hDib);
};

class JavaAcquirerInfo
{
    private:
        JavaTwainAcquisitionArray m_jAcquisitionArray;
        JavaTwainAcquisitionData m_jAcquisitionData;
        JavaTwainImageData m_jImageData;

    public:
        JavaAcquirerInfo(JNIEnv *pEnv) : m_jAcquisitionArray(pEnv), m_jAcquisitionData(pEnv), m_jImageData(pEnv)
        {}

        jobject CreateJavaImageDataObject(JNIEnv *env)
        { return m_jImageData.defaultConstructObject(); }

        jobject CreateJavaAcquisitionDataObject(JNIEnv *env)
        { return m_jAcquisitionData.defaultConstructObject(); }

        jobject CreateJavaAcquisitionArrayObject(JNIEnv *env)
        { return m_jAcquisitionArray.defaultConstructObject(); }

        void addAcquisitionToArray(jobject jAcquisitionArrayObject, jobject jAcquisitionDataObject)
        {
            m_jAcquisitionArray.setObject(jAcquisitionArrayObject);
            m_jAcquisitionArray.addData(jAcquisitionDataObject);
        }

        void addImageDataToAcquisition(jobject jAcquisitionDataObject, jobject jImageDataObject)
        {
            m_jAcquisitionData.setObject(jAcquisitionDataObject);
            m_jAcquisitionData.addImageData(jImageDataObject);
        }

        void setImageData(JNIEnv* env, jobject jImageDataObject, LPVOID imgData, unsigned long nDataSize, HANDLE handle =  static_cast<HANDLE>(nullptr))
        {
            // Create a jarray of bytes
            jbyteArray bArray = CreateJArrayFromCArray<JavaByteArrayTraits<char> >(env, static_cast<char*>(imgData), nDataSize);

            // Call the method
            m_jImageData.setObject(jImageDataObject);
            m_jImageData.setImageData(bArray);
            m_jImageData.setDibHandle(handle);
            // release the array for internal Java GC
            env->DeleteLocalRef(bArray);
        }

        void setStatus(jobject jAcquisitionArray, LONG status)
        {
            m_jAcquisitionArray.setObject(jAcquisitionArray);
            m_jAcquisitionArray.callVoidMethod("setStatus", status);
        }
};


class JavaDTwainLowLevel_TW_BOOL : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";
public:
    using value_type = TW_BOOL;
    JavaDTwainLowLevel_TW_BOOL(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);
    value_type getValue();
    void setValue(value_type val);

    value_type JavaToNative();
    jobject NativeToJava(value_type val);
};


class JavaDTwainLowLevel_TW_INT32 : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
    using value_type = TW_INT32;
    using actual_type = int32_t;
    JavaDTwainLowLevel_TW_INT32(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);
    value_type getValue();
    void setValue(value_type val);

    value_type JavaToNative();
    jobject NativeToJava(value_type val);
};

class JavaDTwainLowLevel_TW_UINT32 : public JavaObjectCaller
{
    static constexpr const char*  GetValue = "GetValue";
    static constexpr const char*  SetValue = "SetValue";

    public:
        JavaDTwainLowLevel_TW_UINT32(JNIEnv* env);

    using value_type = TW_UINT32;
    using actual_type = uint64_t;

    jobject createDefaultObject();
    jobject createFullObject(TW_UINT32 var1);

    value_type getValue();
    void setValue(value_type val);

    value_type JavaToNative();
    jobject NativeToJava(value_type val);
};

class JavaDTwainLowLevel_TW_UINTPTR : public JavaObjectCaller
{
    static constexpr const char *SetValue1 = "SetValue1";
    static constexpr const char *SetValue2 = "SetValue2";
    static constexpr const char *GetValueAsString = "GetValueAsString";
    static constexpr const char *GetValue = "GetValue";

public:
    using value_type = TW_UINTPTR;
    JavaDTwainLowLevel_TW_UINTPTR(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int16_t var1);
    value_type getValue();
    void setValue(value_type val);
};

class JavaDTwainLowLevel_TW_INT16 : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
    using value_type = TW_INT16;
    using actual_type = int16_t;

    JavaDTwainLowLevel_TW_INT16(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int16_t var1);
    value_type getValue();
    void setValue(value_type val);

    value_type JavaToNative();
    jobject NativeToJava(value_type val);

};

class JavaDTwainLowLevel_TW_UINT16 : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
    using value_type = TW_UINT16;
    using actual_type = uint32_t;

    JavaDTwainLowLevel_TW_UINT16(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(value_type var1);

    value_type getValue();
    void setValue(value_type val);

    value_type JavaToNative();
    jobject NativeToJava(value_type val);
};

class JavaDTwainLowLevel_TW_INT8 : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
    using value_type = TW_INT8;

    JavaDTwainLowLevel_TW_INT8(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int8_t var1);
    value_type getValue();
    void setValue(value_type val);
};

class JavaDTwainLowLevel_TW_UINT8 : public JavaObjectCaller
{
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
    using value_type = TW_UINT8;
    using actual_type = uint32_t;
    JavaDTwainLowLevel_TW_UINT8(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(actual_type var1);
    value_type getValue();
    void setValue(value_type val);
};

class JavaDTwainLowLevel_TW_STRING : public JavaObjectCaller
{
protected:
    int m_strSize;
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue = "SetValue";

public:
#ifdef UNICODE
    using value_type = std::wstring;
#else
    using value_type = std::string;
#endif
    using StringType = value_type;

    JavaDTwainLowLevel_TW_STRING(JNIEnv* env, int strSize, const std::string& className="TW_STRING");

    jobject createDefaultObject();
    jobject createFullObject(int allocSize, int actualSize);

    StringType getValue();
    void setValue(StringType val);
    void setValue(const char* val);
    int size() const { return m_strSize; }
};

class JavaDTwainLowLevel_TW_STRING_Base : public JavaObjectCaller
{
protected:
    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *SetValue1 = "SetValue1";
    static constexpr const char *SetValue2 = "SetValue2";
    int m_strSize;
public:
#ifdef UNICODE
    using value_type = std::wstring;
#else
    using value_type = std::string;
#endif
    using StringType = value_type;

    JavaDTwainLowLevel_TW_STRING_Base(JNIEnv* env, int strSize, const std::string& className);

    jobject createDefaultObject();

    StringType getValue();
    void setValue(StringType val);
    void setValue(const char* val);
    StringType JavaToNative();
    jobject NativeToJava(StringType val);
    jobject NativeToJava(const char *arr);
    int size() const { return m_strSize; }
};

class JavaDTwainLowLevel_TW_STR32 : public JavaDTwainLowLevel_TW_STRING_Base
{
    public:
        JavaDTwainLowLevel_TW_STR32(JNIEnv* env);
        jobject createDefaultObject();
        jobject createFullObject();
};

class JavaDTwainLowLevel_TW_STR64 : public JavaDTwainLowLevel_TW_STRING_Base
{
public:
    JavaDTwainLowLevel_TW_STR64(JNIEnv* env);
    jobject createDefaultObject();
    jobject createFullObject();
};

class JavaDTwainLowLevel_TW_STR128 : public JavaDTwainLowLevel_TW_STRING_Base
{
public:
    JavaDTwainLowLevel_TW_STR128(JNIEnv* env);
    jobject createDefaultObject();
    jobject createFullObject();
};

class JavaDTwainLowLevel_TW_STR255 : public JavaDTwainLowLevel_TW_STRING_Base
{
public:
    JavaDTwainLowLevel_TW_STR255(JNIEnv* env);
    jobject createDefaultObject();
    jobject createFullObject();
};

class JavaDTwainLowLevel_TW_STR1024 : public JavaDTwainLowLevel_TW_STRING_Base
{
public:
    JavaDTwainLowLevel_TW_STR1024(JNIEnv* env);
    jobject createDefaultObject();
    jobject createFullObject();
};

class JavaDTwainLowLevel_TW_FIX32 : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 uProxy;
    JavaDTwainLowLevel_TW_INT16 iProxy;

    static constexpr const char *GetValue = "GetValue";
    static constexpr const char *GetWhole = "GetWhole";
    static constexpr const char *GetFrac = "GetFrac";
    static constexpr const char *SetValue1 = "SetValue1";
    static constexpr const char *SetValue2 = "SetValue2";
    static constexpr const char *SetWhole = "SetWhole";
    static constexpr const char *SetFrac = "SetFrac";

public:
    using value_type = TW_FIX32;
    JavaDTwainLowLevel_TW_FIX32(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject();

    TW_FIX32 getValue();

    void setValue(double d);
    void setValue(TW_FIX32 val);

    TW_FIX32 JavaToNative();
    static TW_FIX32 fromDouble(double val);
};


class JavaDTwainLowLevel_TW_VERSION : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy;
    JavaDTwainLowLevel_TW_STR32 strproxy;
#ifdef UNICODE
    using StringType = std::wstring;
#else
    using StringType = std::string;
#endif

    static constexpr const char*  GetMajor = "GetMajor";
    static constexpr const char*  GetMinor = "GetMinor";
    static constexpr const char*  GetLanguage = "GetLanguage";
    static constexpr const char*  GetCountry = "GetCountry";
    static constexpr const char*  GetInfo = "GetInfo";
    static constexpr const char*  SetMajor = "SetMajor";
    static constexpr const char*  SetMinor = "SetMinor";
    static constexpr const char*  SetLanguage = "SetLanguage";
    static constexpr const char*  SetCountry = "SetCountry";
    static constexpr const char*  SetInfo = "SetInfo";

public:
    using value_type = TW_VERSION;

    JavaDTwainLowLevel_TW_VERSION(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16 getMajorNum();
    TW_UINT16 getMinorNum();
    TW_UINT16 getLanguage();
    TW_UINT16 getCountry();
    StringType getInfo();

    void setMajorNum(TW_UINT16 val);
    void setMinorNum(TW_UINT16 val);
    void setLanguage(TW_UINT16 val);
    void setCountry(TW_UINT16 val);
    void setInfo(StringType val);

    TW_VERSION JavaToNative();
    TW_VERSION getValue();
    void NativeToJava(const TW_VERSION& twversion);
    void setValue(const TW_VERSION& twversion);
};

class JavaDTwainLowLevel_TW_IDENTITY : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy;
    JavaDTwainLowLevel_TW_STR32 strproxy;
    JavaDTwainLowLevel_TW_UINT32 uproxy;
    JavaDTwainLowLevel_TW_VERSION vProxy;

#ifdef UNICODE
    using StringType = std::wstring;
#else
    using StringType = std::string;
#endif
    static constexpr const char* GetId = "GetId";
    static constexpr const char* GetProtocolMajor = "GetProtocolMajor";
    static constexpr const char* GetProtocolMinor = "GetProtocolMinor";
    static constexpr const char* GetSupportedGroups = "GetSupportedGroups";
    static constexpr const char* GetManufacturer = "GetManufacturer";
    static constexpr const char* GetProductFamily = "GetProductFamily";
    static constexpr const char* GetProductName = "GetProductName";
    static constexpr const char* GetVersion = "GetVersion";
    static constexpr const char* SetId = "SetId";
    static constexpr const char* SetProtocolMajor = "SetProtocolMajor";
    static constexpr const char* SetProtocolMinor = "SetProtocolMinor";
    static constexpr const char* SetSupportedGroups = "SetSupportedGroups";
    static constexpr const char* SetManufacturer = "SetManufacturer";
    static constexpr const char* SetProductFamily = "SetProductFamily";
    static constexpr const char* SetProductName = "SetProductName";
    static constexpr const char* SetVersion = "SetVersion";


public:
    using value_type = TW_IDENTITY;

    JavaDTwainLowLevel_TW_IDENTITY(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject();

    TW_UINT32 getId();
    TW_UINT16 getProtocolMajor();
    TW_UINT16 getProtocolMinor();
    TW_UINT32 getSupportedGroups();
    StringType getManufacturer();
    StringType getProductFamily();
    StringType getProductName();
    TW_VERSION getVersion();

    void setId(TW_UINT32 val);
    void setProtocolMajor(TW_UINT16 val);
    void setProtocolMinor(TW_UINT16 val);
    void setSupportedGroups(TW_UINT32 val);
    void setManufacturer(StringType val);
    void setProductFamily(StringType val);
    void setProductName(StringType val);
    void setVersion(const TW_VERSION version);

    void NativeToJava(const TW_IDENTITY& twIdentity);
    TW_IDENTITY JavaToNative();
    TW_IDENTITY getValue();
};

class JavaDTwainLowLevel_TW_AUDIOINFO : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy;
    JavaDTwainLowLevel_TW_STR255 strproxy;

#ifdef UNICODE
    using StringType = std::wstring;
#else
    using StringType = std::string;
#endif

    static constexpr const char * GetName = "GetName";
    static constexpr const char * GetReserved = "GetReserved";
    static constexpr const char * SetName = "SetName";
    static constexpr const char * SetReserved = "SetReserved";

public:
    JavaDTwainLowLevel_TW_AUDIOINFO(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getReserved();
    StringType getName();

    void setReserved(TW_UINT32 val);
    void setName(StringType val);

    TW_AUDIOINFO JavaToNative();
    jobject NativeToJava(const TW_AUDIOINFO& twaudioinfo);
};

struct MemoryHandleTW_HANDLETraits
{
    using value_type = TW_HANDLE;
    static constexpr const char *className = "TW_HANDLE";
};

struct MemoryHandleTW_MEMREFTraits
{
    using value_type = TW_MEMREF;
    static constexpr const char *className = "TW_MEMREF";
};

using JavaDTwainLowLevel_TW_HANDLE = MemoryHandleJavaInterface<MemoryHandleTW_HANDLETraits>;

class JavaDTwainLowLevel_TW_CAPABILITY : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy;
    JavaDTwainLowLevel_TW_HANDLE handleproxy;
    static constexpr const char * GetCap = "GetCap";
    static constexpr const char * GetConType = "GetConType";
    static constexpr const char * GetContainerHandle = "GetContainerHandle";
    static constexpr const char * SetCap = "SetCap";
    static constexpr const char * SetConType = "SetConType";
    static constexpr const char * SetContainerHandle = "SetContainerHandle";

public:
    JavaDTwainLowLevel_TW_CAPABILITY(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16 getCap();
    TW_UINT16 getConType();
    TW_HANDLE gethContainer();

    void setCap(TW_UINT16 val);
    void setConType(TW_UINT16 val);
    void sethContainer(TW_HANDLE val);

    TW_CAPABILITY JavaToNative();
    jobject NativeToJava(const TW_CAPABILITY& twcapability);
};

class JavaDTwainLowLevel_TW_CUSTOMDSDATA : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy;
    JavaDTwainLowLevel_TW_HANDLE handleproxy;
    static constexpr const char * GetInfoLength = "GetInfoLength";
    static constexpr const char * GetData = "GetData";
    static constexpr const char * SetInfoLength = "SetInfoLength";
    static constexpr const char * SetData = "SetData";

public:
    JavaDTwainLowLevel_TW_CUSTOMDSDATA(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getInfoLength();
    TW_HANDLE gethData();

    void setInfoLength(TW_UINT32 val);
    void sethData(TW_HANDLE val);

    TW_CUSTOMDSDATA JavaToNative();
    jobject NativeToJava(const TW_CUSTOMDSDATA& twcustomdsdata);
};

class JavaDTwainLowLevel_TW_DEVICEEVENT : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_32;
    JavaDTwainLowLevel_TW_INT16 proxy_16;
    JavaDTwainLowLevel_TW_INT32 proxy_i32;
    JavaDTwainLowLevel_TW_STR255 strproxy;
    JavaDTwainLowLevel_TW_FIX32  proxy_fix32;

#ifdef UNICODE
    using StringType = std::wstring;
#else
    using StringType = std::string;
#endif
    static constexpr const char * GetEvent = "GetEvent";
    static constexpr const char * GetBatteryMinutes = "GetBatteryMinutes";
    static constexpr const char * GetFlashUsed2 = "GetFlashUsed2";
    static constexpr const char * GetAutoCapture = "GetAutoCapture";
    static constexpr const char * GetXRes = "GetXRes";
    static constexpr const char * GetYRes = "GetYRes";
    static constexpr const char * GetTimeBefore = "GetTimeBefore";
    static constexpr const char * GetTimeBetween = "GetTimeBetween";
    static constexpr const char * GetDeviceName = "GetDeviceName";
    static constexpr const char * GetBatteryPct = "GetBatteryPct";
    static constexpr const char * GetPowerSupply = "GetPowerSupply";
    static constexpr const char * SetEvent = "SetEvent";
    static constexpr const char * SetBatteryMinutes = "SetBatteryMinutes";
    static constexpr const char * SetFlashUsed2 = "SetFlashUsed2";
    static constexpr const char * SetAutoCapture = "SetAutoCapture";
    static constexpr const char * SetTimeBefore = "SetTimeBefore";
    static constexpr const char * SetTimeBetween = "SetTimeBetween";
    static constexpr const char * SetXRes = "SetXRes";
    static constexpr const char * SetYRes = "SetYRes";
    static constexpr const char * SetDeviceName = "SetDeviceName";
    static constexpr const char * SetBatteryPct = "SetBatteryPct";
    static constexpr const char * SetPowerSupply = "SetPowerSupply";


public:
    JavaDTwainLowLevel_TW_DEVICEEVENT(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getEvent();
    TW_UINT32 getBatteryMinutes();
    TW_UINT32 getFlashUsed2();
    TW_UINT32 getAutomaticCapture();
    TW_FIX32 getXResolution();
    TW_FIX32 getYResolution();
    TW_UINT32 getTimeBeforeFirstCapture();
    TW_UINT32 getTimeBetweenCaptures();
    StringType getDeviceName();
    TW_INT16 getBatteryPercentage();
    TW_INT32 getPowerSupply();

    void setEvent(TW_UINT32 val);
    void setBatteryMinutes(TW_UINT32 val);
    void setFlashUsed2(TW_UINT32 val);
    void setAutomaticCapture(TW_UINT32 val);
    void setTimeBeforeFirstCapture(TW_UINT32 val);
    void setTimeBetweenCaptures(TW_UINT32 val);
    void setXResolution(TW_FIX32 val);
    void setYResolution(TW_FIX32 val);
    void setDeviceName(const TW_STR255 val);
    void setBatteryPercentage(TW_INT16 val);
    void setPowerSupply(TW_INT32 val);

    TW_DEVICEEVENT JavaToNative();
    jobject NativeToJava(const TW_DEVICEEVENT& twdevicevent);
};

class JavaDTwainLowLevel_TW_PENDINGXFERS : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_32;
    JavaDTwainLowLevel_TW_UINT16 proxy_16;
    static constexpr const char * GetCount = "GetCount";
    static constexpr const char * GetEOJ = "GetEOJ";
    static constexpr const char * SetCount = "SetCount";
    static constexpr const char * SetEOJ = "SetEOJ";


public:
    JavaDTwainLowLevel_TW_PENDINGXFERS(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16 getCount();
    TW_UINT32 getEOJ();

    void setCount(TW_UINT16 val);
    void setEOJ(TW_UINT32 val);

    TW_PENDINGXFERS JavaToNative();
    jobject NativeToJava(const TW_PENDINGXFERS& twpendingxfers);
};

class JavaDTwainLowLevel_TW_SETUPFILEXFER: public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_16;
    JavaDTwainLowLevel_TW_INT16 proxy_i16;
    JavaDTwainLowLevel_TW_STR255 strproxy;

#ifdef UNICODE
    using StringType = std::wstring;
#else
    using StringType = std::string;
#endif
    static constexpr const char * GetFileName = "GetFileName";
    static constexpr const char * GetFormat = "GetFormat";
    static constexpr const char * GetVRefNum = "GetVRefNum";
    static constexpr const char * SetFileName = "SetFileName";
    static constexpr const char * SetFormat = "SetFormat";
    static constexpr const char * SetVRefNum = "SetVRefNum";

public:
    JavaDTwainLowLevel_TW_SETUPFILEXFER(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    StringType getFileName();
    TW_UINT16 getFormat();
    TW_INT16 getVRefNum();

    void setFileName(StringType val);
    void setFileName(const TW_STR255 val);
    void setFormat(TW_UINT16 val);
    void setVRefNum(TW_INT16 val);

    TW_SETUPFILEXFER JavaToNative();
    jobject NativeToJava(const TW_SETUPFILEXFER& twsetupfilexfer);
};


class JavaDTwainLowLevel_TW_SETUPMEMXFER : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_32;
    static constexpr const char * GetMinSize = "GetMinSize";
    static constexpr const char * GetMaxSize = "GetMaxSize";
    static constexpr const char * GetPrefSize = "GetPrefSize";
    static constexpr const char * SetMinSize = "SetMinSize";
    static constexpr const char * SetMaxSize = "SetMaxSize";
    static constexpr const char * SetPrefSize = "SetPrefSize";

public:
    JavaDTwainLowLevel_TW_SETUPMEMXFER(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getMinBufSize();
    TW_UINT32 getMaxBufSize();
    TW_UINT32 getPreferred();

    void setMinBufSize(TW_UINT32 val);
    void setMaxBufSize(TW_UINT32 val);
    void setPreferred(TW_UINT32 val);

    TW_SETUPMEMXFER JavaToNative();
    jobject NativeToJava(const TW_SETUPMEMXFER& twsetupmemxfer);
};

using JavaDTwainLowLevel_TW_MEMREF = MemoryHandleJavaInterface<MemoryHandleTW_MEMREFTraits>;

class JavaDTwainLowLevel_TW_EVENT : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_16;
    JavaDTwainLowLevel_TW_MEMREF proxy_memref;

    static constexpr const char * GetEvent = "GetEvent";
    static constexpr const char * GetTWMessage = "GetTWMessage";
    static constexpr const char * SetEvent = "SetEvent";
    static constexpr const char * SetTWMessage = "SetTWMessage";

public:
    JavaDTwainLowLevel_TW_EVENT(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_MEMREF getpEvent();
    TW_UINT16 getTWMessage();

    void setTWMessage(TW_UINT16 val);
    void setpEvent(TW_MEMREF pEvent);

    TW_EVENT JavaToNative();
    jobject NativeToJava(const TW_EVENT& twevent);
};

class JavaDTwainLowLevel_TW_FILESYSTEM : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_INT32 proxy_i32;
    JavaDTwainLowLevel_TW_STR255 strproxy_255;
    JavaDTwainLowLevel_TW_STR32 strproxy_32;
    JavaDTwainLowLevel_TW_INT8 proxy_i8;
    JavaDTwainLowLevel_TW_MEMREF proxy_memref;

#ifdef UNICODE
    using value_type = std::wstring;
#else
    using value_type = std::string;
#endif
    using StringType = value_type;
    static constexpr const char * GetInputName = "GetInputName";
    static constexpr const char * GetOutputName = "GetOutputName";
    static constexpr const char * GetContext = "GetContext";
    static constexpr const char * GetRecursive = "GetRecursive";
    static constexpr const char * GetFileType = "GetFileType";
    static constexpr const char * GetSize = "GetSize";
    static constexpr const char * GetCreateTime = "GetCreateTime";
    static constexpr const char * GetModifiedTime = "GetModifiedTime";
    static constexpr const char * GetFreeSpace = "GetFreeSpace";
    static constexpr const char * GetImageSize = "GetImageSize";
    static constexpr const char * GetNumFiles = "GetNumFiles";
    static constexpr const char * GetNumSnippets = "GetNumSnippets";
    static constexpr const char * GetDeviceMask = "GetDeviceMask";
    static constexpr const char * GetReserved = "GetReserved";
    static constexpr const char * SetInputName = "SetInputName";
    static constexpr const char * SetOutputName = "SetOutputName";
    static constexpr const char * SetContext = "SetContext";
    static constexpr const char * SetRecursive = "SetRecursive";
    static constexpr const char * SetFileType = "SetFileType";
    static constexpr const char * SetSize = "SetSize";
    static constexpr const char * SetCreateTime = "SetCreateTime";
    static constexpr const char * SetModifiedTime = "SetModifiedTime";
    static constexpr const char * SetFreeSpace = "SetFreeSpace";
    static constexpr const char * SetImageSize = "SetImageSize";
    static constexpr const char * SetNumFiles = "SetNumFiles";
    static constexpr const char * SetNumSnippets = "SetNumSnippets";
    static constexpr const char * SetDeviceMask = "SetDeviceMask";
    static constexpr const char * SetReserved = "SetReserved";

public:
    JavaDTwainLowLevel_TW_FILESYSTEM(JNIEnv* env);


    jobject createDefaultObject();
    jobject createFullObject(int var1);

    StringType getInputName();
    StringType getOutputName();
    TW_MEMREF getContext();
    TW_INT32 getRecursiveOrSubdirectories();
    TW_UINT32 getFileTypeOrFileSystemType();
    TW_UINT32 getSize();
    StringType getCreateDateTime();
    StringType getModifiedTimeDate();
    TW_UINT32 getFreeSpace();
    TW_INT32 getNewImageSize();
    TW_UINT32 getNumberOfFiles();
    TW_UINT32 getNumberOfSnippets();
    TW_UINT32 getDeviceGroupMask();
    std::vector<TW_INT8> getReserved();

    void setInputName(StringType val);
    void setOutputName(StringType val);
    void setInputName(const TW_STR255 val);
    void setOutputName(const TW_STR255 val);
    void setContext(TW_MEMREF val);
    void setRecursiveOrSubdirectories(TW_INT32 val);
    void setFileTypeOrFileSystemType(TW_UINT32 val);
    void setSize(TW_UINT32 val);
    void setCreateDateTime(StringType val);
    void setCreateDateTime(const TW_STR32 val);
    void setModifiedTimeDate(StringType val);
    void setModifiedTimeDate(const TW_STR32 val);
    void setFreeSpace(TW_UINT32 val);
    void setNewImageSize(TW_INT32 val);
    void setNumberOfFiles(TW_UINT32 val);
    void setNumberOfSnippets(TW_UINT32 val);
    void setDeviceGroupMask(TW_UINT32 val);
    void setReserved(const TW_INT8* val);

    TW_FILESYSTEM JavaToNative();
    jobject NativeToJava(const TW_FILESYSTEM& twfilesystem);
};

class JavaDTwainLowLevel_TW_METRICS : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    static constexpr const char * GetSizeOf = "GetSizeOf";
    static constexpr const char * GetImageCount = "GetImageCount";
    static constexpr const char * GetSheetCount = "GetSheetCount";
    static constexpr const char * SetSizeOf = "SetSizeOf";
    static constexpr const char * SetImageCount = "SetImageCount";
    static constexpr const char * SetSheetCount = "SetSheetCount";


public:
    JavaDTwainLowLevel_TW_METRICS(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getSizeOf();
    TW_UINT32 getImageCount();
    TW_UINT32 getSheetCount();

    void setSizeOf(TW_UINT32 val);
    void setImageCount(TW_UINT32 val);
    void setSheetCount(TW_UINT32 val);

    TW_METRICS JavaToNative();
    jobject NativeToJava(const TW_METRICS& twmetrics);
};

class JavaDTwainLowLevel_TW_PASSTHRU : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_INT32 proxy_i32;
    JavaDTwainLowLevel_TW_MEMREF proxy_memref;
    static constexpr const char * GetCommand = "GetCommand";
    static constexpr const char * GetCommandBytes = "GetCommandBytes";
    static constexpr const char * GetDirection = "GetDirection";
    static constexpr const char * GetData = "GetData";
    static constexpr const char * GetDataBytes = "GetDataBytes";
    static constexpr const char * GetDataBytesXfered = "GetDataBytesXfered";
    static constexpr const char * SetCommand = "SetCommand";
    static constexpr const char * SetCommandBytes = "SetCommandBytes";
    static constexpr const char * SetDirection = "SetDirection";
    static constexpr const char * SetData = "SetData";
    static constexpr const char * SetDataBytes = "SetDataBytes";
    static constexpr const char * SetDataBytesXfered = "SetDataBytesXfered";


public:
    JavaDTwainLowLevel_TW_PASSTHRU(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_MEMREF getpCommand();
    TW_MEMREF getpData();
    TW_UINT32 getCommandBytes();
    TW_UINT32 getDataBytes();
    TW_INT32 getDirection();
    TW_INT32 getDataBytesXfered();

    void setpCommand(TW_MEMREF val);
    void setpData(TW_MEMREF val);
    void setCommandBytes(TW_UINT32 val);
    void setDataBytes(TW_UINT32 val);
    void setDirection(TW_INT32 val);
    void setDataBytesXfered(TW_INT32 val);

    TW_PASSTHRU JavaToNative();
    jobject NativeToJava(const TW_PASSTHRU& twpassthru);
};

class JavaDTwainLowLevel_TW_STATUS : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    using value_type = TW_STATUS;
    using actual_type = TW_STATUS;

    static constexpr const char * GetConditionCode  = "GetConditionCode";
    static constexpr const char * GetData = "GetData";
    static constexpr const char * GetReserved = "GetReserved";
    static constexpr const char * SetConditionCode = "SetConditionCode";
    static constexpr const char * SetData = "SetData";
    static constexpr const char * SetReserved = "SetReserved";

public:
    JavaDTwainLowLevel_TW_STATUS(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16 getConditonCode();
    TW_UINT16 getData();
    TW_UINT16 getReserved();

    void setConditionCode(TW_UINT16 val);
    void setData(TW_UINT16 val);
    void setReserved(TW_UINT16 val);

    TW_STATUS JavaToNative();
    jobject NativeToJava(const TW_STATUS& twstatus);
    TW_STATUS getValue() { return JavaToNative(); }
    void setValue(const TW_STATUS& twstatus) { NativeToJava(twstatus); }
};


class JavaDTwainLowLevel_TW_STATUSUTF8 : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_STATUS proxy_status;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_HANDLE proxy_handle;

    static constexpr const char * GetStatus = "GetStatus";
    static constexpr const char * GetSize = "GetSize";
    static constexpr const char * GetUTF8String = "GetUTF8String";
    static constexpr const char * SetStatus = "SetStatus";
    static constexpr const char * SetSize = "SetSize";
    static constexpr const char * SetUTF8String = "SetUTF8String";

public:
    JavaDTwainLowLevel_TW_STATUSUTF8(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_STATUS getStatus();
    TW_UINT32 getSize();
    TW_HANDLE getUTF8string();

    void setStatus(TW_STATUS val);
    void setSize(TW_UINT32 val);
    void setUTF8string(TW_HANDLE val);

    TW_STATUSUTF8 JavaToNative();
    jobject NativeToJava(const TW_STATUSUTF8& twstatusutf8);
};

class JavaDTwainLowLevel_TW_CIEPOINT : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;

    static constexpr const char * GetX = "GetX";
    static constexpr const char * GetY = "GetY";
    static constexpr const char * GetZ = "GetZ";
    static constexpr const char * SetX = "SetX";
    static constexpr const char * SetY = "SetY";
    static constexpr const char * SetZ = "SetZ";

public:
    using value_type = TW_CIEPOINT;
    JavaDTwainLowLevel_TW_CIEPOINT(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_FIX32 getX();
    TW_FIX32 getY();
    TW_FIX32 getZ();
    void setX(TW_FIX32 val);
    void setY(TW_FIX32 val);
    void setZ(TW_FIX32 val);

    TW_CIEPOINT JavaToNative();
    TW_CIEPOINT getValue();
    void setValue(const TW_CIEPOINT& val);
};

class JavaDTwainLowLevel_TW_DECODEFUNCTION : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;
    static constexpr const char *  GetStartIn = "GetStartIn";
    static constexpr const char *  GetBreakIn = "GetBreakIn";
    static constexpr const char *  GetEndIn = "GetEndIn";
    static constexpr const char *  GetStartOut = "GetStartOut";
    static constexpr const char *  GetBreakOut = "GetBreakOut";
    static constexpr const char *  GetEndOut = "GetEndOut";
    static constexpr const char *  GetGamma = "GetGamma";
    static constexpr const char *  GetSampleCount = "GetSampleCount";
    static constexpr const char *  SetStartIn = "SetStartIn";
    static constexpr const char *  SetBreakIn = "SetBreakIn";
    static constexpr const char *  SetEndIn = "SetEndIn";
    static constexpr const char *  SetStartOut = "SetStartOut";
    static constexpr const char *  SetBreakOut = "SetBreakOut";
    static constexpr const char *  SetEndOut = "SetEndOut";
    static constexpr const char *  SetGamma = "SetGamma";
    static constexpr const char *  SetSampleCount = "SetSampleCount";

public:
    using value_type = TW_DECODEFUNCTION;

    JavaDTwainLowLevel_TW_DECODEFUNCTION(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_FIX32 getStartIn();
    TW_FIX32 getBreakIn();
    TW_FIX32 getEndIn();
    TW_FIX32 getStartOut();
    TW_FIX32 getBreakOut();
    TW_FIX32 getEndOut();
    TW_FIX32 getGamma();
    TW_FIX32 getSampleCount();
    void setStartIn(TW_FIX32 val);
    void setBreakIn(TW_FIX32 val);
    void setEndIn(TW_FIX32 val);
    void setStartOut(TW_FIX32 val);
    void setBreakOut(TW_FIX32 val);
    void setEndOut(TW_FIX32 val);
    void setGamma(TW_FIX32 val);
    void setSampleCount(TW_FIX32 val);

    TW_DECODEFUNCTION JavaToNative();
    TW_DECODEFUNCTION getValue();

    void setValue(const TW_DECODEFUNCTION& val);
};

class JavaDTwainLowLevel_TW_TRANSFORMSTAGE : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;
    JavaDTwainLowLevel_TW_DECODEFUNCTION proxy_decode;

    static constexpr const char * GetDecodeValue = "GetDecodeValue";
    static constexpr const char * GetMixValue = "GetMixValue";
    static constexpr const char * SetDecodeValue = "SetDecodeValue";
    static constexpr const char * SetMixValue = "SetMixValue";

public:
    using value_type = TW_TRANSFORMSTAGE;
    JavaDTwainLowLevel_TW_TRANSFORMSTAGE(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_DECODEFUNCTION getDecodeValue(int value);
    TW_FIX32 getMixValue(int val1, int val2);

    void setDecodeValue(const TW_DECODEFUNCTION& val1, int val2);
    void setMixValue(TW_FIX32 val1, int val2, int val3);

    TW_TRANSFORMSTAGE JavaToNative();
    TW_TRANSFORMSTAGE getValue();

    void setValue(const TW_TRANSFORMSTAGE& val);
};

class JavaDTwainLowLevel_TW_CIECOLOR : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_INT16 proxy_i16;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_INT32 proxy_i32;
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;
    JavaDTwainLowLevel_TW_CIEPOINT proxy_ciepoint;
    JavaDTwainLowLevel_TW_TRANSFORMSTAGE proxy_transformstage;
    JavaDTwainLowLevel_TW_STATUS proxy_status;

public:
    JavaDTwainLowLevel_TW_CIECOLOR(JNIEnv* env);

    static constexpr const char * GetNumSamples = "GetNumSamples";
    static constexpr const char * GetColorSpace = "GetColorSpace";
    static constexpr const char * GetLowEndian = "GetLowEndian";
    static constexpr const char * GetDeviceDependent = "GetDeviceDependent";
    static constexpr const char * GetVersionNumber = "GetVersionNumber";
    static constexpr const char * GetStageABC = "GetStageABC";
    static constexpr const char * GetStageLMN = "GetStageLMN";
    static constexpr const char * GetWhitePoint = "GetWhitePoint";
    static constexpr const char * GetBlackPoint = "GetBlackPoint";
    static constexpr const char * GetWhitePaper = "GetWhitePaper";
    static constexpr const char * GetBlackInk = "GetBlackInk";
    static constexpr const char * GetSamples = "GetSamples";
    static constexpr const char * GetSample = "GetSample";
    static constexpr const char * SetNumSamples = "SetNumSamples";
    static constexpr const char * SetColorSpace = "SetColorSpace";
    static constexpr const char * SetLowEndian = "SetLowEndian";
    static constexpr const char * SetDeviceDependent = "SetDeviceDependent";
    static constexpr const char * SetVersionNumber = "SetVersionNumber";
    static constexpr const char * SetStageABC = "SetStageABC";
    static constexpr const char * SetStageLMN = "SetStageLMN";
    static constexpr const char * SetWhitePoint = "SetWhitePoint";
    static constexpr const char * SetBlackPoint = "SetBlackPoint";
    static constexpr const char * SetWhitePaper = "SetWhitePaper";
    static constexpr const char * SetBlackInk = "SetBlackInk";
    static constexpr const char * SetSample = "SetSample";

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_INT32 getNumSamples();
    TW_UINT16 getColorSpace();
    TW_INT16 getLowEndian();
    TW_INT16 getDeviceDependent();
    TW_INT32 getVersionNumber();
    TW_TRANSFORMSTAGE getStageABC();
    TW_TRANSFORMSTAGE getStageLMN();
    TW_CIEPOINT getWhitePoint();
    TW_CIEPOINT getBlackPoint();
    TW_CIEPOINT getWhitePaper();
    TW_CIEPOINT getBlackInk();
    TW_FIX32 getSample(int i);
    std::vector<TW_FIX32> getSamples();

    void setNumSamples(TW_INT32 val);
    void setColorSpace(TW_UINT16 val);
    void setLowEndian(TW_INT16 val);
    void setDeviceDependent(TW_INT16 val);
    void setVersionNumber(TW_INT32 val);
    void setStageABC(const TW_TRANSFORMSTAGE& val);
    void setStageLMN(const TW_TRANSFORMSTAGE& val);
    void setWhitePoint(const TW_CIEPOINT& val);
    void setBlackPoint(const TW_CIEPOINT& val);
    void setWhitePaper(const TW_CIEPOINT& val);
    void setBlackInk(const TW_CIEPOINT& val);
    void setSample(TW_FIX32 val, int i);
    TW_CIECOLOR JavaToNative();
    jobject NativeToJava(const TW_CIECOLOR& twciecolor);
    TW_CIECOLOR getValue();
};

class JavaDTwainLowLevel_TW_INFO: public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_UINTPTR proxy_u64;

public:
    using value_type = TW_INFO;
    JavaDTwainLowLevel_TW_INFO(JNIEnv* env);

    static constexpr const char * GetInfoID = "GetInfoID";
    static constexpr const char * GetItemType = "GetItemType";
    static constexpr const char * GetNumItems = "GetNumItems";
    static constexpr const char * GetReturnCode = "GetReturnCode";
    static constexpr const char * GetItem = "GetItem";
    static constexpr const char * SetInfoID = "SetInfoID";
    static constexpr const char * SetItemType = "SetItemType";
    static constexpr const char * SetNumItems = "SetNumItems";
    static constexpr const char * SetReturnCode = "SetReturnCode";
    static constexpr const char * SetItem = "SetItem";

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16 getInfoID();
    TW_UINT16 getItemType();
    TW_UINT16 getNumItems();
    TW_UINT16 getReturnCode();
    TW_UINTPTR getItem();

    void setInfoID(TW_UINT16 val);
    void setItemType(TW_UINT16 val);
    void setNumItems(TW_UINT16 val);
    void setReturnCode(TW_UINT16 val);
    void setItem(TW_UINTPTR val);

    TW_INFO JavaToNative();
    TW_INFO getValue();
    void setValue(const TW_INFO& val);
};

class JavaDTwainLowLevel_TW_EXTIMAGEINFO : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_INFO proxy_info;

public:
    JavaDTwainLowLevel_TW_EXTIMAGEINFO(JNIEnv* env);

    static constexpr const char * GetNumInfos = "GetNumInfos";
    static constexpr const char * GetOneInfo = "GetOneInfo";
    static constexpr const char * SetNumInfos = "SetNumInfos";
    static constexpr const char * SetOneInfo = "SetOneInfo";

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getNumInfos();
    TW_INFO getOneInfo(int i);
    void setNumInfos(TW_UINT32 val);
    void setOneInfo(const TW_INFO& info, int i);

    TW_EXTIMAGEINFO JavaToNative();
    jobject NativeToJava(const TW_EXTIMAGEINFO& twextimageinfo);
    TW_EXTIMAGEINFO getValue();
};

class JavaDTwainLowLevel_TW_FILTER : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_HANDLE proxy_handle;

public:
    JavaDTwainLowLevel_TW_FILTER(JNIEnv* env);

    static constexpr const char* GetSize = "GetSize";
    static constexpr const char* GetDescriptorCount = "GetDescriptorCount";
    static constexpr const char* GetMaxDescriptorCount = "GetMaxDescriptorCount";
    static constexpr const char* GetCondition = "GetCondition";
    static constexpr const char* GethDescriptors = "GethDescriptors";
    static constexpr const char* SetSize = "SetSize";
    static constexpr const char* SetDescriptorCount = "SetDescriptorCount";
    static constexpr const char* SetMaxDescriptorCount = "SetMaxDescriptorCount";
    static constexpr const char* SetCondition = "SetCondition";
    static constexpr const char* SethDescriptors = "SethDescriptors";

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getSize();
    TW_UINT32 getDescriptorCount();
    TW_UINT32 getMaxDescriptorCount();
    TW_UINT32 getCondition();
    TW_HANDLE gethDescriptors();

    void setSize(TW_UINT32 val);
    void setDescriptorCount(TW_UINT32 val);
    void setMaxDescriptorCount(TW_UINT32 val);
    void setCondition(TW_UINT32 val);
    void sethDescriptors(TW_HANDLE val);

    TW_FILTER JavaToNative();
    jobject NativeToJava(const TW_FILTER& twfilter);
    TW_FILTER getValue();

    void setValue(const TW_FILTER& filter);
};

class JavaDTwainLowLevel_TW_ELEMENT8 : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT8 proxy_u8;

public:

    static constexpr const char* GetIndex = "GetIndex";
    static constexpr const char* GetChannel1 = "GetChannel1";
    static constexpr const char* GetChannel2 = "GetChannel2";
    static constexpr const char* GetChannel3 = "GetChannel3";
    static constexpr const char* SetIndex = "SetIndex";
    static constexpr const char* SetChannel1 = "SetChannel1";
    static constexpr const char* SetChannel2 = "SetChannel2";
    static constexpr const char* SetChannel3 = "SetChannel3";

    using value_type = TW_ELEMENT8;
    JavaDTwainLowLevel_TW_ELEMENT8(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT8 getIndex();
    TW_UINT8 getChannel1();
    TW_UINT8 getChannel2();
    TW_UINT8 getChannel3();

    void setIndex(TW_UINT8 val);
    void setChannel1(TW_UINT8 val);
    void setChannel2(TW_UINT8 val);
    void setChannel3(TW_UINT8 val);

    TW_ELEMENT8 JavaToNative();
    TW_ELEMENT8 getValue();

    void setValue(const TW_ELEMENT8& element8);
};

class JavaDTwainLowLevel_TW_IMAGEINFO : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;
    JavaDTwainLowLevel_TW_INT32 proxy_i32;
    JavaDTwainLowLevel_TW_INT16 proxy_i16;

public:
    using value_type = TW_IMAGEINFO;

    static constexpr const char* GetXRes = "GetXRes";
    static constexpr const char* GetYRes = "GetYRes";
    static constexpr const char* GetWidth = "GetWidth";
    static constexpr const char* GetLength = "GetLength";
    static constexpr const char* GetSamples = "GetSamples";
    static constexpr const char* GetBPP = "GetBPP";
    static constexpr const char* GetPlanar = "GetPlanar";
    static constexpr const char* GetPixelType = "GetPixelType";
    static constexpr const char* GetCompression = "GetCompression";
    static constexpr const char* GetBitsPerSample = "GetBitsPerSample";
    static constexpr const char* SetXRes = "SetXRes";
    static constexpr const char* SetYRes = "SetYRes";
    static constexpr const char* SetWidth = "SetWidth";
    static constexpr const char* SetLength = "SetLength";
    static constexpr const char* SetSamples = "SetSamples";
    static constexpr const char* SetBPP = "SetBPP";
    static constexpr const char* SetPlanar = "SetPlanar";
    static constexpr const char* SetPixelType = "SetPixelType";
    static constexpr const char* SetCompression = "SetCompression";
    static constexpr const char* SetBitsPerSample = "SetBitsPerSample";

    JavaDTwainLowLevel_TW_IMAGEINFO(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_FIX32 getXResolution();
    TW_FIX32 getYResolution();
    TW_INT32 getImageWidth();
    TW_INT32 getImageLength();
    TW_INT16 getSamplesPerPixel();
    TW_INT16 getBitsPerPixel();
    TW_INT16 getPlanar();
    TW_INT16 getPixelType();
    TW_INT16 getCompression();
    TW_INT16 getBitsPerSampleValue(int32_t val);

    void setXResolution(TW_FIX32 val);
    void setYResolution(TW_FIX32 val);
    void setImageWidth(TW_INT32 val);
    void setImageLength(TW_INT32 val);
    void setSamplesPerPixel(TW_INT16 val);
    void setBitsPerPixel(TW_INT16 val);
    void setPlanar(TW_INT16 val);
    void setPixelType(TW_INT16 val);
    void setCompression(TW_INT16 val);
    void setBitsPerSampleValue(TW_INT16 val1, int32_t val2);

    TW_IMAGEINFO JavaToNative();
    jobject NativeToJava(const TW_IMAGEINFO& twimageinfo);

    void setValue(const TW_IMAGEINFO& twimageinfo);
    TW_IMAGEINFO getValue();
};

class JavaDTwainLowLevel_TW_FRAME : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FIX32 proxy_fix32;
    static constexpr const char *SetTop = "SetTop";
    static constexpr const char *GetTop = "GetTop";
    static constexpr const char *SetBottom = "SetBottom";
    static constexpr const char *GetBottom = "GetBottom";
    static constexpr const char *SetRight = "SetRight";
    static constexpr const char *GetRight = "GetRight";
    static constexpr const char *SetLeft = "SetLeft";
    static constexpr const char *GetLeft = "GetLeft";
public:
    using value_type = TW_FRAME;
    JavaDTwainLowLevel_TW_FRAME(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_FIX32 getLeft();
    TW_FIX32 getTop();
    TW_FIX32 getRight();
    TW_FIX32 getBottom();

    void setLeft(TW_FIX32 val);
    void setTop(TW_FIX32 val);
    void setRight(TW_FIX32 val);
    void setBottom(TW_FIX32 val);

    TW_FRAME JavaToNative();
    TW_FRAME getValue();
    void setValue(TW_FRAME twframe);
};

class JavaDTwainLowLevel_TW_MEMORY : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_MEMREF proxy_memref;

    static constexpr const char *GetFlags = "GetFlags";
    static constexpr const char *GetLength = "GetLength";
    static constexpr const char *GetMem = "GetMem";
    static constexpr const char *SetFlags = "SetFlags";
    static constexpr const char *SetLength = "SetLength";
    static constexpr const char *SetMem = "SetMem";

public:
    using value_type = TW_MEMORY;

    JavaDTwainLowLevel_TW_MEMORY(JNIEnv* env);
    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32 getFlags();
    TW_UINT32 getLength();
    TW_MEMREF getTheMem();

    void setFlags(TW_UINT32 val);
    void setLength(TW_UINT32 val);
    void setTheMem(TW_MEMREF val);

    TW_MEMORY JavaToNative();
    jobject NativeToJava(const TW_MEMORY& twmemory);
    TW_MEMORY getValue();
    void setValue(TW_MEMORY twmemory);
};

class JavaDTwainLowLevel_TW_IMAGELAYOUT: public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_FRAME proxy_frame;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    static constexpr const char * GetFrame = "GetFrame";
    static constexpr const char * GetDocumentNumber = "GetDocumentNumber";
    static constexpr const char * GetPageNumber = "GetPageNumber";
    static constexpr const char * GetFrameNumber = "GetFrameNumber";
    static constexpr const char * SetFrame = "SetFrame";
    static constexpr const char * SetDocumentNumber = "SetDocumentNumber";
    static constexpr const char * SetPageNumber = "SetPageNumber";
    static constexpr const char * SetFrameNumber = "SetFrameNumber";

public:
    using value_type = TW_IMAGELAYOUT;
    JavaDTwainLowLevel_TW_IMAGELAYOUT(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_FRAME getFrame();
    TW_UINT32 getDocumentNumber();
    TW_UINT32 getPageNumber();
    TW_UINT32 getFrameNumber();

    void setFrame(TW_FRAME val);

    void setDocumentNumber(TW_UINT32 val);
    void setPageNumber(TW_UINT32 val);
    void setFrameNumber(TW_UINT32 val);

    TW_IMAGELAYOUT JavaToNative();
    jobject NativeToJava(const TW_IMAGELAYOUT& twimagelayout);
    TW_IMAGELAYOUT getValue();
    void setValue(const TW_IMAGELAYOUT& twimagelayout);
};

class JavaDTwainLowLevel_TW_IMAGEMEMXFER : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_MEMORY proxy_memory;

    static constexpr const char * GetCompression = "GetCompression";
    static constexpr const char * GetBytesPerRow = "GetBytesPerRow";
    static constexpr const char * GetColumns = "GetColumns";
    static constexpr const char * GetRows = "GetRows";
    static constexpr const char * GetXOffset = "GetXOffset";
    static constexpr const char * GetYOffset = "GetYOffset";
    static constexpr const char * GetBytesWritten = "GetBytesWritten";
    static constexpr const char * GetMemory = "GetMemory";
    static constexpr const char * SetCompression = "SetCompression";
    static constexpr const char * SetBytesPerRow = "SetBytesPerRow";
    static constexpr const char * SetColumns = "SetColumns";
    static constexpr const char * SetRows = "SetRows";
    static constexpr const char * SetXOffset = "SetXOffset";
    static constexpr const char * SetYOffset = "SetYOffset";
    static constexpr const char * SetBytesWritten = "SetBytesWritten";
    static constexpr const char * SetMemory = "SetMemory";

public:
    using value_type = TW_IMAGEMEMXFER;
    JavaDTwainLowLevel_TW_IMAGEMEMXFER(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16   getCompression();
    TW_UINT32   getBytesPerRow();
    TW_UINT32   getColumns();
    TW_UINT32   getRows();
    TW_UINT32   getXOffset();
    TW_UINT32   getYOffset();
    TW_UINT32   getBytesWritten();
    TW_MEMORY   getMemory();

    void setCompression(TW_UINT16 val);
    void setBytesPerRow(TW_UINT32 val);
    void setColumns(TW_UINT32 val);
    void setRows(TW_UINT32 val);
    void setXOffset(TW_UINT32 val);
    void setYOffset(TW_UINT32 val);
    void setBytesWritten(TW_UINT32 val);
    void setMemory(TW_MEMORY val);

    TW_IMAGEMEMXFER JavaToNative();
    jobject NativeToJava(const TW_IMAGEMEMXFER& twimagememxfer);
    TW_IMAGEMEMXFER getValue();
    void setValue(const TW_IMAGEMEMXFER& twimagelayout);
};

class JavaDTwainLowLevel_TW_JPEGCOMPRESSION : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_MEMORY proxy_memory;
    static constexpr const char *GetColorSpace         = "GetColorSpace";
    static constexpr const char *GetNumComponents      = "GetNumComponents";
    static constexpr const char *GetRestartFrequency   = "GetRestartFrequency";
    static constexpr const char *GetSubSampling        = "GetSubSampling";
    static constexpr const char *GetQuantMapValue      = "GetQuantMapValue";
    static constexpr const char *GetQuantTableValue    = "GetQuantTableValue";
    static constexpr const char *GetHuffmanMapValue    = "GetHuffmanMapValue";
    static constexpr const char *GetHuffmanDCValue     = "GetHuffmanDCValue";
    static constexpr const char *GetHuffmanACValue     = "GetHuffmanACValue";
    static constexpr const char *SetColorSpace         = "SetColorSpace";
    static constexpr const char *SetNumComponents      = "SetNumComponents";
    static constexpr const char *SetRestartFrequency   = "SetRestartFrequency";
    static constexpr const char *SetSubSampling        = "SetSubSampling";
    static constexpr const char *SetQuantMapValue      = "SetQuantMapValue";
    static constexpr const char *SetQuantTableValue    = "SetQuantTableValue";
    static constexpr const char *SetHuffmanMapValue    = "SetHuffmanMapValue";
    static constexpr const char *SetHuffmanDCValue     = "SetHuffmanDCValue";
    static constexpr const char *SetHuffmanACValue     = "SetHuffmanACValue";

public:
    using value_type = TW_JPEGCOMPRESSION;
     JavaDTwainLowLevel_TW_JPEGCOMPRESSION(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16   getColorSpace();
    TW_UINT32   getSubSampling();
    TW_UINT16   getNumComponents();
    TW_UINT16   getRestartFrequency();
    TW_UINT16   getQuantMapValue(int32_t val);
    TW_MEMORY   getQuantTableValue(int32_t val);
    TW_UINT16   getHuffmanMapValue(int32_t val);
    TW_MEMORY   getHuffmanDCValue(int32_t val);
    TW_MEMORY   getHuffmanACValue(int32_t val);

    void setColorSpace(TW_UINT16 val);
    void setSubSampling(TW_UINT32 val);
    void setNumComponents(TW_UINT16 val);
    void setRestartFrequency(TW_UINT16 val);
    void setQuantMapValue(TW_UINT16 val, int32_t which);
    void setQuantTableValue(TW_MEMORY val, int32_t which);
    void setHuffmanMapValue(TW_UINT16 val, int32_t which);
    void setHuffmanDCValue(TW_MEMORY val, int32_t which);
    void setHuffmanACValue(TW_MEMORY val, int32_t which);

    TW_JPEGCOMPRESSION JavaToNative();
    jobject NativeToJava(const TW_JPEGCOMPRESSION& twjpegcompression);
    TW_JPEGCOMPRESSION getValue();
    void setValue(const TW_JPEGCOMPRESSION& twjpegcompression);
};

class JavaDTwainLowLevel_TW_PALETTE8 : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_ELEMENT8 proxy_element8;
    static constexpr const char *GetNumColors   = "GetNumColors";
    static constexpr const char *GetPaletteType = "GetPaletteType";
    static constexpr const char *GetColorValue = "GetColorValue";
    static constexpr const char *SetNumColors = "SetNumColors";
    static constexpr const char *SetPaletteType = "SetPaletteType";
    static constexpr const char *SetColorValue = "SetColorValue";

public:
    using value_type = TW_PALETTE8;
    JavaDTwainLowLevel_TW_PALETTE8(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT16   getNumColors();
    TW_UINT16   getPaletteType();
    TW_ELEMENT8 getColorValue(int32_t which);

    void setNumColors(TW_UINT16 val);
    void setPaletteType(TW_UINT16 val);
    void setColorValue(TW_ELEMENT8 val, int32_t which);

    TW_PALETTE8 JavaToNative();
    jobject NativeToJava(const TW_PALETTE8& twpalette8);
    TW_PALETTE8 getValue();
    void setValue(const TW_PALETTE8& twpalette8);
};

class JavaDTwainLowLevel_TW_TWAINDIRECT : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_UINT16 proxy_u16;
    JavaDTwainLowLevel_TW_UINT32 proxy_u32;
    JavaDTwainLowLevel_TW_HANDLE proxy_handle;
    static constexpr const char *GetSizeOf = "GetSizeOf";
    static constexpr const char *GetCommunicationManager = "GetCommunicationManager";
    static constexpr const char *GetSend = "GetSend";
    static constexpr const char *GetSendSize = "GetSendSize";
    static constexpr const char *GetReceive = "GetReceive";
    static constexpr const char *GetReceiveSize = "GetReceiveSize";
    static constexpr const char *SetSizeOf = "SetSizeOf";
    static constexpr const char *SetCommunicationManager = "SetCommunicationManager";
    static constexpr const char *SetSend = "SetSend";
    static constexpr const char *SetSendSize = "SetSendSize";
    static constexpr const char *SetReceive = "SetReceive";
    static constexpr const char *SetReceiveSize = "SetReceiveSize";

public:
    using value_type = TW_TWAINDIRECT;
    JavaDTwainLowLevel_TW_TWAINDIRECT(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_UINT32   getSizeOf();
    TW_UINT16   getCommunicationManager();
    TW_HANDLE   getSend();
    TW_UINT32   getSendSize();
    TW_HANDLE   getReceive();
    TW_UINT32   getReceiveSize();

    void    setSizeOf(TW_UINT32 val);
    void    setCommunicationManager(TW_UINT16 val);
    void    setSend(TW_HANDLE val);
    void    setSendSize(TW_UINT32 val);
    void    setReceive(TW_HANDLE val);
    void    setReceiveSize(TW_UINT32 val);

    TW_TWAINDIRECT JavaToNative();
    jobject NativeToJava(const TW_TWAINDIRECT& twtwaindirect);
    TW_TWAINDIRECT getValue();
    void setValue(const TW_TWAINDIRECT& twtwaindirect);
};

class JavaDTwainLowLevel_TW_USERINTERFACE : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_BOOL proxy_bool;
    JavaDTwainLowLevel_TW_HANDLE proxy_handle;
    static constexpr const char *GetShowUI = "GetShowUI";
    static constexpr const char *GetModalUI = "GetModalUI";
    static constexpr const char *GetParent = "GetParent";
    static constexpr const char *SetShowUI = "SetShowUI";
    static constexpr const char *SetModalUI = "SetModalUI";
    static constexpr const char *SetParent = "SetParent";

public:
    using value_type = TW_USERINTERFACE;
    JavaDTwainLowLevel_TW_USERINTERFACE(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_BOOL getShowUI();
    TW_BOOL getModalUI();
    TW_HANDLE gethParent();

    void setShowUI(TW_BOOL val);
    void setModalUI(TW_BOOL val);
    void sethParent(TW_HANDLE val);

    TW_USERINTERFACE JavaToNative();
    jobject NativeToJava(const TW_USERINTERFACE& twuserinterface);
    TW_USERINTERFACE getValue();
    void setValue(const TW_USERINTERFACE& twuserinterface);
};

class JavaDTwainLowLevel_TW_NULL : public JavaObjectCaller
{
public:
    using value_type = TW_NULL;
    JavaDTwainLowLevel_TW_NULL(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    TW_NULL JavaToNative();
    TW_NULL getValue();
    void setValue(const TW_NULL& twnull);
};

class JavaDTwainLowLevel_TW_RESPONSETYPE : public JavaObjectCaller
{
    JavaDTwainLowLevel_TW_ELEMENT8 proxy_element8;
    static constexpr const char *GetNumItems = "GetNumItems";
    static constexpr const char *SetNumItems = "SetNumItems";
    static constexpr const char *GetResponseValue = "GetResponseValue";
    static constexpr const char *SetResponseValue = "SetResponseValue";

public:
    using value_type = TW_RESPONSETYPE;
    JavaDTwainLowLevel_TW_RESPONSETYPE(JNIEnv* env);

    jobject createDefaultObject();
    jobject createFullObject(int var1);

    int32_t getNumItems();
    TW_ELEMENT8 getResponseValue(int32_t which);

    void setNumItems(int32_t val);
    void setResponseValue(TW_ELEMENT8 val, int32_t nWhich);

    TW_RESPONSETYPE JavaToNative();
    TW_RESPONSETYPE getValue();
    void setValue(const TW_RESPONSETYPE& twresponsetype);
};

class JavaDTwainLowLevel_TwainLowLevel : public JavaObjectCaller
{
    static constexpr const char* GetTwainObject = "GetTwainObject";
    public:
        using value_type = TwainLowLevel;
        JavaDTwainLowLevel_TwainLowLevel(JNIEnv* env);

        jobject createDefaultObject();
        jobject createFullObject(int var1);
        jobject getTwainObject();

        TwainLowLevel JavaToNative();
        TwainLowLevel getValue();
        void setValue(const TwainLowLevel& twainlowlevel);
};


class JavaDTwainLowLevel_TwainTriplet : public JavaObjectCaller
{
    JavaDTwainLowLevel_TwainLowLevel proxy_twlowlevel;
    static constexpr const char *GetOriginID = "GetOriginID";
    static constexpr const char *GetDestinationID = "GetDestinationID";
    static constexpr const char *GetDG = "GetDG";
    static constexpr const char *GetDAT = "GetDAT";
    static constexpr const char *GetMSG = "GetMSG";
    static constexpr const char *GetData = "GetData";
public:
    JavaDTwainLowLevel_TwainTriplet(JNIEnv* env);

    jobject createDefaultObject();

    jobject getOriginID();
    jobject getDestinationID();
    jobject getDG();
    jobject getDAT();
    jobject getMSG();
    jobject getData();

    TwainTripletFromJava JavaToNative();
    TwainTripletFromJava getValue();
};

class JavaTwainImageInfo : public JavaObjectCaller
{
    private:
        JavaDTwainLowLevel_TW_IMAGEINFO proxy_imageinfo;
    public:

        static constexpr const char * GetImageInfo = "GetImageInfo";
        static constexpr const char * SetImageInfo = "SetImageInfo";
        JavaTwainImageInfo(JNIEnv* env);
        jobject createDefaultObject();
        TW_IMAGEINFO getImageInfo();
        void setImageInfo(TW_IMAGEINFO iInfo);
        jobject NativeToJava(TW_IMAGEINFO iInfo);
};

class JavaInteger : public JavaObjectCaller
{
    static constexpr const char *IntValue = "IntValue";
public:
    JavaInteger(JNIEnv* env);
    jobject NativeToJava(int32_t iInfo);
    int32_t JavaToNative();
    jobject createFullObject(int32_t var1);
};

struct ArrayIntegerList
{
    using value_type = int32_t;
    using java_type = jobject;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, jobject value)
    {
        JavaInteger javaInteger(pEnv);
        javaInteger.setObject(value);
        int32_t val = javaInteger.JavaToNative();
        cont.push_back(val);
    }

    static jobject GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        JavaInteger javaInteger(pEnv);
        return javaInteger.NativeToJava(v);
    }
};

class JavaDouble : public JavaObjectCaller
{
    static constexpr const char *DoubleValue = "DoubleValue";
public:
    JavaDouble(JNIEnv* env);
    jobject NativeToJava(double iInfo);
    double JavaToNative();
    jobject createFullObject(double var1);
};

struct ArrayDoubleList
{
    using value_type = double;
    using java_type = jobject;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, jobject value)
    {
        JavaDouble javaDouble(pEnv);
        javaDouble.setObject(value);
        auto val = javaDouble.JavaToNative();
        cont.push_back(val);
    }

    static jobject GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        JavaDouble javaDouble(pEnv);
        return javaDouble.NativeToJava(v);
    }
};

struct FrameStruct
{
    double left = 0;
    double top = 0;
    double right = 0;
    double bottom = 0;
};

struct ArrayFrameList
{
    using value_type = FrameStruct;
    using java_type = jobject;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, jobject value)
    {
        JavaFrameInfo javaFrame(pEnv);
        javaFrame.setObject(value);
        auto val = javaFrame.JavaToNative();
        cont.push_back(val);
    }

    static jobject GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        JavaFrameInfo javaFrame(pEnv);
        return javaFrame.NativeToJava(v);
    }
};


class JavaBoolean : public JavaObjectCaller
{
    static constexpr const char *BooleanValue = "BooleanValue";
public:
    JavaBoolean(JNIEnv* env);
    jobject NativeToJava(bool iInfo);
    bool JavaToNative();
    jobject createFullObject(bool var1);
};

struct ArrayBooleanList
{
    using value_type = jboolean;
    using java_type = jobject;

    template <typename Container>
    static void AddValueToCPPList(JNIEnv* pEnv, Container& cont, jobject value)
    {
        JavaBoolean javaBoolean(pEnv);
        javaBoolean.setObject(value);
        int32_t val = javaBoolean.JavaToNative();
        cont.push_back(val);
    }

    static jobject GetValueFromCPPList(JNIEnv* pEnv, value_type v)
    {
        JavaBoolean javaBoolean(pEnv);
        return javaBoolean.NativeToJava(v);
    }
};

class JavaExtendedImageInfo;

template <typename JavaClass>
class JavaExtendedImageInfo_ParentClass : public JavaObjectCaller
{
    protected:
        JavaClass* m_ptrExtendedImageInfo;

        template <typename T, typename U>
        void CallArrayImpl(std::string methodName, std::vector<T>& val)
        {
            JavaArrayListHandler<U> aHandler(getEnvironment());
            jobject java_array = aHandler.NativeToJava(val);
            callObjectMethod(methodName, java_array);
        }


    public:
        JavaExtendedImageInfo_ParentClass(JNIEnv* env, std::string objectName, const std::vector<std::string>& funcNames) :
            JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), objectName, funcNames), m_ptrExtendedImageInfo{}
    {}
    void setExtendedObject(JavaClass* ptr) { m_ptrExtendedImageInfo = ptr; }
};

class JavaExtendedImageInfo_BarcodeInfo;

class JavaExtendedImageInfo_BarcodeInfo_SingleInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo_BarcodeInfo>
{
    static constexpr const char * SetConfidence = "SetConfidence";
    static constexpr const char * SetRotation = "SetRotation";
    static constexpr const char * SetTextLength = "SetTextLength";
    static constexpr const char * SetText = "SetText";
    static constexpr const char * SetX = "SetX";
    static constexpr const char * SetY = "SetY";
    static constexpr const char * SetType = "SetType";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_BarcodeInfo_SingleInfo(JNIEnv* env);

    void setConfidence(TW_UINT32);
    void setRotation(TW_UINT32);
    void setTextLength(TW_UINT32);
    void setText(std::string s);
    void setX(TW_UINT32);
    void setY(TW_UINT32);
    void setType(TW_UINT32);
    void NativeToJava(ExtendedImageInfo_BarcodeInfoNative& val);
};


class JavaExtendedImageInfo_BarcodeInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetCount = "SetCount";
    static constexpr const char * SetSingleInfo = "SetSingleInfo";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;
    JavaExtendedImageInfo_BarcodeInfo_SingleInfo proxy_singleinfo;

public:

    JavaExtendedImageInfo_BarcodeInfo(JNIEnv* env);

    void setCount(TW_UINT32);
    void setSingleInfo(jobject objParent, ExtendedImageInfo_BarcodeInfoNative&, int);
};
///////////////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ShadedAreaDetectionInfo;

class JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo_ShadedAreaDetectionInfo>
{
    static constexpr const char * SetTop = "SetTop";
    static constexpr const char * SetLeft = "SetLeft";
    static constexpr const char * SetHeight = "SetHeight";
    static constexpr const char * SetWidth = "SetWidth";
    static constexpr const char * SetSize = "SetSize";
    static constexpr const char * SetBlackCountOld = "SetBlackCountOld";
    static constexpr const char * SetBlackCountNew = "SetBlackCountNew";
    static constexpr const char * SetBlackRLMin = "SetBlackRLMin";
    static constexpr const char * SetBlackRLMax = "SetBlackRLMax";
    static constexpr const char * SetWhiteCountOld = "SetWhiteCountOld";
    static constexpr const char * SetWhiteCountNew = "SetWhiteCountNew";
    static constexpr const char * SetWhiteRLMin = "SetWhiteRLMin";
    static constexpr const char * SetWhiteRLMax = "SetWhiteRLMax";
    static constexpr const char * SetWhiteRLAvg = "SetWhiteRLAvg";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo(JNIEnv* env);

    void setTop(TW_UINT32);
    void setLeft(TW_UINT32);
    void setHeight(TW_UINT32);
    void setWidth(TW_UINT32);
    void setSize(TW_UINT32);
    void setBlackCountOld(TW_UINT32);
    void setBlackCountNew(TW_UINT32);
    void setBlackRLMin(TW_UINT32);
    void setBlackRLMax(TW_UINT32);
    void setWhiteCountOld(TW_UINT32);
    void setWhiteCountNew(TW_UINT32);
    void setWhiteRLMin(TW_UINT32);
    void setWhiteRLMax(TW_UINT32);
    void setWhiteRLAvg(TW_UINT32);
    void NativeToJava(const ExtendedImageInfo_ShadedAreaDetectionInfoNative& val);
};

class JavaExtendedImageInfo_ShadedAreaDetectionInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetCount = "SetCount";
    static constexpr const char * SetSingleInfo = "SetSingleInfo";
    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

    JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo proxy_singleinfo;

public:
    JavaExtendedImageInfo_ShadedAreaDetectionInfo(JNIEnv* env);

    void setCount(TW_UINT32);
    void setSingleInfo(jobject objParent, ExtendedImageInfo_ShadedAreaDetectionInfoNative&, int);
};
//////////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_SpeckleRemovalInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetSpecklesRemoved = "SetSpecklesRemoved";
    static constexpr const char * SetWhiteSpecklesRemoved = "SetWhiteSpecklesRemoved";
    static constexpr const char * SetBlackSpecklesRemoved = "SetBlackSpecklesRemoved";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_SpeckleRemovalInfo(JNIEnv* env);

    void setSpecklesRemoved(TW_UINT32);
    void setWhiteSpecklesRemoved(TW_UINT32);
    void setBlackSpecklesRemoved(TW_UINT32);
    void NativeToJava(const ExtendedImageInfo_SpeckleRemovalInfoNative& val);
};
//////////////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_LineSingleInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetXCoordinate = "SetXCoordinate";
    static constexpr const char* SetYCoordinate = "SetYCoordinate";
    static constexpr const char* SetLength = "SetLength";
    static constexpr const char* SetThickness = "SetThickness";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_LineSingleInfo(JNIEnv* env);

    void setXCoordinate(TW_UINT32);
    void setYCoordinate(TW_UINT32);
    void setLength(TW_UINT32);
    void setThickness(TW_UINT32);
    void NativeToJava(const ExtendedImageInfo_LineDetectionInfoNative& info);
};

class JavaExtendedImageInfo_LineDetectionInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetCount = "SetCount";
    static constexpr const char * SetSingleInfo = "SetSingleInfo";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;
    JavaExtendedImageInfo_LineSingleInfo proxy_lineInfo;

public:
    JavaExtendedImageInfo_LineDetectionInfo(JNIEnv* env);

    void setCount(TW_UINT32);
    void setSingleInfo(jobject objParent, const ExtendedImageInfo_LineDetectionInfoNative&, int);
};

/////////////////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_PatchcodeDetectionInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetPatchcode= "SetPatchcode";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_PatchcodeDetectionInfo(JNIEnv* env);

    void setPatchcode(TW_UINT32);
};
///////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_SkewDetectionInfo: public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetDeskewStatus  = "SetDeskewStatus";
    static constexpr const char * SetOriginalAngle = "SetOriginalAngle";
    static constexpr const char * SetFinalAngle    = "SetFinalAngle";
    static constexpr const char * SetConfidence    = "SetConfidence";
    static constexpr const char * SetWindowX1      = "SetWindowX1";
    static constexpr const char * SetWindowX2      = "SetWindowX2";
    static constexpr const char * SetWindowX3      = "SetWindowX3";
    static constexpr const char * SetWindowX4      = "SetWindowX4";
    static constexpr const char * SetWindowY1      = "SetWindowY1";
    static constexpr const char * SetWindowY2      = "SetWindowY2";
    static constexpr const char * SetWindowY3      = "SetWindowY3";
    static constexpr const char * SetWindowY4      = "SetWindowY4";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_SkewDetectionInfo(JNIEnv* env);

    void setDeskewStatus  (TW_UINT32);
    void setOriginalAngle (TW_UINT32);
    void setFinalAngle    (TW_UINT32);
    void setConfidence    (TW_UINT32);
    void setWindowX1      (TW_UINT32);
    void setWindowX2      (TW_UINT32);
    void setWindowX3      (TW_UINT32);
    void setWindowX4      (TW_UINT32);
    void setWindowY1      (TW_UINT32);
    void setWindowY2      (TW_UINT32);
    void setWindowY3      (TW_UINT32);
    void setWindowY4      (TW_UINT32);
};

///////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_EndorsedTextInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetText = "SetText";
    JavaDTwainLowLevel_TW_STR255 strproxy;

    public:
        JavaExtendedImageInfo_EndorsedTextInfo(JNIEnv* env);
        void setText(const TW_STR255);
};
///////////////////////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_FormsRecognitionInfo: public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetConfidence = "SetConfidence";
    static constexpr const char * SetTemplateMatch = "SetTemplateMatch";
    static constexpr const char * SetTemplatePageMatch = "SetTemplatePageMatch";
    static constexpr const char * SetHorizontalDocOffset = "SetHorizontalDocOffset";
    static constexpr const char * SetVerticalDocOffset = "SetVerticalDocOffset";

#ifdef UNICODE
    using value_type = std::wstring;
#else
    using value_type = std::string;
#endif
    using StringType = value_type;

public:
    JavaExtendedImageInfo_FormsRecognitionInfo(JNIEnv* env);

    void setConfidence(std::vector<TW_UINT32>& val);
    void setTemplateMatch(std::vector<std::string>& val);
    void setTemplatePageMatch(std::vector<TW_UINT32>& val);
    void setHorizontalDocOffset(std::vector<TW_UINT32>& val);
    void setVerticalDocOffset(std::vector<TW_UINT32>& val);
    void setAllValues(ExtendedImageInfo_FormsRecognitionNative& info);
};
///////////////////////////////////////////////////////////////////////////////////

class JavaExtendedImageInfo_PageSourceInfo: public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetBookname = "SetBookname";
    static constexpr const char * SetChapterNumber = "SetChapterNumber";
    static constexpr const char * SetDocumentNumber = "SetDocumentNumber";
    static constexpr const char * SetPageNumber = "SetPageNumber";
    static constexpr const char * SetCamera = "SetCamera";
    static constexpr const char * SetFrameNumber = "SetFrameNumber";
    static constexpr const char * SetFrame = "SetFrame";
    static constexpr const char * SetPixelFlavor = "SetPixelFlavor";
    static constexpr const char*  SetPageSide = "SetPageSide";


    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;
    JavaDTwainLowLevel_TW_UINT16 proxy_uint16;
    JavaDTwainLowLevel_TW_STR255 proxy_str255;
    JavaDTwainLowLevel_TW_FRAME  proxy_frame;

public:
    JavaExtendedImageInfo_PageSourceInfo(JNIEnv* env);
    void setBookname(TW_STR255);
    void setChapterNumber(TW_UINT32);
    void setDocumentNumber(TW_UINT32);
    void setPageNumber(TW_UINT32);
    void setCamera(TW_STR255);
    void setFrameNumber(TW_UINT32);
    void setFrame(TW_FRAME);
    void setPixelFlavor(TW_UINT16);
    void setPageSide(TW_UINT16);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ImageSegmentationInfo : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetICCProfile = "SetICCProfile";
    static constexpr const char * SetLastSegment ="SetLastSegment";
    static constexpr const char * SetSegmentNumber = "SetSegmentNumber";

    JavaDTwainLowLevel_TW_BOOL   proxy_bool;
    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;
    JavaDTwainLowLevel_TW_STR255 proxy_str255;

public:
    JavaExtendedImageInfo_ImageSegmentationInfo(JNIEnv* env);
    void setICCProfile(TW_STR255);
    void setLastSegment(TW_BOOL);
    void setSegmentNumber(TW_UINT32);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo20 : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char * SetMagType = "SetMagType";

    JavaDTwainLowLevel_TW_UINT16 proxy_uint16;

public:
    JavaExtendedImageInfo_ExtendedImageInfo20(JNIEnv* env);
    void setMagType(TW_UINT16);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo21: public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetFileSystemSource = "SetFileSystemSource";
    static constexpr const char* SetMagData = "SetMagData";
    static constexpr const char* SetImageMerged = "SetImageMerged";
    static constexpr const char* SetMagDataLength = "SetMagDataLength";
    static constexpr const char* SetPageSide = "SetPageSide";

    JavaDTwainLowLevel_TW_BOOL   proxy_bool;
    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;
    JavaDTwainLowLevel_TW_UINT16 proxy_uint16;
    JavaDTwainLowLevel_TW_STR255 proxy_str255;

public:
    JavaExtendedImageInfo_ExtendedImageInfo21(JNIEnv* env);
    void setFileSystemSource(TW_STR255);
    void setImageMerged(TW_BOOL);
    void setMagDataLength(TW_UINT32);
    void setPageSide(TW_UINT16);
    void setMagData(jbyteArray imageData);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo22 : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetPaperCount = "SetPaperCount";

    JavaDTwainLowLevel_TW_UINT32 proxy_uint32;

public:
    JavaExtendedImageInfo_ExtendedImageInfo22(JNIEnv* env);
    void setPaperCount(TW_UINT32);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo23 : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetPrinterText= "SetPrinterText";

    JavaDTwainLowLevel_TW_STR255 proxy_str255;

public:
    JavaExtendedImageInfo_ExtendedImageInfo23(JNIEnv* env);
    void setPrinterText(TW_STR255);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo24 : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetTwainDirectMetaData = "SetTwainDirectMetaData";

public:
    JavaExtendedImageInfo_ExtendedImageInfo24(JNIEnv* env);
    void setTwainDirectMetaData(std::string sMetaData);
};
/////////////////////////////////////////////////////////////////
class JavaExtendedImageInfo_ExtendedImageInfo25 : public JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>
{
    static constexpr const char* SetIAFieldA = "SetIAFieldA";
    static constexpr const char* SetIAFieldB = "SetIAFieldB";
    static constexpr const char* SetIAFieldC = "SetIAFieldC";
    static constexpr const char* SetIAFieldD = "SetIAFieldD";
    static constexpr const char* SetIAFieldE = "SetIAFieldE";
    static constexpr const char* SetIALevel  = "SetIALevel";
    static constexpr const char* SetPrinter =  "SetPrinter";
    static constexpr const char* AddBarcodeText = "AddBarcodeText";

    JavaDTwainLowLevel_TW_STR32 proxy_str32;
    JavaDTwainLowLevel_TW_UINT16 proxy_uint16;

public:
    JavaExtendedImageInfo_ExtendedImageInfo25(JNIEnv* env);
    void setIAFieldA(TW_STR32 val);
    void setIAFieldB(TW_STR32 val);
    void setIAFieldC(TW_STR32 val);
    void setIAFieldD(TW_STR32 val);
    void setIAFieldE(TW_STR32 val);
    void setIALevel(TW_UINT16 val);
    void setPrinter(TW_UINT16 val);
    void addBarcodeText(std::string val);
};

////////////////////////////////////////////////////////////
class JavaExtendedImageInfo : public JavaObjectCaller
{
    public:
        static constexpr const char * SetBarcodeInfo                   = "SetBarcodeInfo";
        static constexpr const char * SetShadedAreaDetectionInfo       = "SetShadedAreaDetectionInfo";
        static constexpr const char * SetSpeckleRemovalInfo            = "SetSpeckleRemovalInfo";
        static constexpr const char * SetHorizontalLineDetectionInfo   = "SetHorizontalLineDetectionInfo";
        static constexpr const char * SetVerticalLineDetectionInfo     = "SetVerticalLineDetectionInfo";
        static constexpr const char * SetPatchcodeDetectionInfo        = "SetPatchcodeDetectionInfo";
        static constexpr const char * SetSkewDetectionInfo             = "SetSkewDetectionInfo";
        static constexpr const char * SetEndorsedTextInfo              = "SetEndorsedTextInfo";
        static constexpr const char * SetFormsRecognitionInfo          = "SetFormsRecognitionInfo";
        static constexpr const char * SetPageSourceInfo                = "SetPageSourceInfo";
        static constexpr const char * SetImageSegmentationInfo         = "SetImageSegmentationInfo";
        static constexpr const char * SetExtendedImageInfo20           = "SetExtendedImageInfo20";
        static constexpr const char * SetExtendedImageInfo21           = "SetExtendedImageInfo21";
        static constexpr const char * SetSupportedExtendedImageInfo    = "SetSupportedExtendedImageInfo";

        static constexpr const char * GetBarcodeInfo = "GetBarcodeInfo";
        static constexpr const char * GetShadedAreaDetectionInfo = "GetShadedAreaDetectionInfo";
        static constexpr const char * GetSpeckleRemovalInfo = "GetSpeckleRemovalInfo";
        static constexpr const char * GetHorizontalLineDetectionInfo = "GetHorizontalLineDetectionInfo";
        static constexpr const char * GetVerticalLineDetectionInfo = "GetVerticalLineDetectionInfo";
        static constexpr const char * GetPatchcodeDetectionInfo = "GetPatchcodeDetectionInfo";
        static constexpr const char * GetSkewDetectionInfo = "GetSkewDetectionInfo";
        static constexpr const char * GetEndorsedTextInfo = "GetEndorsedTextInfo";
        static constexpr const char * GetFormsRecognitionInfo = "GetFormsRecognitionInfo";
        static constexpr const char * GetPageSourceInfo = "GetPageSourceInfo";
        static constexpr const char * GetImageSegmentationInfo = "GetImageSegmentationInfo";
        static constexpr const char * GetExtendedImageInfo20 = "GetExtendedImageInfo20";
        static constexpr const char * GetExtendedImageInfo21 = "GetExtendedImageInfo21";
        static constexpr const char * GetExtendedImageInfo22 = "GetExtendedImageInfo22";
        static constexpr const char * GetExtendedImageInfo23 = "GetExtendedImageInfo23";
        static constexpr const char * GetExtendedImageInfo24 = "GetExtendedImageInfo24";
        static constexpr const char * GetExtendedImageInfo25 = "GetExtendedImageInfo25";

    private:
        JavaExtendedImageInfo_BarcodeInfo proxy_barcodeinfo;
        JavaExtendedImageInfo_ShadedAreaDetectionInfo proxy_shadedareainfo;
        JavaExtendedImageInfo_SpeckleRemovalInfo proxy_speckleremovalinfo;
        JavaExtendedImageInfo_LineDetectionInfo proxy_linedetectioninfo;
        JavaExtendedImageInfo_PatchcodeDetectionInfo proxy_patchcodedetioninfo;
        JavaExtendedImageInfo_SkewDetectionInfo proxy_skewdetectioninfo;
        JavaExtendedImageInfo_EndorsedTextInfo proxy_endorsedtextinfo;
        JavaExtendedImageInfo_FormsRecognitionInfo proxy_formsdefinitioninfo;
        JavaExtendedImageInfo_PageSourceInfo proxy_pagesourceinfo;
        JavaExtendedImageInfo_ImageSegmentationInfo proxy_imagesegmentationinfo;
        JavaExtendedImageInfo_ExtendedImageInfo20 proxy_extendedimageinfo20;
        JavaExtendedImageInfo_ExtendedImageInfo21 proxy_extendedimageinfo21;
        JavaExtendedImageInfo_ExtendedImageInfo22 proxy_extendedimageinfo22;
        JavaExtendedImageInfo_ExtendedImageInfo23 proxy_extendedimageinfo23;
        JavaExtendedImageInfo_ExtendedImageInfo24 proxy_extendedimageinfo24;
        JavaExtendedImageInfo_ExtendedImageInfo25 proxy_extendedimageinfo25;

        void setAllLineInfo(const ExtendedImageInfo_LineDetectionNative& sInfo, const char* fn);
public:
        JavaExtendedImageInfo(JNIEnv* env);
        void setExtendedImageInfoTypes(std::vector<LONG>& vInfos);
        void setAllBarcodeInfo(ExtendedImageInfo_BarcodeNative& info);
        void setBarcodeInfo(ExtendedImageInfo_BarcodeInfoNative&, int nWhich);
        void setBarcodeInfoCount(TW_UINT32 count);
        void setPageSourceInfo(ExtendedImageInfo_PageSourceInfoNative& info);
        void setSkewDetectionInfo(ExtendedImageInfo_SkewDetectionInfoNative& info);
        void setShadedAreaDetectionInfo(ExtendedImageInfo_ShadedAreaDetectionInfoNative&, int nWhich);
        void setShadedAreaDetectionInfo(ExtendedImageInfo_ShadedAreaDetectionInfoNativeV& info);
        void setShadedAreaInfoCount(TW_UINT32 count);
        void setSpeckleRemovalInfo(const ExtendedImageInfo_SpeckleRemovalInfoNative& sInfo);
        void setAllHorizontalLineInfo(const ExtendedImageInfo_LineDetectionNative& sInfo);
        void setAllVerticalLineInfo(const ExtendedImageInfo_LineDetectionNative& sInfo);
        void setAllFormsRecognitionInfo(ExtendedImageInfo_FormsRecognitionNative& info);
        void setAllImageSegmentationInfo(ExtendedImageInfo_ImageSegmentationInfoNative& info);
        void setAllEndorsedInfo(ExtendedImageInfo_EndorsedTextInfoNative& info);
        void setAllExtendedImageInfo20(ExtendedImageInfo_ExtendedImageInfo20Native& info);
        void setAllExtendedImageInfo21(ExtendedImageInfo_ExtendedImageInfo21Native& info);
        void setAllExtendedImageInfo22(ExtendedImageInfo_ExtendedImageInfo22Native& info);
        void setAllExtendedImageInfo23(ExtendedImageInfo_ExtendedImageInfo23Native& info);
        void setAllExtendedImageInfo24(ExtendedImageInfo_ExtendedImageInfo24Native& info);
        void setAllExtendedImageInfo25(ExtendedImageInfo_ExtendedImageInfo25Native& info);
        void setAllPatchCodeInfo(ExtendedImageInfo_PatchCodeNative& info);
        /*
    void setSpeckleRemoveInfo
    void setHorizontalLineDetectionInfo
    void setVerticalLineDetectionInfo
    void setPatchcodeDetectionInfo
    void setSkewDetectionInfo
    void setEndorsedTextInfo
    void setFormsRecognitionInfo
    void setPageSourceInfo
    void setImageSegmentationInfo
    void setMicrInfo*/

};
//////////////////////////////////////////////////////////////////////////////////////////////
class JavaEnumBase : public JavaObjectCaller
{
    public:
        static constexpr const char *Value = "Value";
        JavaEnumBase(JNIEnv* env, std::string objectName);
        int getValue();
};

class JavaTextRenderMode : public JavaEnumBase
{
    public:
        JavaTextRenderMode(JNIEnv* env);
};

class JavaTextDisplayOptions : public JavaEnumBase
{
    public:
        JavaTextDisplayOptions(JNIEnv* env);
};

class JavaRGBColor : public JavaObjectCaller
{
public:
    static constexpr const char *GetR = "GetR";
    static constexpr const char *GetG = "GetG";
    static constexpr const char *GetB = "GetB";
    JavaRGBColor(JNIEnv *env);
    int getR();
    int getG();
    int getB();
};

class RGBNative
{
    int R = 0;
    int G = 0;
    int B = 0;
    public:
        void setR(int R_) { R = R_;}
        void setG(int G_) { G = G_; }
        void setB(int B_) { B = B_; }
        int getR() const { return R; }
        int getG() const { return G; }
        int getB() const { return B; }
};

class JavaPDFTextElement : public JavaObjectCaller
{
    static constexpr const char *GetFontName        =  "GetFontName";
    static constexpr const char *GetText            =  "GetText";
    static constexpr const char *GetXPos            =  "GetXPos";
    static constexpr const char *GetYPos            =  "GetYPos";
    static constexpr const char *GetFontSize        =  "GetFontSize";
    static constexpr const char *GetScaling         =  "GetScaling";
    static constexpr const char *GetCharSpacing     =  "GetCharSpacing";
    static constexpr const char *GetWordSpacing     =  "GetWordSpacing";
    static constexpr const char *GetStrokeWidth     =  "GetStrokeWidth";
    static constexpr const char *GetRGBColor        =  "GetRGBColor";
    static constexpr const char *GetRenderMode      =  "GetRenderMode";
    static constexpr const char *GetDisplayOptions  =  "GetDisplayOptions";
#ifdef UNICODE
    using PDFStringType = std::wstring;
#else
    using PDFStringType = std::string;
#endif
public:
    JavaPDFTextElement(JNIEnv *env);
    PDFStringType getFontName();
    RGBNative getRGB();
    PDFStringType getText();
    int getRenderMode();
    int getXPosition();
    int getYPosition();
    double getFontSize();
    double getScaling();
    double getCharSpacing();
    double getWordSpacing();
    int getStrokeWidth();
    int getDisplayOptions();
};
using ArrayList_TWBOOL = ArrayLowLevelTwainList<bool, JavaDTwainLowLevel_TW_BOOL>;
using ArrayList_TWINT8 = ArrayLowLevelTwainList<TW_INT8, JavaDTwainLowLevel_TW_INT8>;
using ArrayList_TWINT16 = ArrayLowLevelTwainList<TW_INT16, JavaDTwainLowLevel_TW_INT16>;
using ArrayList_TWUINT16 = ArrayLowLevelTwainList<TW_UINT16, JavaDTwainLowLevel_TW_UINT16>;
using ArrayList_TWINT32 = ArrayLowLevelTwainList<TW_INT32, JavaDTwainLowLevel_TW_INT32>;
using ArrayList_TWUINT32 = ArrayLowLevelTwainList<TW_UINT32, JavaDTwainLowLevel_TW_UINT32>;
#ifdef UNICODE
using ArrayListStringType = std::wstring;
#else
using ArrayListStringType = std::string;
#endif
using ArrayList_TWSTR32 = ArrayLowLevelTwainList<TW_STR32, JavaDTwainLowLevel_TW_STR32>;
using ArrayList_TWSTR64 = ArrayLowLevelTwainList<TW_STR64, JavaDTwainLowLevel_TW_STR64>;
using ArrayList_TWSTR128 = ArrayLowLevelTwainList<TW_STR128, JavaDTwainLowLevel_TW_STR128>;
using ArrayList_TWSTR255 = ArrayLowLevelTwainList<TW_STR255, JavaDTwainLowLevel_TW_STR255>;
using ArrayList_TWSTR1024 = ArrayLowLevelTwainList<TW_STR1024, JavaDTwainLowLevel_TW_STR1024>;
#endif