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
#include "DTWAINJNIGlobals.h"
#include <algorithm>
#include "twain.h"

#define NAME_TO_STRING(x) #x
#define ADD_FUNCTION_ENTRY(m, fName)  (g_##m##Map)->m_FnMap[NAME_TO_STRING(fName)] = fName;\
                                       g_ptrBase.insert((g_##m##Map).get());

#define INITIALIZE_MAP_ENTRY(Entry)   g_##Entry##Map (FnGlobal##Entry##Ptr(new FnGlobal##Entry))

static bool UndefinedFunc(FnGlobalBase* fn)
{ return fn->isEmpty(); }

DTWAINJNIGlobals::DTWAINJNIGlobals() : g_DTWAINModule(nullptr),
        INITIALIZE_MAP_ENTRY(LV),
        INITIALIZE_MAP_ENTRY(LS),
        INITIALIZE_MAP_ENTRY(SV),
        INITIALIZE_MAP_ENTRY(HV),
        INITIALIZE_MAP_ENTRY(AV),
        INITIALIZE_MAP_ENTRY(HandleV),
        INITIALIZE_MAP_ENTRY(LL),
        INITIALIZE_MAP_ENTRY(WW),
        INITIALIZE_MAP_ENTRY(WSW),
        INITIALIZE_MAP_ENTRY(LSa),
        INITIALIZE_MAP_ENTRY(LSL),
        INITIALIZE_MAP_ENTRY(LSF),
        INITIALIZE_MAP_ENTRY(LSl),
        INITIALIZE_MAP_ENTRY(LSf),
        INITIALIZE_MAP_ENTRY(LHt),
        INITIALIZE_MAP_ENTRY(LSLl),
        INITIALIZE_MAP_ENTRY(LSaB),
        INITIALIZE_MAP_ENTRY(LSLLa),
        INITIALIZE_MAP_ENTRY(LSLL),
        INITIALIZE_MAP_ENTRY(LSFF),
        INITIALIZE_MAP_ENTRY(LSLFF),
        INITIALIZE_MAP_ENTRY(LST),
        INITIALIZE_MAP_ENTRY(LLTL),
        INITIALIZE_MAP_ENTRY(St),
        INITIALIZE_MAP_ENTRY(Lt),
        INITIALIZE_MAP_ENTRY(LSt),
        INITIALIZE_MAP_ENTRY(LTL),
        INITIALIZE_MAP_ENTRY(LSLttLL),
        INITIALIZE_MAP_ENTRY(SHtLLL),
        INITIALIZE_MAP_ENTRY(LSFLL),
        INITIALIZE_MAP_ENTRY(LSFLLL),
        INITIALIZE_MAP_ENTRY(LHF),
        INITIALIZE_MAP_ENTRY(BH),
        INITIALIZE_MAP_ENTRY(Ltttt),
        INITIALIZE_MAP_ENTRY(LTTTT),
        INITIALIZE_MAP_ENTRY(LSLLLa),
        INITIALIZE_MAP_ENTRY(LSLLLLa),
        INITIALIZE_MAP_ENTRY(LSLLA),
        INITIALIZE_MAP_ENTRY(LSLLLA),
        INITIALIZE_MAP_ENTRY(LSLLLLA),
        INITIALIZE_MAP_ENTRY(LSLBB),
        INITIALIZE_MAP_ENTRY(LLt),
        INITIALIZE_MAP_ENTRY(LSB),
        INITIALIZE_MAP_ENTRY(Ltt),
        INITIALIZE_MAP_ENTRY(HandleS),
        INITIALIZE_MAP_ENTRY(LSffffl),
        INITIALIZE_MAP_ENTRY(LSFFFFLL),
        INITIALIZE_MAP_ENTRY(LSaLLLLLLl),
        INITIALIZE_MAP_ENTRY(LSlll),
        INITIALIZE_MAP_ENTRY(La),
        INITIALIZE_MAP_ENTRY(LLa),
        INITIALIZE_MAP_ENTRY(LLLLa),
        INITIALIZE_MAP_ENTRY(LLLLA),
        INITIALIZE_MAP_ENTRY(LLLLLLL),
        INITIALIZE_MAP_ENTRY(LLtLL),
        INITIALIZE_MAP_ENTRY(OV),
        INITIALIZE_MAP_ENTRY(Ot),
        INITIALIZE_MAP_ENTRY(HLLTLlL),
        INITIALIZE_MAP_ENTRY(LSH),
        INITIALIZE_MAP_ENTRY(LStLLLLLLl),
        INITIALIZE_MAP_ENTRY(LSlllllll),
        INITIALIZE_MAP_ENTRY(LOLLa),
        INITIALIZE_MAP_ENTRY(LOLLA),
        INITIALIZE_MAP_ENTRY(LO),
        INITIALIZE_MAP_ENTRY(LOLLLLL),
        INITIALIZE_MAP_ENTRY(LOtLL),
        INITIALIZE_MAP_ENTRY(HOLTLlL),
        INITIALIZE_MAP_ENTRY(HL),
        INITIALIZE_MAP_ENTRY(LH),
        INITIALIZE_MAP_ENTRY(LLL)

    {
        // 0 arguments
        ADD_FUNCTION_ENTRY(AV, API_INSTANCE DTWAIN_CreateAcquisitionArray)
        ADD_FUNCTION_ENTRY(AV, API_INSTANCE DTWAIN_CreateAcquisitionArray)
        ADD_FUNCTION_ENTRY(HandleV, API_INSTANCE DTWAIN_SysInitialize)
        ADD_FUNCTION_ENTRY(HV, API_INSTANCE DTWAIN_GetTwainHwnd)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_ClearErrorBuffer)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_EndTwainSession)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE  DTWAIN_GetCountry)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_GetErrorBufferThreshold)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_GetLanguage)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_GetLastError)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_GetTwainAvailability)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_GetTwainMode)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_InitOCRInterface)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_IsAcquiring)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_IsMsgNotifyEnabled)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_IsSessionEnabled)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_IsTwainAvailable)
        ADD_FUNCTION_ENTRY(OV, API_INSTANCE DTWAIN_SelectDefaultOCREngine)
        ADD_FUNCTION_ENTRY(OV, API_INSTANCE DTWAIN_SelectOCREngine)
        ADD_FUNCTION_ENTRY(LV, API_INSTANCE DTWAIN_SysDestroy)
        ADD_FUNCTION_ENTRY(SV, API_INSTANCE DTWAIN_SelectDefaultSource)
        ADD_FUNCTION_ENTRY(SV, API_INSTANCE DTWAIN_SelectSource)

        // 1 argument LONG
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_AppHandlesExceptions)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_EnableMsgNotify)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_OpenSourcesOnSelect)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetCountry)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetErrorBufferThreshold)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetLanguage)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetQueryCapSupport)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetTwainDSM)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetTwainTimeout)
        ADD_FUNCTION_ENTRY(LL, API_INSTANCE DTWAIN_SetTwainMode)
        ADD_FUNCTION_ENTRY(Ot, API_INSTANCE  DTWAIN_SelectOCREngineByName)
        ADD_FUNCTION_ENTRY(HL, API_INSTANCE  DTWAIN_AllocateMemory)
        ADD_FUNCTION_ENTRY(LH, API_INSTANCE  DTWAIN_FreeMemory)

        // 1 argument DTWAIN_SOURCE
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_ClearPage)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_ClearPDFText)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_CloseSource)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_CloseSourceUI)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_FeedPage)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_FlushAcquiredPages)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_FreeExtImageInfo)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetBlankPageAutoDetection)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetCurrentPageNum)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetCurrentRetryCount)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetExtImageInfo)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetFeederFuncs)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetMaxAcquisitions)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetMaxPagesToAcquire)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_GetMaxRetryAttempts)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_InitExtImageInfo)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoBorderDetectEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoBorderDetectSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoBrightEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoDeskewEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoDeskewSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoFeedEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoFeedSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoRotateEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsAutoScanEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsBlankPageDetectionOn)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsCustomDSDataSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsDeviceEventSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsDeviceOnLine)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsDuplexEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsDuplexSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsExtImageInfoSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsFeederEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsFeederLoaded)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsFeederSensitive)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsFeederSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsFileSystemSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsIndicatorEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsIndicatorSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsLampEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsLampSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsLightPathSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsLightSourceSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsPaperDetectable)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsPatchCapsSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsPatchDetectEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsPrinterSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsRotationSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsSkipImageInfoError)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsSourceAcquiring)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsSourceOpen)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsThumbnailEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsThumbnailSupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsUIControllable)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsUIEnabled)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_IsUIOnlySupported)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_OpenSource)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_RewindPage)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_SetAllCapsToDefault)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_SetDefaultSource)
        ADD_FUNCTION_ENTRY(LS, API_INSTANCE DTWAIN_ShowUIOnly)
        ADD_FUNCTION_ENTRY(BH, API_INSTANCE DTWAIN_StartThread)
        ADD_FUNCTION_ENTRY(BH, API_INSTANCE DTWAIN_EndThread)
        ADD_FUNCTION_ENTRY(WW, API_INSTANCE DTWAIN_GetTwainAppIDEx)

        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumSupportedCaps)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumExtendedCaps)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumCustomCaps)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumSourceUnits)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumFileXferFormats)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumCompressionTypes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPrinterStringModes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumTwainPrintersArray)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumOrientations)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPaperSizes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPixelTypes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumBitDepths)

        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumJobControls)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumLightPaths)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumLightSources)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_GetLightSources)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumExtImageInfoTypes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumAlarms)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumNoiseFilters)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPatchMaxRetries)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPatchMaxPriorities)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPatchSearchModes)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPatchTimeOutValues)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_GetPatchPriorities)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumPatchPriorities)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumTopCameras)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumBottomCameras)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumCameras)
        ADD_FUNCTION_ENTRY(WSW, API_INSTANCE  DTWAIN_GetSourceIDEx)

        // 2 argument functions (DTWAIN_SOURCE, API_INSTANCE  LONG) returning LONG
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsCapSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_GetCapDataType)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetMaxAcquisitions)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetSourceUnit)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsFileXferSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableIndicator)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_GetCapArrayType)

        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsCompressionSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsPrinterEnabled)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableFeeder)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnablePrinter)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableThumbnail)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_ForceAcquireBitDepth)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetAvailablePrinters)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetDeviceNotifications)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPrinterStartNumber)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoFeed)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableDuplex)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsOrientationSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsPaperSizeSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsPixelTypeSupported)
        //    ADD_FUNCTION_ENTRY(g_LSL, API_INSTANCE DTWAIN_SetPDFCompression)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPDFASCIICompression)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPostScriptType)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPDFJpegQuality)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetTIFFInvert)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetTIFFCompressType)

        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsJobControlSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableJobFileHandling)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoDeskew)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoBorderDetect)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetLightPath)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableLamp)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetMaxRetryAttempts)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetCurrentRetryCount)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SkipImageInfoError)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetMultipageScanMode)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetAlarmVolume)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoScan)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_ClearBuffers)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetFeederAlignment)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetFeederOrder)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetMaxBuffers)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_IsMaxBuffersSupported)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoBright)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_EnableAutoRotate)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetNoiseFilter)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPixelFlavor)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE DTWAIN_SetPDFOrientation)
        ADD_FUNCTION_ENTRY(LSF, API_INSTANCE DTWAIN_SetRotation)

        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetSourceUnit)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetDeviceNotifications)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetDeviceEvent)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetCompressionSize)
        ADD_FUNCTION_ENTRY(LSa, API_INSTANCE DTWAIN_EnumTwainPrinters)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetPrinterStartNumber)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetDuplexType)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetLightPath)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetAlarmVolume)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetBatteryMinutes)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetBatteryPercent)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetFeederAlignment)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetFeederOrder)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetMaxBuffers)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetNoiseFilter)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetPixelFlavor)
        ADD_FUNCTION_ENTRY(LSl, API_INSTANCE DTWAIN_GetPixelFlavor)
        ADD_FUNCTION_ENTRY(LSf, API_INSTANCE DTWAIN_GetRotation)
        ADD_FUNCTION_ENTRY(LSf, API_INSTANCE DTWAIN_GetContrast)
        ADD_FUNCTION_ENTRY(LSf, API_INSTANCE DTWAIN_GetBrightness)
        ADD_FUNCTION_ENTRY(LSf, API_INSTANCE DTWAIN_GetResolution)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetCamera)
        ADD_FUNCTION_ENTRY(LSLl, API_INSTANCE DTWAIN_GetCapOperations)

        ADD_FUNCTION_ENTRY(LHt, API_INSTANCE  DTWAIN_StartTwainSession)
        ADD_FUNCTION_ENTRY(LSaB, API_INSTANCE DTWAIN_EnumContrastValues)
        ADD_FUNCTION_ENTRY(LSaB, API_INSTANCE DTWAIN_EnumResolutionValues)
        ADD_FUNCTION_ENTRY(LSaB, API_INSTANCE DTWAIN_EnumBrightnessValues)
        ADD_FUNCTION_ENTRY(LSaB, API_INSTANCE DTWAIN_EnumMaxBuffers)

        ADD_FUNCTION_ENTRY(LSLLa, API_INSTANCE  DTWAIN_SetCapValues)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetFileXferFormat)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_GetCapContainer)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetCompressionType)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetPrinter)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetPrinterStringMode)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetOrientation)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetPaperSize)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetBitDepth)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetJobControl)
        ADD_FUNCTION_ENTRY(LSLL, API_INSTANCE  DTWAIN_SetManualDuplexMode)

        ADD_FUNCTION_ENTRY(LSFF, API_INSTANCE  DTWAIN_SetAcquireImageScale)

        ADD_FUNCTION_ENTRY(LSLFF, API_INSTANCE  DTWAIN_SetPDFPageSize)
        ADD_FUNCTION_ENTRY(LSLFF, API_INSTANCE  DTWAIN_SetPDFPageScale)

        // string functions
        ADD_FUNCTION_ENTRY(LST, API_INSTANCE  DTWAIN_GetAuthor)
        ADD_FUNCTION_ENTRY(LST, API_INSTANCE  DTWAIN_GetCaption)
        ADD_FUNCTION_ENTRY(LTL, API_INSTANCE  DTWAIN_GetLibraryPath)
        ADD_FUNCTION_ENTRY(LTL, API_INSTANCE  DTWAIN_GetVersionString)
        ADD_FUNCTION_ENTRY(LTL, API_INSTANCE  DTWAIN_GetShortVersionString)
        ADD_FUNCTION_ENTRY(Ltt, API_INSTANCE  DTWAIN_SetDSMSearchOrderEx)

        ADD_FUNCTION_ENTRY(LLTL, API_INSTANCE  DTWAIN_GetNameFromCap)
        ADD_FUNCTION_ENTRY(LLTL, API_INSTANCE  DTWAIN_GetErrorString)
        ADD_FUNCTION_ENTRY(St, API_INSTANCE  DTWAIN_SelectSourceByName)
        ADD_FUNCTION_ENTRY(Lt, API_INSTANCE  DTWAIN_GetCapFromName)
        ADD_FUNCTION_ENTRY(Lt, API_INSTANCE  DTWAIN_SetTempFileDirectory)
        ADD_FUNCTION_ENTRY(Lt, API_INSTANCE  DTWAIN_SetResourcePath)
        ADD_FUNCTION_ENTRY(Lt, API_INSTANCE  DTWAIN_LogMessage)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetCaption)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetAuthor)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPDFAuthor)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPDFCreator)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPDFTitle)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPDFSubject)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPDFKeywords)
        ADD_FUNCTION_ENTRY(LSt, API_INSTANCE  DTWAIN_SetPostScriptTitle)

        ADD_FUNCTION_ENTRY(LTL, API_INSTANCE  DTWAIN_GetTempFileDirectory)
        ADD_FUNCTION_ENTRY(LSLttLL, API_INSTANCE  DTWAIN_SetPDFEncryption)
        ADD_FUNCTION_ENTRY(SHtLLL, API_INSTANCE  DTWAIN_SelectSource2)
        ADD_FUNCTION_ENTRY(LSFLL, API_INSTANCE  DTWAIN_SetBlankPageDetection)
        ADD_FUNCTION_ENTRY(LHF, API_INSTANCE  DTWAIN_IsDIBBlank)
        ADD_FUNCTION_ENTRY(Ltttt, API_INSTANCE  DTWAIN_SetAppInfo)
        ADD_FUNCTION_ENTRY(LTTTT, API_INSTANCE  DTWAIN_GetAppInfo)
        ADD_FUNCTION_ENTRY(LSLLLa, API_INSTANCE  DTWAIN_SetCapValuesEx)
        ADD_FUNCTION_ENTRY(LSLLLLa, API_INSTANCE  DTWAIN_SetCapValuesEx2)
        ADD_FUNCTION_ENTRY(LSLLA, API_INSTANCE  DTWAIN_GetCapValues)
        ADD_FUNCTION_ENTRY(LSLLLA, API_INSTANCE  DTWAIN_GetCapValuesEx)
        ADD_FUNCTION_ENTRY(LSLLLLA, API_INSTANCE  DTWAIN_GetCapValuesEx2)
        ADD_FUNCTION_ENTRY(LSLBB, API_INSTANCE  DTWAIN_SetFileAutoIncrement)
        ADD_FUNCTION_ENTRY(LLt, API_INSTANCE  DTWAIN_SetTwainLog)
        ADD_FUNCTION_ENTRY(HandleS, API_INSTANCE  DTWAIN_GetCurrentAcquiredImage)
        ADD_FUNCTION_ENTRY(LSffffl, API_INSTANCE  DTWAIN_GetAcquireArea2)
        ADD_FUNCTION_ENTRY(LSFFFFLL, API_INSTANCE  DTWAIN_SetAcquireArea2)
        ADD_FUNCTION_ENTRY(LSaLLLLLLl, API_INSTANCE  DTWAIN_AcquireFileEx)
        ADD_FUNCTION_ENTRY(LSlll, API_INSTANCE  DTWAIN_GetAcquireStripSizes)
        ADD_FUNCTION_ENTRY(La, API_INSTANCE  DTWAIN_EnumOCRInterfaces)
        ADD_FUNCTION_ENTRY(LLa, API_INSTANCE  DTWAIN_EnumOCRSupportedCaps)
        ADD_FUNCTION_ENTRY(LOLLa, API_INSTANCE  DTWAIN_GetOCRCapValues)
        ADD_FUNCTION_ENTRY(LOLLA, API_INSTANCE  DTWAIN_SetOCRCapValues)
        ADD_FUNCTION_ENTRY(LO, API_INSTANCE  DTWAIN_ShutdownOCREngine)
        ADD_FUNCTION_ENTRY(LO, API_INSTANCE  DTWAIN_IsOCREngineActivated)
        ADD_FUNCTION_ENTRY(LOLLLLL, API_INSTANCE  DTWAIN_SetPDFOCRConversion)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE  DTWAIN_SetPDFOCRMode)
        ADD_FUNCTION_ENTRY(LSL, API_INSTANCE  DTWAIN_SetAcquireStripSize)
        ADD_FUNCTION_ENTRY(LOtLL, API_INSTANCE  DTWAIN_ExecuteOCR)
        ADD_FUNCTION_ENTRY(HOLTLlL, API_INSTANCE  DTWAIN_GetOCRText)
        ADD_FUNCTION_ENTRY(LSH, API_INSTANCE  DTWAIN_SetAcquireStripBuffer)
        ADD_FUNCTION_ENTRY(LSlllllll, API_INSTANCE  DTWAIN_GetAcquireStripData)
        ADD_FUNCTION_ENTRY(LStLLLLLLl, API_INSTANCE  DTWAIN_AcquireFile)
        ADD_FUNCTION_ENTRY(LSB, API_INSTANCE  DTWAIN_SetAcquireImageNegative)
        ADD_FUNCTION_ENTRY(LSFLLL, API_INSTANCE  DTWAIN_SetBlankPageDetectionEx)

        GlobalSetType::iterator it = std::find_if(g_ptrBase.begin(), g_ptrBase.end(), UndefinedFunc);
        if ( it != g_ptrBase.end())
            throw "Function Not Found";
    }

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

    void DTWAINJNIGlobals::InitializeDSMCallerMap(JNIEnv *pEnv, const std::string& javaLowLevelDirectory)
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
