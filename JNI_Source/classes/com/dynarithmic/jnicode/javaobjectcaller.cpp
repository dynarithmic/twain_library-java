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
#include "javaobjectcaller.h"
#include "DTWAINJNIGlobals.h"
#include "DTWAINRAII.h"

extern DTWAINJNIGlobals g_JNIGlobals;

std::string JavaObjectCaller::getJavaClassName(JNIEnv *pEnv, jobject jobj)
{
    std::string retStr;
    jclass sourceClass = pEnv->GetObjectClass(jobj);
    jmethodID mid = pEnv->GetMethodID(sourceClass, "getClass", "()Ljava/lang/Class;");
    const jobject clsObj = pEnv->CallObjectMethod(jobj, mid);
    sourceClass = pEnv->GetObjectClass(clsObj);
    mid = pEnv->GetMethodID(sourceClass, "getName", "()Ljava/lang/String;");
    const auto strObj = static_cast<jstring>(pEnv->CallObjectMethod(clsObj, mid));
    GetStringCharsHandler strHandler(pEnv, strObj);

    StringType sType = strHandler.GetStringChars();
    copy_until_null_check(sType.begin(), sType.end(), std::back_inserter(retStr));
    return retStr;
}

static void RegisterMemberFunctions(JavaObjectCaller& thisCaller, std::string javaClass )
{
    const auto iter = JavaFunctionNameMapInstance::getFunctionMap().find(javaClass);
    iter->second.m_ObjectCallerTemplate->copyMethodInfo(thisCaller);
    DTWAINJNIGlobals::RegisterJavaFunctionInterface(&thisCaller, thisCaller.getFunctionMap(), DTWAINJNIGlobals::INIT_METHODS);
}

JavaDTwainSourceInfo::JavaDTwainSourceInfo(JNIEnv* env) :
        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainSourceInfo")
{
    RegisterMemberFunctions(*this, getObjectName());
}

JavaDTwainLowLevel_TW_IMAGEINFO::JavaDTwainLowLevel_TW_IMAGEINFO(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_IMAGEINFO", {
                        GetXRes, GetYRes, GetWidth, GetLength, GetSamples,
                        GetBPP, GetPlanar, GetPixelType, GetCompression, GetBitsPerSample,
                        SetXRes, SetYRes, SetWidth, SetLength, SetSamples,
                        SetBPP, SetPlanar, SetPixelType, SetCompression, SetBitsPerSample
                     }), proxy_fix32 (env), proxy_i32(env), proxy_i16(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGEINFO::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGEINFO::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_FIX32 JavaDTwainLowLevel_TW_IMAGEINFO::getXResolution()
{
    return getProxyData(proxy_fix32, getFunctionName(GetXRes).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_IMAGEINFO::getYResolution()
{
    return getProxyData(proxy_fix32, getFunctionName(GetYRes).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_IMAGEINFO::getImageWidth()
{
    return getProxyData(proxy_i32, getFunctionName(GetWidth).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_IMAGEINFO::getImageLength()
{
    return getProxyData(proxy_i32, getFunctionName(GetLength).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getSamplesPerPixel()
{
    return getProxyData(proxy_i16, getFunctionName(GetSamples).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getBitsPerPixel()
{
    return getProxyData(proxy_i16, getFunctionName(GetBPP).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getPlanar()
{
    return getProxyData(proxy_i16, getFunctionName(GetPlanar).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getPixelType()
{
    return getProxyData(proxy_i16, getFunctionName(GetPixelType).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getCompression()
{
    return getProxyData(proxy_i16, getFunctionName(GetCompression).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_IMAGEINFO::getBitsPerSampleValue(int32_t val)
{
    return getProxyData(proxy_i16, getFunctionName(GetBitsPerSample).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setXResolution(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetXRes).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setYResolution(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetYRes).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setImageWidth(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetWidth).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setImageLength(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetLength).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setSamplesPerPixel(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetSamples).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setBitsPerPixel(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetBPP).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setPlanar(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetPlanar).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setPixelType(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetPixelType).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setCompression(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetCompression).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setBitsPerSampleValue(TW_INT16 val1, int32_t val2)
{
    return setProxyData(proxy_i16, getFunctionName(SetBitsPerSample).c_str(), val1, val2);
}

TW_IMAGEINFO JavaDTwainLowLevel_TW_IMAGEINFO::JavaToNative()
{
    TW_IMAGEINFO twimageinfo{};
    twimageinfo.BitsPerPixel = getBitsPerPixel();
    twimageinfo.Compression = getCompression();
    twimageinfo.ImageLength = getImageLength();
    twimageinfo.ImageWidth = getImageWidth();
    twimageinfo.PixelType = getPixelType();
    twimageinfo.Planar = getPlanar();
    twimageinfo.SamplesPerPixel = getSamplesPerPixel();
    twimageinfo.XResolution = getXResolution();
    twimageinfo.YResolution = getYResolution();
    for (int i = 0; i < 8; ++i)
        twimageinfo.BitsPerSample[i] = getBitsPerSampleValue(i);
    return twimageinfo;
}

TW_IMAGEINFO JavaDTwainLowLevel_TW_IMAGEINFO::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_IMAGEINFO::setValue(const TW_IMAGEINFO& twimageinfo)
{
    NativeToJava(twimageinfo);
}

jobject JavaDTwainLowLevel_TW_IMAGEINFO::NativeToJava(const TW_IMAGEINFO& twimageinfo)
{
    setObject(defaultConstructObject());
    setBitsPerPixel(twimageinfo.BitsPerPixel);
    setCompression(twimageinfo.Compression);
    setImageLength(twimageinfo.ImageLength);
    setImageWidth(twimageinfo.ImageWidth);
    setPixelType(twimageinfo.PixelType);
    setPlanar(twimageinfo.Planar);
    setSamplesPerPixel(twimageinfo.SamplesPerPixel);
    setXResolution(twimageinfo.XResolution);
    setYResolution(twimageinfo.YResolution);
    for (int i = 0; i < 8; ++i)
        setBitsPerSampleValue(twimageinfo.BitsPerSample[i], i);
    return getObject();
}

////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_ELEMENT8::JavaDTwainLowLevel_TW_ELEMENT8(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_ELEMENT8",
                     { GetIndex, GetChannel1, GetChannel2, GetChannel3,
                         SetIndex, SetChannel1, SetChannel2, SetChannel3 }), proxy_u8(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_ELEMENT8::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_ELEMENT8::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT8 JavaDTwainLowLevel_TW_ELEMENT8::getIndex()
{
    return getProxyData(proxy_u8, getFunctionName(GetIndex).c_str());
}

TW_UINT8 JavaDTwainLowLevel_TW_ELEMENT8::getChannel1()
{
    return getProxyData(proxy_u8, getFunctionName(GetChannel1).c_str());
}

TW_UINT8 JavaDTwainLowLevel_TW_ELEMENT8::getChannel2()
{
    return getProxyData(proxy_u8, getFunctionName(GetChannel2).c_str());
}

TW_UINT8 JavaDTwainLowLevel_TW_ELEMENT8::getChannel3()
{
    return getProxyData(proxy_u8, getFunctionName(GetChannel3).c_str());
}

void JavaDTwainLowLevel_TW_ELEMENT8::setIndex(TW_UINT8 val)
{
    setProxyData(proxy_u8, getFunctionName(SetIndex).c_str(), val);
}

void JavaDTwainLowLevel_TW_ELEMENT8::setChannel1(TW_UINT8 val)
{
    setProxyData(proxy_u8, getFunctionName(SetChannel1).c_str(), val);
}

void JavaDTwainLowLevel_TW_ELEMENT8::setChannel2(TW_UINT8 val)
{
    setProxyData(proxy_u8, getFunctionName(SetChannel2).c_str(), val);
}

void JavaDTwainLowLevel_TW_ELEMENT8::setChannel3(TW_UINT8 val)
{
    setProxyData(proxy_u8, getFunctionName(SetChannel3).c_str(), val);
}

TW_ELEMENT8 JavaDTwainLowLevel_TW_ELEMENT8::JavaToNative()
{
    TW_ELEMENT8 twelement8{};
    twelement8.Channel1 = getChannel1();
    twelement8.Channel2 = getChannel2();
    twelement8.Channel3 = getChannel3();
    twelement8.Index = getIndex();
    return twelement8;
}

TW_ELEMENT8 JavaDTwainLowLevel_TW_ELEMENT8::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_ELEMENT8::setValue(const TW_ELEMENT8& element8)
{
    setChannel1(element8.Channel1);
    setChannel2(element8.Channel2);
    setChannel3(element8.Channel3);
    setIndex(element8.Index);
}
//////////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_FILTER::JavaDTwainLowLevel_TW_FILTER(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_FILTER",
                     {GetSize, GetDescriptorCount, GetMaxDescriptorCount, GetCondition, GethDescriptors,
                      SetSize, SetDescriptorCount, SetMaxDescriptorCount, SetCondition, SethDescriptors}
                    ),proxy_u32(env),proxy_handle(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FILTER::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FILTER::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_FILTER::getSize()
{
    return getProxyData(proxy_u32, getFunctionName(GetSize).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILTER::getDescriptorCount()
{
    return getProxyData(proxy_u32, getFunctionName(GetDescriptorCount).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILTER::getMaxDescriptorCount()
{
    return getProxyData(proxy_u32, getFunctionName(GetMaxDescriptorCount).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILTER::getCondition()
{
    return getProxyData(proxy_u32, getFunctionName(GetCondition).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_FILTER::gethDescriptors()
{
    return getProxyData(proxy_handle, getFunctionName(GethDescriptors).c_str());
}

void JavaDTwainLowLevel_TW_FILTER::setSize(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILTER::setDescriptorCount(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetDescriptorCount).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILTER::setMaxDescriptorCount(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetMaxDescriptorCount).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILTER::setCondition(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetCondition).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILTER::sethDescriptors(TW_HANDLE val)
{
    setProxyData(proxy_handle, getFunctionName(SethDescriptors).c_str(), val);
}

TW_FILTER JavaDTwainLowLevel_TW_FILTER::JavaToNative()
{
    TW_FILTER twfilter{};
    twfilter.Condition = getCondition();
    twfilter.DescriptorCount = getDescriptorCount();
    twfilter.hDescriptors = gethDescriptors();
    twfilter.MaxDescriptorCount = getMaxDescriptorCount();
    twfilter.Size = getSize();
    return twfilter;
}

jobject JavaDTwainLowLevel_TW_FILTER::NativeToJava(const TW_FILTER& twfilter)
{
    setObject(createDefaultObject());
    setCondition(twfilter.Condition);
    setDescriptorCount(twfilter.DescriptorCount);
    sethDescriptors(twfilter.hDescriptors);
    setMaxDescriptorCount(twfilter.MaxDescriptorCount);
    setSize(twfilter.Size);
    return getObject();
}

TW_FILTER JavaDTwainLowLevel_TW_FILTER::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_FILTER::setValue(const TW_FILTER& filter)
{
    NativeToJava(filter);
}
/////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_VERSION::JavaDTwainLowLevel_TW_VERSION(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_VERSION",
        {GetMajor, GetMinor, GetLanguage, GetCountry, GetInfo, SetMajor, SetMinor,
         SetLanguage, SetCountry, SetInfo}),
            proxy(env),strproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_VERSION::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_VERSION::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_VERSION::getMajorNum()
{
    return getProxyData(proxy, getFunctionName(GetMajor).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_VERSION::getMinorNum()
{
    return getProxyData(proxy, getFunctionName(GetMinor).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_VERSION::getLanguage()
{
    return getProxyData(proxy, getFunctionName(GetLanguage).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_VERSION::getCountry()
{
    return getProxyData(proxy, getFunctionName(GetCountry).c_str());
}

JavaDTwainLowLevel_TW_VERSION::StringType JavaDTwainLowLevel_TW_VERSION::getInfo()
{
    return getProxyData(strproxy, getFunctionName(GetInfo).c_str());
}

void JavaDTwainLowLevel_TW_VERSION::setMajorNum(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetMajor).c_str(), val);
}

void JavaDTwainLowLevel_TW_VERSION::setMinorNum(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetMinor).c_str(), val);
}

void JavaDTwainLowLevel_TW_VERSION::setLanguage(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetLanguage).c_str(), val);
}

void JavaDTwainLowLevel_TW_VERSION::setCountry(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetCountry).c_str(), val);
}

void JavaDTwainLowLevel_TW_VERSION::setInfo(StringType val)
{
    setProxyData(strproxy, getFunctionName(SetInfo).c_str(), val);
}

TW_VERSION JavaDTwainLowLevel_TW_VERSION::JavaToNative()
{
    TW_VERSION twversion{};
    twversion.Country = getCountry();
    twversion.Language = getLanguage();
    twversion.MajorNum = getMajorNum();
    twversion.MinorNum = getMinorNum();
    auto infostr = getInfo();
    std::copy(infostr.begin(), infostr.end(), twversion.Info);
    return twversion;
}

TW_VERSION JavaDTwainLowLevel_TW_VERSION::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_VERSION::NativeToJava(const TW_VERSION& twversion)
{
    StringType infostr;
    copy_until_null(&twversion.Info[0], std::back_inserter(infostr));
    setInfo(infostr);
    setCountry(twversion.Country);
    setLanguage(twversion.Language);
    setMajorNum(twversion.MajorNum);
    setMinorNum(twversion.MinorNum);
}

void JavaDTwainLowLevel_TW_VERSION::setValue(const TW_VERSION& twversion)
{
    NativeToJava(twversion);
}
///////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_IDENTITY::JavaDTwainLowLevel_TW_IDENTITY(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_IDENTITY",
                     { GetId, GetProtocolMajor, GetProtocolMinor, GetSupportedGroups,
                         GetManufacturer, GetProductFamily, GetProductName, GetVersion,
                         SetId, SetProtocolMajor, SetProtocolMinor, SetSupportedGroups,
                         SetManufacturer, SetProductFamily, SetProductName, SetVersion}),
                         proxy(env), strproxy(env), uproxy(env), vProxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IDENTITY::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IDENTITY::createFullObject()
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_IDENTITY::getId()
{
    return getProxyData(uproxy, getFunctionName(GetId).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_IDENTITY::getProtocolMajor()
{
    return getProxyData(proxy, getFunctionName(GetProtocolMajor).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_IDENTITY::getProtocolMinor()
{
    return getProxyData(proxy, getFunctionName(GetProtocolMinor).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IDENTITY::getSupportedGroups()
{
    return getProxyData(uproxy, getFunctionName(GetSupportedGroups).c_str());
}

JavaDTwainLowLevel_TW_IDENTITY::StringType JavaDTwainLowLevel_TW_IDENTITY::getManufacturer()
{
    return getProxyData(strproxy, getFunctionName(GetManufacturer).c_str());
}

JavaDTwainLowLevel_TW_IDENTITY::StringType JavaDTwainLowLevel_TW_IDENTITY::getProductFamily()
{
    return getProxyData(strproxy, getFunctionName(GetProductFamily).c_str());
}

JavaDTwainLowLevel_TW_IDENTITY::StringType JavaDTwainLowLevel_TW_IDENTITY::getProductName()
{
    return getProxyData(strproxy, getFunctionName(GetProductName).c_str());
}

TW_VERSION JavaDTwainLowLevel_TW_IDENTITY::getVersion()
{
    return getProxyData(vProxy, getFunctionName(GetVersion).c_str());
}

void JavaDTwainLowLevel_TW_IDENTITY::setId(TW_UINT32 val)
{
    setProxyData(uproxy, getFunctionName(SetId).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setProtocolMajor(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetProtocolMajor).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setProtocolMinor(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetProtocolMinor).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setSupportedGroups(TW_UINT32 val)
{
    setProxyData(uproxy, getFunctionName(SetSupportedGroups).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setManufacturer(StringType val)
{
    setProxyData(strproxy, getFunctionName(SetManufacturer).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setProductFamily(StringType val)
{
    setProxyData(strproxy, getFunctionName(SetProductFamily).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setProductName(StringType val)
{
    setProxyData(strproxy, getFunctionName(SetProductName).c_str(), val);
}

void JavaDTwainLowLevel_TW_IDENTITY::setVersion(const TW_VERSION version)
{
    setProxyData(vProxy, getFunctionName(SetVersion).c_str(), version);
}

void JavaDTwainLowLevel_TW_IDENTITY::NativeToJava(const TW_IDENTITY& twIdentity)
{
    StringType infostr;
    copy_until_null(&twIdentity.Manufacturer[0], std::back_inserter(infostr));
    setManufacturer(infostr);
    infostr.clear();

    copy_until_null(&twIdentity.ProductFamily[0], std::back_inserter(infostr));
    setProductFamily(infostr);
    infostr.clear();

    copy_until_null(&twIdentity.ProductName[0], std::back_inserter(infostr));
    setProductName(infostr);
    infostr.clear();

    setId(twIdentity.Id);
    setSupportedGroups(twIdentity.SupportedGroups);
    setVersion(twIdentity.Version);
    setProtocolMajor(twIdentity.ProtocolMajor);
    setProtocolMinor(twIdentity.ProtocolMinor);
}

TW_IDENTITY JavaDTwainLowLevel_TW_IDENTITY::JavaToNative()
{
    TW_IDENTITY retIdentity{};
    retIdentity.Id = getId();
    retIdentity.SupportedGroups = getSupportedGroups();
    retIdentity.ProtocolMajor = getProtocolMajor();
    retIdentity.ProtocolMinor = getProtocolMinor();
    retIdentity.Version = getVersion();

    StringType str = getManufacturer();
    std::copy(str.begin(), str.end(), retIdentity.Manufacturer);
    str = getProductFamily();
    std::copy(str.begin(), str.end(), retIdentity.ProductFamily);
    str = getProductName();
    std::copy(str.begin(), str.end(), retIdentity.ProductName);

    return retIdentity;
}

TW_IDENTITY JavaDTwainLowLevel_TW_IDENTITY::getValue()
{
    return JavaToNative();
}
///////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_DEVICEEVENT::JavaDTwainLowLevel_TW_DEVICEEVENT(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_DEVICEEVENT",
{GetEvent,GetBatteryMinutes, GetFlashUsed2, GetAutoCapture, GetXRes, GetYRes, GetTimeBefore,
     GetTimeBetween, GetDeviceName, GetBatteryPct, GetPowerSupply, SetEvent, SetBatteryMinutes, SetFlashUsed2,
    SetAutoCapture, SetTimeBefore, SetTimeBetween, SetXRes, SetYRes, SetDeviceName, SetBatteryPct, SetPowerSupply}
), proxy_32(env), proxy_16(env), proxy_i32(env), strproxy(env), proxy_fix32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_DEVICEEVENT::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_DEVICEEVENT::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getEvent()
{
    return getProxyData(proxy_32, getFunctionName(GetEvent).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getBatteryMinutes()
{
    return getProxyData(proxy_32, getFunctionName(GetBatteryMinutes).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getFlashUsed2()
{
    return getProxyData(proxy_32, getFunctionName(GetFlashUsed2).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getAutomaticCapture()
{
    return getProxyData(proxy_32, getFunctionName(GetAutoCapture).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DEVICEEVENT::getXResolution()
{
    return getProxyData(proxy_fix32, getFunctionName(GetXRes).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DEVICEEVENT::getYResolution()
{
    return getProxyData(proxy_fix32, getFunctionName(GetYRes).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getTimeBeforeFirstCapture()
{
    return getProxyData(proxy_32, getFunctionName(GetTimeBefore).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getTimeBetweenCaptures()
{
    return getProxyData(proxy_32, getFunctionName(GetTimeBetween).c_str());
}

JavaDTwainLowLevel_TW_DEVICEEVENT::StringType JavaDTwainLowLevel_TW_DEVICEEVENT::getDeviceName()
{
    return getProxyData(strproxy, getFunctionName(GetDeviceName).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_DEVICEEVENT::getBatteryPercentage()
{
    return getProxyData(proxy_16, getFunctionName(GetBatteryPct).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_DEVICEEVENT::getPowerSupply()
{
    return getProxyData(proxy_i32, getFunctionName(GetPowerSupply).c_str());
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setEvent(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetEvent).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setBatteryMinutes(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetBatteryMinutes).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setFlashUsed2(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetFlashUsed2).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setAutomaticCapture(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetAutoCapture).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setTimeBeforeFirstCapture(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetTimeBefore).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setTimeBetweenCaptures(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetTimeBetween).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setXResolution(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetXRes).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setYResolution(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetYRes).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setDeviceName(const TW_STR255 val)
{
    return setProxyData(strproxy, getFunctionName(SetDeviceName).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setBatteryPercentage(TW_INT16 val)
{
    return setProxyData(proxy_16, getFunctionName(SetBatteryPct).c_str(), val);
}

void JavaDTwainLowLevel_TW_DEVICEEVENT::setPowerSupply(TW_INT32 val)
{
    return setProxyData(proxy_i32, getFunctionName(SetPowerSupply).c_str(), val);
}

TW_DEVICEEVENT JavaDTwainLowLevel_TW_DEVICEEVENT::JavaToNative()
{
    TW_DEVICEEVENT twdeviceevent{};
    twdeviceevent.AutomaticCapture = getAutomaticCapture();
    twdeviceevent.BatteryMinutes = getBatteryMinutes();
    twdeviceevent.BatteryPercentage = getBatteryPercentage();

    auto str = getDeviceName();
    copy_until_null_check(str.begin(), str.end(), twdeviceevent.DeviceName);

    twdeviceevent.Event = getEvent();
    twdeviceevent.FlashUsed2 = getFlashUsed2();
    twdeviceevent.PowerSupply = getPowerSupply();
    twdeviceevent.TimeBeforeFirstCapture = getTimeBeforeFirstCapture();
    twdeviceevent.TimeBetweenCaptures = getTimeBetweenCaptures();
    twdeviceevent.XResolution = getXResolution();
    twdeviceevent.YResolution = getYResolution();
    return twdeviceevent;
}

jobject JavaDTwainLowLevel_TW_DEVICEEVENT::NativeToJava(const TW_DEVICEEVENT& twdevicevent)
{
    setObject(defaultConstructObject());
    setAutomaticCapture(twdevicevent.AutomaticCapture);
    setBatteryMinutes(twdevicevent.BatteryMinutes);
    setBatteryPercentage(twdevicevent.BatteryPercentage);
    setDeviceName(twdevicevent.DeviceName);
    setEvent(twdevicevent.Event);
    setFlashUsed2(twdevicevent.FlashUsed2);
    setPowerSupply(twdevicevent.PowerSupply);
    setTimeBeforeFirstCapture(twdevicevent.TimeBeforeFirstCapture);
    setTimeBetweenCaptures(twdevicevent.TimeBetweenCaptures);
    setXResolution(twdevicevent.XResolution);
    setYResolution(twdevicevent.YResolution);
    return getObject();
}

///////////////////////////////////////////////////////////////////////////////

JavaDTwainLowLevel_TW_PENDINGXFERS::JavaDTwainLowLevel_TW_PENDINGXFERS(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_PENDINGXFERS",
        {GetCount, GetEOJ, SetCount, SetEOJ}), proxy_32(env), proxy_16(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PENDINGXFERS::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PENDINGXFERS::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_PENDINGXFERS::getCount()
{
    return getProxyData(proxy_16, getFunctionName(GetCount).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_PENDINGXFERS::getEOJ()
{
    return getProxyData(proxy_32, getFunctionName(GetEOJ).c_str());
}

void JavaDTwainLowLevel_TW_PENDINGXFERS::setCount(TW_UINT16 val)
{
    setProxyData(proxy_16, getFunctionName(SetCount).c_str(), val);
}

void JavaDTwainLowLevel_TW_PENDINGXFERS::setEOJ(TW_UINT32 val)
{
    setProxyData(proxy_32, getFunctionName(SetEOJ).c_str(), val);
}

TW_PENDINGXFERS JavaDTwainLowLevel_TW_PENDINGXFERS::JavaToNative()
{
    TW_PENDINGXFERS twpendingxfers{};
    twpendingxfers.Count = getCount();
    twpendingxfers.EOJ = getEOJ();
    return twpendingxfers;
}

jobject JavaDTwainLowLevel_TW_PENDINGXFERS::NativeToJava(const TW_PENDINGXFERS& twpendingxfers)
{
    setObject(defaultConstructObject());
    setCount(twpendingxfers.Count);
    setEOJ(twpendingxfers.EOJ);
    return getObject();
}
/////////////////////////////////////////////////
JavaDTwainLowLevel_TW_SETUPFILEXFER::JavaDTwainLowLevel_TW_SETUPFILEXFER(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_SETUPFILEXFER",
    {GetFileName, GetFormat, GetVRefNum, SetFileName, SetFormat, SetVRefNum }),
    proxy_16(env), proxy_i16(env), strproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_SETUPFILEXFER::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_SETUPFILEXFER::createFullObject(int var1)
{
    return createDefaultObject();
}

JavaDTwainLowLevel_TW_SETUPFILEXFER::StringType JavaDTwainLowLevel_TW_SETUPFILEXFER::getFileName()
{
    return getProxyData(strproxy, getFunctionName(GetFileName).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_SETUPFILEXFER::getFormat()
{
    return getProxyData(proxy_16, getFunctionName(GetFormat).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_SETUPFILEXFER::getVRefNum()
{
    return getProxyData(proxy_i16, getFunctionName(GetVRefNum).c_str());
}

void JavaDTwainLowLevel_TW_SETUPFILEXFER::setFileName(StringType val)
{
    setProxyData(strproxy, getFunctionName(SetFileName).c_str(), val);
}

void JavaDTwainLowLevel_TW_SETUPFILEXFER::setFileName(const TW_STR255 val)
{
    setProxyData(strproxy, getFunctionName(SetFileName).c_str(), val);
}

void JavaDTwainLowLevel_TW_SETUPFILEXFER::setFormat(TW_UINT16 val)
{
    setProxyData(proxy_16, getFunctionName(SetFormat).c_str(), val);
}

void JavaDTwainLowLevel_TW_SETUPFILEXFER::setVRefNum(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetVRefNum).c_str(), val);
}

TW_SETUPFILEXFER JavaDTwainLowLevel_TW_SETUPFILEXFER::JavaToNative()
{
    TW_SETUPFILEXFER twsetupfilexfer{};
    StringType str = getFileName();
    copy_until_null_check(str.begin(), str.end(), twsetupfilexfer.FileName);
    twsetupfilexfer.Format = getFormat();
    twsetupfilexfer.VRefNum = getVRefNum();
    return twsetupfilexfer;
}

jobject JavaDTwainLowLevel_TW_SETUPFILEXFER::NativeToJava(const TW_SETUPFILEXFER& twsetupfilexfer)
{
    setObject(defaultConstructObject());
    setFileName(twsetupfilexfer.FileName);
    setFormat(twsetupfilexfer.Format);
    setVRefNum(twsetupfilexfer.VRefNum);
    return getObject();
}

//////////////////////////////////////////////////
JavaDTwainLowLevel_TW_SETUPMEMXFER::JavaDTwainLowLevel_TW_SETUPMEMXFER(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_SETUPMEMXFER",
    { GetMinSize, GetMaxSize, GetPrefSize, SetMinSize, SetMaxSize, SetPrefSize }), proxy_32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_SETUPMEMXFER::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_SETUPMEMXFER::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_SETUPMEMXFER::getMinBufSize()
{
    return getProxyData(proxy_32, getFunctionName(GetMinSize).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_SETUPMEMXFER::getMaxBufSize()
{
    return getProxyData(proxy_32, getFunctionName(GetMaxSize).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_SETUPMEMXFER::getPreferred()
{
    return getProxyData(proxy_32, getFunctionName(GetPrefSize).c_str());
}

void JavaDTwainLowLevel_TW_SETUPMEMXFER::setMinBufSize(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetMinSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_SETUPMEMXFER::setMaxBufSize(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetMaxSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_SETUPMEMXFER::setPreferred(TW_UINT32 val)
{
    return setProxyData(proxy_32, getFunctionName(SetPrefSize).c_str(), val);
}

TW_SETUPMEMXFER JavaDTwainLowLevel_TW_SETUPMEMXFER::JavaToNative()
{
    TW_SETUPMEMXFER twsetupmemxfer{};
    twsetupmemxfer.MinBufSize = getMinBufSize();
    twsetupmemxfer.MaxBufSize = getMaxBufSize();
    twsetupmemxfer.Preferred = getPreferred();
    return twsetupmemxfer;
}

jobject JavaDTwainLowLevel_TW_SETUPMEMXFER::NativeToJava(const TW_SETUPMEMXFER& twsetupmemxfer)
{
    setObject(createDefaultObject());
    setMinBufSize(twsetupmemxfer.MinBufSize);
    setMaxBufSize(twsetupmemxfer.MaxBufSize);
    setPreferred(twsetupmemxfer.Preferred);
    return getObject();
}
//////////////////////////////////////////////////
JavaDTwainLowLevel_TW_FILESYSTEM::JavaDTwainLowLevel_TW_FILESYSTEM(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_FILESYSTEM",
    {
    GetInputName, GetOutputName, GetContext, GetRecursive, GetFileType, GetSize, GetCreateTime, GetModifiedTime, GetFreeSpace,
    GetImageSize, GetNumFiles, GetNumSnippets, GetDeviceMask, GetReserved, SetInputName, SetOutputName, SetContext,
    SetRecursive, SetFileType, SetSize, SetCreateTime, SetModifiedTime, SetFreeSpace, SetImageSize, SetNumFiles, SetNumSnippets,
    SetDeviceMask, SetReserved
    }), proxy_u32(env), proxy_i32(env), strproxy_255(env), strproxy_32(env), proxy_i8(env), proxy_memref(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FILESYSTEM::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FILESYSTEM::createFullObject(int var1)
{
    return createDefaultObject();
}

JavaDTwainLowLevel_TW_FILESYSTEM::StringType JavaDTwainLowLevel_TW_FILESYSTEM::getInputName()
{
    return getProxyData(strproxy_255, getFunctionName(GetInputName).c_str());
}

JavaDTwainLowLevel_TW_FILESYSTEM::StringType JavaDTwainLowLevel_TW_FILESYSTEM::getOutputName()
{
    return getProxyData(strproxy_255, getFunctionName(GetOutputName).c_str());
}

TW_MEMREF JavaDTwainLowLevel_TW_FILESYSTEM::getContext()
{
    return getProxyData(proxy_memref, getFunctionName(GetContext).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_FILESYSTEM::getRecursiveOrSubdirectories()
{
    return getProxyData(proxy_i32, getFunctionName(GetRecursive).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getFileTypeOrFileSystemType()
{
    return getProxyData(proxy_u32, getFunctionName(GetFileType).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getSize()
{
    return getProxyData(proxy_u32, getFunctionName(GetSize).c_str());
}

JavaDTwainLowLevel_TW_FILESYSTEM::StringType JavaDTwainLowLevel_TW_FILESYSTEM::getCreateDateTime()
{
    return getProxyData(strproxy_32, getFunctionName(GetCreateTime).c_str());
}

JavaDTwainLowLevel_TW_FILESYSTEM::StringType JavaDTwainLowLevel_TW_FILESYSTEM::getModifiedTimeDate()
{
    return getProxyData(strproxy_32, getFunctionName(GetModifiedTime).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getFreeSpace()
{
    return getProxyData(proxy_u32, getFunctionName(GetFreeSpace).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_FILESYSTEM::getNewImageSize()
{
    return getProxyData(proxy_i32, getFunctionName(GetImageSize).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getNumberOfFiles()
{
    return getProxyData(proxy_u32, getFunctionName(GetNumFiles).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getNumberOfSnippets()
{
    return getProxyData(proxy_u32, getFunctionName(GetNumSnippets).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_FILESYSTEM::getDeviceGroupMask()
{
    return getProxyData(proxy_u32, getFunctionName(GetDeviceMask).c_str());
}

std::vector<TW_INT8> JavaDTwainLowLevel_TW_FILESYSTEM::getReserved()
{
    std::vector<TW_INT8> aRet;
    JavaDTwainLowLevel_TW_INT8_ArrayTraits aTraits;
    JavaDTwainLowLevel_INT8Array int8Array(getEnvironment(), 508, aTraits, aTraits.m_className);
    const auto objArray = callObjectMethod(getFunctionName(GetReserved));
    int8Array.setObject(objArray);
    const auto aObjects = int8Array.JavaToNative();
    for (const auto& iObj : aObjects)
    {
        proxy_i8.setObject(iObj);
        aRet.push_back(proxy_i8.getValue());
    }
    return aRet;
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setInputName(JavaDTwainLowLevel_TW_FILESYSTEM::StringType val)
{
    setProxyData(strproxy_255, getFunctionName(SetInputName).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setOutputName(StringType val)
{
    setProxyData(strproxy_255, getFunctionName(SetOutputName).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setInputName(const TW_STR255 val)
{
    setProxyData(strproxy_255, getFunctionName(SetInputName).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setOutputName(const TW_STR255 val)
{
    setProxyData(strproxy_255, getFunctionName(SetOutputName).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setContext(TW_MEMREF val)
{
    setProxyData(proxy_memref, getFunctionName(SetContext).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setRecursiveOrSubdirectories(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetRecursive).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setFileTypeOrFileSystemType(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetFileType).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setSize(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setCreateDateTime(StringType val)
{
    setProxyData(strproxy_32, getFunctionName(SetCreateTime).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setCreateDateTime(const TW_STR32 val)
{
    setProxyData(strproxy_32, getFunctionName(SetCreateTime).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setModifiedTimeDate(StringType val)
{
    setProxyData(strproxy_32, getFunctionName(SetModifiedTime).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setModifiedTimeDate(const TW_STR32 val)
{
    setProxyData(strproxy_32, getFunctionName(SetModifiedTime).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setFreeSpace(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetFreeSpace).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setNewImageSize(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetImageSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setNumberOfFiles(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetNumFiles).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setNumberOfSnippets(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetNumSnippets).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setDeviceGroupMask(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetDeviceMask).c_str(), val);
}

void JavaDTwainLowLevel_TW_FILESYSTEM::setReserved(const TW_INT8* val)
{
    JavaDTwainLowLevel_TW_INT8_ArrayTraits aTraits;
    JavaDTwainLowLevel_INT8Array int8Array(getEnvironment(), 508, aTraits, aTraits.m_className);
    std::vector<jobject> aObjects;
    for (int i = 0; i < 508; ++i)
    {
        proxy_i8.createDefaultObject();
        proxy_i8.setValue(val[i]);
        aObjects.push_back(proxy_i8.getObject());
    }
    const auto newArray = int8Array.NativeToJava(aObjects);
    callObjectMethod(getFunctionName(SetReserved),newArray);
}

TW_FILESYSTEM JavaDTwainLowLevel_TW_FILESYSTEM::JavaToNative()
{
    TW_FILESYSTEM twfilesystem{};
    twfilesystem.Context = getContext();

    StringType str = getCreateDateTime();
    copy_until_null_check(str.begin(), str.end(), twfilesystem.CreateTimeDate);
    str = getModifiedTimeDate();
    copy_until_null_check(str.begin(), str.end(), twfilesystem.ModifiedTimeDate);
    str = getInputName();
    copy_until_null_check(str.begin(), str.end(), twfilesystem.InputName);
    str = getOutputName();
    copy_until_null_check(str.begin(), str.end(), twfilesystem.OutputName);

    twfilesystem.DeviceGroupMask = getDeviceGroupMask();
    twfilesystem.FileSystemType = getFileTypeOrFileSystemType();
    twfilesystem.FreeSpace = getFreeSpace();
    twfilesystem.NewImageSize = getNewImageSize();
    twfilesystem.NumberOfFiles = getNumberOfFiles();
    twfilesystem.NumberOfSnippets = getNumberOfSnippets();
    twfilesystem.Size = getSize();
    twfilesystem.Recursive = getRecursiveOrSubdirectories();
    auto v = getReserved();
    std::copy_n(twfilesystem.Reserved, (std::min)(508, static_cast<int>(v.size())), v.begin());
    return twfilesystem;
}

jobject JavaDTwainLowLevel_TW_FILESYSTEM::NativeToJava(const TW_FILESYSTEM& twfilesystem)
{
    setObject(defaultConstructObject());
    setContext(twfilesystem.Context);
    setCreateDateTime(twfilesystem.CreateTimeDate);
    setDeviceGroupMask(twfilesystem.DeviceGroupMask);
    setModifiedTimeDate(twfilesystem.ModifiedTimeDate);
    setFileTypeOrFileSystemType(twfilesystem.FileSystemType);
    setFreeSpace(twfilesystem.FreeSpace);
    setNewImageSize(twfilesystem.NewImageSize);
    setNumberOfFiles(twfilesystem.NumberOfFiles);
    setNumberOfSnippets(twfilesystem.NumberOfSnippets);
    setInputName(twfilesystem.InputName);
    setOutputName(twfilesystem.OutputName);
    setSize(twfilesystem.Size);
    setRecursiveOrSubdirectories(twfilesystem.Recursive);
    setReserved(twfilesystem.Reserved);
    return getObject();
}
//////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_METRICS::JavaDTwainLowLevel_TW_METRICS(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_METRICS",
    { GetSizeOf, GetImageCount, GetSheetCount, SetSizeOf, SetImageCount, SetSheetCount }), proxy_u32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_METRICS::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_METRICS::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_METRICS::getSizeOf()
{
    return getProxyData(proxy_u32, getFunctionName(GetSizeOf).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_METRICS::getImageCount()
{
    return getProxyData(proxy_u32, getFunctionName(GetImageCount).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_METRICS::getSheetCount()
{
    return getProxyData(proxy_u32, getFunctionName(GetSheetCount).c_str());
}

void JavaDTwainLowLevel_TW_METRICS::setSizeOf(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSizeOf).c_str(), val);
}

void JavaDTwainLowLevel_TW_METRICS::setImageCount(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetImageCount).c_str(), val);
}

void JavaDTwainLowLevel_TW_METRICS::setSheetCount(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSheetCount).c_str(), val);
}

TW_METRICS JavaDTwainLowLevel_TW_METRICS::JavaToNative()
{
    TW_METRICS twmetrics{};
    twmetrics.ImageCount = getImageCount();
    twmetrics.SheetCount = getSheetCount();
    twmetrics.SizeOf = sizeof(TW_METRICS);
    return twmetrics;
}

jobject JavaDTwainLowLevel_TW_METRICS::NativeToJava(const TW_METRICS& twmetrics)
{
    setObject(defaultConstructObject());
    setImageCount(twmetrics.ImageCount);
    setSheetCount(twmetrics.SheetCount);
    setSizeOf(twmetrics.SizeOf);
    return getObject();
}
/////////////////////////////////////////////////////////////

JavaDTwainLowLevel_TW_PASSTHRU::JavaDTwainLowLevel_TW_PASSTHRU(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_PASSTHRU",
{GetCommand, GetCommandBytes, GetDirection, GetData, GetDataBytes, GetDataBytesXfered,
    SetCommand, SetCommandBytes, SetDirection, SetData, SetDataBytes, SetDataBytesXfered }),
    proxy_u32(env), proxy_i32(env), proxy_memref(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PASSTHRU::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PASSTHRU::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_MEMREF JavaDTwainLowLevel_TW_PASSTHRU::getpCommand()
{
    return getProxyData(proxy_memref, getFunctionName(GetCommand).c_str());
}

TW_MEMREF JavaDTwainLowLevel_TW_PASSTHRU::getpData()
{
    return getProxyData(proxy_memref, getFunctionName(GetData).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_PASSTHRU::getCommandBytes()
{
    return getProxyData(proxy_u32, getFunctionName(GetCommandBytes).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_PASSTHRU::getDataBytes()
{
    return getProxyData(proxy_u32, getFunctionName(GetDataBytes).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_PASSTHRU::getDirection()
{
    return getProxyData(proxy_i32, getFunctionName(GetDirection).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_PASSTHRU::getDataBytesXfered()
{
    return getProxyData(proxy_i32, getFunctionName(GetDataBytesXfered).c_str());
}

void JavaDTwainLowLevel_TW_PASSTHRU::setpCommand(TW_MEMREF val)
{
    setProxyData(proxy_memref, getFunctionName(SetCommand).c_str(), val);
}

void JavaDTwainLowLevel_TW_PASSTHRU::setpData(TW_MEMREF val)
{
    setProxyData(proxy_memref, getFunctionName(SetData).c_str(), val);
}

void JavaDTwainLowLevel_TW_PASSTHRU::setCommandBytes(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetCommandBytes).c_str(), val);
}

void JavaDTwainLowLevel_TW_PASSTHRU::setDataBytes(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetDataBytes).c_str(), val);
}

void JavaDTwainLowLevel_TW_PASSTHRU::setDirection(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetDirection).c_str(), val);
}

void JavaDTwainLowLevel_TW_PASSTHRU::setDataBytesXfered(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetDataBytesXfered).c_str(), val);
}

TW_PASSTHRU JavaDTwainLowLevel_TW_PASSTHRU::JavaToNative()
{
    TW_PASSTHRU twpassthru{};
    twpassthru.CommandBytes = getCommandBytes();
    twpassthru.DataBytes = getDataBytes();
    twpassthru.DataBytesXfered = getDataBytesXfered();
    twpassthru.Direction = getDirection();
    twpassthru.pCommand = getpCommand();
    twpassthru.pData = getpData();
    return twpassthru;
}

jobject JavaDTwainLowLevel_TW_PASSTHRU::NativeToJava(const TW_PASSTHRU& twpassthru)
{
    setObject(defaultConstructObject());
    setDirection(twpassthru.Direction);
    setCommandBytes(twpassthru.CommandBytes);
    setDataBytes(twpassthru.DataBytes);
    setDataBytesXfered(twpassthru.DataBytesXfered);
    setpCommand(twpassthru.pCommand);
    setpData(twpassthru.pData);
    return getObject();
}

//////////////////////////////////
JavaDTwainLowLevel_TW_EXTIMAGEINFO::JavaDTwainLowLevel_TW_EXTIMAGEINFO(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_EXTIMAGEINFO",
    {GetNumInfos, GetOneInfo, SetNumInfos, SetOneInfo }), proxy_u32(env), proxy_info(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_EXTIMAGEINFO::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_EXTIMAGEINFO::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_EXTIMAGEINFO::getNumInfos()
{
    return getProxyData(proxy_u32, getFunctionName(GetNumInfos).c_str());
}

TW_INFO JavaDTwainLowLevel_TW_EXTIMAGEINFO::getOneInfo(int i)
{
    return getProxyData(proxy_info, getFunctionName(GetOneInfo).c_str(), i);
}

void JavaDTwainLowLevel_TW_EXTIMAGEINFO::setNumInfos(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetNumInfos).c_str(), val);
}

void JavaDTwainLowLevel_TW_EXTIMAGEINFO::setOneInfo(const TW_INFO& info, int i)
{
    return setProxyData(proxy_info, getFunctionName(SetOneInfo).c_str(), info, i);
}

TW_EXTIMAGEINFO JavaDTwainLowLevel_TW_EXTIMAGEINFO::JavaToNative()
{
    TW_EXTIMAGEINFO twinfo{};
    const auto numInfos = getNumInfos();
    for (size_t i = 0; i < numInfos; ++i)
        twinfo.Info[i] = getOneInfo(i);
    twinfo.NumInfos = numInfos;
    return twinfo;
}

jobject JavaDTwainLowLevel_TW_EXTIMAGEINFO::NativeToJava(const TW_EXTIMAGEINFO& twextimageinfo)
{
    setObject(createDefaultObject());
    setNumInfos(twextimageinfo.NumInfos);
    for ( size_t i = 0; i < twextimageinfo.NumInfos; ++i)
        setOneInfo(twextimageinfo.Info[i], i);
    return getObject();
}

TW_EXTIMAGEINFO JavaDTwainLowLevel_TW_EXTIMAGEINFO::getValue()
{
    return JavaToNative();
}
/////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_INFO::JavaDTwainLowLevel_TW_INFO(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_INFO",
        {GetInfoID, GetItemType, GetNumItems, GetReturnCode, GetItem,
         SetInfoID, SetItemType, SetNumItems, SetReturnCode, SetItem}),
    proxy_u16(env), proxy_u64(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INFO::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INFO::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_INFO::getInfoID()
{
    return getProxyData(proxy_u16, getFunctionName(GetInfoID).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_INFO::getItemType()
{
    return getProxyData(proxy_u16, getFunctionName(GetItemType).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_INFO::getNumItems()
{
    return getProxyData(proxy_u16, getFunctionName(GetNumItems).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_INFO::getReturnCode()
{
    return getProxyData(proxy_u16, getFunctionName(GetReturnCode).c_str());
}

TW_UINTPTR JavaDTwainLowLevel_TW_INFO::getItem()
{
    return getProxyData(proxy_u64, getFunctionName(GetItem).c_str());
}

void JavaDTwainLowLevel_TW_INFO::setInfoID(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetInfoID).c_str(), val);
}

void JavaDTwainLowLevel_TW_INFO::setItemType(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetItemType).c_str(), val);
}

void JavaDTwainLowLevel_TW_INFO::setNumItems(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetNumItems).c_str(), val);
}

void JavaDTwainLowLevel_TW_INFO::setReturnCode(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetReturnCode).c_str(), val);
}

void JavaDTwainLowLevel_TW_INFO::setItem(TW_UINTPTR val)
{
    return setProxyData(proxy_u64, getFunctionName(SetItem).c_str(), val);
}

TW_INFO JavaDTwainLowLevel_TW_INFO::JavaToNative()
{
    TW_INFO twinfo{};
    twinfo.ReturnCode = getReturnCode();
    twinfo.InfoID = getInfoID();
    twinfo.ItemType = getItemType();
    twinfo.Item = getItem();
    twinfo.NumItems = getNumItems();
    return twinfo;
}

TW_INFO JavaDTwainLowLevel_TW_INFO::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_INFO::setValue(const TW_INFO& val)
{
    setReturnCode(val.ReturnCode);
    setInfoID(val.InfoID);
    setItemType(val.ItemType);
    setItem(val.Item);
    setNumItems(val.NumItems);
}
/////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_CIECOLOR::JavaDTwainLowLevel_TW_CIECOLOR(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_CIECOLOR",
{GetNumSamples, GetColorSpace, GetLowEndian, GetDeviceDependent, GetVersionNumber, GetStageABC, GetStageLMN,
    GetWhitePoint, GetBlackPoint, GetWhitePaper, GetBlackInk, GetSamples, GetSample,
    SetNumSamples, SetColorSpace, SetLowEndian, SetDeviceDependent, SetVersionNumber, SetStageABC,
    SetStageLMN, SetWhitePoint, SetBlackPoint, SetWhitePaper, SetBlackInk, SetSample }),
    proxy_u16(env),
    proxy_i16(env),
    proxy_u32(env),
    proxy_i32(env),
    proxy_fix32(env),
    proxy_ciepoint(env),
    proxy_transformstage(env),
    proxy_status(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CIECOLOR::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CIECOLOR::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_INT32 JavaDTwainLowLevel_TW_CIECOLOR::getNumSamples()
{
    return callIntMethod(getFunctionName(GetNumSamples));
}

TW_UINT16 JavaDTwainLowLevel_TW_CIECOLOR::getColorSpace()
{
    return getProxyData(proxy_u16, getFunctionName(GetColorSpace).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_CIECOLOR::getLowEndian()
{
    return getProxyData(proxy_i16, getFunctionName(GetLowEndian).c_str());
}

TW_INT16 JavaDTwainLowLevel_TW_CIECOLOR::getDeviceDependent()
{
    return getProxyData(proxy_i16, getFunctionName(GetDeviceDependent).c_str());
}

TW_INT32 JavaDTwainLowLevel_TW_CIECOLOR::getVersionNumber()
{
    return getProxyData(proxy_i32, getFunctionName(GetVersionNumber).c_str());
}

TW_TRANSFORMSTAGE JavaDTwainLowLevel_TW_CIECOLOR::getStageABC()
{
    return getProxyData(proxy_transformstage, getFunctionName(GetStageABC).c_str());
}

TW_TRANSFORMSTAGE JavaDTwainLowLevel_TW_CIECOLOR::getStageLMN()
{
    return getProxyData(proxy_transformstage, getFunctionName(GetStageLMN).c_str());
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIECOLOR::getWhitePoint()
{
    return getProxyData(proxy_ciepoint, getFunctionName(GetWhitePoint).c_str());
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIECOLOR::getBlackPoint()
{
    return getProxyData(proxy_ciepoint, getFunctionName(GetBlackPoint).c_str());
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIECOLOR::getWhitePaper()
{
    return getProxyData(proxy_ciepoint, getFunctionName(GetWhitePaper).c_str());
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIECOLOR::getBlackInk()
{
    return getProxyData(proxy_ciepoint, getFunctionName(GetBlackInk).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_CIECOLOR::getSample(int i)
{
    return getProxyData(proxy_fix32, getFunctionName(GetSample).c_str(), i);
}

std::vector<TW_FIX32> JavaDTwainLowLevel_TW_CIECOLOR::getSamples()
{
    std::vector<TW_FIX32> retVal;
    const TW_INT32 numSamples = getNumSamples();
    for (int i = 0; i < numSamples; ++i)
        retVal.push_back(getSample(i));
    return retVal;
}

void JavaDTwainLowLevel_TW_CIECOLOR::setNumSamples(TW_INT32 val)
{
    callObjectMethod(getFunctionName(SetNumSamples), static_cast<int32_t>(val));
}

void JavaDTwainLowLevel_TW_CIECOLOR::setColorSpace(TW_UINT16 val)
{
    setProxyData(proxy_u16,  getFunctionName(SetColorSpace).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setLowEndian(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetLowEndian).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setDeviceDependent(TW_INT16 val)
{
    setProxyData(proxy_i16, getFunctionName(SetDeviceDependent).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setVersionNumber(TW_INT32 val)
{
    setProxyData(proxy_i32, getFunctionName(SetVersionNumber).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setStageABC(const TW_TRANSFORMSTAGE& val)
{
    setProxyData(proxy_transformstage, getFunctionName(SetStageABC).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setStageLMN(const TW_TRANSFORMSTAGE& val)
{
    setProxyData(proxy_transformstage, getFunctionName(SetStageLMN).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setWhitePoint(const TW_CIEPOINT& val)
{
    setProxyData(proxy_ciepoint, getFunctionName(SetWhitePoint).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setBlackPoint(const TW_CIEPOINT& val)
{
    setProxyData(proxy_ciepoint, getFunctionName(SetBlackPoint).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setWhitePaper(const TW_CIEPOINT& val)
{
    setProxyData(proxy_ciepoint, getFunctionName(SetWhitePaper).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setBlackInk(const TW_CIEPOINT& val)
{
    setProxyData(proxy_ciepoint, getFunctionName(SetBlackInk).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIECOLOR::setSample(TW_FIX32 val, int i)
{
    setProxyData(proxy_fix32, getFunctionName(SetSample).c_str(), val, i);
}

TW_CIECOLOR JavaDTwainLowLevel_TW_CIECOLOR::JavaToNative()
{
    TW_CIECOLOR twciecolor{};
    twciecolor.BlackInk = getBlackInk();
    twciecolor.BlackPoint = getBlackPoint();
    twciecolor.ColorSpace = getColorSpace();
    twciecolor.DeviceDependent = getDeviceDependent();
    twciecolor.LowEndian = getLowEndian();
    twciecolor.StageABC = getStageABC();
    twciecolor.StageLMN = getStageLMN();
    twciecolor.VersionNumber = getVersionNumber();
    twciecolor.WhitePaper = getWhitePaper();
    twciecolor.WhitePoint = getWhitePoint();
    const int numSamples = getNumSamples();
    for (int i = 0; i < numSamples; ++i)
        twciecolor.Samples[i] = getSample(i);
    return twciecolor;
}

TW_CIECOLOR JavaDTwainLowLevel_TW_CIECOLOR::getValue()
{
    return JavaToNative();
}

jobject JavaDTwainLowLevel_TW_CIECOLOR::NativeToJava(const TW_CIECOLOR& twciecolor)
{
    setObject(createDefaultObject());
    setBlackInk(twciecolor.BlackInk);
    setBlackPoint(twciecolor.BlackPoint);
    setColorSpace(twciecolor.ColorSpace);
    setDeviceDependent(twciecolor.DeviceDependent);
    setLowEndian(twciecolor.LowEndian);
    setStageABC(twciecolor.StageABC);
    setStageLMN(twciecolor.StageLMN);
    setVersionNumber(twciecolor.VersionNumber);
    setWhitePaper(twciecolor.WhitePaper);
    setWhitePoint(twciecolor.WhitePoint);
    const int numSamples = getNumSamples();
    for (int i = 0; i < numSamples; ++i)
        setSample(twciecolor.Samples[i], i);
    return getObject();
}
////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_DECODEFUNCTION::JavaDTwainLowLevel_TW_DECODEFUNCTION(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_DECODEFUNCTION",
{
    GetStartIn,GetBreakIn,GetEndIn,GetStartOut,GetBreakOut,GetEndOut,GetGamma,GetSampleCount,
    SetStartIn,SetBreakIn,SetEndIn,SetStartOut,SetBreakOut,SetEndOut,SetGamma,SetSampleCount }),
         proxy_fix32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_DECODEFUNCTION::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_DECODEFUNCTION::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getStartIn()
{
    return getProxyData(proxy_fix32, getFunctionName(GetStartIn).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getBreakIn()
{
    return getProxyData(proxy_fix32, getFunctionName(GetBreakIn).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getEndIn()
{
    return getProxyData(proxy_fix32, getFunctionName(GetEndIn).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getStartOut()
{
    return getProxyData(proxy_fix32, getFunctionName(GetStartOut).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getBreakOut()
{
    return getProxyData(proxy_fix32, getFunctionName(GetBreakOut).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getEndOut()
{
    return getProxyData(proxy_fix32, getFunctionName(GetEndOut).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getGamma()
{
    return getProxyData(proxy_fix32, getFunctionName(GetGamma).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_DECODEFUNCTION::getSampleCount()
{
    return getProxyData(proxy_fix32, getFunctionName(GetSampleCount).c_str());
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setStartIn(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetStartIn).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setBreakIn(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetBreakIn).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setEndIn(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetEndIn).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setStartOut(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetStartOut).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setBreakOut(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetBreakOut).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setEndOut(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetEndOut).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setGamma(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetGamma).c_str(), val);
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setSampleCount(TW_FIX32 val)
{
    return setProxyData(proxy_fix32, getFunctionName(SetSampleCount).c_str(), val);
}

TW_DECODEFUNCTION JavaDTwainLowLevel_TW_DECODEFUNCTION::JavaToNative()
{
    TW_DECODEFUNCTION twdecodefunction{};
    twdecodefunction.BreakIn = getBreakIn();
    twdecodefunction.BreakOut = getBreakOut();
    twdecodefunction.EndIn = getEndIn();
    twdecodefunction.EndOut = getEndOut();
    twdecodefunction.Gamma = getGamma();
    twdecodefunction.SampleCount = getSampleCount();
    twdecodefunction.StartIn = getStartIn();
    twdecodefunction.StartOut = getStartOut();
    return twdecodefunction;
}

TW_DECODEFUNCTION JavaDTwainLowLevel_TW_DECODEFUNCTION::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_DECODEFUNCTION::setValue(const TW_DECODEFUNCTION& val)
{
    setBreakIn(val.BreakIn);
    setBreakOut(val.BreakOut);
    setEndIn(val.EndIn);
    setEndOut(val.EndIn);
    setGamma(val.Gamma);
    setSampleCount(val.SampleCount);
    setStartIn(val.StartIn);
    setStartOut(val.StartOut);
}

JavaDTwainLowLevel_TW_CUSTOMDSDATA::JavaDTwainLowLevel_TW_CUSTOMDSDATA(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_CUSTOMDSDATA",
    {GetInfoLength, GetData, SetInfoLength, SetData }),
         proxy(env), handleproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CUSTOMDSDATA::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CUSTOMDSDATA::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_CUSTOMDSDATA::getInfoLength()
{
    return getProxyData(proxy, getFunctionName(GetInfoLength).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_CUSTOMDSDATA::gethData()
{
    return getProxyData(handleproxy, getFunctionName(GetData).c_str());
}

void JavaDTwainLowLevel_TW_CUSTOMDSDATA::setInfoLength(TW_UINT32 val)
{
    setProxyData(proxy, getFunctionName(SetInfoLength).c_str(), val);
}

void JavaDTwainLowLevel_TW_CUSTOMDSDATA::sethData(TW_HANDLE val)
{
    setProxyData(handleproxy, getFunctionName(SetData).c_str(), val);
}

TW_CUSTOMDSDATA JavaDTwainLowLevel_TW_CUSTOMDSDATA::JavaToNative()
{
    TW_CUSTOMDSDATA twcustomdsdata{};
    twcustomdsdata.InfoLength = getInfoLength();
    twcustomdsdata.hData = gethData();
    return twcustomdsdata;
}

jobject JavaDTwainLowLevel_TW_CUSTOMDSDATA::NativeToJava(const TW_CUSTOMDSDATA& twcustomdsdata)
{
    setObject(defaultConstructObject());
    setInfoLength(twcustomdsdata.InfoLength);
    sethData(twcustomdsdata.hData);
    return getObject();
}
/////////////////////////////////////////////////////////////////////////

JavaDTwainLowLevel_TW_CAPABILITY::JavaDTwainLowLevel_TW_CAPABILITY(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_CAPABILITY",
{ GetCap, GetConType, GetContainerHandle, SetCap, SetConType, SetContainerHandle }
), proxy(env), handleproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CAPABILITY::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CAPABILITY::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_CAPABILITY::getCap()
{
    return getProxyData(proxy, getFunctionName(GetCap).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_CAPABILITY::getConType()
{
    return getProxyData(proxy, getFunctionName(GetConType).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_CAPABILITY::gethContainer()
{
    return getProxyData(handleproxy, getFunctionName(GetContainerHandle).c_str());
}

void JavaDTwainLowLevel_TW_CAPABILITY::setCap(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetCap).c_str(), val);
}

void JavaDTwainLowLevel_TW_CAPABILITY::setConType(TW_UINT16 val)
{
    setProxyData(proxy, getFunctionName(SetConType).c_str(), val);
}

void JavaDTwainLowLevel_TW_CAPABILITY::sethContainer(TW_HANDLE val)
{
    setProxyData(handleproxy, getFunctionName(SetContainerHandle).c_str(), val);
}

TW_CAPABILITY JavaDTwainLowLevel_TW_CAPABILITY::JavaToNative()
{
    TW_CAPABILITY twcapability{};
    twcapability.hContainer = gethContainer();
    twcapability.ConType = getConType();
    twcapability.Cap = getCap();
    return twcapability;
}

jobject JavaDTwainLowLevel_TW_CAPABILITY::NativeToJava(const TW_CAPABILITY& twcapability)
{
    setObject(defaultConstructObject());
    setCap(twcapability.Cap);
    setConType(twcapability.ConType);
    sethContainer(twcapability.hContainer);
    return getObject();
}
////////////////////////////////////////////////

JavaDTwainLowLevel_TW_AUDIOINFO::JavaDTwainLowLevel_TW_AUDIOINFO(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_AUDIOINFO",
    {GetName, GetReserved, SetName, SetReserved }),
        proxy(env), strproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_AUDIOINFO::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_AUDIOINFO::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_AUDIOINFO::getReserved()
{
    return getProxyData(proxy, getFunctionName(GetReserved).c_str());
}

JavaDTwainLowLevel_TW_AUDIOINFO::StringType JavaDTwainLowLevel_TW_AUDIOINFO::getName()
{
    return getProxyData(strproxy, getFunctionName(GetName).c_str());
}

void JavaDTwainLowLevel_TW_AUDIOINFO::setReserved(TW_UINT32 val)
{
    setProxyData(proxy, getFunctionName(SetReserved).c_str(), val);
}

void JavaDTwainLowLevel_TW_AUDIOINFO::setName(JavaDTwainLowLevel_TW_AUDIOINFO::StringType val)
{
    setProxyData(strproxy, getFunctionName(SetName).c_str(), val);
}

TW_AUDIOINFO JavaDTwainLowLevel_TW_AUDIOINFO::JavaToNative()
{
    TW_AUDIOINFO twaudioinfo{};
    JavaDTwainLowLevel_TW_AUDIOINFO::StringType str = getName();
    std::copy(str.begin(), str.end(), twaudioinfo.Name);
    twaudioinfo.Reserved = getReserved();
    return twaudioinfo;
}

jobject JavaDTwainLowLevel_TW_AUDIOINFO::NativeToJava(const TW_AUDIOINFO& twaudioinfo)
{
    setObject(defaultConstructObject());
    JavaDTwainLowLevel_TW_AUDIOINFO::StringType str;
    copy_until_null_check(twaudioinfo.Name, twaudioinfo.Name + strproxy.size(), std::back_inserter(str));
    setName(str);
    setReserved(twaudioinfo.Reserved);
    return getObject();
}

//////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_TRANSFORMSTAGE::JavaDTwainLowLevel_TW_TRANSFORMSTAGE(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_TRANSFORMSTAGE",
        { GetDecodeValue, GetMixValue, SetDecodeValue, SetMixValue }),proxy_fix32(env), proxy_decode(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_TRANSFORMSTAGE::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_TRANSFORMSTAGE::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_DECODEFUNCTION JavaDTwainLowLevel_TW_TRANSFORMSTAGE::getDecodeValue(int value)
{
    return getProxyData(proxy_decode, getFunctionName(GetDecodeValue).c_str(), value);
}

TW_FIX32 JavaDTwainLowLevel_TW_TRANSFORMSTAGE::getMixValue(int val1, int val2)
{
    return getProxyData(proxy_fix32, getFunctionName(GetMixValue).c_str(), val1, val2);
}

void JavaDTwainLowLevel_TW_TRANSFORMSTAGE::setDecodeValue(const TW_DECODEFUNCTION& val1, int val2)
{
    setProxyData(proxy_decode, getFunctionName(SetDecodeValue).c_str(), val1, val2);
}

void JavaDTwainLowLevel_TW_TRANSFORMSTAGE::setMixValue(TW_FIX32 val1, int val2, int val3)
{
    setProxyData(proxy_fix32, getFunctionName(SetMixValue).c_str(), val1, val2, val3);
}

TW_TRANSFORMSTAGE JavaDTwainLowLevel_TW_TRANSFORMSTAGE::JavaToNative()
{
    TW_TRANSFORMSTAGE twtransformstage{};
    for (int i = 0; i < 3; ++i)
        twtransformstage.Decode[i] = getDecodeValue(i);
    for (int i = 0; i < 3; ++i)
    {
        for (int j = 0; j < 3; ++j)
            twtransformstage.Mix[i][j] = getMixValue(i, j);
    }
    return twtransformstage;
}

TW_TRANSFORMSTAGE JavaDTwainLowLevel_TW_TRANSFORMSTAGE::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_TRANSFORMSTAGE::setValue(const TW_TRANSFORMSTAGE& val)
{
    for (int i = 0; i < 3; ++i)
        setDecodeValue(val.Decode[i], i);
    for (int i = 0; i < 3; ++i)
    {
        for (int j = 0; j < 3; ++j)
            setMixValue(val.Mix[i][j], i, j);
    }
}

JavaDTwainLowLevel_TW_CIEPOINT::JavaDTwainLowLevel_TW_CIEPOINT(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_CIEPOINT",
        {GetX, GetY, GetZ, SetX, SetY, SetZ }), proxy_fix32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CIEPOINT::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_CIEPOINT::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_FIX32 JavaDTwainLowLevel_TW_CIEPOINT::getX()
{
    return getProxyData(proxy_fix32, getFunctionName(GetX).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_CIEPOINT::getY()
{
    return getProxyData(proxy_fix32, getFunctionName(GetY).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_CIEPOINT::getZ()
{
    return getProxyData(proxy_fix32, getFunctionName(GetZ).c_str());
}

void JavaDTwainLowLevel_TW_CIEPOINT::setX(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetX).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIEPOINT::setY(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetY).c_str(), val);
}

void JavaDTwainLowLevel_TW_CIEPOINT::setZ(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetZ).c_str(), val);
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIEPOINT::JavaToNative()
{
    TW_CIEPOINT twciepoint{};
    twciepoint.X = getX();
    twciepoint.Y = getY();
    twciepoint.Z = getZ();
    return twciepoint;
}

TW_CIEPOINT JavaDTwainLowLevel_TW_CIEPOINT::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_CIEPOINT::setValue(const TW_CIEPOINT& val)
{
    setX(val.X);
    setY(val.Y);
    setZ(val.Z);
}

JavaDTwainLowLevel_TW_STATUSUTF8::JavaDTwainLowLevel_TW_STATUSUTF8(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_STATUSUTF8",
    {GetStatus, GetSize, GetUTF8String, SetStatus, SetSize, SetUTF8String}),
         proxy_status(env), proxy_u32(env), proxy_handle(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STATUSUTF8::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STATUSUTF8::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_STATUS JavaDTwainLowLevel_TW_STATUSUTF8::getStatus()
{
    jobject jobj = callObjectMethod("GetStatus");
    if (jobj)
    {
        proxy_status.setObject(jobj);
        return proxy_status.getValue();
    }
    return{};
}

TW_UINT32 JavaDTwainLowLevel_TW_STATUSUTF8::getSize()
{
    return getProxyData(proxy_u32, getFunctionName(GetSize).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_STATUSUTF8::getUTF8string()
{
    return getProxyData(proxy_handle, getFunctionName(GetUTF8String).c_str());
}

void JavaDTwainLowLevel_TW_STATUSUTF8::setStatus(TW_STATUS val)
{
    setProxyData(proxy_status, getFunctionName(SetStatus).c_str(), val);
}

void JavaDTwainLowLevel_TW_STATUSUTF8::setSize(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_STATUSUTF8::setUTF8string(TW_HANDLE val)
{
    setProxyData(proxy_handle, getFunctionName(SetUTF8String).c_str(), val);
}

TW_STATUSUTF8 JavaDTwainLowLevel_TW_STATUSUTF8::JavaToNative()
{
    TW_STATUSUTF8 twstatusutf8{};
    twstatusutf8.Size = getSize();
    twstatusutf8.Status = getStatus();
    twstatusutf8.UTF8string = getUTF8string();
    return twstatusutf8;
}

jobject JavaDTwainLowLevel_TW_STATUSUTF8::NativeToJava(const TW_STATUSUTF8& twstatusutf8)
{
    setObject(createDefaultObject());
    setSize(twstatusutf8.Size);
    setUTF8string(twstatusutf8.UTF8string);
    setStatus(twstatusutf8.Status);
    return getObject();
}
/////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STATUS::JavaDTwainLowLevel_TW_STATUS(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_STATUS",
    {GetConditionCode, GetData, GetReserved, SetConditionCode, SetData, SetReserved }),
    proxy_u16(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STATUS::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STATUS::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_STATUS::getConditonCode()
{
    return getProxyData(proxy_u16, getFunctionName(GetConditionCode).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_STATUS::getData()
{
    return getProxyData(proxy_u16, getFunctionName(GetData).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_STATUS::getReserved()
{
    return getProxyData(proxy_u16, getFunctionName(GetReserved).c_str());
}

void JavaDTwainLowLevel_TW_STATUS::setConditionCode(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetConditionCode).c_str(), val);
}

void JavaDTwainLowLevel_TW_STATUS::setData(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetData).c_str(), val);
}

void JavaDTwainLowLevel_TW_STATUS::setReserved(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetReserved).c_str(), val);
}

TW_STATUS JavaDTwainLowLevel_TW_STATUS::JavaToNative()
{
    TW_STATUS twstatus{};
    twstatus.ConditionCode = getConditonCode();
    twstatus.Data = getData();
    return twstatus;
}

jobject JavaDTwainLowLevel_TW_STATUS::NativeToJava(const TW_STATUS& twstatus)
{
    setObject(createDefaultObject());
    setConditionCode(twstatus.ConditionCode);
    setData(twstatus.Data);
    setReserved(twstatus.Reserved);
    return getObject();
}
////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_EVENT::JavaDTwainLowLevel_TW_EVENT(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_EVENT",
    {GetEvent, GetTWMessage, SetEvent, SetTWMessage }), proxy_16(env), proxy_memref(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_EVENT::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_EVENT::createFullObject(int var1)
{
    return createDefaultObject();
}

TW_MEMREF JavaDTwainLowLevel_TW_EVENT::getpEvent()
{
    return getProxyData(proxy_memref, getFunctionName(GetEvent).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_EVENT::getTWMessage()
{
    return getProxyData(proxy_16, getFunctionName(GetTWMessage).c_str());
}

void JavaDTwainLowLevel_TW_EVENT::setTWMessage(TW_UINT16 val)
{
    return setProxyData(proxy_16, getFunctionName(SetTWMessage).c_str(), val);
}

void JavaDTwainLowLevel_TW_EVENT::setpEvent(TW_MEMREF pEvent)
{
    setProxyData(proxy_memref, getFunctionName(SetEvent).c_str(), pEvent);
}

TW_EVENT JavaDTwainLowLevel_TW_EVENT::JavaToNative()
{
    TW_EVENT twevent{};
    twevent.pEvent = getpEvent();
    twevent.TWMessage = getTWMessage();
    return twevent;
}

jobject JavaDTwainLowLevel_TW_EVENT::NativeToJava(const TW_EVENT& twevent)
{
    setObject(defaultConstructObject());
    setTWMessage(twevent.TWMessage);
    setpEvent(twevent.pEvent);
    return getObject();
}
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STRING_Base::JavaDTwainLowLevel_TW_STRING_Base(JNIEnv* env, int strSize, const std::string& className) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), className,
    { GetValue, SetValue1, SetValue2 }), m_strSize(strSize)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STRING_Base::createDefaultObject()
{
    return defaultConstructObject();
}

JavaDTwainLowLevel_TW_STRING_Base::StringType JavaDTwainLowLevel_TW_STRING_Base::getValue()
{
    const jobject obj = callObjectMethod(getFunctionName(GetValue));
    if (obj)
    {
        GetStringCharsHandler charsHandler(m_pJavaEnv, static_cast<jstring>(obj));
        const auto val = reinterpret_cast<const GetStringCharsHandler::native_type*>(charsHandler.GetStringChars());
        if (val)
            return val;
    }
    return{};
}

JavaDTwainLowLevel_TW_STRING_Base::StringType JavaDTwainLowLevel_TW_STRING_Base::JavaToNative()
{
    return getValue();
}

void JavaDTwainLowLevel_TW_STRING_Base::setValue(JavaDTwainLowLevel_TW_STRING_Base::StringType val)
{
    const jstring str = CreateJStringFromCString(m_pJavaEnv, val.c_str());
    callObjectMethod(getFunctionName(SetValue1), str);
}

jobject JavaDTwainLowLevel_TW_STRING_Base::NativeToJava(JavaDTwainLowLevel_TW_STRING_Base::StringType val)
{
    setObject(createDefaultObject());
    setValue(val);
    return getObject();
}

jobject JavaDTwainLowLevel_TW_STRING_Base::NativeToJava(const char *arr)
{
    setObject(createDefaultObject());
#ifdef UNICODE
    std::wstring sTemp(arr, arr + m_strSize);
    return NativeToJava(sTemp);
#else
    return NativeToJava(std::string(arr, arr + m_strSize));
#endif
}

void JavaDTwainLowLevel_TW_STRING_Base::setValue(const char* val)
{
    JavaDTwainLowLevel_TW_STRING::StringType str;
    std::copy_n(val, m_strSize, std::back_inserter(str));
    setValue(str);
}
/////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STRING::JavaDTwainLowLevel_TW_STRING(JNIEnv* env, int strSize, const std::string& className):
        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), className,
        {GetValue, SetValue}), m_strSize(strSize)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STRING::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STRING::createFullObject(int allocSize, int actualSize)
{
    return constructObject(1, allocSize, actualSize);
}

JavaDTwainLowLevel_TW_STRING::StringType JavaDTwainLowLevel_TW_STRING::getValue()
{
    const jobject obj = callObjectMethod(getFunctionName(GetValue));
    if (obj)
    {
        GetStringCharsHandler charsHandler(m_pJavaEnv, static_cast<jstring>(obj));
        const auto val = reinterpret_cast<const GetStringCharsHandler::native_type*>(charsHandler.GetStringChars());
        if (val)
            return val;
    }
    return{};
}

void JavaDTwainLowLevel_TW_STRING::setValue(JavaDTwainLowLevel_TW_STRING::StringType val)
{
    const jstring str = CreateJStringFromCString(m_pJavaEnv, val.c_str());
    callObjectMethod(getFunctionName(SetValue), str);
}

void JavaDTwainLowLevel_TW_STRING::setValue(const char* val)
{
    JavaDTwainLowLevel_TW_STRING::StringType str;
    std::copy_n(val, m_strSize, std::back_inserter(str));
    setValue(str);
}
///////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_FIX32::JavaDTwainLowLevel_TW_FIX32(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_FIX32",
    { GetValue, GetWhole, GetFrac, SetValue1, SetValue2, SetWhole, SetFrac }),
    uProxy(env), iProxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FIX32::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FIX32::createFullObject()
{
    return defaultConstructObject();
}

TW_FIX32 JavaDTwainLowLevel_TW_FIX32::getValue()
{
    TW_FIX32 fix32;
    fix32.Whole = getProxyData(iProxy, getFunctionName(GetWhole).c_str());
    fix32.Frac = getProxyData(uProxy, getFunctionName(GetFrac).c_str());
    return fix32;
}

void JavaDTwainLowLevel_TW_FIX32::setValue(TW_FIX32 val)
{
    setProxyData(iProxy, getFunctionName(SetWhole).c_str(), val.Whole);
    setProxyData(uProxy, getFunctionName(SetFrac).c_str(), val.Frac);
}

void JavaDTwainLowLevel_TW_FIX32::setValue(double d)
{
    callDoubleMethod(getFunctionName(SetValue1), d);
}

TW_FIX32 JavaDTwainLowLevel_TW_FIX32::JavaToNative()
{
    return getValue();
}

TW_FIX32 JavaDTwainLowLevel_TW_FIX32::fromDouble(double val)
{
    TW_FIX32 retVal {};
    const bool sign = (val < 0) ? true : false;
    int value = static_cast<int>(val * 65536.0 + (sign ? (-0.5) : 0.5));
    retVal.Whole = static_cast<short>(value >> 16);
    retVal.Frac = static_cast<short>(value & 0x0000ffffL);
    return retVal;
}


////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_INT32::JavaDTwainLowLevel_TW_INT32(JNIEnv* env):
        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_INT32",
            {GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT32::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT32::createFullObject(int var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_INT32::value_type JavaDTwainLowLevel_TW_INT32::getValue()
{
    return callIntMethod(getFunctionName(GetValue));
}

void JavaDTwainLowLevel_TW_INT32::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue), val);
}

JavaDTwainLowLevel_TW_INT32::value_type JavaDTwainLowLevel_TW_INT32::JavaToNative()
{
    return getValue();
}

jobject JavaDTwainLowLevel_TW_INT32::NativeToJava(value_type val)
{
    return callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}

//////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_UINT32::JavaDTwainLowLevel_TW_UINT32(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_UINT32",
    {GetValue, SetValue} )
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT32::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT32::createFullObject(TW_UINT32 var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_UINT32::value_type JavaDTwainLowLevel_TW_UINT32::getValue()
{
    return static_cast<TW_UINT32>(callLongMethod(getFunctionName(GetValue)));
}

void JavaDTwainLowLevel_TW_UINT32::setValue(value_type val)
{
    NativeToJava(val);
}

JavaDTwainLowLevel_TW_UINT32::value_type JavaDTwainLowLevel_TW_UINT32::JavaToNative()
{
    return getValue();
}

jobject JavaDTwainLowLevel_TW_UINT32::NativeToJava(value_type val)
{
    return callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}
////////////////////////////////////////////
JavaDTwainLowLevel_TW_UINTPTR::JavaDTwainLowLevel_TW_UINTPTR(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_UINTPTR",
        {SetValue1, SetValue2, GetValueAsString, GetValue })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINTPTR::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINTPTR::createFullObject(int16_t var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_UINTPTR::value_type JavaDTwainLowLevel_TW_UINTPTR::getValue()
{
    return static_cast<TW_UINTPTR>(callLongMethod(getFunctionName(GetValue)));
}

void JavaDTwainLowLevel_TW_UINTPTR::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue1), static_cast<int64_t>(val));
}
/////////////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_INT16::JavaDTwainLowLevel_TW_INT16(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_INT16",
            {GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT16::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT16::createFullObject(int16_t var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_INT16::value_type JavaDTwainLowLevel_TW_INT16::getValue()
{
    return callShortMethod(getFunctionName(GetValue));
}

void JavaDTwainLowLevel_TW_INT16::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue), val);
}

JavaDTwainLowLevel_TW_INT16::value_type JavaDTwainLowLevel_TW_INT16::JavaToNative()
{
    return getValue();
}

jobject JavaDTwainLowLevel_TW_INT16::NativeToJava(value_type val)
{
    return callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}

//////////////////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_UINT16::JavaDTwainLowLevel_TW_UINT16(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_UINT16",
{GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT16::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT16::createFullObject(value_type var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_UINT16::value_type JavaDTwainLowLevel_TW_UINT16::getValue()
{
    return static_cast<value_type>(callIntMethod(getFunctionName(GetValue)));
}

void JavaDTwainLowLevel_TW_UINT16::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}

JavaDTwainLowLevel_TW_UINT16::value_type JavaDTwainLowLevel_TW_UINT16::JavaToNative()
{
    return getValue();
}

jobject JavaDTwainLowLevel_TW_UINT16::NativeToJava(value_type val)
{
    return callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}
//////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_INT8::JavaDTwainLowLevel_TW_INT8(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_INT8",
    {GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT8::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_INT8::createFullObject(int8_t var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_INT8::value_type JavaDTwainLowLevel_TW_INT8::getValue()
{
    return callByteMethod(getFunctionName(GetValue));
}

void JavaDTwainLowLevel_TW_INT8::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue), val);
}
////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_UINT8::JavaDTwainLowLevel_TW_UINT8(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_UINT8",
        {GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT8::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_UINT8::createFullObject(actual_type var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_UINT8::value_type JavaDTwainLowLevel_TW_UINT8::getValue()
{
    return static_cast<JavaDTwainLowLevel_TW_UINT8::value_type>(callIntMethod(getFunctionName(GetValue)));
}

void JavaDTwainLowLevel_TW_UINT8::setValue(value_type val)
{
    callObjectMethod(getFunctionName(SetValue), static_cast<actual_type>(val));
}
/////////////////////////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STR32::JavaDTwainLowLevel_TW_STR32(JNIEnv* env) :
    JavaDTwainLowLevel_TW_STRING_Base(env, 32, "TW_STR32")
{}

jobject JavaDTwainLowLevel_TW_STR32::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STR32::createFullObject()
{
    return defaultConstructObject();
}
////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STR64::JavaDTwainLowLevel_TW_STR64(JNIEnv* env) :
    JavaDTwainLowLevel_TW_STRING_Base(env, 64, "TW_STR64")
{}

jobject JavaDTwainLowLevel_TW_STR64::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STR64::createFullObject()
{
    return defaultConstructObject();
}
/////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_STR128::JavaDTwainLowLevel_TW_STR128(JNIEnv* env) : JavaDTwainLowLevel_TW_STRING_Base(env, 128, "TW_STR128")
{}

jobject JavaDTwainLowLevel_TW_STR128::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STR128::createFullObject()
{
    return defaultConstructObject();
}

JavaDTwainLowLevel_TW_STR255::JavaDTwainLowLevel_TW_STR255(JNIEnv* env) :
    JavaDTwainLowLevel_TW_STRING_Base(env, 255, "TW_STR255")
{}

jobject JavaDTwainLowLevel_TW_STR255::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STR255::createFullObject()
{
    return defaultConstructObject();
}

JavaDTwainLowLevel_TW_STR1024::JavaDTwainLowLevel_TW_STR1024(JNIEnv* env) :
    JavaDTwainLowLevel_TW_STRING_Base(env, 1024, "TW_STR1024")
{}

jobject JavaDTwainLowLevel_TW_STR1024::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_STR1024::createFullObject()
{
    return defaultConstructObject();
}

//////////////////////////////////////////////////////////////////
JavaBufferedStripInfo::JavaBufferedStripInfo(JNIEnv* env) :
        JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "BufferedStripInfo",
{ GetBufferHandle, GetBufferedStripData, GetCompressionType, GetBufferSize, GetImageInfo, GetColumns, GetRows, GetXOff, GetYOff, GetBytesWritten,
   GetBytesPerRow, GetPreferredSize, IsAppAllocatesBuffer, SetBufferHandle, SetBufferedStripData, SetCompressionType, SetBufferSize, SetImageInfo, SetColumns, SetRows,
   SetXOff, SetYOff, SetBytesWritten, SetBytesPerRow })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

void JavaBufferedStripInfo::setBufferHandle(HANDLE handle)
{
    callVoidMethod(getFunctionName(SetBufferHandle), reinterpret_cast<jlong>(handle));
}

HANDLE JavaBufferedStripInfo::getBufferHandle()
{
    return reinterpret_cast<HANDLE>(callLongMethod(getFunctionName(GetBufferHandle)));
}

int JavaBufferedStripInfo::getBufferSize()
{
    return callIntMethod(getFunctionName(GetBufferSize));
}

int JavaBufferedStripInfo::getPreferredSize()
{
    return callIntMethod(getFunctionName(GetPreferredSize));
}

bool JavaBufferedStripInfo::isAppAllocatesBuffer()
{
    return callBooleanMethod(getFunctionName(IsAppAllocatesBuffer));
}

void JavaBufferedStripInfo::setBufferedStripData(LPBYTE pBytes, LONG size)
{
    callVoidMethod(getFunctionName(SetBufferedStripData),
                    CreateJArrayFromCArray<JavaByteArrayTraits<char> >(m_pJavaEnv, reinterpret_cast<char*>(pBytes), size));
}

void JavaBufferedStripInfo::setImageInfo(jobject jImageInfo)
{
    callVoidMethod(getFunctionName(SetImageInfo), jImageInfo);
}

void JavaBufferedStripInfo::setBufferStripInfo(LONG columns, LONG rows, LONG xOffset, LONG yOffset, LONG bytesWritten, LONG BytesPerRow)
{
    const LONG vals[] = { columns, rows, xOffset, yOffset, bytesWritten, BytesPerRow };
    const char* fnNames[] = {SetColumns, SetRows, SetXOff, SetYOff, SetBytesWritten, SetBytesPerRow};
    int j = 0;
    for (size_t i = 0; i < std::size(fnNames); ++i)
        callVoidMethod(getFunctionName(fnNames[i]), vals[i]);
}

void JavaBufferedStripInfo::setBufferSize(LONG size)
{
    callVoidMethod(getFunctionName(SetBufferSize), size);
}

jobject JavaBufferedStripInfo::createFullObject(LONG prefSize, LONG minimumSiz, LONG maximumSiz)
{
    return constructObject(1, prefSize, minimumSiz, maximumSiz);
}


////////////////////////////////////////////////////////////////////////////////////////
JavaBufferedTileInfo::JavaBufferedTileInfo(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "BufferedTileInfo",
        { SetInfo, SetTileData })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaBufferedTileInfo::createFullObject(TW_IMAGEMEMXFER memXFer)
{
    JavaDTwainLowLevel_TW_IMAGEMEMXFER memXFerTo(m_pJavaEnv);
    memXFerTo.setValue(memXFer);
    jobject javaMemXFer = memXFerTo.getObject();
    callVoidMethod(getFunctionName(SetInfo), javaMemXFer);
    if (memXFer.Memory.TheMem)
    {
        HandleRAII byteData(memXFer.Memory.TheMem);
        callVoidMethod(getFunctionName(SetTileData), 
            CreateJArrayFromCArray<JavaByteArrayTraits<char> >(m_pJavaEnv, reinterpret_cast<char*>(byteData.getData()), memXFer.Memory.Length));
    }
    return getObject();
}

///////////////////////////////////////////////////////////////////////////////////////

JavaFrameInfo::JavaFrameInfo(JNIEnv *pEnv) :
    JavaObjectCaller(pEnv, JavaFunctionNameMapInstance::getFunctionMap(), "TwainFrame",
            {SetLeft, SetTop, SetBottom, SetRight, GetLeft, GetTop, GetRight, GetBottom})
{
    RegisterMemberFunctions(*this, getObjectName());
    setObject(defaultConstructObject());
}

jobject JavaFrameInfo::CreateJFrameObject()
{
    setObject(defaultConstructObject());
    return getObject();
}

jobject JavaFrameInfo::createDefaultObject()
{
    setObject(defaultConstructObject());
    return getObject();
}

jobjectArray JavaFrameInfo::CreateJFrameObjectArray(jsize numElements)
{
    return m_pJavaEnv->NewObjectArray(numElements, getClass(), defaultConstructObject());
}

void JavaFrameInfo::setJFrameDimensions(double left, double top, double right, double bottom)
{
    const char* fnName[] = { SetLeft, SetTop, SetRight, SetBottom };
    const double vals[] = { left, top, right, bottom };
    for (int i = 0; i < 4; ++i)
        callVoidMethod(getFunctionName(fnName[i]), vals[i]);
}

void JavaFrameInfo::getJFrameDimensions(double* left, double* top, double* right, double* bottom)
{
    const char* fnName[] = { GetLeft, GetTop, GetRight, GetBottom };
    double* vals[] = { left, top, right, bottom };
    for (int i = 0; i < 4; ++i)
        *(vals[i]) = callDoubleMethod(getFunctionName(fnName[i]));
}

jobject JavaFrameInfo::NativeToJava(FrameStruct& fs)
{
    setObject(createDefaultObject());
    setJFrameDimensions(fs.left, fs.top, fs.right, fs.bottom);
    return getObject();
}

FrameStruct JavaFrameInfo::JavaToNative()
{
    FrameStruct fs;
    getJFrameDimensions(&fs.left, &fs.top, &fs.right, &fs.bottom);
    return fs;
}

////////////////////////////////////////////////////////////////////////////////////////////
JavaDTwainVersionInfo::JavaDTwainVersionInfo(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "DTwainVersionInfo",
{SetMajorVersion, SetMinorVersion, SetExePath,
 SetLongName, SetShortName, SetPatchVersion, SetVersionType})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainVersionInfo::createDefaultObject()
{
    setObject(defaultConstructObject());
    return getObject();
}

jobject JavaDTwainVersionInfo::createFullObject(LONG var1, LONG var2, LONG var3, LONG var4, jstring exePath)
{
    return constructObject(1, var1, var2, var3, var4, exePath);
}

void JavaDTwainVersionInfo::setMajorVersion(int32_t majorVersion)
{
    callVoidMethod(getFunctionName(SetMajorVersion), majorVersion);
}

void JavaDTwainVersionInfo::setMinorVersion(int32_t minorVersion)
{
    callVoidMethod(getFunctionName(SetMinorVersion), minorVersion);
}

void JavaDTwainVersionInfo::setVersionType(int32_t versionType)
{
    callVoidMethod(getFunctionName(SetVersionType), versionType);
}

void JavaDTwainVersionInfo::setPatchVersion(int32_t patchVersion)
{
    callVoidMethod(getFunctionName(SetPatchVersion), patchVersion);
}

void JavaDTwainVersionInfo::setExePath(LPCTSTR str)
{
    callVoidMethod(getFunctionName(SetExePath), CreateJStringFromCString(m_pJavaEnv, str));
}

void JavaDTwainVersionInfo::setLongName(LPCTSTR str)
{
    callVoidMethod(getFunctionName(SetLongName), CreateJStringFromCString(m_pJavaEnv, str));
}

void JavaDTwainVersionInfo::setShortName(LPCTSTR str)
{
    callVoidMethod(getFunctionName(SetShortName), CreateJStringFromCString(m_pJavaEnv, str));
}

void JavaDTwainVersionInfo::setVersionCopyright(LPCTSTR str)
{
    callVoidMethod(getFunctionName(SetVersionCopyright), CreateJStringFromCString(m_pJavaEnv, str));
}

/////////////////////////////////////////////////////////////////////////////////////////
JavaTwainAcquireArea::JavaTwainAcquireArea(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainAcquireArea",
                    { Left, Top, Right, Bottom, GetUnitMeasure })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaTwainAcquireArea::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaTwainAcquireArea::createFullObject(double var1, double var2, double var3, double var4, int var5)
{
    return constructObject(2, var1, var2, var3, var4, var5);
}

double JavaTwainAcquireArea::getLeft()
{
    return callDoubleMethod(getFunctionName(Left));
}

double JavaTwainAcquireArea::getTop()
{
    return callDoubleMethod(getFunctionName(Top));
}

double JavaTwainAcquireArea::getRight()
{
    return callDoubleMethod(getFunctionName(Right));
}

double JavaTwainAcquireArea::getBottom()
{
    return callDoubleMethod(getFunctionName(Bottom));
}

int JavaTwainAcquireArea::getUnitOfMeasure()
{
    return callIntMethod(getFunctionName(GetUnitMeasure));
}
//////////////////////////////////////////////////////////////////////////////////////////////
JavaTwainImageInfo::JavaTwainImageInfo(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainImageInfo",
                    {GetImageInfo, SetImageInfo}), proxy_imageinfo(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaTwainImageInfo::createDefaultObject()
{
    return defaultConstructObject();
}

TW_IMAGEINFO JavaTwainImageInfo::getImageInfo()
{
    return getProxyData(proxy_imageinfo, getFunctionName(GetImageInfo).c_str());
}

void JavaTwainImageInfo::setImageInfo(TW_IMAGEINFO iInfo)
{
    setProxyData(proxy_imageinfo, getFunctionName(SetImageInfo).c_str(), iInfo);
}

jobject JavaTwainImageInfo::NativeToJava(TW_IMAGEINFO iInfo)
{
    setObject(createDefaultObject());
    setImageInfo(iInfo);
    return getObject();
}
/////////////////////////////////////////////////////////

jobject JavaDTwainSourceInfo::createFullObject(LPCTSTR arg1, LPCTSTR arg2, LPCTSTR arg3, LPCTSTR arg4, int arg5, int arg6)
{
    return constructObject(1,
                           CreateJStringFromCString(m_pJavaEnv, arg1),
                           CreateJStringFromCString(m_pJavaEnv, arg2),
                           CreateJStringFromCString(m_pJavaEnv, arg3),
                           CreateJStringFromCString(m_pJavaEnv, arg4),
                           arg5, arg6);
}

JavaTwainAcquisitionArray::JavaTwainAcquisitionArray(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainAcquisitionArray",
{AddData, SetStatus})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

void JavaTwainAcquisitionArray::addData(jobject acquisition)
{
    callVoidMethod(getFunctionName(AddData), acquisition);
}

//////////////////////////////////////////////////////////////////////////////////////////////////
JavaTwainAcquisitionData::JavaTwainAcquisitionData(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainAcquisitionData",
    {AddImageData})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

void JavaTwainAcquisitionData::addImageData(jobject ImageObject)
{
    callVoidMethod(getFunctionName(AddImageData), ImageObject);
}
///////////////////////////////////////////////////////////////
JavaExtendedImageInfo::JavaExtendedImageInfo(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "ExtendedImageInfo",
     {SetBarcodeInfo,SetShadedAreaDetectionInfo, SetSpeckleRemovalInfo, SetHorizontalLineDetectionInfo, SetVerticalLineDetectionInfo,
      SetPatchcodeDetectionInfo, SetSkewDetectionInfo, SetEndorsedTextInfo, SetFormsRecognitionInfo, SetPageSourceInfo,
      SetImageSegmentationInfo, SetExtendedImageInfo20, SetExtendedImageInfo21, SetSupportedExtendedImageInfo,

     GetBarcodeInfo,GetShadedAreaDetectionInfo, GetSpeckleRemovalInfo, GetHorizontalLineDetectionInfo, GetVerticalLineDetectionInfo,
     GetPatchcodeDetectionInfo, GetSkewDetectionInfo, GetEndorsedTextInfo, GetFormsRecognitionInfo, GetPageSourceInfo,
     GetImageSegmentationInfo, GetExtendedImageInfo20, GetExtendedImageInfo21, GetExtendedImageInfo22, GetExtendedImageInfo23, GetExtendedImageInfo24,
     GetExtendedImageInfo25 }),

            proxy_barcodeinfo(env),
            proxy_shadedareainfo(env),
            proxy_speckleremovalinfo(env),
            proxy_hlinedetectioninfo(env),
            proxy_vlinedetectioninfo(env),
            proxy_patchcodedetioninfo(env),
            proxy_skewdetectioninfo(env),
            proxy_endorsedtextinfo(env),
            proxy_formsdefinitioninfo(env),
            proxy_pagesourceinfo(env),
            proxy_imagesegmentationinfo(env),
            proxy_extendedimageinfo20(env),
            proxy_extendedimageinfo21(env),
            proxy_extendedimageinfo22(env),
            proxy_extendedimageinfo23(env),
            proxy_extendedimageinfo24(env),
            proxy_extendedimageinfo25(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

void JavaExtendedImageInfo::setExtendedImageInfoTypes(std::vector<LONG>& vInfos)
{
    // Create a java object array of items from the buffer
    auto jArray = CreateJArrayFromCArray<JavaIntArrayTraits>(m_pJavaEnv, &vInfos[0], vInfos.size());
    callVoidMethod(getFunctionName(SetSupportedExtendedImageInfo), jArray);
}

void JavaExtendedImageInfo::setAllBarcodeInfo(ExtendedImageInfo_BarcodeNative& info)
{
    setBarcodeInfoCount(info.m_vBarInfos.size());
    int i = 0;
    for (auto& oneBarInfo : info.m_vBarInfos)
    {
        ExtendedImageInfo_BarcodeInfoNative bcNative;
        bcNative.xCoordinate = oneBarInfo.xCoordinate;
        bcNative.yCoordinate = oneBarInfo.yCoordinate;
        bcNative.rotation = oneBarInfo.rotation;
        bcNative.confidence = oneBarInfo.confidence;
        bcNative.type = oneBarInfo.type;
        bcNative.text = oneBarInfo.text;
        setBarcodeInfo(bcNative, i);
        ++i;
    }
}

void JavaExtendedImageInfo::setAllFormsRecognitionInfo(ExtendedImageInfo_FormsRecognitionNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetFormsRecognitionInfo));
    proxy_formsdefinitioninfo.setObject(sObject);
    proxy_formsdefinitioninfo.setConfidence(info.m_vConfidence);
    proxy_formsdefinitioninfo.setTemplateMatch(info.m_vTemplateMatch);
    proxy_formsdefinitioninfo.setTemplatePageMatch(info.m_vTemplatePageMatch);
    proxy_formsdefinitioninfo.setHorizontalDocOffset(info.m_vHorizontalDocOffset);
    proxy_formsdefinitioninfo.setVerticalDocOffset(info.m_vVerticalDocOffset);
}

void JavaExtendedImageInfo::setAllImageSegmentationInfo(ExtendedImageInfo_ImageSegmentationInfoNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetImageSegmentationInfo));
    proxy_imagesegmentationinfo.setObject(sObject);
    proxy_imagesegmentationinfo.setICCProfile(info.m_sICCProfile);
    proxy_imagesegmentationinfo.setLastSegment(info.m_bLastSegment);
    proxy_imagesegmentationinfo.setSegmentNumber(info.m_segmentNumber);
}

void JavaExtendedImageInfo::setAllEndorsedInfo(ExtendedImageInfo_EndorsedTextInfoNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetEndorsedTextInfo));
    proxy_endorsedtextinfo.setObject(sObject);
    proxy_endorsedtextinfo.setText(info.m_sEndorsedText);
}

void JavaExtendedImageInfo::setAllExtendedImageInfo20(ExtendedImageInfo_ExtendedImageInfo20Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo20));
    proxy_extendedimageinfo20.setObject(sObject);
    proxy_extendedimageinfo20.setMagType(info.m_magType);
}

void JavaExtendedImageInfo::setAllExtendedImageInfo21(ExtendedImageInfo_ExtendedImageInfo21Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo21));
    proxy_extendedimageinfo21.setObject(sObject);
    proxy_extendedimageinfo21.setFileSystemSource(info.m_fileSystemSource);
    proxy_extendedimageinfo21.setImageMerged(info.m_imageMerged);
    proxy_extendedimageinfo21.setMagDataLength(info.m_magDataLength);
    proxy_extendedimageinfo21.setPageSide(info.m_pageSide);
    proxy_extendedimageinfo21.setMagData(
        CreateJArrayFromCArray<JavaByteArrayTraits<char>>(m_pJavaEnv, 
                                                          reinterpret_cast<char*>(info.m_magData.data()), 
                                                          info.m_magData.size()));
}

void JavaExtendedImageInfo::setAllExtendedImageInfo22(ExtendedImageInfo_ExtendedImageInfo22Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo22));
    proxy_extendedimageinfo22.setObject(sObject);
    proxy_extendedimageinfo22.setPaperCount(info.m_PaperCount);
}

void JavaExtendedImageInfo::setAllExtendedImageInfo23(ExtendedImageInfo_ExtendedImageInfo23Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo23));
    proxy_extendedimageinfo23.setObject(sObject);
    proxy_extendedimageinfo23.setPrinterText(info.m_PrinterText);
}

void JavaExtendedImageInfo::setAllExtendedImageInfo24(ExtendedImageInfo_ExtendedImageInfo24Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo24));
    proxy_extendedimageinfo24.setObject(sObject);
    proxy_extendedimageinfo24.setTwainDirectMetaData(info.m_twainDirectMetaData);
}

void JavaExtendedImageInfo::setAllExtendedImageInfo25(ExtendedImageInfo_ExtendedImageInfo25Native& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetExtendedImageInfo25));
    proxy_extendedimageinfo25.setObject(sObject);
    proxy_extendedimageinfo25.setIAFieldA(info.m_ImageAddressing.m_AddressInfo[0]);
    proxy_extendedimageinfo25.setIAFieldB(info.m_ImageAddressing.m_AddressInfo[1]);
    proxy_extendedimageinfo25.setIAFieldC(info.m_ImageAddressing.m_AddressInfo[2]);
    proxy_extendedimageinfo25.setIAFieldD(info.m_ImageAddressing.m_AddressInfo[3]);
    proxy_extendedimageinfo25.setIAFieldE(info.m_ImageAddressing.m_AddressInfo[4]);
    proxy_extendedimageinfo25.setIALevel(info.m_ImageAddressing.m_iaLevel);
    proxy_extendedimageinfo25.setPrinter(info.m_printer);
    for (auto& str : info.m_barcodeText)
        proxy_extendedimageinfo25.addBarcodeText(str);
}

void JavaExtendedImageInfo::setAllPatchCodeInfo(ExtendedImageInfo_PatchCodeNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetPatchcodeDetectionInfo));
    proxy_patchcodedetioninfo.setObject(sObject);
    proxy_patchcodedetioninfo.setPatchcode(info.m_patchCode);
}


void JavaExtendedImageInfo::setBarcodeInfo(ExtendedImageInfo_BarcodeInfoNative& info, int nWhich)
{
    jobject sObject = callObjectMethod(getFunctionName(GetBarcodeInfo));
    proxy_barcodeinfo.setObject(sObject);
    proxy_barcodeinfo.setSingleInfo(sObject,info, nWhich);
}

void JavaExtendedImageInfo::setBarcodeInfoCount(TW_UINT32 nNumCodes)
{
    jobject sObject = callObjectMethod(getFunctionName(GetBarcodeInfo));
    proxy_barcodeinfo.setObject(sObject);
    proxy_barcodeinfo.setCount(nNumCodes);
}

void JavaExtendedImageInfo::setPageSourceInfo(ExtendedImageInfo_PageSourceInfoNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetPageSourceInfo));
    proxy_pagesourceinfo.setObject(sObject);
    proxy_pagesourceinfo.setCamera(info.camera);
    proxy_pagesourceinfo.setBookname(info.bookname);
    proxy_pagesourceinfo.setChapterNumber(info.chapterNumber);
    proxy_pagesourceinfo.setPageNumber(info.pageNumber);
    proxy_pagesourceinfo.setFrameNumber(info.frameNumber);
    proxy_pagesourceinfo.setDocumentNumber(info.documentNumber);
    proxy_pagesourceinfo.setPageSide(info.pageSide);
    proxy_pagesourceinfo.setFrame(info.frame);
    proxy_pagesourceinfo.setPixelFlavor(info.pixelFlavor);
}

void JavaExtendedImageInfo::setSkewDetectionInfo(ExtendedImageInfo_SkewDetectionInfoNative& info)
{
    jobject sObject = callObjectMethod(getFunctionName(GetSkewDetectionInfo));
    proxy_skewdetectioninfo.setObject(sObject);
    proxy_skewdetectioninfo.setConfidence(info.Confidence);
    proxy_skewdetectioninfo.setDeskewStatus(info.DeskewStatus);
    proxy_skewdetectioninfo.setOriginalAngle(info.OriginalAngle);
    proxy_skewdetectioninfo.setFinalAngle(info.FinalAngle);
    proxy_skewdetectioninfo.setWindowX1(info.WindowX1);
    proxy_skewdetectioninfo.setWindowX2(info.WindowX2);
    proxy_skewdetectioninfo.setWindowX3(info.WindowX3);
    proxy_skewdetectioninfo.setWindowX4(info.WindowX4);
    proxy_skewdetectioninfo.setWindowY1(info.WindowY1);
    proxy_skewdetectioninfo.setWindowY2(info.WindowY2);
    proxy_skewdetectioninfo.setWindowY3(info.WindowY3);
    proxy_skewdetectioninfo.setWindowY4(info.WindowY4);
}

void JavaExtendedImageInfo::setShadedAreaDetectionInfo(ExtendedImageInfo_ShadedAreaDetectionInfoNativeV& info)
{
    setShadedAreaInfoCount(info.count);
    for (TW_UINT32 i = 0; i < info.count; ++i)
    {
        ExtendedImageInfo_ShadedAreaDetectionInfoNative sInfo;
        sInfo.left = info.leftV[i];
        sInfo.top = info.topV[i];
        sInfo.width = info.widthV[i];
        sInfo.height = info.heightV[i];
        sInfo.blackCountOld = info.blackCountOldV[i];
        sInfo.blackCountNew = info.blackCountNewV[i];
        sInfo.blackRLMin = info.blackRLMinV[i];
        sInfo.blackRLMax = info.blackRLMaxV[i];
        sInfo.whiteCountOld = info.whiteCountOldV[i];
        sInfo.whiteCountNew = info.whiteCountNewV[i];
        sInfo.whiteRLMin = info.whiteRLMinV[i];
        sInfo.whiteRLMax = info.whiteRLMaxV[i];
        sInfo.whiteRLAvg = info.whiteRLAvgV[i];
        sInfo.size = info.sizeV[i];
        setShadedAreaDetectionInfo(sInfo, i);
    }
}

void JavaExtendedImageInfo::setShadedAreaDetectionInfo(ExtendedImageInfo_ShadedAreaDetectionInfoNative& sInfo, int nWhich)
{
    jobject sObject = callObjectMethod(getFunctionName(GetShadedAreaDetectionInfo));
    proxy_shadedareainfo.setObject(sObject);
    proxy_shadedareainfo.setSingleInfo(sObject, sInfo, nWhich);
}

void JavaExtendedImageInfo::setShadedAreaInfoCount(TW_UINT32 count)
{
    jobject sObject = callObjectMethod(getFunctionName(GetShadedAreaDetectionInfo));
    proxy_shadedareainfo.setObject(sObject);
    proxy_shadedareainfo.setCount(count);
}

void JavaExtendedImageInfo::setSpeckleRemovalInfo(const ExtendedImageInfo_SpeckleRemovalInfoNative& sInfo)
{
    jobject sObject = callObjectMethod(getFunctionName(GetSpeckleRemovalInfo));
    proxy_speckleremovalinfo.setObject(sObject);
    proxy_speckleremovalinfo.NativeToJava(sInfo);
}

void JavaExtendedImageInfo::setAllHorizontalLineInfo(const ExtendedImageInfo_LineDetectionNative& sInfo)
{
    jobject sObject = callObjectMethod(getFunctionName(GetHorizontalLineDetectionInfo));
    proxy_hlinedetectioninfo.setObject(sObject);
    proxy_hlinedetectioninfo.setCount(sInfo.m_vLineInfo.size());
    int i = 0;
    for (auto& oneInfo : sInfo.m_vLineInfo)
    {
        proxy_hlinedetectioninfo.setSingleInfo(sObject, oneInfo, i, 0);
        ++i;
    }
}

void JavaExtendedImageInfo::setAllVerticalLineInfo(const ExtendedImageInfo_LineDetectionNative& sInfo)
{
    jobject sObject = callObjectMethod(getFunctionName(GetVerticalLineDetectionInfo));
    proxy_vlinedetectioninfo.setObject(sObject);
    proxy_vlinedetectioninfo.setCount(sInfo.m_vLineInfo.size());
    int i = 0;
    for (auto& oneInfo : sInfo.m_vLineInfo)
    {
        proxy_vlinedetectioninfo.setSingleInfo(sObject, oneInfo, i, 1);
        ++i;
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_BarcodeInfo_SingleInfo::JavaExtendedImageInfo_BarcodeInfo_SingleInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_BarcodeInfo_SingleInfo",
    { SetConfidence, SetRotation, SetTextLength,SetText, SetX, SetY, SetType }), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setConfidence(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetConfidence).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setRotation(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetRotation).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setTextLength(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetTextLength).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setText(std::string val)
{
    const jstring str = CreateJStringFromCStringA(m_pJavaEnv, val.c_str());
    callObjectMethod(getFunctionName(SetText), str);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setX(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetX).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setY(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetY).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::setType(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetType).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo_SingleInfo::NativeToJava(ExtendedImageInfo_BarcodeInfoNative& val)
{
    setConfidence(val.confidence);
    setTextLength(val.length);
    setRotation(val.rotation);
    setType(val.type);
    setX(val.xCoordinate);
    setY(val.yCoordinate);
    setText(val.text);
}

///////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_BarcodeInfo::JavaExtendedImageInfo_BarcodeInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_BarcodeInfo",
        {SetCount, SetSingleInfo}), proxy_uint32(env), proxy_singleinfo(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_BarcodeInfo::setCount(TW_UINT32 val)
{
    setProxyData(proxy_uint32,  getFunctionName(SetCount).c_str(), val);
}

void JavaExtendedImageInfo_BarcodeInfo::setSingleInfo(jobject objParent, ExtendedImageInfo_BarcodeInfoNative& info, int nWhich)
{
    // Get the BarCodeInfo object
    proxy_singleinfo.constructObject(1, objParent);
    proxy_singleinfo.NativeToJava(info);
    callObjectMethod(getFunctionName(SetSingleInfo), proxy_singleinfo.getObject(), nWhich);
}

////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo",
    { SetTop, SetLeft, SetHeight, SetWidth, SetSize, SetBlackCountOld, SetBlackCountNew, SetBlackRLMin,
        SetBlackRLMax, SetWhiteCountOld, SetWhiteCountNew, SetWhiteRLMin, SetWhiteRLMax, SetWhiteRLAvg}), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setTop(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetTop).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setLeft(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetLeft).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setHeight(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetHeight).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWidth(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWidth).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setSize(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetSize).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setBlackCountOld(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetBlackCountOld).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setBlackCountNew(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetBlackCountNew).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setBlackRLMin(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetBlackRLMin).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setBlackRLMax(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetBlackRLMax).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWhiteCountOld(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteCountOld).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWhiteCountNew(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteCountNew).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWhiteRLMin(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteRLMin).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWhiteRLMax(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteRLMax).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::setWhiteRLAvg(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteRLAvg).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo_SingleInfo::NativeToJava(const ExtendedImageInfo_ShadedAreaDetectionInfoNative& val)
{
    setBlackCountNew(val.blackCountNew);
    setBlackCountOld(val.blackCountOld);
    setBlackRLMin(val.blackRLMin);
    setBlackRLMax(val.blackRLMax);
    setWhiteCountNew(val.whiteCountNew);
    setWhiteCountOld(val.whiteCountOld);
    setWhiteRLMin(val.whiteRLMin);
    setWhiteRLMax(val.whiteRLMax);
    setWhiteRLAvg(val.whiteRLAvg);
    setLeft(val.left);
    setSize(val.size);
    setTop(val.top);
    setHeight(val.height);
    setWidth(val.width);
}
/////////////////////////////////////////////////////////
JavaExtendedImageInfo_ShadedAreaDetectionInfo::JavaExtendedImageInfo_ShadedAreaDetectionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ShadedAreaDetectionInfo",
    { SetCount, SetSingleInfo }), proxy_uint32(env), proxy_singleinfo(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo::setCount(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetCount).c_str(), val);
}

void JavaExtendedImageInfo_ShadedAreaDetectionInfo::setSingleInfo(jobject objParent, ExtendedImageInfo_ShadedAreaDetectionInfoNative& info, int nWhich)
{
    proxy_singleinfo.constructObject(1, objParent);
    proxy_singleinfo.NativeToJava(info);
    callObjectMethod(getFunctionName(SetSingleInfo), proxy_singleinfo.getObject(), nWhich);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_SpeckleRemovalInfo::JavaExtendedImageInfo_SpeckleRemovalInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_SpeckleRemovalInfo",
    { SetSpecklesRemoved, SetWhiteSpecklesRemoved, SetBlackSpecklesRemoved }),
    proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_SpeckleRemovalInfo::setSpecklesRemoved(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetSpecklesRemoved).c_str(), val);
}

void JavaExtendedImageInfo_SpeckleRemovalInfo::setWhiteSpecklesRemoved(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWhiteSpecklesRemoved).c_str(), val);
}

void JavaExtendedImageInfo_SpeckleRemovalInfo::setBlackSpecklesRemoved(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetBlackSpecklesRemoved).c_str(), val);
}

void JavaExtendedImageInfo_SpeckleRemovalInfo::NativeToJava(const ExtendedImageInfo_SpeckleRemovalInfoNative& val)
{
    setSpecklesRemoved(val.specklesRemoved);
    setWhiteSpecklesRemoved(val.whiteSpecklesRemoved);
    setBlackSpecklesRemoved(val.blackSpecklesRemoved);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_LineSingleInfo::JavaExtendedImageInfo_LineSingleInfo(JNIEnv* env, std::string orientation) 
    : JavaExtendedImageInfo_ParentClass<JavaExtendedImageInfo>(env, "ExtendedImageInfo_" + orientation + "LineDetectionInfo_SingleInfo",
        { SetXCoordinate, SetYCoordinate, SetLength, SetThickness}), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_LineSingleInfo::setXCoordinate(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetXCoordinate).c_str(), val);
}

void JavaExtendedImageInfo_LineSingleInfo::setYCoordinate(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetYCoordinate).c_str(), val);
}

void JavaExtendedImageInfo_LineSingleInfo::setLength(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetLength).c_str(), val);
}

void JavaExtendedImageInfo_LineSingleInfo::setThickness(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetThickness).c_str(), val);
}

void JavaExtendedImageInfo_LineSingleInfo::NativeToJava(const ExtendedImageInfo_LineDetectionInfoNative& info)
{
    setXCoordinate(info.xCoordinate);
    setYCoordinate(info.yCoordinate);
    setThickness(info.thickness);
    setLength(info.length);
}

JavaExtendedImageInfo_LineDetectionInfo::JavaExtendedImageInfo_LineDetectionInfo(JNIEnv* env, std::string orientation) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_"+orientation+"LineDetectionInfo",
    {SetCount, SetSingleInfo}), proxy_uint32(env), proxy_hlineInfo(env), proxy_vlineInfo(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_LineDetectionInfo::setCount(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetCount).c_str(), val);
}

void JavaExtendedImageInfo_LineDetectionInfo::setSingleInfo(jobject objParent, const ExtendedImageInfo_LineDetectionInfoNative& info, int nWhich, int nWhichProxy)
{
    if (nWhichProxy == 0) // Horizontal
    {
        proxy_hlineInfo.constructObject(1, objParent);
        proxy_hlineInfo.NativeToJava(info);
        callObjectMethod(getFunctionName(SetSingleInfo), proxy_hlineInfo.getObject(), nWhich);
    }
    else
    {
        proxy_vlineInfo.constructObject(1, objParent);
        proxy_vlineInfo.NativeToJava(info);
        callObjectMethod(getFunctionName(SetSingleInfo), proxy_vlineInfo.getObject(), nWhich);
    }
}

JavaExtendedImageInfo_HorizontalLineDetectionInfo::JavaExtendedImageInfo_HorizontalLineDetectionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_LineDetectionInfo(env, "Horizontal") {}

JavaExtendedImageInfo_VerticalLineDetectionInfo::JavaExtendedImageInfo_VerticalLineDetectionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_LineDetectionInfo(env, "Vertical") {}

JavaExtendedImageInfo_HorizontalLineDetectionInfo_SingleInfo::JavaExtendedImageInfo_HorizontalLineDetectionInfo_SingleInfo(JNIEnv* env) :
    JavaExtendedImageInfo_LineSingleInfo(env, "Horizontal") {}

JavaExtendedImageInfo_VerticalLineDetectionInfo_SingleInfo::JavaExtendedImageInfo_VerticalLineDetectionInfo_SingleInfo(JNIEnv* env) :
    JavaExtendedImageInfo_LineSingleInfo(env, "Vertical") {}


//////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_PatchcodeDetectionInfo::JavaExtendedImageInfo_PatchcodeDetectionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_PatchcodeDetectionInfo",
                     { SetPatchcode }), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_PatchcodeDetectionInfo::setPatchcode(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetPatchcode).c_str(), val);
}

//////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_SkewDetectionInfo::JavaExtendedImageInfo_SkewDetectionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_SkewDetectionInfo",
                     {SetDeskewStatus,SetOriginalAngle, SetFinalAngle,SetConfidence,SetWindowX1,
                      SetWindowX2,SetWindowX3,SetWindowX4,SetWindowY1,SetWindowY2,SetWindowY3,
                      SetWindowY4}), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_SkewDetectionInfo::setDeskewStatus(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetDeskewStatus).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setOriginalAngle(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetOriginalAngle).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setFinalAngle(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetFinalAngle).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setConfidence(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetConfidence).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowX1(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowX1).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowX2(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowX2).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowX3(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowX3).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowX4(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowX4).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowY1(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowY1).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowY2(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowY2).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowY3(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowY3).c_str(), val);
}

void JavaExtendedImageInfo_SkewDetectionInfo::setWindowY4(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetWindowY4).c_str(), val);
}
//////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_EndorsedTextInfo::JavaExtendedImageInfo_EndorsedTextInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_EndorsedTextInfo", { SetText }), strproxy(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_EndorsedTextInfo::setText(const TW_STR255 val)
{
    setProxyData(strproxy, getFunctionName(SetText).c_str(), val);
}

/////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_FormsRecognitionInfo::JavaExtendedImageInfo_FormsRecognitionInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_FormsRecognitionInfo",
                     {SetConfidence, SetTemplateMatch, SetTemplatePageMatch, SetHorizontalDocOffset, SetVerticalDocOffset})
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setConfidence(std::vector<TW_UINT32>& val)
{
    CallArrayImpl<TW_UINT32, ArrayList_TWUINT32>(getFunctionName(SetConfidence), val);
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setTemplateMatch(std::vector<std::string>& val)
{
    std::vector<TW_STR255> vTemp(val.size());
    size_t i = 0;
    for (auto& s : val)
    {
        char *ptr = vTemp[i];
        std::copy(s.begin(), s.end(), ptr);
        ++i;
    }

    CallArrayImpl<TW_STR255, ArrayList_TWSTR255>(getFunctionName(SetTemplateMatch), vTemp);
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setTemplatePageMatch(std::vector<TW_UINT32>& val)
{
    CallArrayImpl<TW_UINT32, ArrayList_TWUINT32>(getFunctionName(SetTemplatePageMatch), val);
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setHorizontalDocOffset(std::vector<TW_UINT32>& val)
{
    CallArrayImpl<TW_UINT32, ArrayList_TWUINT32>(getFunctionName(SetHorizontalDocOffset), val);
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setVerticalDocOffset(std::vector<TW_UINT32>& val)
{
    CallArrayImpl<TW_UINT32, ArrayList_TWUINT32>(getFunctionName(SetVerticalDocOffset), val);
}

void JavaExtendedImageInfo_FormsRecognitionInfo::setAllValues(ExtendedImageInfo_FormsRecognitionNative& info)
{
    setTemplateMatch(info.m_vTemplateMatch);
    setConfidence(info.m_vConfidence);
    setTemplatePageMatch(info.m_vTemplatePageMatch);
    setHorizontalDocOffset(info.m_vHorizontalDocOffset);
    setVerticalDocOffset(info.m_vVerticalDocOffset);
}

//////////////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_PageSourceInfo::JavaExtendedImageInfo_PageSourceInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_PageSourceInfo",
                     { SetBookname, SetChapterNumber, SetDocumentNumber,
                         SetPageNumber, SetCamera, SetFrameNumber, SetFrame, SetPixelFlavor, SetPageSide }),
                        proxy_uint32(env), proxy_uint16(env), proxy_str255(env), proxy_frame(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_PageSourceInfo::setBookname(TW_STR255 val)
{
    setProxyData(proxy_str255, getFunctionName(SetBookname).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setChapterNumber(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetChapterNumber).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setDocumentNumber(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetDocumentNumber).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setPageNumber(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetPageNumber).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setCamera(TW_STR255 val)
{
    setProxyData(proxy_str255, getFunctionName(SetCamera).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setFrameNumber(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetFrameNumber).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setFrame(TW_FRAME val)
{
    setProxyData(proxy_frame, getFunctionName(SetFrame).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setPixelFlavor(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetPixelFlavor).c_str(), val);
}

void JavaExtendedImageInfo_PageSourceInfo::setPageSide(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetPageSide).c_str(), val);
}

///////////////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ImageSegmentationInfo::JavaExtendedImageInfo_ImageSegmentationInfo(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ImageSegmentationInfo",
                     { SetICCProfile, SetLastSegment, SetSegmentNumber }),
                        proxy_bool(env), proxy_uint32(env), proxy_str255(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ImageSegmentationInfo::setICCProfile(TW_STR255 val)
{
    setProxyData(proxy_str255, getFunctionName(SetICCProfile).c_str(), val);
}

void JavaExtendedImageInfo_ImageSegmentationInfo::setLastSegment(TW_BOOL val)
{
    setProxyData(proxy_bool, getFunctionName(SetLastSegment).c_str(), val);
}

void JavaExtendedImageInfo_ImageSegmentationInfo::setSegmentNumber(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetSegmentNumber).c_str(), val);
}

/////////////////////////////////////////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo20::JavaExtendedImageInfo_ExtendedImageInfo20(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo20",
                     { SetMagType }), proxy_uint16(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo20::setMagType(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetMagType).c_str(), val);
}
////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo21::JavaExtendedImageInfo_ExtendedImageInfo21(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo21",
        { SetFileSystemSource, SetImageMerged, SetPageSide, SetMagData, SetMagDataLength}), 
        proxy_uint16(env), proxy_uint32(env), proxy_str255(env), proxy_bool(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo21::setFileSystemSource(TW_STR255 val)
{
    setProxyData(proxy_str255, getFunctionName(SetFileSystemSource).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo21::setImageMerged(TW_BOOL val)
{
    setProxyData(proxy_bool, getFunctionName(SetImageMerged).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo21::setPageSide(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetPageSide).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo21::setMagDataLength(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetMagDataLength).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo21::setMagData(jbyteArray magData)
{
    callVoidMethod(getFunctionName(SetMagData), magData);
}
////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo22::JavaExtendedImageInfo_ExtendedImageInfo22(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo22",
        { SetPaperCount }), proxy_uint32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo22::setPaperCount(TW_UINT32 val)
{
    setProxyData(proxy_uint32, getFunctionName(SetPaperCount).c_str(), val);
}
////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo23::JavaExtendedImageInfo_ExtendedImageInfo23(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo23", { SetPrinterText }), proxy_str255(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo23::setPrinterText(TW_STR255 val)
{
    setProxyData(proxy_str255, getFunctionName(SetPrinterText).c_str(), val);
}
////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo24::JavaExtendedImageInfo_ExtendedImageInfo24(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo24", { SetTwainDirectMetaData })
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo24::setTwainDirectMetaData(std::string sMetaData)
{
    callVoidMethod(getFunctionName(SetTwainDirectMetaData), CreateJStringFromCStringA(m_pJavaEnv, sMetaData.c_str()));
}
////////////////////////////////////////////////////////////
JavaExtendedImageInfo_ExtendedImageInfo25::JavaExtendedImageInfo_ExtendedImageInfo25(JNIEnv* env) :
    JavaExtendedImageInfo_ParentClass(env, "ExtendedImageInfo_ExtendedImageInfo25", 
        { SetIAFieldA, SetIAFieldB, SetIAFieldC, SetIAFieldD, SetIAFieldE, SetIALevel, SetPrinter, AddBarcodeText }),
        proxy_str32(env), proxy_uint16(env)
{
    RegisterMemberFunctions(*this, getObjectName());
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIAFieldA(TW_STR32 val)
{
    setProxyData(proxy_str32, getFunctionName(SetIAFieldA).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIAFieldB(TW_STR32 val)
{
    setProxyData(proxy_str32, getFunctionName(SetIAFieldB).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIAFieldC(TW_STR32 val)
{
    setProxyData(proxy_str32, getFunctionName(SetIAFieldC).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIAFieldD(TW_STR32 val)
{
    setProxyData(proxy_str32, getFunctionName(SetIAFieldD).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIAFieldE(TW_STR32 val)
{
    setProxyData(proxy_str32, getFunctionName(SetIAFieldE).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setIALevel(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetIALevel).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::setPrinter(TW_UINT16 val)
{
    setProxyData(proxy_uint16, getFunctionName(SetPrinter).c_str(), val);
}

void JavaExtendedImageInfo_ExtendedImageInfo25::addBarcodeText(std::string text)
{
    const jstring str = CreateJStringFromCStringA(m_pJavaEnv, text.c_str());
    callObjectMethod(getFunctionName(AddBarcodeText), str);
}

/////////////////////////////////////////////////////////////////////////////////////////////////
JavaTwainImageData::JavaTwainImageData(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainImageData",
    {SetImageData, SetDibHandle})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

void JavaTwainImageData::setImageData(jbyteArray imageData)
{
    callVoidMethod(getFunctionName(SetImageData), imageData);
}

void JavaTwainImageData::setDibHandle(HANDLE handle)
{
    callVoidMethod(getFunctionName(SetDibHandle), handle);
}

/////////////////////////////////////////////////////////////////////////////////
JavaTwainOCRInfo::JavaTwainOCRInfo(JNIEnv *env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainOCRInfo", {})
{
    DTWAINJNIGlobals::RegisterJavaFunctionInterface(this, getFunctionMap());
}

jobject JavaTwainOCRInfo::createFullObject(LPCTSTR arg1, LPCTSTR arg2, LPCTSTR arg3, LPCTSTR arg4)
{
    return constructObject(1,
                           CreateJStringFromCString(m_pJavaEnv, arg1),
                           CreateJStringFromCString(m_pJavaEnv, arg2),
                           CreateJStringFromCString(m_pJavaEnv, arg3),
                           CreateJStringFromCString(m_pJavaEnv, arg4));
}
/////////////////////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_FRAME::JavaDTwainLowLevel_TW_FRAME(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_FRAME",
        {SetTop, GetTop, SetBottom, GetBottom, SetRight, GetRight, SetLeft, GetLeft}), proxy_fix32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FRAME::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_FRAME::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_FIX32 JavaDTwainLowLevel_TW_FRAME::getLeft()
{
    return getProxyData(proxy_fix32, getFunctionName(GetLeft).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_FRAME::getTop()
{
    return getProxyData(proxy_fix32, getFunctionName(GetTop).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_FRAME::getRight()
{
    return getProxyData(proxy_fix32, getFunctionName(GetRight).c_str());
}

TW_FIX32 JavaDTwainLowLevel_TW_FRAME::getBottom()
{
    return getProxyData(proxy_fix32, getFunctionName(GetBottom).c_str());
}

void JavaDTwainLowLevel_TW_FRAME::setLeft(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetLeft).c_str(), val);
}

void JavaDTwainLowLevel_TW_FRAME::setTop(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetTop).c_str(), val);
}

void JavaDTwainLowLevel_TW_FRAME::setRight(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetRight).c_str(), val);
}

void JavaDTwainLowLevel_TW_FRAME::setBottom(TW_FIX32 val)
{
    setProxyData(proxy_fix32, getFunctionName(SetBottom).c_str(), val);
}

TW_FRAME JavaDTwainLowLevel_TW_FRAME::JavaToNative()
{
    TW_FRAME twframe{};
    twframe.Bottom = getBottom();
    twframe.Top = getTop();
    twframe.Left = getLeft();
    twframe.Right = getRight();
    return twframe;
}

TW_FRAME JavaDTwainLowLevel_TW_FRAME::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_FRAME::setValue(TW_FRAME twframe)
{
    setBottom(twframe.Bottom);
    setLeft(twframe.Left);
    setTop(twframe.Top);
    setRight(twframe.Right);
}
//====================================================================
JavaDTwainLowLevel_TW_IMAGELAYOUT::JavaDTwainLowLevel_TW_IMAGELAYOUT(JNIEnv* env) :
     JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_IMAGELAYOUT",
    {GetFrame, GetDocumentNumber, GetPageNumber, GetFrameNumber, SetFrame,
        SetDocumentNumber, SetPageNumber, SetFrameNumber}), proxy_frame(env), proxy_u32(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGELAYOUT::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGELAYOUT::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_FRAME JavaDTwainLowLevel_TW_IMAGELAYOUT::getFrame()
{
    return getProxyData(proxy_frame, getFunctionName(GetFrame).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGELAYOUT::getDocumentNumber()
{
    return getProxyData(proxy_u32, getFunctionName(GetDocumentNumber).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGELAYOUT::getPageNumber()
{
    return getProxyData(proxy_u32, getFunctionName(GetPageNumber).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGELAYOUT::getFrameNumber()
{
    return getProxyData(proxy_u32, getFunctionName(GetFrameNumber).c_str());
}

void JavaDTwainLowLevel_TW_IMAGELAYOUT::setFrame(TW_FRAME val)
{
    setProxyData(proxy_frame, getFunctionName(SetFrame).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGELAYOUT::setDocumentNumber(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetDocumentNumber).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGELAYOUT::setPageNumber(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetPageNumber).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGELAYOUT::setFrameNumber(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetFrameNumber).c_str(), val);
}

TW_IMAGELAYOUT JavaDTwainLowLevel_TW_IMAGELAYOUT::JavaToNative()
{
    TW_IMAGELAYOUT twimagelayout{};
    twimagelayout.DocumentNumber = getDocumentNumber();
    twimagelayout.Frame = getFrame();
    twimagelayout.FrameNumber = getFrameNumber();
    twimagelayout.PageNumber = getPageNumber();
    return twimagelayout;
}

jobject JavaDTwainLowLevel_TW_IMAGELAYOUT::NativeToJava(const TW_IMAGELAYOUT& twimagelayout)
{
    setObject(createDefaultObject());
    setDocumentNumber(twimagelayout.DocumentNumber);
    setFrame(twimagelayout.Frame);
    setFrameNumber(twimagelayout.FrameNumber);
    setPageNumber(twimagelayout.PageNumber);
    return getObject();
}

TW_IMAGELAYOUT JavaDTwainLowLevel_TW_IMAGELAYOUT::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_IMAGELAYOUT::setValue(const TW_IMAGELAYOUT& twimagelayout)
{
    NativeToJava(twimagelayout);
}
//////////////////////////////////////////////////////////////////////////////////
// =========================================================================
JavaDTwainLowLevel_TW_MEMORY::JavaDTwainLowLevel_TW_MEMORY(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_MEMORY",
    {GetFlags, GetLength, GetMem, SetFlags, SetLength, SetMem}), proxy_u32(env), proxy_memref(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_MEMORY::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_MEMORY::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_MEMORY::getFlags()
{
    return getProxyData(proxy_u32, getFunctionName(GetFlags).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_MEMORY::getLength()
{
    return getProxyData(proxy_u32, getFunctionName(GetLength).c_str());
}

TW_MEMREF JavaDTwainLowLevel_TW_MEMORY::getTheMem()
{
    return getProxyData(proxy_memref, getFunctionName(GetMem).c_str());
}

void JavaDTwainLowLevel_TW_MEMORY::setFlags(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetFlags).c_str(), val);
}

void JavaDTwainLowLevel_TW_MEMORY::setLength(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetLength).c_str(), val);
}

void JavaDTwainLowLevel_TW_MEMORY::setTheMem(TW_MEMREF val)
{
    setProxyData(proxy_memref, getFunctionName(SetMem).c_str(), val);
}

TW_MEMORY JavaDTwainLowLevel_TW_MEMORY::JavaToNative()
{
    TW_MEMORY twmemory{};
    twmemory.Flags = getFlags();
    twmemory.Length = getLength();
    twmemory.TheMem = getTheMem();
    return twmemory;
}

jobject JavaDTwainLowLevel_TW_MEMORY::NativeToJava(const TW_MEMORY& twmemory)
{
    setObject(createDefaultObject());
    setFlags(twmemory.Flags);
    setLength(twmemory.Length);
    setTheMem(twmemory.TheMem);
    return getObject();
}

TW_MEMORY JavaDTwainLowLevel_TW_MEMORY::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_MEMORY::setValue(TW_MEMORY twmemory)
{
    NativeToJava(twmemory);
}

//===========================================================================================================
JavaDTwainLowLevel_TW_IMAGEMEMXFER::JavaDTwainLowLevel_TW_IMAGEMEMXFER(JNIEnv* env):
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_IMAGEMEMXFER",
{
    GetCompression, GetBytesPerRow, GetColumns, GetRows, GetXOffset, GetYOffset, GetBytesWritten, GetMemory,
    SetCompression, SetBytesPerRow, SetColumns, SetRows, SetXOffset, SetYOffset, SetBytesWritten, SetMemory
  }), proxy_u16(env), proxy_u32(env), proxy_memory(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGEMEMXFER::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_IMAGEMEMXFER::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getCompression()
{
    return getProxyData(proxy_u16, getFunctionName(GetCompression).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getBytesPerRow()
{
    return getProxyData(proxy_u32, getFunctionName(GetBytesPerRow).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getColumns()
{
    return getProxyData(proxy_u32, getFunctionName(GetColumns).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getRows()
{
    return getProxyData(proxy_u32, getFunctionName(GetRows).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getXOffset()
{
    return getProxyData(proxy_u32, getFunctionName(GetXOffset).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getYOffset()
{
    return getProxyData(proxy_u32, getFunctionName(GetYOffset).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_IMAGEMEMXFER::getBytesWritten()
{
    return getProxyData(proxy_u32, getFunctionName(GetBytesWritten).c_str());
}

TW_MEMORY JavaDTwainLowLevel_TW_IMAGEMEMXFER::getMemory()
{
    return getProxyData(proxy_memory, getFunctionName(GetMemory).c_str());
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setCompression(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetCompression).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setBytesPerRow(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetBytesPerRow).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setColumns(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetColumns).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setRows(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetRows).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setXOffset(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetXOffset).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setYOffset(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetYOffset).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setBytesWritten(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetBytesWritten).c_str(), val);
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setMemory(TW_MEMORY val)
{
    setProxyData(proxy_memory, getFunctionName(SetMemory).c_str(), val);
}

TW_IMAGEMEMXFER JavaDTwainLowLevel_TW_IMAGEMEMXFER::JavaToNative()
{
    TW_IMAGEMEMXFER twimagememxfer{};
    twimagememxfer.BytesPerRow = getBytesPerRow();
    twimagememxfer.BytesWritten = getBytesWritten();
    twimagememxfer.Columns = getColumns();
    twimagememxfer.Compression = getCompression();
    twimagememxfer.Memory = getMemory();
    twimagememxfer.Rows = getRows();
    twimagememxfer.XOffset = getXOffset();
    twimagememxfer.YOffset = getYOffset();
    return twimagememxfer;
}

jobject JavaDTwainLowLevel_TW_IMAGEMEMXFER::NativeToJava(const TW_IMAGEMEMXFER& twimagememxfer)
{
    setObject(createDefaultObject());
    setBytesPerRow(twimagememxfer.BytesPerRow);
    setBytesWritten(twimagememxfer.BytesWritten);
    setColumns(twimagememxfer.Columns);
    setCompression(twimagememxfer.Compression);
    setMemory(twimagememxfer.Memory);
    setRows(twimagememxfer.Rows);
    setXOffset(twimagememxfer.XOffset);
    setYOffset(twimagememxfer.YOffset);
    return getObject();
}

TW_IMAGEMEMXFER JavaDTwainLowLevel_TW_IMAGEMEMXFER::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_IMAGEMEMXFER::setValue(const TW_IMAGEMEMXFER & twimagememxfer)
{
    NativeToJava(twimagememxfer);
}
//////////////////////////////////////////////////////////////////////////////////

JavaDTwainLowLevel_TW_JPEGCOMPRESSION::JavaDTwainLowLevel_TW_JPEGCOMPRESSION(JNIEnv* env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_JPEGCOMPRESSION",
    { GetColorSpace,GetNumComponents,GetRestartFrequency,GetSubSampling,GetQuantMapValue,GetQuantTableValue,
        GetHuffmanMapValue,GetHuffmanDCValue,GetHuffmanACValue,SetColorSpace,SetNumComponents,
        SetRestartFrequency,SetSubSampling,SetQuantMapValue,SetQuantTableValue,SetHuffmanMapValue,
        SetHuffmanDCValue,SetHuffmanACValue}),
         proxy_u16(env), proxy_u32(env), proxy_memory(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_JPEGCOMPRESSION::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_JPEGCOMPRESSION::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getColorSpace()
{
    return getProxyData(proxy_u16, getFunctionName(GetColorSpace).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getSubSampling()
{
    return getProxyData(proxy_u32, getFunctionName(GetSubSampling).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getNumComponents()
{
    return getProxyData(proxy_u16, getFunctionName(GetNumComponents).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getRestartFrequency()
{
    return getProxyData(proxy_u16, getFunctionName(GetRestartFrequency).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getQuantMapValue(int32_t val)
{
    return getProxyData(proxy_u16, getFunctionName(GetQuantMapValue).c_str(), val);
}

TW_MEMORY JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getQuantTableValue(int32_t val)
{
    return getProxyData(proxy_memory, getFunctionName(GetQuantTableValue).c_str(), val);
}

TW_UINT16 JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getHuffmanMapValue(int32_t val)
{
    return getProxyData(proxy_u16, getFunctionName(GetHuffmanMapValue).c_str(), val);
}

TW_MEMORY JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getHuffmanDCValue(int32_t val)
{
    return getProxyData(proxy_memory, getFunctionName(GetHuffmanDCValue).c_str(), val);
}

TW_MEMORY JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getHuffmanACValue(int32_t val)
{
    return getProxyData(proxy_memory, getFunctionName(GetHuffmanACValue).c_str(), val);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setColorSpace(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetColorSpace).c_str(), val);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setSubSampling(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSubSampling).c_str(), val);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setNumComponents(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetNumComponents).c_str(), val);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setRestartFrequency(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetRestartFrequency).c_str(), val);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setQuantMapValue(TW_UINT16 val, int32_t which)
{
    setProxyData(proxy_u16, getFunctionName(SetQuantMapValue).c_str(), val, which);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setQuantTableValue(TW_MEMORY val, int32_t which)
{
    setProxyData(proxy_memory, getFunctionName(SetQuantTableValue).c_str(), val, which);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setHuffmanMapValue(TW_UINT16 val, int32_t which)
{
    setProxyData(proxy_u16, getFunctionName(SetHuffmanMapValue).c_str(), val, which);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setHuffmanDCValue(TW_MEMORY val, int32_t which)
{
    setProxyData(proxy_memory, getFunctionName(SetHuffmanDCValue).c_str(), val, which);
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setHuffmanACValue(TW_MEMORY val, int32_t which)
{
    setProxyData(proxy_memory, getFunctionName(SetHuffmanACValue).c_str(), val, which);
}

TW_JPEGCOMPRESSION JavaDTwainLowLevel_TW_JPEGCOMPRESSION::JavaToNative()
{
    TW_JPEGCOMPRESSION twjpegcompression{};
    twjpegcompression.ColorSpace = getColorSpace();
    twjpegcompression.NumComponents = getNumComponents();
    twjpegcompression.RestartFrequency = getRestartFrequency();
    twjpegcompression.SubSampling = getSubSampling();
    for (int i = 0; i < 4; ++i)
    {
        twjpegcompression.QuantMap[i] = getQuantMapValue(i);
        twjpegcompression.QuantTable[i] = getQuantTableValue(i);
        twjpegcompression.HuffmanMap[i] = getHuffmanMapValue(i);
    }
    for (int i = 0; i < 2; ++i)
    {
        twjpegcompression.HuffmanAC[i] = getHuffmanACValue(i);
        twjpegcompression.HuffmanDC[i] = getHuffmanDCValue(i);
    }
    return twjpegcompression;
}

jobject JavaDTwainLowLevel_TW_JPEGCOMPRESSION::NativeToJava(const TW_JPEGCOMPRESSION& twjpegcompression)
{
    setObject(createDefaultObject());
    setColorSpace(twjpegcompression.ColorSpace);
    setNumComponents(twjpegcompression.NumComponents);
    setRestartFrequency(twjpegcompression.RestartFrequency);
    setSubSampling(twjpegcompression.SubSampling);
    for (int i = 0; i < 4; ++i)
    {
        setQuantMapValue(twjpegcompression.QuantMap[i], i);
        setQuantTableValue(twjpegcompression.QuantTable[i], i);
        setHuffmanMapValue(twjpegcompression.HuffmanMap[i], i);
    }
    for (int i = 0; i < 2; ++i)
    {
        setHuffmanACValue(twjpegcompression.HuffmanAC[i], i);
        setHuffmanDCValue(twjpegcompression.HuffmanDC[i], i);
    }
    return getObject();
}

TW_JPEGCOMPRESSION JavaDTwainLowLevel_TW_JPEGCOMPRESSION::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_JPEGCOMPRESSION::setValue(const TW_JPEGCOMPRESSION & twjpegcompression)
{
    NativeToJava(twjpegcompression);
}

JavaDTwainLowLevel_TW_PALETTE8::JavaDTwainLowLevel_TW_PALETTE8(JNIEnv* env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_PALETTE8",
    {GetNumColors,GetPaletteType,GetColorValue,SetNumColors,SetPaletteType,SetColorValue}),
        proxy_u16(env), proxy_element8(env)

{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PALETTE8::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_PALETTE8::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_UINT16 JavaDTwainLowLevel_TW_PALETTE8::getNumColors()
{
    return getProxyData(proxy_u16, getFunctionName(GetNumColors).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_PALETTE8::getPaletteType()
{
    return getProxyData(proxy_u16, getFunctionName(GetPaletteType).c_str());
}

TW_ELEMENT8 JavaDTwainLowLevel_TW_PALETTE8::getColorValue(int32_t which)
{
    return getProxyData(proxy_element8, getFunctionName(GetColorValue).c_str(), which);
}

void JavaDTwainLowLevel_TW_PALETTE8::setNumColors(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetNumColors).c_str(), val);
}

void JavaDTwainLowLevel_TW_PALETTE8::setPaletteType(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetPaletteType).c_str(), val);
}

void JavaDTwainLowLevel_TW_PALETTE8::setColorValue(TW_ELEMENT8 val, int32_t which)
{
    setProxyData(proxy_element8, getFunctionName(SetColorValue).c_str(), val, which);
}

TW_PALETTE8 JavaDTwainLowLevel_TW_PALETTE8::JavaToNative()
{
    TW_PALETTE8 twpalette8{};
    twpalette8.NumColors = getNumColors();
    twpalette8.PaletteType = getPaletteType();
    for (int i = 0; i < 256; ++i)
        twpalette8.Colors[i] = getColorValue(i);
    return twpalette8;
}

jobject JavaDTwainLowLevel_TW_PALETTE8::NativeToJava(const TW_PALETTE8& twpalette8)
{
    setObject(createDefaultObject());
    setNumColors(twpalette8.NumColors);
    setPaletteType(twpalette8.PaletteType);
    for (int i = 0; i < 256; ++i)
        setColorValue(twpalette8.Colors[i], i);
    return getObject();
}

TW_PALETTE8 JavaDTwainLowLevel_TW_PALETTE8::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_PALETTE8::setValue(const TW_PALETTE8 & twpalette8)
{
    NativeToJava(twpalette8);
}
//////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_TWAINDIRECT::JavaDTwainLowLevel_TW_TWAINDIRECT(JNIEnv * env)
 : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_TWAINDIRECT",
{
     GetSizeOf, GetCommunicationManager, GetSend, GetSendSize,
     GetReceive, GetReceiveSize, SetSizeOf, SetCommunicationManager, SetSend,
     SetSendSize, SetReceive, SetReceiveSize
 }), proxy_u16(env), proxy_u32(env), proxy_handle(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_TWAINDIRECT::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_TWAINDIRECT::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_UINT32 JavaDTwainLowLevel_TW_TWAINDIRECT::getSizeOf()
{
    return getProxyData(proxy_u32, getFunctionName(GetSizeOf).c_str());
}

TW_UINT16 JavaDTwainLowLevel_TW_TWAINDIRECT::getCommunicationManager()
{
    return getProxyData(proxy_u16, getFunctionName(GetCommunicationManager).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_TWAINDIRECT::getSend()
{
    return getProxyData(proxy_handle, getFunctionName(GetSend).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_TWAINDIRECT::getSendSize()
{
    return getProxyData(proxy_u32, getFunctionName(GetSendSize).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_TWAINDIRECT::getReceive()
{
    return getProxyData(proxy_handle, getFunctionName(GetReceive).c_str());
}

TW_UINT32 JavaDTwainLowLevel_TW_TWAINDIRECT::getReceiveSize()
{
    return getProxyData(proxy_u32, getFunctionName(GetReceiveSize).c_str());
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setSizeOf(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSizeOf).c_str(), val);
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setCommunicationManager(TW_UINT16 val)
{
    setProxyData(proxy_u16, getFunctionName(SetCommunicationManager).c_str(), val);
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setSend(TW_HANDLE val)
{
    setProxyData(proxy_handle, getFunctionName(SetSend).c_str(), val);
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setSendSize(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetSendSize).c_str(), val);
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setReceive(TW_HANDLE val)
{
    setProxyData(proxy_handle, getFunctionName(SetReceive).c_str(), val);
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setReceiveSize(TW_UINT32 val)
{
    setProxyData(proxy_u32, getFunctionName(SetReceiveSize).c_str(), val);
}

TW_TWAINDIRECT JavaDTwainLowLevel_TW_TWAINDIRECT::JavaToNative()
{
    TW_TWAINDIRECT twtwaindirect{};
    twtwaindirect.CommunicationManager = getCommunicationManager();
    twtwaindirect.Receive = getReceive();
    twtwaindirect.ReceiveSize = getReceiveSize();
    twtwaindirect.Send = getSend();
    twtwaindirect.SendSize = getSendSize();
    twtwaindirect.SizeOf = getSizeOf();
    return twtwaindirect;
}

jobject JavaDTwainLowLevel_TW_TWAINDIRECT::NativeToJava(const TW_TWAINDIRECT& twtwaindirect)
{
    setObject(createDefaultObject());
    setCommunicationManager(twtwaindirect.CommunicationManager);
    setReceive(twtwaindirect.Receive);
    setReceiveSize(twtwaindirect.ReceiveSize);
    setSend(twtwaindirect.Send);
    setSendSize(twtwaindirect.SendSize);
    setSizeOf(twtwaindirect.SizeOf);
    return getObject();
}

TW_TWAINDIRECT JavaDTwainLowLevel_TW_TWAINDIRECT::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_TWAINDIRECT::setValue(const TW_TWAINDIRECT & twtwaindirect)
{
    NativeToJava(twtwaindirect);
}
///////////////////////////////////////////////////
JavaDTwainLowLevel_TW_BOOL::JavaDTwainLowLevel_TW_BOOL(JNIEnv* env) :
    JavaObjectCaller(env,  JavaFunctionNameMapInstance::getFunctionMap(), "TW_BOOL",
    {GetValue, SetValue})
{
    RegisterMemberFunctions(*this, getObjectName());
}

jobject JavaDTwainLowLevel_TW_BOOL::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_BOOL::createFullObject(int var1)
{
    return constructObject(1, var1);
}

JavaDTwainLowLevel_TW_BOOL::value_type JavaDTwainLowLevel_TW_BOOL::getValue()
{
    return static_cast<JavaDTwainLowLevel_TW_BOOL::value_type>(callBooleanMethod(getFunctionName(GetValue)));
}

void JavaDTwainLowLevel_TW_BOOL::setValue(JavaDTwainLowLevel_TW_BOOL::value_type val)
{
    callObjectMethod(getFunctionName(SetValue), static_cast<jboolean>(val));
}

jobject JavaDTwainLowLevel_TW_BOOL::NativeToJava(JavaDTwainLowLevel_TW_BOOL::value_type iInfo)
{
    return createFullObject(iInfo);
};

JavaDTwainLowLevel_TW_BOOL::value_type JavaDTwainLowLevel_TW_BOOL::JavaToNative()
{
    return getValue();
}
//=================================================================
JavaDTwainLowLevel_TW_USERINTERFACE::JavaDTwainLowLevel_TW_USERINTERFACE(JNIEnv * env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_USERINTERFACE",
{GetShowUI, GetModalUI, GetParent, SetShowUI, SetModalUI, SetParent }), proxy_bool(env), proxy_handle(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_USERINTERFACE::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_USERINTERFACE::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_BOOL JavaDTwainLowLevel_TW_USERINTERFACE::getShowUI()
{
    return getProxyData(proxy_bool, getFunctionName(GetShowUI).c_str());
}

TW_BOOL JavaDTwainLowLevel_TW_USERINTERFACE::getModalUI()
{
    return getProxyData(proxy_bool, getFunctionName(GetModalUI).c_str());
}

TW_HANDLE JavaDTwainLowLevel_TW_USERINTERFACE::gethParent()
{
    return getProxyData(proxy_handle, getFunctionName(GetParent).c_str());
}

void JavaDTwainLowLevel_TW_USERINTERFACE::setShowUI(TW_BOOL val)
{
    setProxyData(proxy_bool, getFunctionName(SetShowUI).c_str(), val);
}

void JavaDTwainLowLevel_TW_USERINTERFACE::setModalUI(TW_BOOL val)
{
    setProxyData(proxy_bool, getFunctionName(SetModalUI).c_str(), val);
}

void JavaDTwainLowLevel_TW_USERINTERFACE::sethParent(TW_HANDLE val)
{
    setProxyData(proxy_handle, getFunctionName(SetParent).c_str(), val);
}

TW_USERINTERFACE JavaDTwainLowLevel_TW_USERINTERFACE::JavaToNative()
{
    TW_USERINTERFACE twuserinterface{};
    twuserinterface.hParent = gethParent();
    twuserinterface.ModalUI = getModalUI();
    twuserinterface.ShowUI = getShowUI();
    return twuserinterface;
}

jobject JavaDTwainLowLevel_TW_USERINTERFACE::NativeToJava(const TW_USERINTERFACE& twuserinterface)
{
    setObject(createDefaultObject());
    sethParent(twuserinterface.hParent);
    setModalUI(twuserinterface.ModalUI);
    setShowUI(twuserinterface.ShowUI);
    return getObject();
}

TW_USERINTERFACE JavaDTwainLowLevel_TW_USERINTERFACE::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_USERINTERFACE::setValue(const TW_USERINTERFACE & twuserinterface)
{
    NativeToJava(twuserinterface);
}
///////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TW_RESPONSETYPE::JavaDTwainLowLevel_TW_RESPONSETYPE(JNIEnv* env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_RESPONSETYPE",
{GetNumItems, GetResponseValue, SetNumItems, SetResponseValue}), proxy_element8(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_RESPONSETYPE::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_RESPONSETYPE::createFullObject(int var1)
{
    return defaultConstructObject();
}

int32_t JavaDTwainLowLevel_TW_RESPONSETYPE::getNumItems()
{
    return callIntMethod(getFunctionName(GetNumItems));
}

TW_ELEMENT8 JavaDTwainLowLevel_TW_RESPONSETYPE::getResponseValue(int32_t which)
{
    return getProxyData(proxy_element8, getFunctionName(GetResponseValue).c_str(), which);
}

void JavaDTwainLowLevel_TW_RESPONSETYPE::setNumItems(int32_t val)
{
    callObjectMethod(getFunctionName(SetNumItems), val);
}

void JavaDTwainLowLevel_TW_RESPONSETYPE::setResponseValue(TW_ELEMENT8 val, int32_t nWhich)
{
    setProxyData(proxy_element8, getFunctionName(SetResponseValue).c_str(), val, nWhich);
}

TW_RESPONSETYPE JavaDTwainLowLevel_TW_RESPONSETYPE::JavaToNative()
{
    TW_RESPONSETYPE twresponsetype{};
    twresponsetype.NumItems = getNumItems();
    twresponsetype.ResponseVector.resize(twresponsetype.NumItems);
    for (int i = 0; i < twresponsetype.NumItems; ++i)
        twresponsetype.ResponseVector[i] = getResponseValue(i);
    return twresponsetype;
}

TW_RESPONSETYPE JavaDTwainLowLevel_TW_RESPONSETYPE::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_RESPONSETYPE::setValue(const TW_RESPONSETYPE & twresponsetype)
{
    setNumItems(twresponsetype.NumItems);
    for (int i = 0; i < twresponsetype.NumItems; ++i)
        setResponseValue(twresponsetype.ResponseVector[i], i);
}
/////////////////////////////////////////////////////////////////////////////////////////////

JavaDTwainLowLevel_TW_NULL::JavaDTwainLowLevel_TW_NULL(JNIEnv* env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TW_NULL", {})
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_NULL::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TW_NULL::createFullObject(int var1)
{
    return defaultConstructObject();
}

TW_NULL JavaDTwainLowLevel_TW_NULL::JavaToNative()
{
    return {};
}

TW_NULL JavaDTwainLowLevel_TW_NULL::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TW_NULL::setValue(const TW_NULL& twnull)
{
    return;
}
////////////////////////////////////////////////////////////////
JavaDTwainLowLevel_TwainLowLevel::JavaDTwainLowLevel_TwainLowLevel(JNIEnv* env)
    : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainLowLevel",
        { GetTwainObject })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TwainLowLevel::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TwainLowLevel::createFullObject(int var1)
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TwainLowLevel::getTwainObject()
{
    return callObjectMethod(getFunctionName(GetTwainObject));
}

TwainLowLevel JavaDTwainLowLevel_TwainLowLevel::JavaToNative()
{
    TwainLowLevel twlowlevel {};
    twlowlevel.m_TwainObject = getTwainObject();
    return twlowlevel;
}

TwainLowLevel JavaDTwainLowLevel_TwainLowLevel::getValue()
{
    return JavaToNative();
}

void JavaDTwainLowLevel_TwainLowLevel::setValue(const TwainLowLevel& twlowlevel)
{
    return;
}
///////////////////////////////////////////////////
JavaDTwainLowLevel_TwainTriplet::JavaDTwainLowLevel_TwainTriplet(JNIEnv* env)
        : JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "TwainTriplet",
        { GetOriginID,GetDestinationID,GetDG,GetDAT,GetMSG,GetData}), proxy_twlowlevel(env)
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

jobject JavaDTwainLowLevel_TwainTriplet::createDefaultObject()
{
    return defaultConstructObject();
}

jobject JavaDTwainLowLevel_TwainTriplet::getOriginID()
{
    return callObjectMethod(getFunctionName(GetOriginID));
}

jobject JavaDTwainLowLevel_TwainTriplet::getDestinationID()
{
    return callObjectMethod(getFunctionName(GetDestinationID));
}

jobject JavaDTwainLowLevel_TwainTriplet::getDG()
{
    return callObjectMethod(getFunctionName(GetDG));
}

jobject JavaDTwainLowLevel_TwainTriplet::getDAT()
{
    return callObjectMethod(getFunctionName(GetDAT));
}

jobject JavaDTwainLowLevel_TwainTriplet::getMSG()
{
    return callObjectMethod(getFunctionName(GetMSG));
}

jobject JavaDTwainLowLevel_TwainTriplet::getData()
{
    return getProxyData(proxy_twlowlevel, getFunctionName(GetData).c_str()).m_TwainObject;
}

TwainTripletFromJava JavaDTwainLowLevel_TwainTriplet::JavaToNative()
{
    TwainTripletFromJava twtriplet {};
    twtriplet.m_OriginID = getOriginID();
    twtriplet.m_DestinationID = getDestinationID();
    twtriplet.m_DG = getDG();
    twtriplet.m_DAT = getDAT();
    twtriplet.m_MSG = getMSG();
    twtriplet.m_memRef = getData();
    return twtriplet;
}
TwainTripletFromJava JavaDTwainLowLevel_TwainTriplet::getValue()
{
    return JavaToNative();
}
////////////////////////////////////////////////////////////////////////////////////////////////////
JavaInteger::JavaInteger(JNIEnv *env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "JavaInteger",
        {IntValue})
{
    RegisterMemberFunctions(*this, getObjectName());
}

jobject JavaInteger::createFullObject(int var1)
{
    return constructObject(1, var1);
}

jobject JavaInteger::NativeToJava(int32_t iInfo)
{
    return createFullObject(iInfo);
};

int32_t JavaInteger::JavaToNative()
{
    return callIntMethod(getFunctionName(IntValue));
}
////////////////////////////////////////////////////////////////////////////////////////////////////
JavaDouble::JavaDouble(JNIEnv *env) :
JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "JavaDouble",
                {DoubleValue})
{
    RegisterMemberFunctions(*this, getObjectName());
}

jobject JavaDouble::createFullObject(double var1)
{
    return constructObject(1, var1);
}

jobject JavaDouble::NativeToJava(double iInfo)
{
    return createFullObject(iInfo);
};

double JavaDouble::JavaToNative()
{
    return callDoubleMethod(getFunctionName(DoubleValue));
}
////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
JavaBoolean::JavaBoolean(JNIEnv *env) :
   JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "JavaBoolean",
    {BooleanValue})
{
    RegisterMemberFunctions(*this, getObjectName());
}

jobject JavaBoolean::createFullObject(bool var1)
{
    return constructObject(1, var1);
}

jobject JavaBoolean::NativeToJava(bool iInfo)
{
    return createFullObject(iInfo);
};

bool JavaBoolean::JavaToNative()
{
    return callBooleanMethod(getFunctionName(BooleanValue));
}
/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
JavaEnumBase::JavaEnumBase(JNIEnv* env, std::string objectName) :
       JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), objectName, {"Value"})
{
    RegisterMemberFunctions(*this, getObjectName());
}

int JavaEnumBase::getValue()
{
    return callIntMethod(getFunctionName(Value));
}

JavaTextRenderMode::JavaTextRenderMode(JNIEnv* env) :
    JavaEnumBase(env, "TextRenderMode")
{}

JavaTextDisplayOptions::JavaTextDisplayOptions(JNIEnv* env) :
    JavaEnumBase(env, "TextDisplayOptions")
{}

JavaRGBColor::JavaRGBColor(JNIEnv* env) :
    JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "RGBColor",
    { GetR, GetG, GetB })
{
    RegisterMemberFunctions(*this, getObjectName());
    defaultConstructObject();
}

int JavaRGBColor::getR()
{
    return callIntMethod(getFunctionName(GetR));
}

int JavaRGBColor::getG()
{
    return callIntMethod(getFunctionName(GetG));
}

int JavaRGBColor::getB()
{
    return callIntMethod(getFunctionName(GetB));
}

JavaPDFTextElement::JavaPDFTextElement(JNIEnv *env) :
JavaObjectCaller(env, JavaFunctionNameMapInstance::getFunctionMap(), "PDFTextElement",
        {GetFontName, GetText, GetXPos, GetYPos, GetFontSize, GetScaling, GetCharSpacing, GetWordSpacing,
        GetStrokeWidth, GetRGBColor, GetRenderMode, GetDisplayOptions})
{
    RegisterMemberFunctions(*this, getObjectName());
}

JavaPDFTextElement::PDFStringType JavaPDFTextElement::getFontName()
{
    return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetFontName)))).GetWindowsStringChars();
}

JavaPDFTextElement::PDFStringType JavaPDFTextElement::getText()
{
    return GetStringCharsHandler(m_pJavaEnv, static_cast<jstring>(callObjectMethod(getFunctionName(GetText)))).GetWindowsStringChars();
}

RGBNative JavaPDFTextElement::getRGB()
{
    RGBNative ret;
    jobject javaRGB = callObjectMethod(getFunctionName(GetRGBColor));
    JavaRGBColor colorObject(getEnvironment());
    colorObject.setObject(javaRGB);
    ret.setR(colorObject.callIntMethod(colorObject.getFunctionName(JavaRGBColor::GetR)));
    ret.setG(colorObject.callIntMethod(colorObject.getFunctionName(JavaRGBColor::GetG)));
    ret.setB(colorObject.callIntMethod(colorObject.getFunctionName(JavaRGBColor::GetB)));
    return ret;
}

int JavaPDFTextElement::getRenderMode()
{
    return callIntMethod(getFunctionName(GetRenderMode));
}

int JavaPDFTextElement::getXPosition()
{
    return callIntMethod(getFunctionName(GetXPos));
}

int JavaPDFTextElement::getYPosition()
{
    return callIntMethod(getFunctionName(GetYPos));
}

double JavaPDFTextElement::getFontSize()
{
    return callDoubleMethod(getFunctionName(GetFontSize));
}

double JavaPDFTextElement::getScaling()
{
    return callDoubleMethod(getFunctionName(GetScaling));
}

double JavaPDFTextElement::getCharSpacing()
{
    return callDoubleMethod(getFunctionName(GetCharSpacing));
}

double JavaPDFTextElement::getWordSpacing()
{
    return callDoubleMethod(getFunctionName(GetWordSpacing));
}

int JavaPDFTextElement::getStrokeWidth()
{
    return callIntMethod(getFunctionName(GetStrokeWidth));
}

int JavaPDFTextElement::getDisplayOptions()
{
    return callIntMethod(getFunctionName(GetDisplayOptions));
}
