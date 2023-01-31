#ifndef DSMCALLER_H
#define DSMCALLER_H

#include <map>
#include <tuple>
#include <string>
#include <memory>
#include <jni.h>
#include "twain.h"
#include "javaobjectcaller.h"

using TwainTriplet = std::tuple<TW_UINT32, TW_UINT16, TW_UINT16>;

template <typename TwainType, typename ObjectCallerType>
struct VoidFunctor
{
    TwainType GetNativeObject(jobject javaObject, ObjectCallerType& objectCaller) { return{}; }
};

struct JNITripletTranslatorBase
{
    std::string m_JavaClassInfo;
    std::string m_TwainClassName;
    virtual void* TranslateRaw(JNIEnv* pEnv, jobject jobj, std::string sTwainClassName) { return nullptr; }
    virtual void DestroyRaw(void *pRaw) {}
    virtual void* GetRaw() { return nullptr; }
    void SetTwainClassName(std::string sName) { m_TwainClassName = sName; }
    virtual ~JNITripletTranslatorBase() {}
};

template <typename TwainType, typename ObjectCallerType>
struct JNITripletTranslator : JNITripletTranslatorBase
{
    std::shared_ptr<VoidFunctor<TwainType, ObjectCallerType>> m_pJavaToNativeFn;
};

using DSMCallerMap = std::map<TwainTriplet, std::shared_ptr<JNITripletTranslatorBase>>;

#endif