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
#include "DTWAINJNIGlobals.h"
#include <algorithm>
#include "twain.h"

DTWAINJNIGlobals::DTWAINJNIGlobals() : g_DTWAINModule(nullptr)
    {}

    template <typename Proxy, typename TwainType>
    TwainType* TranslateRawImpl(JNIEnv* pEnv, jobject jobj)
    {
        auto* pRaw = new TwainType();
        Proxy proxy(pEnv);
        proxy.setObject(jobj);
        *pRaw = proxy.JavaToNative();
        return pRaw;
    }

    template <typename Proxy, typename TwainType>
    jobject TranslateToJavaImpl(JNIEnv* pEnv, TW_MEMREF memref)
    {
        Proxy testClass(pEnv);
        jobject jobj = testClass.defaultConstructObject();
        testClass.setObject(jobj);
        testClass.NativeToJava(*(static_cast<TwainType*>(memref)));
        return testClass.getObject();
    }

    template <typename TwainType>
    void DestroyRawImpl(void* pRaw)
    {
        delete static_cast<TwainType*>(pRaw);
    }

    #define CREATE_TRIPLET_TRANSLATOR(TwainStruct) \
    struct JNITripletTranslatorBase_##TwainStruct : JNITripletTranslatorBase \
    {\
        TwainStruct m_TwainStruct; \
        JNITripletTranslatorBase_##TwainStruct() : m_TwainStruct{} { SetTwainClassName(#TwainStruct); }\
        void *TranslateRaw(JNIEnv* pEnv, jobject jobj, std::string) override\
        {\
            return TranslateRawImpl<JavaDTwainLowLevel_##TwainStruct, ##TwainStruct>(pEnv, jobj);\
        }\
        jobject TranslateToJava(JNIEnv* pEnv, TW_MEMREF pMemRef)\
        {\
            return TranslateToJavaImpl<JavaDTwainLowLevel_##TwainStruct, ##TwainStruct>(pEnv, pMemRef);\
        }\
        void DestroyRaw(void *pRaw)\
        {\
            DestroyRawImpl<##TwainStruct>(pRaw);\
        }\
    };

    struct JNITripletTranslatorBase_TW_RESPONSETYPE: JNITripletTranslatorBase
    {
        TW_RESPONSETYPE* m_pRaw = nullptr;
        void* TranslateRaw(JNIEnv* pEnv, jobject jobj, std::string TwainClassName) override
        {
            m_pRaw = new TW_RESPONSETYPE();
            JavaDTwainLowLevel_TW_RESPONSETYPE proxy(pEnv);
            proxy.setObject(jobj);
            *m_pRaw = proxy.JavaToNative();
            return m_pRaw;
        }

        void DestroyRaw(void *pRaw) override
        {
            delete static_cast<TW_RESPONSETYPE*>(pRaw);
        }

        void* GetRaw() override
        {
            return m_pRaw->getData();
        }
    };

    CREATE_TRIPLET_TRANSLATOR(TW_HANDLE)
    CREATE_TRIPLET_TRANSLATOR(TW_AUDIOINFO)
    CREATE_TRIPLET_TRANSLATOR(TW_CAPABILITY)
    CREATE_TRIPLET_TRANSLATOR(TW_CUSTOMDSDATA)
    CREATE_TRIPLET_TRANSLATOR(TW_DEVICEEVENT)
    CREATE_TRIPLET_TRANSLATOR(TW_EVENT)
    CREATE_TRIPLET_TRANSLATOR(TW_FILESYSTEM)
    CREATE_TRIPLET_TRANSLATOR(TW_IDENTITY)
    CREATE_TRIPLET_TRANSLATOR(TW_PASSTHRU)
    CREATE_TRIPLET_TRANSLATOR(TW_PENDINGXFERS)
    CREATE_TRIPLET_TRANSLATOR(TW_SETUPFILEXFER)
    CREATE_TRIPLET_TRANSLATOR(TW_SETUPMEMXFER)
    CREATE_TRIPLET_TRANSLATOR(TW_METRICS)
    CREATE_TRIPLET_TRANSLATOR(TW_STATUS)
    CREATE_TRIPLET_TRANSLATOR(TW_STATUSUTF8)
    CREATE_TRIPLET_TRANSLATOR(TW_CIECOLOR)
    CREATE_TRIPLET_TRANSLATOR(TW_EXTIMAGEINFO)
    CREATE_TRIPLET_TRANSLATOR(TW_FILTER)
    CREATE_TRIPLET_TRANSLATOR(TW_IMAGEINFO)
    CREATE_TRIPLET_TRANSLATOR(TW_IMAGELAYOUT)
    CREATE_TRIPLET_TRANSLATOR(TW_UINT32)
    CREATE_TRIPLET_TRANSLATOR(TW_IMAGEMEMXFER)
    CREATE_TRIPLET_TRANSLATOR(TW_JPEGCOMPRESSION)
    CREATE_TRIPLET_TRANSLATOR(TW_PALETTE8)
    CREATE_TRIPLET_TRANSLATOR(TW_TWAINDIRECT)
    CREATE_TRIPLET_TRANSLATOR(TW_USERINTERFACE)
    CREATE_TRIPLET_TRANSLATOR(TW_MEMORY)

    void DTWAINJNIGlobals::InitializeDSMCallerMap(const std::string& javaLowLevelDirectory)
    {
        g_DSMCallerMap.clear();
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_AUDIOINFO();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_AUDIOINFO";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_AUDIO, DAT_AUDIOINFO, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_HANDLE();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_HANDLE";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_AUDIO, DAT_AUDIONATIVEXFER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGENATIVEXFER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PARENT, MSG_OPENDSM)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PARENT, MSG_CLOSEDSM)] = capShare;
        }
        {
            auto* caprawPtr = new JNITripletTranslatorBase_TW_CAPABILITY();
            caprawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_CAPABILITY";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(caprawPtr);

            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GETCURRENT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GETHELP)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GETLABEL)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_GETLABELENUM)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_QUERYSUPPORT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_RESETALL)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_SET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CAPABILITY, MSG_SETCONSTRAINT)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_CUSTOMDSDATA();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_CUSTOMDSDATA";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CUSTOMDSDATA, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_CUSTOMDSDATA, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_DEVICEEVENT();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_DEVICEEVENT";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_DEVICEEVENT, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_IDENTITY();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_IDENTITY";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_CLOSEDS)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_GETFIRST)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_GETNEXT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_OPENDS)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_USERSELECT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_IDENTITY, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_NULL";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_NULL, MSG_CLOSEDSOK)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_NULL, MSG_CLOSEDSREQ)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_NULL, MSG_DEVICEEVENT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_NULL, MSG_XFERREADY )] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_AUDIO, DAT_AUDIOFILEXFER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGEFILEXFER, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_PENDINGXFERS();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_PENDINGXFERS";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PENDINGXFERS, MSG_ENDXFER)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PENDINGXFERS, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PENDINGXFERS, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PENDINGXFERS, MSG_STOPFEEDER)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_SETUPFILEXFER();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_SETUPFILEXFER";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_SETUPFILEXFER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_SETUPFILEXFER, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_SETUPFILEXFER, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_SETUPFILEXFER, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_SETUPMEMXFER();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_SETUPMEMXFER";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_SETUPMEMXFER, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_EVENT();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_EVENT";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_EVENT, MSG_PROCESSEVENT)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_FILESYSTEM();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_FILESYSTEM";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_AUTOMATICCAPTUREDIRECTORY)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_CHANGEDIRECTORY)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_COPY)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_CREATEDIRECTORY)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_DELETE)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_FORMATMEDIA)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_GETCLOSE)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_GETFIRSTFILE)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_GETINFO)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_GETNEXTFILE)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_FILESYSTEM, MSG_RENAME)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_METRICS();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_METRICS";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_METRICS, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_PASSTHRU();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_PASSTHRU";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_PASSTHRU, MSG_PASSTHRU)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_STATUS();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_STATUS";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_STATUS, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_STATUSUTF8();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_STATUSUTF8";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_STATUSUTF8, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_CIECOLOR();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_CIECOLOR";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_CIECOLOR, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_EXTIMAGEINFO();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_EXTIMAGEINFO";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_EXTIMAGEINFO, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_FILTER();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_FILTER";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_FILTER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_FILTER, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_FILTER, MSG_SET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_FILTER, MSG_RESET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_UINT32();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_UINT32";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_XFERGROUP, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_XFERGROUP, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_IMAGEINFO();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_IMAGEINFO";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGEINFO, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_IMAGELAYOUT();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_IMAGELAYOUT";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGELAYOUT, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGELAYOUT, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGELAYOUT, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGELAYOUT, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_IMAGEMEMXFER();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_IMAGEMEMXFER";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGEMEMFILEXFER, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_IMAGEMEMXFER, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_JPEGCOMPRESSION();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_JPEGCOMPRESSION";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_JPEGCOMPRESSION, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_JPEGCOMPRESSION, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_JPEGCOMPRESSION, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_JPEGCOMPRESSION, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_PALETTE8();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_PALETTE8";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_PALETTE8, MSG_GET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_PALETTE8, MSG_GETDEFAULT)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_PALETTE8, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_PALETTE8, MSG_SET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_TWAINDIRECT();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_TWAINDIRECT";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_TWAINDIRECT, MSG_SETTASK)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_USERINTERFACE();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_USERINTERFACE";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_USERINTERFACE, MSG_DISABLEDS)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_USERINTERFACE, MSG_ENABLEDS)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_CONTROL, DAT_USERINTERFACE, MSG_ENABLEDSUIONLY)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_MEMORY();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_MEMORY";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_ICCPROFILE, MSG_GET)] = capShare;
        }
        {
            auto* rawPtr = new JNITripletTranslatorBase_TW_RESPONSETYPE();
            rawPtr->m_JavaClassInfo = javaLowLevelDirectory + "TW_RESPONSETYPE";
            auto capShare = std::shared_ptr<JNITripletTranslatorBase>(rawPtr);
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_GRAYRESPONSE, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_GRAYRESPONSE, MSG_SET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_RGBRESPONSE, MSG_RESET)] = capShare;
            g_DSMCallerMap[std::make_tuple(DG_IMAGE, DAT_RGBRESPONSE, MSG_SET)] = capShare;
        }
    }

    DTWAINJNIGlobals::DSMObjectChecker
        DTWAINJNIGlobals::CheckCallDSMObject(JNIEnv* pEnv, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 TMSG, jobject jobj)
    {
        DSMObjectChecker retVal;
        auto iter = g_DSMCallerMap.find({DG, DAT, TMSG});
        if (iter == g_DSMCallerMap.end())
            return retVal;

        auto jniTranslator = iter->second;
        auto& prClassInfo = jniTranslator->m_JavaClassInfo;
        retVal.m_expectedType = prClassInfo;
        std::replace(retVal.m_expectedType.begin(), retVal.m_expectedType.end(), '/', '.');
        if (prClassInfo.empty())
        {
            retVal.m_tripletPtr = jniTranslator;
            return retVal;
        }

        retVal.m_sSourceType = JavaObjectCaller::getJavaClassName(pEnv, jobj);

        jclass testClass = pEnv->FindClass(prClassInfo.c_str());
        const auto retInstance = pEnv->IsInstanceOf(jobj, testClass);
        if (retInstance == JNI_TRUE)
            retVal.m_tripletPtr = jniTranslator;
        return retVal;
     }

    std::pair<jobject, bool> DTWAINJNIGlobals::CreateObjectFromTriplet(JNIEnv* pEnv, TW_UINT32 DG, TW_UINT16 DAT, TW_UINT16 TMSG)
    {
        DSMObjectChecker retVal;
        const auto iter = g_DSMCallerMap.find(std::make_tuple(DG, DAT, TMSG));
        if (iter == g_DSMCallerMap.end())
            return {nullptr, false};

        const auto jniTranslator = iter->second;
        auto& prClassInfo = jniTranslator->m_JavaClassInfo;

        if (prClassInfo.empty())
        {
            return {nullptr, true};
        }

        const auto slashPos = prClassInfo.find_last_of('/');
        if ( slashPos != std::string::npos )
            prClassInfo = prClassInfo.substr(slashPos+1);

        // Attempt to default construct the object
        JavaObjectCaller defConstructorCaller(pEnv, JavaFunctionNameMapInstance::getFunctionMap(), prClassInfo);
        defConstructorCaller.registerMethod("", "()V");
        defConstructorCaller.initializeMethods();
        jobject theObject = defConstructorCaller.defaultConstructObject();
        return {theObject, theObject?true:false};
    }

    void DTWAINJNIGlobals::RegisterJavaFunctionInterface(JavaObjectCaller* pObject,
                                                        const JavaFunctionInfoCategoryMap& fnList,
                                                        int whichAction)
    {
        if ( whichAction & DEFINE_METHODS )
        {
            for (auto& fn : fnList)
            {
                if (fn.second.funcName == "<>")
                    pObject->registerMethod("", fn.second.funcSig);
                else
                    pObject->registerMethod(fn.second.funcName, fn.second.funcSig);
            }
        }
        if ( whichAction & INIT_METHODS )
            pObject->initializeMethods();
    }
