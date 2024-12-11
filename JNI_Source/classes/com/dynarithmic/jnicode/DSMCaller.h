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
    virtual void* TranslateRaw(JNIEnv* /*pEnv*/, jobject /*jobj*/, std::string sTwainClassName) { return nullptr; }
    virtual void DestroyRaw(void */*pRaw*/) {}
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